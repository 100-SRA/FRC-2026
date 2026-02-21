// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.Drive;
import frc.robot.Constants.DriveConstants;
import java.util.function.DoubleSupplier;

/**
 * TeleopDrive command for PS4 controller:
 * - R2 trigger: both motors forward
 * - L2 trigger: both motors backward
 * - Right joystick Y: forward/backward (arcade)
 * - Right joystick X: turning (arcade)
 * All inputs blend additively.
 */
public class TeleopDrive extends Command {
  private final Drive m_drive;
  private final DoubleSupplier m_joystickY;
  private final DoubleSupplier m_joystickX;
  private final DoubleSupplier m_r2Trigger;
  private final DoubleSupplier m_l2Trigger;

  /**
   * Creates a new TeleopDrive command.
   *
   * @param drive The drive subsystem this command will control
   * @param joystickY Right joystick Y-axis (-1.0 to 1.0, forward = negative raw)
   * @param joystickX Right joystick X-axis (-1.0 to 1.0)
   * @param r2Trigger R2 trigger axis (-1.0 to 1.0 raw)
   * @param l2Trigger L2 trigger axis (-1.0 to 1.0 raw)
   */
  public TeleopDrive(
      Drive drive,
      DoubleSupplier joystickY,
      DoubleSupplier joystickX,
      DoubleSupplier r2Trigger,
      DoubleSupplier l2Trigger) {
    m_drive = drive;
    m_joystickY = joystickY;
    m_joystickX = joystickX;
    m_r2Trigger = r2Trigger;
    m_l2Trigger = l2Trigger;

    addRequirements(drive);
  }

  @Override
  public void initialize() {}

  @Override
  public void execute() {
    // Invert Y so pushing forward = positive
    double forward = -m_joystickY.getAsDouble();
    double turn = m_joystickX.getAsDouble();

    // Normalize triggers from [-1.0, 1.0] to [0.0, 1.0]
    double r2 = (m_r2Trigger.getAsDouble() + 1.0) / 2.0;
    double l2 = (m_l2Trigger.getAsDouble() + 1.0) / 2.0;

    // Apply deadbands
    forward = applyDeadband(forward, DriveConstants.kJoystickDeadband);
    turn = applyDeadband(turn, DriveConstants.kJoystickDeadband);
    r2 = applyDeadband(r2, DriveConstants.kTriggerDeadband);
    l2 = applyDeadband(l2, DriveConstants.kTriggerDeadband);

    // Scale triggers
    r2 *= DriveConstants.kTriggerScale;
    l2 *= DriveConstants.kTriggerScale;

    // Blend: triggers add to/subtract from the forward component
    double forwardComponent = forward + r2 - l2;

    // Arcade mixing: left side faster when turning left, right side faster when turning right
    double leftSpeed = forwardComponent + turn;
    double rightSpeed = forwardComponent - turn;

    // Clamp to [-1.0, 1.0]
    leftSpeed = Math.max(-1.0, Math.min(1.0, leftSpeed));
    rightSpeed = Math.max(-1.0, Math.min(1.0, rightSpeed));

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
}
