// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.Climb;

/**
 * Runs the climb mechanism in a given direction while held.
 * Stops both motors when the command ends.
 */
public class RunClimb extends Command {
  public enum ClimbDirection { UP, DOWN }

  private final Climb          m_climb;
  private final ClimbDirection m_direction;

  public RunClimb(Climb climb, ClimbDirection direction) {
    m_climb     = climb;
    m_direction = direction;
    addRequirements(climb);
  }

  @Override
  public void execute() {
    if (m_direction == ClimbDirection.UP) m_climb.climbUp();
    else                                  m_climb.climbDown();
  }

  @Override
  public void end(boolean interrupted) { m_climb.stop(); }

  @Override
  public boolean isFinished() { return false; }
}
