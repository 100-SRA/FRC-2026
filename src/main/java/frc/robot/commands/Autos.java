// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.subsystems.Collector;
import frc.robot.subsystems.CollectorArm;
import frc.robot.subsystems.Drive;
import frc.robot.subsystems.Loader;
import frc.robot.subsystems.Shooter;
import frc.robot.Constants.CollectorArmConstants;
import frc.robot.Constants.ShooterConstants;
import frc.robot.subsystems.Vision;

/**
 * Utility class for autonomous command factories.
 * Add autonomous routines here as static methods.
 */
public final class Autos {

  /**
   * Drive forward for 2 seconds, then shoot for 3 seconds.
   *
   * Uncomment the return statement below (and comment out Commands.none())
   * once you're ready to test autonomous.
   *
   * Tune kAutoDriveSpeed and kAutoShootTime to match your robot on the field.
   */
  public static Command driveAndShoot(Drive drive, Shooter shooter, Loader loader) {
    return new SequentialCommandGroup(
        // Step 1: Drive straight forward for 2 seconds
        Commands.run(() -> drive.tankDrive(0.5, 0.5), drive)
                .withTimeout(2.0),
        // Step 2: Stop driving
        Commands.runOnce(drive::stop, drive),
        // Step 3: Run shooter and loader simultaneously for 3 seconds
        Commands.parallel(
            Commands.run(() -> shooter.run(-ShooterConstants.kShooterPresetHigh), shooter),
            Commands.run(loader::run, loader)
        ).withTimeout(10.0),
        // Step 4: Stop shooter and loader
        Commands.runOnce(shooter::stop, shooter),
        Commands.runOnce(loader::stop, loader)
    );
  }

  /**
   * Drive forward, lock onto a vision target, then shoot at a speed calculated from distance.
   *
   * SETUP REQUIRED before enabling:
   *   1. Enable the Vision subsystem in Vision.java and fill in its physical constants.
   *   2. Add a Vision instance to RobotContainer and pass it here.
   *   3. Uncomment this method body and the Vision import at the top of this file.
   */
  /**
   * Full auto cycle: extend arm → drive forward to collect fuel → retract → drive back → shoot.
   *
   * TUNING NOTES (adjust these timeouts based on field testing):
   *   - Arm extend time:     how long it takes the arm to fully lower
   *   - Collect drive time:  how far forward to travel to reach fuel
   *   - Arm retract time:    how long it takes the arm to fully raise
   *   - Return drive time:   how far back to travel to reach shooting position
   *
   * To enable: remove the block comment markers below and pass instances from RobotContainer.
   */
  // public static Command collectAndShoot(
  //     Drive drive, Collector collector, CollectorArm arm, Shooter shooter, Loader loader) {
  //   return new SequentialCommandGroup(
  //       // Step 1: Lower the collector arm while running the intake
  //       Commands.parallel(
  //           Commands.run(arm::extend, arm),
  //           Commands.run(collector::run, collector)
  //       ).withTimeout(1.5),                              // tune: arm extend time
  //       // Step 2: Drive forward slowly to scoop up fuel (intake still running)
  //       Commands.parallel(
  //           Commands.run(() -> drive.tankDrive(0.3, 0.3), drive),
  //           Commands.run(collector::run, collector)
  //       ).withTimeout(2.0),                              // tune: collection drive time
  //       // Step 3: Stop driving, stop collector
  //       Commands.runOnce(drive::stop, drive),
  //       Commands.runOnce(collector::stop, collector),
  //       // Step 4: Retract the arm
  //       Commands.run(arm::retract, arm)
  //               .withTimeout(1.5),                       // tune: arm retract time
  //       Commands.runOnce(arm::stop, arm),
  //       // Step 5: Drive back toward the goal
  //       Commands.run(() -> drive.tankDrive(-0.5, -0.5), drive)
  //               .withTimeout(2.0),                       // tune: return drive time
  //       Commands.runOnce(drive::stop, drive),
  //       // Step 6: Fire — run shooter and loader simultaneously
  //       Commands.parallel(
  //           Commands.run(() -> shooter.run(-ShooterConstants.kShooterPresetHigh), shooter),
  //           Commands.run(loader::run, loader)
  //       ).withTimeout(3.0),
  //       // Step 7: Stop everything
  //       Commands.runOnce(shooter::stop, shooter),
  //       Commands.runOnce(loader::stop, loader)
  //   );
  // }

  public static Command visionDriveAndShoot(
      Drive drive, Shooter shooter, Loader loader, Vision vision) {
    return new SequentialCommandGroup(
        // Step 1: Drive forward until a target is visible (max 3 seconds)
        Commands.run(() -> drive.tankDrive(0.4, 0.4), drive)
                .until(vision::hasTarget)
                .withTimeout(3.0),
        // Step 2: Stop driving
        Commands.runOnce(drive::stop, drive),
        // Step 3: Spin up shooter at vision-calculated speed, then feed with loader
        //         Falls back to full power if no target is visible
        Commands.parallel(
            Commands.run(() -> {
                double speed = vision.getCalculatedShooterSpeed();
                shooter.run(speed > 0 ? speed : ShooterConstants.kShooterPresetHigh);
            }, shooter),
            Commands.run(loader::run, loader)
        ).withTimeout(10.0),
        // Step 4: Stop shooter and loader
        Commands.runOnce(shooter::stop, shooter),
        Commands.runOnce(loader::stop, loader)
    );
  }

  private Autos() {
    throw new UnsupportedOperationException("This is a utility class!");
  }
}
