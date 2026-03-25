// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import edu.wpi.first.math.filter.SlewRateLimiter;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.Drive;
import frc.robot.Constants.DriveConstants;
import java.util.function.DoubleSupplier;

/**
 * TeleopDrive command for PS4 controller (tank drive):
 * - R2 trigger:      both motors forward (boost)
 * - L2 trigger:      both motors backward (boost)
 * - Left joystick Y: left side motors
 * - Right joystick Y: right side motors
 * All inputs blend additively.
 */
public class TeleopDrive extends Command {
  private final Drive m_drive;
  private final DoubleSupplier m_leftY;
  private final DoubleSupplier m_rightY;
  private final DoubleSupplier m_r2Trigger;
  private final DoubleSupplier m_l2Trigger;

  private final SlewRateLimiter m_leftLimiter  = new SlewRateLimiter(DriveConstants.kDriveSlewRate);
  private final SlewRateLimiter m_rightLimiter = new SlewRateLimiter(DriveConstants.kDriveSlewRate);

  /**
   * Creates a new TeleopDrive command.
   *
   * @param drive The drive subsystem this command will control
   * @param leftY Left joystick Y-axis (-1.0 to 1.0, forward = negative raw)
   * @param rightY Right joystick Y-axis (-1.0 to 1.0, forward = negative raw)
   * @param r2Trigger R2 trigger axis (-1.0 to 1.0 raw)
   * @param l2Trigger L2 trigger axis (-1.0 to 1.0 raw)
   */
  public TeleopDrive(
      Drive drive,
      DoubleSupplier leftY,
      DoubleSupplier rightY,
      DoubleSupplier r2Trigger,
      DoubleSupplier l2Trigger) {
    m_drive = drive;
    m_leftY = leftY;
    m_rightY = rightY;
    m_r2Trigger = r2Trigger;
    m_l2Trigger = l2Trigger;

    addRequirements(drive);
  }

  @Override
  public void initialize() {}

  @Override
  public void execute() {
    double leftSpeed  = m_leftY.getAsDouble();
    double rightSpeed = m_rightY.getAsDouble();

    // Normalize triggers from [-1.0, 1.0] to [0.0, 1.0]
    double r2 = (m_r2Trigger.getAsDouble() + 1.0) / 2.0;
    double l2 = (m_l2Trigger.getAsDouble() + 1.0) / 2.0;

    // Apply deadbands
    leftSpeed  = applyDeadband(leftSpeed,  DriveConstants.kJoystickDeadband);
    rightSpeed = applyDeadband(rightSpeed, DriveConstants.kJoystickDeadband);
    r2 = applyDeadband(r2, DriveConstants.kTriggerDeadband);
    l2 = applyDeadband(l2, DriveConstants.kTriggerDeadband);

    // Square joystick inputs: small movements → finer control, full stick → still 100%
    leftSpeed  = squareInput(leftSpeed);
    rightSpeed = squareInput(rightSpeed);

    // Scale triggers
    r2 *= DriveConstants.kTriggerScale;
    l2 *= DriveConstants.kTriggerScale;

    // Triggers add a boost/brake to both sides equally
    double boost = r2 - l2;
    leftSpeed  += boost;
    rightSpeed += boost;

    // Clamp to [-1.0, 1.0]
    leftSpeed  = Math.max(-1.0, Math.min(1.0, leftSpeed));
    rightSpeed = Math.max(-1.0, Math.min(1.0, rightSpeed));

    // Slew rate limit to smooth acceleration and reduce jerk
    leftSpeed  = m_leftLimiter.calculate(leftSpeed);
    rightSpeed = m_rightLimiter.calculate(rightSpeed);

    m_drive.tankDrive(leftSpeed, rightSpeed);
  }

  @Override
  public void end(boolean interrupted) {
    m_drive.stop();
  }

  @Override
  public boolean isFinished() {
    return false;
  }

  private double applyDeadband(double value, double deadband) {
    if (Math.abs(value) < deadband) {
      return 0.0;
    }
    return value;
  }

  private double squareInput(double input) {
    return Math.signum(input) * input * input;
  }
}
