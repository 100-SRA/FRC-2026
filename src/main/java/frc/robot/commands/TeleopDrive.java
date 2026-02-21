// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.Drive;
import frc.robot.Constants.DriveConstants;
import java.util.function.DoubleSupplier;

/**
 * TeleopDrive command for unique PS4 controller scheme:
 * - Left joystick Y: Controls both sides synchronized (forward/backward)
 * - L2 trigger: Adds power to LEFT side motors
 * - R2 trigger: Adds power to RIGHT side motors
 * - Additive mixing: Triggers and joystick combine for advanced maneuvering
 *
 * <p>Control examples:
 * - Joystick forward → Straight ahead
 * - L2 only → Left side spins (turn right in place)
 * - R2 only → Right side spins (turn left in place)
 * - Joystick + L2 → Left side faster (turn right while moving)
 * - Joystick + R2 → Right side faster (turn left while moving)
 * - L2 + R2 → Independent differential control
 */
public class TeleopDrive extends Command {
  private final Drive m_drive;
  private final DoubleSupplier m_joystickY;
  private final DoubleSupplier m_leftTrigger;
  private final DoubleSupplier m_rightTrigger;

  /**
   * Creates a new TeleopDrive command.
   *
   * @param drive The drive subsystem this command will control
   * @param joystickY Supplier for left joystick Y-axis (-1.0 to 1.0)
   * @param leftTrigger Supplier for L2 trigger axis (0.0 to 1.0)
   * @param rightTrigger Supplier for R2 trigger axis (0.0 to 1.0)
   */
  public TeleopDrive(
      Drive drive,
      DoubleSupplier joystickY,
      DoubleSupplier leftTrigger,
      DoubleSupplier rightTrigger) {
    m_drive = drive;
    m_joystickY = joystickY;
    m_leftTrigger = leftTrigger;
    m_rightTrigger = rightTrigger;

    // This command requires the Drive subsystem
    addRequirements(drive);
  }

  @Override
  public void initialize() {
    // Called when the command is initially scheduled
  }

  @Override
  public void execute() {
    // Get raw inputs from controller
    // Invert joystick Y so pushing forward = positive (forward drive)
    double joystick = -m_joystickY.getAsDouble();
    // Normalize triggers from [-1.0, 1.0] to [0.0, 1.0]
    // PS4 triggers report -1.0 when fully released, +1.0 when fully pressed
    double leftTrig = (m_leftTrigger.getAsDouble() + 1.0) / 2.0;
    double rightTrig = (m_rightTrigger.getAsDouble() + 1.0) / 2.0;

    // Apply deadbands to ignore small unintentional inputs
    joystick = applyDeadband(joystick, DriveConstants.kJoystickDeadband);
    leftTrig = applyDeadband(leftTrig, DriveConstants.kTriggerDeadband);
    rightTrig = applyDeadband(rightTrig, DriveConstants.kTriggerDeadband);

    // Scale triggers for finer control
    leftTrig *= DriveConstants.kTriggerScale;
    rightTrig *= DriveConstants.kTriggerScale;

    // Additive mixing: Combine joystick with triggers
    // Left side = joystick (baseline) + left trigger (extra left power)
    // Right side = joystick (baseline) + right trigger (extra right power)
    double leftSpeed = joystick + leftTrig;
    double rightSpeed = joystick + rightTrig;

    // Clamp speeds to valid range [-1.0, 1.0]
    leftSpeed = Math.max(-1.0, Math.min(1.0, leftSpeed));
    rightSpeed = Math.max(-1.0, Math.min(1.0, rightSpeed));

    // Send commands to drive subsystem
    m_drive.tankDrive(leftSpeed, rightSpeed);
  }

  @Override
  public void end(boolean interrupted) {
    // Stop motors when command ends
    m_drive.stop();
  }

  @Override
  public boolean isFinished() {
    // This command never finishes - runs continuously as default command
    return false;
  }

  /**
   * Applies deadband to input value.
   * If the absolute value is below the deadband threshold, returns 0.
   *
   * @param value The input value
   * @param deadband The deadband threshold
   * @return The value with deadband applied
   */
  private double applyDeadband(double value, double deadband) {
    if (Math.abs(value) < deadband) {
      return 0.0;
    }
    return value;
  }
}
