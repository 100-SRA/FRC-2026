// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.Shooter;
import java.util.function.DoubleSupplier;

/**
 * Runs the shooter at a speed proportional to the R2 trigger axis.
 * Activated while the trigger is held past the deadband; releasing stops the motor.
 */
public class RunShooter extends Command {
  private final Shooter m_shooter;
  private final DoubleSupplier m_speed;

  public RunShooter(Shooter shooter, DoubleSupplier speed) {
    m_shooter = shooter;
    m_speed = speed;
    addRequirements(shooter);
  }

  @Override
  public void execute() {
    // Normalize R2 axis from [-1.0, 1.0] to [0.0, 1.0]
    double speed = (m_speed.getAsDouble() + 1.0) / 2.0;
    m_shooter.run(speed);
  }

  @Override
  public void end(boolean interrupted) {
    m_shooter.stop();
  }

  @Override
  public boolean isFinished() {
    return false;
  }
}
