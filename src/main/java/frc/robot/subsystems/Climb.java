// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

// CLIMB DISABLED — mechanism is too long and is currently illegal.
// Re-enable by uncommenting the block below.

/*
import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.SparkLowLevel.MotorType;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants.ClimbConstants;

// Climb subsystem. Two NEO motors via SPARK MAX on CAN IDs 6 and 7.
public class Climb extends SubsystemBase {
  private final SparkMax m_leftMotor;
  private final SparkMax m_rightMotor;

  public Climb() {
    m_leftMotor  = new SparkMax(ClimbConstants.kClimbLeftCanId,  MotorType.kBrushless);
    m_rightMotor = new SparkMax(ClimbConstants.kClimbRightCanId, MotorType.kBrushless);
    m_leftMotor.setInverted(ClimbConstants.kClimbLeftInverted);
    m_rightMotor.setInverted(ClimbConstants.kClimbRightInverted);
  }

  // Drives both motors up at the configured climb speed.
  public void climbUp() {
    m_leftMotor.set(ClimbConstants.kClimbUpSpeed);
    m_rightMotor.set(ClimbConstants.kClimbUpSpeed);
  }

  // Drives both motors down (lowers/reverses) at the configured speed.
  public void climbDown() {
    m_leftMotor.set(ClimbConstants.kClimbDownSpeed);
    m_rightMotor.set(ClimbConstants.kClimbDownSpeed);
  }

  // Stops both climb motors.
  public void stop() {
    m_leftMotor.set(0.0);
    m_rightMotor.set(0.0);
  }
}
*/
