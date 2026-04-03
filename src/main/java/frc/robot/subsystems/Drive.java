// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import edu.wpi.first.wpilibj.motorcontrol.Spark;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants.DriveConstants;

/**
 * Drive subsystem for 4-motor tank drive (2 motors per side).
 * Uses PWM Spark motor controllers on ports 0-3.
 *
 * Control scheme:
 * - R2 trigger: both sides forward
 * - L2 trigger: both sides backward
 * - Left joystick Y: left side motors (tank)
 * - Right joystick Y: right side motors (tank)
 */
public class Drive extends SubsystemBase {
  // Left side motors (PWM ports 0 and 1)
  private final Spark m_leftFront;
  private final Spark m_leftBack;

  // Right side motors (PWM ports 2 and 3)
  private final Spark m_rightFront;
  private final Spark m_rightBack;

  /** Creates a new Drive subsystem. */
  public Drive() {
    m_leftFront  = new Spark(DriveConstants.kLeftFrontMotorPort);
    m_leftBack   = new Spark(DriveConstants.kLeftBackMotorPort);
    m_rightFront = new Spark(DriveConstants.kRightFrontMotorPort);
    m_rightBack  = new Spark(DriveConstants.kRightBackMotorPort);

    m_leftFront.setInverted(DriveConstants.kLeftMotorsInverted);
    m_leftBack.setInverted(DriveConstants.kLeftMotorsInverted);
    m_rightFront.setInverted(DriveConstants.kRightMotorsInverted);
    m_rightBack.setInverted(DriveConstants.kRightMotorsInverted);
  }

  /**
   * Tank drive — sets left and right side speeds independently.
   *
   * @param leftSpeed  Speed for left side motors (-1.0 to 1.0)
   * @param rightSpeed Speed for right side motors (-1.0 to 1.0)
   */
  public void tankDrive(double leftSpeed, double rightSpeed) {
    leftSpeed  = Math.max(-1.0, Math.min(1.0, leftSpeed))  * DriveConstants.kMaxSpeed;
    rightSpeed = Math.max(-1.0, Math.min(1.0, rightSpeed)) * DriveConstants.kMaxSpeed;
    m_leftFront.set(leftSpeed);
    m_leftBack.set(leftSpeed);
    m_rightFront.set(rightSpeed);
    m_rightBack.set(rightSpeed);
  }

  /** Stops all drive motors immediately. */
  public void stop() {
    m_leftFront.set(0.0);
    m_leftBack.set(0.0);
    m_rightFront.set(0.0);
    m_rightBack.set(0.0);
  }

  @Override
  public void periodic() {
    SmartDashboard.putNumber("Drive/Left Speed",  m_leftFront.get());
    SmartDashboard.putNumber("Drive/Right Speed", m_rightFront.get());
  }

}
