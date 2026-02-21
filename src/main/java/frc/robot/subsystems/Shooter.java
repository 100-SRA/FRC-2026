// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import edu.wpi.first.wpilibj.motorcontrol.Spark;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants.ShooterConstants;

/** Shooter subsystem. Runs a single PWM Spark motor on port 5 to shoot game pieces. */
public class Shooter extends SubsystemBase {
  private final Spark m_motor;

  public Shooter() {
    m_motor = new Spark(ShooterConstants.kShooterMotorPort);
    m_motor.setInverted(ShooterConstants.kShooterInverted);
  }

  /** Runs the shooter motor at the configured speed. */
  public void run() {
    m_motor.set(ShooterConstants.kShooterSpeed);
  }

  /** Stops the shooter motor. */
  public void stop() {
    m_motor.set(0.0);
  }

  @Override
  public void periodic() {}
}
