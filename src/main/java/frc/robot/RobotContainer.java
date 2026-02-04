// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import frc.robot.Constants.OperatorConstants;
import frc.robot.commands.TeleopDrive;
import frc.robot.subsystems.Drive;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.button.CommandPS4Controller;

/**
 * This class is where the bulk of the robot should be declared. Since Command-based is a
 * "declarative" paradigm, very little robot logic should actually be handled in the {@link Robot}
 * periodic methods (other than the scheduler calls). Instead, the structure of the robot (including
 * subsystems, commands, and trigger mappings) should be declared here.
 */
public class RobotContainer {
  // The robot's subsystems and commands are defined here...
  private final Drive m_drive = new Drive();

  // PS4 controller for driver (port 0)
  private final CommandPS4Controller m_driverController =
      new CommandPS4Controller(OperatorConstants.kDriverControllerPort);

  /** The container for the robot. Contains subsystems, OI devices, and commands. */
  public RobotContainer() {
    // Configure the trigger bindings
    configureBindings();

    // Set default command for Drive subsystem
    // L2 trigger controls left side, R2 trigger controls right side
    // Left joystick Y controls both sides synchronized (additive mixing)
    m_drive.setDefaultCommand(
        new TeleopDrive(
            m_drive,
            () -> m_driverController.getLeftY(),
            () -> m_driverController.getL2Axis(),
            () -> m_driverController.getR2Axis()));
  }

  /**
   * Use this method to define your trigger->command mappings.
   *
   * <p>Button bindings can be added here for additional robot functionality.
   * For example:
   * - m_driverController.cross().onTrue(Commands.runOnce(() -> m_drive.stop(), m_drive));
   * - m_driverController.square().whileTrue(slowModeCommand);
   */
  private void configureBindings() {
    // Add button bindings here for additional features
    // The drive controls (L2/R2/Left Joystick) are handled by the default TeleopDrive command
  }

  /**
   * Use this to pass the autonomous command to the main {@link Robot} class.
   *
   * @return the command to run in autonomous
   */
  public Command getAutonomousCommand() {
    // No autonomous command configured yet
    // TODO: Add autonomous routines here
    return null;
  }
}
