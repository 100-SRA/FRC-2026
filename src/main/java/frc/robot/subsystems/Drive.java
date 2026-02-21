// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants.DriveConstants;

// Import REV Robotics library for SparkMax motor controllers
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkLowLevel.MotorType;
import com.revrobotics.RelativeEncoder;

/**
 * Drive subsystem for 6-motor tank drive (3 motors per side).
 * Uses CANSparkMax motor controllers with CAN bus.
 *
 * Control scheme:
 * - Tank drive with independent left/right control
 * - L2 trigger controls left side
 * - R2 trigger controls right side
 * - Left joystick Y controls both sides synchronized
 */
public class Drive extends SubsystemBase {
  // Left side motor controllers (CAN)
  private final CANSparkMax m_leftFront;
  private final CANSparkMax m_leftMiddle;
  private final CANSparkMax m_leftBack;

  // Right side motor controllers (CAN)
  private final CANSparkMax m_rightFront;
  private final CANSparkMax m_rightMiddle;
  private final CANSparkMax m_rightBack;

  // Encoders for telemetry and future autonomous use
  private final RelativeEncoder m_leftEncoder;
  private final RelativeEncoder m_rightEncoder;

  /** Creates a new Drive subsystem. */
  public Drive() {
    // Initialize left side motors with CAN IDs
    // MotorType.kBrushless for NEO motors, use kBrushed for CIM/BAG motors
    m_leftFront = new CANSparkMax(DriveConstants.kLeftFrontMotorID, MotorType.kBrushless);
    m_leftMiddle = new CANSparkMax(DriveConstants.kLeftMiddleMotorID, MotorType.kBrushless);
    m_leftBack = new CANSparkMax(DriveConstants.kLeftBackMotorID, MotorType.kBrushless);

    // Initialize right side motors with CAN IDs
    m_rightFront = new CANSparkMax(DriveConstants.kRightFrontMotorID, MotorType.kBrushless);
    m_rightMiddle = new CANSparkMax(DriveConstants.kRightMiddleMotorID, MotorType.kBrushless);
    m_rightBack = new CANSparkMax(DriveConstants.kRightBackMotorID, MotorType.kBrushless);

    // Restore all SparkMaxes to factory defaults (recommended by REV)
    m_leftFront.restoreFactoryDefaults();
    m_leftMiddle.restoreFactoryDefaults();
    m_leftBack.restoreFactoryDefaults();
    m_rightFront.restoreFactoryDefaults();
    m_rightMiddle.restoreFactoryDefaults();
    m_rightBack.restoreFactoryDefaults();

    // Set motor inversions based on constants
    m_leftFront.setInverted(DriveConstants.kLeftMotorsInverted);
    m_leftMiddle.setInverted(DriveConstants.kLeftMotorsInverted);
    m_leftBack.setInverted(DriveConstants.kLeftMotorsInverted);
    m_rightFront.setInverted(DriveConstants.kRightMotorsInverted);
    m_rightMiddle.setInverted(DriveConstants.kRightMotorsInverted);
    m_rightBack.setInverted(DriveConstants.kRightMotorsInverted);

    // Configure follower motors to follow the leader motors
    m_leftMiddle.follow(m_leftFront);
    m_leftBack.follow(m_leftFront);
    m_rightMiddle.follow(m_rightFront);
    m_rightBack.follow(m_rightFront);

    // Get encoders from leader motors for telemetry
    m_leftEncoder = m_leftFront.getEncoder();
    m_rightEncoder = m_rightFront.getEncoder();

    // Burn flash to save configurations (prevents loss on power cycle)
    m_leftFront.burnFlash();
    m_leftMiddle.burnFlash();
    m_leftBack.burnFlash();
    m_rightFront.burnFlash();
    m_rightMiddle.burnFlash();
    m_rightBack.burnFlash();
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

    // Set motor speeds (only leader motors, followers will automatically follow)
    m_leftFront.set(leftSpeed);
    m_rightFront.set(rightSpeed);
  }

  /**
   * Sets left side motor speed only.
   *
   * @param speed Speed for left side motors (-1.0 to 1.0)
   */
  public void setLeftSpeed(double speed) {
    speed = Math.max(-1.0, Math.min(1.0, speed));
    speed *= DriveConstants.kMaxSpeed;
    m_leftFront.set(speed);
  }

  /**
   * Sets right side motor speed only.
   *
   * @param speed Speed for right side motors (-1.0 to 1.0)
   */
  public void setRightSpeed(double speed) {
    speed = Math.max(-1.0, Math.min(1.0, speed));
    speed *= DriveConstants.kMaxSpeed;
    m_rightFront.set(speed);
  }

  /**
   * Stops all motors immediately.
   */
  public void stop() {
    m_leftFront.set(0.0);
    m_rightFront.set(0.0);
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
    // Update telemetry for driver dashboard
    SmartDashboard.putNumber("Drive/Left Speed", m_leftFront.get());
    SmartDashboard.putNumber("Drive/Right Speed", m_rightFront.get());

    // Encoder telemetry (position and velocity)
    SmartDashboard.putNumber("Drive/Left Encoder Position", m_leftEncoder.getPosition());
    SmartDashboard.putNumber("Drive/Right Encoder Position", m_rightEncoder.getPosition());
    SmartDashboard.putNumber("Drive/Left Encoder Velocity", m_leftEncoder.getVelocity());
    SmartDashboard.putNumber("Drive/Right Encoder Velocity", m_rightEncoder.getVelocity());

    // Temperature monitoring (important for SparkMax health)
    SmartDashboard.putNumber("Drive/Left Front Temp (C)", m_leftFront.getMotorTemperature());
    SmartDashboard.putNumber("Drive/Right Front Temp (C)", m_rightFront.getMotorTemperature());
  }

  @Override
  public void simulationPeriodic() {
    // This method will be called once per scheduler run during simulation
  }
}
