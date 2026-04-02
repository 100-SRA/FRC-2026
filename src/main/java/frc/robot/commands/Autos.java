// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.subsystems.Climb;
import frc.robot.subsystems.Drive;
import frc.robot.subsystems.Loader;
import frc.robot.subsystems.Shooter;
import frc.robot.Constants.ShooterConstants;
import frc.robot.subsystems.Vision;

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

  /**
   * Fallback auto: drive straight for 2 seconds, then shoot for 10 seconds.
   * Use this if vision is unavailable.
   */
  public static Command driveAndShoot(Drive drive, Shooter shooter, Loader loader) {
    return new SequentialCommandGroup(
        // Step 1: Drive straight forward for 2 seconds
        Commands.run(() -> drive.tankDrive(0.5, 0.5), drive)
                .withTimeout(2.0),
        // Step 2: Stop driving
        Commands.runOnce(drive::stop, drive),
        // Step 3: Run shooter and loader simultaneously for 10 seconds
        Commands.parallel(
            Commands.run(() -> shooter.run(ShooterConstants.kShooterPresetHigh), shooter),
            Commands.run(loader::run, loader)
        ).withTimeout(10.0),
        // Step 4: Stop shooter and loader
        Commands.runOnce(shooter::stop, shooter),
        Commands.runOnce(loader::stop, loader)
    );
  }

  /**
   * Primary auto: drive forward until a target is visible, then shoot at a
   * speed calculated from the measured distance. Falls back to full power
   * if no target is found within 3 seconds.
   *
   * Tune MIN_DISTANCE_METERS, MAX_DISTANCE_METERS, and the speed range
   * in Vision.java based on field testing.
   */
  public static Command visionDriveAndShoot(
      Drive drive, Shooter shooter, Loader loader, Vision vision) {
    return new SequentialCommandGroup(
        // Step 1: Drive forward until a target is visible (max 3 seconds)
        Commands.run(() -> drive.tankDrive(0.4, 0.4), drive)
                .until(vision::hasTarget)
                .withTimeout(3.0),
        // Step 2: Stop driving
        Commands.runOnce(drive::stop, drive),
        // Step 3: Shoot at vision-calculated speed; fall back to full power if no target
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
