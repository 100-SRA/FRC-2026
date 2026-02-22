// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.SparkLowLevel.MotorType;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants.CollectorConstants;

/** Collector (intake) subsystem. Uses a REV SPARK MAX on CAN ID 3. */
public class Collector extends SubsystemBase {
  private final SparkMax m_motor;

  public Collector() {
    m_motor = new SparkMax(CollectorConstants.kCollectorCanId, MotorType.kBrushless);
    m_motor.setInverted(CollectorConstants.kCollectorInverted);
  }

  /** Runs the collector motor at the configured fixed speed. */
  public void run() {
    m_motor.set(CollectorConstants.kCollectorSpeed);
  }

  /** Runs the collector motor at a variable speed (0.0 to 1.0). */
  public void run(double speed) {
    m_motor.set(speed);
  }

  /** Stops the collector motor. */
  public void stop() {
    m_motor.set(0.0);
  }

  @Override
  public void periodic() {}
}
