// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import frc.robot.Constants.OperatorConstants;
import frc.robot.commands.RunCollector;
import frc.robot.commands.RunLoader;
import frc.robot.commands.TeleopDrive;
import frc.robot.subsystems.Collector;
import frc.robot.subsystems.Drive;
import frc.robot.subsystems.Loader;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.button.CommandPS4Controller;

/**
 * This class is where the bulk of the robot should be declared. Since Command-based is a
 * "declarative" paradigm, very little robot logic should actually be handled in the {@link Robot}
 * periodic methods (other than the scheduler calls). Instead, the structure of the robot (including
 * subsystems, commands, and trigger mappings) should be declared here.
 */
public class RobotContainer {
  // Subsystems
  private final Drive m_drive = new Drive();
  private final Collector m_collector = new Collector();
  private final Loader m_loader = new Loader();

  /**
   * Controller Configuration:
   * - Driver Controller (Port 0):
   *   - R2 trigger: both motors forward
   *   - L2 trigger: both motors backward
   *   - Right joystick Y: forward/backward (arcade)
   *   - Right joystick X: turning (arcade)
   *
   * - Operator Controller (Port 1):
   *   - R1 (hold): run collector
   *   - L1 (hold): run loader
   */
  private final CommandPS4Controller m_driverController =
      new CommandPS4Controller(OperatorConstants.kDriverControllerPort);

  private final CommandPS4Controller m_operatorController =
      new CommandPS4Controller(OperatorConstants.kOperatorControllerPort);

  /** The container for the robot. Contains subsystems, OI devices, and commands. */
  public RobotContainer() {
    configureBindings();

    // R2 = both forward, L2 = both backward, right joystick = arcade drive
    m_drive.setDefaultCommand(
        new TeleopDrive(
            m_drive,
            () -> m_driverController.getRightY(),
            () -> m_driverController.getRightX(),
            () -> m_driverController.getR2Axis(),
            () -> m_driverController.getL2Axis()));
  }

  private void configureBindings() {
    // Operator: hold R1 to run collector, hold L1 to run loader
    m_operatorController.r1().whileTrue(new RunCollector(m_collector));
    m_operatorController.l1().whileTrue(new RunLoader(m_loader));
  }

  public Command getAutonomousCommand() {
    return null;
  }
}
