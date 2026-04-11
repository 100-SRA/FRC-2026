// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import frc.robot.Constants.CollectorConstants;
import frc.robot.Constants.OperatorConstants;
import frc.robot.Constants.ShooterConstants;
import frc.robot.commands.Autos;
// import frc.robot.commands.RunClimb;           // CLIMB DISABLED
// import frc.robot.commands.RunClimb.ClimbDirection; // CLIMB DISABLED
import frc.robot.commands.RunCollector;
import frc.robot.commands.RunCollectorArm;
import frc.robot.commands.RunCollectorArm.CollectorArmMode;
import frc.robot.commands.RunLoader;
import frc.robot.commands.RunShooter;
import frc.robot.commands.TeleopDrive;
// import frc.robot.subsystems.Climb;            // CLIMB DISABLED
import frc.robot.subsystems.Collector;
import frc.robot.subsystems.CollectorArm;
import frc.robot.subsystems.Drive;
import frc.robot.subsystems.Loader;
import frc.robot.subsystems.Shooter;
import frc.robot.subsystems.Vision;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
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
  private final Drive        m_drive        = new Drive();
  private final Collector    m_collector    = new Collector();
  private final Loader       m_loader       = new Loader();
  private final Shooter      m_shooter      = new Shooter();
  private final CollectorArm m_collectorArm = new CollectorArm();
  // private final Climb     m_climb        = new Climb(); // CLIMB DISABLED
  private final Vision       m_vision       = new Vision();

  private final SendableChooser<Command> m_autoChooser = new SendableChooser<>();

  /**
   * Controller Configuration:
   *
   * Driver Controller (Port 0):
   *   - R2 trigger:        both drive motors forward
   *   - L2 trigger:        both drive motors backward
   *   - Left joystick Y:   left side motors (tank)
   *   - Right joystick Y:  right side motors (tank)
   *   - R1 (hold):         [DISABLED — was climb up]
   *   - L1 (hold):         [DISABLED — was climb down]
   *
   * Operator Controller (Port 1):
   *   - R2 (analog, hold): shooter         — speed proportional to trigger pressure
   *   - L2 (analog, hold): collector       — speed proportional to trigger pressure
   *   - L1 (hold):         collector       — reverse (eject)
   *   - Circle (hold):     loader          — fixed speed (feed)
   *   - X (hold):          shooter         — 10% preset (close range)
   *   - D-pad Up (hold):   collector arm   — retract (wind string)
   *   - D-pad Down (hold): collector arm   — extend  (unwind string)
   *   - D-pad Left (hold): shooter         — 40% preset
   *   - D-pad Right (hold): shooter        — 70% preset
   */
  private final CommandPS4Controller m_driverController =
      new CommandPS4Controller(OperatorConstants.kDriverControllerPort);

  private final CommandPS4Controller m_operatorController =
      new CommandPS4Controller(OperatorConstants.kOperatorControllerPort);

  public RobotContainer() {
    configureBindings();

    m_autoChooser.setDefaultOption("Shoot Preloads",
        Autos.shootPreloads(m_shooter, m_loader, m_collector));
    m_autoChooser.addOption("Collect and Score",
        Autos.collectAndScore(m_drive, m_collector, m_collectorArm, m_shooter, m_loader));
    m_autoChooser.addOption("Drive and Shoot",
        Autos.driveAndShoot(m_drive, m_shooter, m_loader));
    m_autoChooser.addOption("Vision Drive and Shoot",
        Autos.visionDriveAndShoot(m_drive, m_shooter, m_loader, m_vision));
    SmartDashboard.putData("Auto Chooser", m_autoChooser);

    m_drive.setDefaultCommand(
        new TeleopDrive(
            m_drive,
            () -> m_driverController.getLeftY(),
            () -> m_driverController.getRightY(),
            () -> m_driverController.getR2Axis(),
            () -> m_driverController.getL2Axis()));
  }

  private void configureBindings() {
    // Operator R2 (analog) — shooter speed proportional to trigger pressure
    new Trigger(() -> m_operatorController.getR2Axis() > -0.95)
        .whileTrue(new RunShooter(m_shooter, () -> m_operatorController.getR2Axis()));

    // Operator L2 (analog) — collector speed proportional to trigger pressure
    new Trigger(() -> m_operatorController.getL2Axis() > -0.95)
        .whileTrue(new RunCollector(m_collector, () -> m_operatorController.getL2Axis()));

    // Operator L1 (hold) — reverse collector to eject
    m_operatorController.L1().whileTrue(
        m_collector.runEnd(
            () -> m_collector.run(-CollectorConstants.kCollectorSpeed),
            m_collector::stop));

    // Operator Circle (hold) — loader at fixed speed, only once shooter has spun up to threshold speed
    Trigger shooterReady = new Trigger(m_shooter::isReadyToShoot);
    m_operatorController.circle().and(shooterReady).whileTrue(new RunLoader(m_loader));

    // Operator X (hold) — shooter at 10% preset (close range)
    m_operatorController.cross().whileTrue(
        m_shooter.runEnd(
            () -> m_shooter.run(ShooterConstants.kShooterPresetMin),
            m_shooter::stop));

    // Operator D-pad Up (hold) — retract collector arm (wind string)
    m_operatorController.povUp().whileTrue(
        new RunCollectorArm(m_collectorArm, () -> CollectorArmMode.RETRACT));

    // Operator D-pad Down (hold) — extend collector arm (unwind string)
    m_operatorController.povDown().whileTrue(
        new RunCollectorArm(m_collectorArm, () -> CollectorArmMode.EXTEND));

    // Operator D-pad Left (hold) — shooter at 40% preset
    m_operatorController.povLeft().whileTrue(
        m_shooter.runEnd(
            () -> m_shooter.run(ShooterConstants.kShooterPresetLow),
            m_shooter::stop));

    // Operator D-pad Right (hold) — shooter at 70% preset
    m_operatorController.povRight().whileTrue(
        m_shooter.runEnd(
            () -> m_shooter.run(ShooterConstants.kShooterPresetHigh),
            m_shooter::stop));

    // CLIMB DISABLED — R1/L1 bindings removed
    // m_driverController.R1().whileTrue(new RunClimb(m_climb, ClimbDirection.UP));
    // m_driverController.L1().whileTrue(new RunClimb(m_climb, ClimbDirection.DOWN));
  }

  public Command getAutonomousCommand() {
    return m_autoChooser.getSelected();
  }

  // CLIMB DISABLED — teleop init no longer needs to lower the climb
  // public Command getTeleopInitCommand() {
  //   return Commands.run(m_climb::climbDown, m_climb)
  //                  .withTimeout(1.0)
  //                  .andThen(Commands.runOnce(m_climb::stop, m_climb));
  // }
}
