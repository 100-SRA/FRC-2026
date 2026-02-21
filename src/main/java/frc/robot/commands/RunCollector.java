// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.Collector;

/** Runs the collector while the command is active. Use with whileTrue() on a button. */
public class RunCollector extends Command {
  private final Collector m_collector;

  public RunCollector(Collector collector) {
    m_collector = collector;
    addRequirements(collector);
  }

  @Override
  public void initialize() {
    m_collector.run();
  }

  @Override
  public void end(boolean interrupted) {
    m_collector.stop();
  }

  @Override
  public boolean isFinished() {
    return false;
  }
}
