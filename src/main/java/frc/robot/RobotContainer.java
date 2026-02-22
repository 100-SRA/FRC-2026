// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import frc.robot.Constants.OperatorConstants;
import frc.robot.commands.RunCollector;
import frc.robot.commands.RunLoader;
import frc.robot.commands.RunShooter;
import frc.robot.commands.TeleopDrive;
import frc.robot.subsystems.Collector;
import frc.robot.subsystems.Drive;
import frc.robot.subsystems.Loader;
import frc.robot.subsystems.Shooter;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.button.CommandPS4Controller;
import edu.wpi.first.wpilibj2.command.button.Trigger;

/**
 * This class is where the bulk of the robot should be declared. Since Command-based is a
 * "declarative" paradigm, very little robot logic should actually be handled in the {@link Robot}
 * periodic methods (other than the scheduler calls). Instead, the structure of the robot (including
 * subsystems, commands, and trigger mappings) should be declared here.
 */
public class RobotContainer {
  // Subsystems
  private final Drive     m_drive     = new Drive();
  private final Collector m_collector = new Collector();
  private final Loader    m_loader    = new Loader();
  private final Shooter   m_shooter   = new Shooter();

  /**
   * Controller Configuration:
   *
   * Driver Controller (Port 0):
   *   - R2 trigger:       both drive motors forward
   *   - L2 trigger:       both drive motors backward
   *   - Right joystick Y: forward/backward (arcade)
   *   - Right joystick X: turning (arcade)
   *
   * Operator Controller (Port 1):
   *   - R2 (analog, hold): collector — speed proportional to trigger pressure
   *   - L2 (analog, hold): loader    — speed proportional to trigger pressure
   *   - Cross (hold):      shooter   — fixed speed
   */
  private final CommandPS4Controller m_driverController =
      new CommandPS4Controller(OperatorConstants.kDriverControllerPort);

  private final CommandPS4Controller m_operatorController =
      new CommandPS4Controller(OperatorConstants.kOperatorControllerPort);

  public RobotContainer() {
    configureBindings();

    m_drive.setDefaultCommand(
        new TeleopDrive(
            m_drive,
            () -> m_driverController.getRightY(),
            () -> m_driverController.getRightX(),
            () -> m_driverController.getR2Axis(),
            () -> m_driverController.getL2Axis()));
  }

  private void configureBindings() {
    // Operator R2 (analog) — collector speed proportional to trigger pressure
    new Trigger(() -> m_operatorController.getR2Axis() > 0.05)
        .whileTrue(new RunCollector(m_collector, () -> m_operatorController.getR2Axis()));

    // Operator L2 (analog) — loader speed proportional to trigger pressure
    new Trigger(() -> m_operatorController.getL2Axis() > 0.05)
        .whileTrue(new RunLoader(m_loader, () -> m_operatorController.getL2Axis()));

    // Operator Cross — shooter at fixed speed
    m_operatorController.cross().whileTrue(new RunShooter(m_shooter));
  }

  public Command getAutonomousCommand() {
    return null;
  }
}
