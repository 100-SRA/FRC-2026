// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import edu.wpi.first.wpilibj.motorcontrol.Spark;
import edu.wpi.first.wpilibj.motorcontrol.MotorControllerGroup;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants.DriveConstants;

/**
 * Drive subsystem for 6-motor tank drive (3 motors per side).
 * Uses PWM Spark motor controllers.
 *
 * Control scheme:
 * - Tank drive with independent left/right control
 * - L2 trigger controls left side
 * - R2 trigger controls right side
 * - Left joystick Y controls both sides synchronized
 */
public class Drive extends SubsystemBase {
  // Left side motor controllers (PWM)
  private final Spark m_leftFront;
  private final Spark m_leftMiddle;
  private final Spark m_leftBack;

  // Right side motor controllers (PWM)
  private final Spark m_rightFront;
  private final Spark m_rightMiddle;
  private final Spark m_rightBack;

  // Motor controller groups for synchronized control
  private final MotorControllerGroup m_leftMotors;
  private final MotorControllerGroup m_rightMotors;

  /** Creates a new Drive subsystem. */
  public Drive() {
    // Initialize left side motors
    m_leftFront = new Spark(DriveConstants.kLeftFrontMotorPort);
    m_leftMiddle = new Spark(DriveConstants.kLeftMiddleMotorPort);
    m_leftBack = new Spark(DriveConstants.kLeftBackMotorPort);

    // Initialize right side motors
    m_rightFront = new Spark(DriveConstants.kRightFrontMotorPort);
    m_rightMiddle = new Spark(DriveConstants.kRightMiddleMotorPort);
    m_rightBack = new Spark(DriveConstants.kRightBackMotorPort);

    // Group motors by side for synchronized control
    m_leftMotors = new MotorControllerGroup(m_leftFront, m_leftMiddle, m_leftBack);
    m_rightMotors = new MotorControllerGroup(m_rightFront, m_rightMiddle, m_rightBack);

    // Set motor inversions based on constants
    m_leftMotors.setInverted(DriveConstants.kLeftMotorsInverted);
    m_rightMotors.setInverted(DriveConstants.kRightMotorsInverted);
  }

  /**
   * Tank drive control - sets left and right side speeds independently.
   *
   * @param leftSpeed Speed for left side motors (-1.0 to 1.0)
   * @param rightSpeed Speed for right side motors (-1.0 to 1.0)
   */
  public void tankDrive(double leftSpeed, double rightSpeed) {
    // Clamp speeds to valid range
    leftSpeed = Math.max(-1.0, Math.min(1.0, leftSpeed));
    rightSpeed = Math.max(-1.0, Math.min(1.0, rightSpeed));

    // Apply max speed limit
    leftSpeed *= DriveConstants.kMaxSpeed;
    rightSpeed *= DriveConstants.kMaxSpeed;

    // Set motor speeds
    m_leftMotors.set(leftSpeed);
    m_rightMotors.set(rightSpeed);
  }

  /**
   * Sets left side motor speed only.
   *
   * @param speed Speed for left side motors (-1.0 to 1.0)
   */
  public void setLeftSpeed(double speed) {
    speed = Math.max(-1.0, Math.min(1.0, speed));
    speed *= DriveConstants.kMaxSpeed;
    m_leftMotors.set(speed);
  }

  /**
   * Sets right side motor speed only.
   *
   * @param speed Speed for right side motors (-1.0 to 1.0)
   */
  public void setRightSpeed(double speed) {
    speed = Math.max(-1.0, Math.min(1.0, speed));
    speed *= DriveConstants.kMaxSpeed;
    m_rightMotors.set(speed);
  }

  /**
   * Stops all motors immediately.
   */
  public void stop() {
    m_leftMotors.set(0.0);
    m_rightMotors.set(0.0);
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
    // Update telemetry for driver dashboard
    SmartDashboard.putNumber("Drive/Left Speed", m_leftFront.get());
    SmartDashboard.putNumber("Drive/Right Speed", m_rightFront.get());
    // Note: PWM Spark controllers don't provide temperature telemetry
  }

  @Override
  public void simulationPeriodic() {
    // This method will be called once per scheduler run during simulation
  }
}
