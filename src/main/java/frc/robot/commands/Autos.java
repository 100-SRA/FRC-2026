// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.subsystems.Climb;

/**
 * Utility class for autonomous command factories.
 */
public final class Autos {

  /**
   * Primary auto: climb up for the full autonomous period (15 seconds).
   */
  public static Command climb(Climb climb) {
    return new SequentialCommandGroup(
        Commands.run(climb::climbUp, climb)
                .withTimeout(15.0),
        Commands.runOnce(climb::stop, climb)
    );
  }

  private Autos() {
    throw new UnsupportedOperationException("This is a utility class!");
  }
}
