// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.motorcontrol.Spark;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants.ShooterConstants;

/** Shooter subsystem. Runs a single PWM Spark motor on port 5 to shoot game pieces. */
public class Shooter extends SubsystemBase {
  private final Spark m_motor;

  private double m_commandedSpeed = 0.0;
  private final Timer m_spinUpTimer = new Timer();
  private boolean m_timerRunning = false;

  public Shooter() {
    m_motor = new Spark(ShooterConstants.kShooterMotorPort);
    m_motor.setInverted(ShooterConstants.kShooterInverted);
  }

  /** Runs the shooter motor at the given speed (0.0 to 1.0). Tracks spin-up time for the interlock. */
  public void run(double speed) {
    m_motor.set(speed);
    m_commandedSpeed = speed;

    if (speed >= ShooterConstants.kShooterReadyThreshold) {
      if (!m_timerRunning) {
        m_spinUpTimer.reset();
        m_spinUpTimer.start();
        m_timerRunning = true;
      }
    } else {
      m_spinUpTimer.stop();
      m_spinUpTimer.reset();
      m_timerRunning = false;
    }
  }

  /** Stops the shooter motor and resets spin-up state. */
  public void stop() {
    m_motor.set(0.0);
    m_commandedSpeed = 0.0;
    m_spinUpTimer.stop();
    m_spinUpTimer.reset();
    m_timerRunning = false;
  }

  /**
   * Returns true when the shooter has been commanded at or above the ready threshold
   * for at least the configured spin-up duration. Since there is no encoder, this is
   * a time-based proxy for flywheel speed.
   */
  public boolean isReadyToShoot() {
    return m_timerRunning && m_spinUpTimer.hasElapsed(ShooterConstants.kShooterSpinUpSeconds);
  }

}
