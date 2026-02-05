// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;

/**
 * Utility class for autonomous command factories.
 * Add autonomous routines here as static methods.
 */
public final class Autos {

  /**
   * Example autonomous command - currently returns a command that does nothing.
   * Replace this with actual autonomous routines when ready.
   *
   * @return a command that does nothing
   */
  public static Command exampleAuto() {
    return Commands.none();
  }

  private Autos() {
    throw new UnsupportedOperationException("This is a utility class!");
  }
}
