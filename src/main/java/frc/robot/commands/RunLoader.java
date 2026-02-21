// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.Loader;

/** Runs the loader while the command is active. Use with whileTrue() on a button. */
public class RunLoader extends Command {
  private final Loader m_loader;

  public RunLoader(Loader loader) {
    m_loader = loader;
    addRequirements(loader);
  }

  @Override
  public void initialize() {
    m_loader.run();
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
