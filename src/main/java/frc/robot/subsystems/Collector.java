// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkLowLevel.MotorType;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants.CollectorConstants;

/**
 * Collector (intake) subsystem.
 * Uses a REV SPARK MAX motor controller over CAN.
 * TODO: Set kCollectorCanId in Constants.java once the CAN ID is confirmed via REV Hardware Client.
 */
public class Collector extends SubsystemBase {
  private final CANSparkMax m_motor;

  public Collector() {
    m_motor = new CANSparkMax(CollectorConstants.kCollectorCanId, MotorType.kBrushless);
    m_motor.setInverted(CollectorConstants.kCollectorInverted);
  }

  /** Runs the collector motor at the configured speed. */
  public void run() {
    m_motor.set(CollectorConstants.kCollectorSpeed);
  }

  /** Stops the collector motor. */
  public void stop() {
    m_motor.set(0.0);
  }

  @Override
  public void periodic() {}
}
