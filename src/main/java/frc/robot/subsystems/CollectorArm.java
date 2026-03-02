// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import edu.wpi.first.wpilibj.motorcontrol.Spark;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants.CollectorArmConstants;

/** Winds/unwinds a string via PWM Spark on port 6 to retract or extend the collector arm. */
public class CollectorArm extends SubsystemBase {
  private final Spark m_motor;

  public CollectorArm() {
    m_motor = new Spark(CollectorArmConstants.kCollectorArmMotorPort);
    m_motor.setInverted(CollectorArmConstants.kCollectorArmInverted);
  }

  /** Winds the string to pull the collector in. */
  public void retract() {
    m_motor.set(CollectorArmConstants.kCollectorArmRetractSpeed);
  }

  /** Unwinds the string to lower/extend the collector. */
  public void extend() {
    m_motor.set(CollectorArmConstants.kCollectorArmExtendSpeed);
  }

  /** Stops the motor, holding current position. */
  public void stop() {
    m_motor.set(0.0);
  }

  @Override
  public void periodic() {}
}
