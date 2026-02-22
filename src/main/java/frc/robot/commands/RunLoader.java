// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.Loader;
import java.util.function.DoubleSupplier;

/**
 * Runs the loader at a speed proportional to the L2 trigger axis.
 * Activated while the trigger is held past the deadband; releasing stops the motor.
 */
public class RunLoader extends Command {
  private final Loader m_loader;
  private final DoubleSupplier m_speed;

  public RunLoader(Loader loader, DoubleSupplier speed) {
    m_loader = loader;
    m_speed = speed;
    addRequirements(loader);
  }

  @Override
  public void execute() {
    // Normalize L2 axis from [-1.0, 1.0] to [0.0, 1.0]
    double speed = (m_speed.getAsDouble() + 1.0) / 2.0;
    m_loader.run(speed);
  }

  @Override
  public void end(boolean interrupted) {
    m_loader.stop();
  }

  @Override
  public boolean isFinished() {
    return false;
  }
}
