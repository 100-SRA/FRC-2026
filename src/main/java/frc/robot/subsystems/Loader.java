// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import edu.wpi.first.wpilibj.motorcontrol.Spark;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants.LoaderConstants;

/** Loader (shooter motor) subsystem. Runs a single PWM motor to shoot game pieces. */
public class Loader extends SubsystemBase {
  private final Spark m_motor;

  public Loader() {
    m_motor = new Spark(LoaderConstants.kLoaderMotorPort);
    m_motor.setInverted(LoaderConstants.kLoaderInverted);
  }

  /** Runs the loader motor at the configured speed. */
  public void run() {
    m_motor.set(LoaderConstants.kLoaderSpeed);
  }

  /** Stops the loader motor. */
  public void stop() {
    m_motor.set(0.0);
  }

  @Override
  public void periodic() {}
}
