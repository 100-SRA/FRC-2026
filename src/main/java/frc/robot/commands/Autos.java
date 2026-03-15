// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
// import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.subsystems.Drive;
import frc.robot.subsystems.Shooter;
import frc.robot.Constants.ShooterConstants;
// import frc.robot.subsystems.Vision;

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
  public static Command driveAndShoot(Drive drive, Shooter shooter) {
    // return new SequentialCommandGroup(
    //     // Step 1: Drive straight forward for 2 seconds
    //     Commands.run(() -> drive.tankDrive(0.5, 0.5), drive)
    //             .withTimeout(2.0),
    //     // Step 2: Stop driving
    //     Commands.runOnce(drive::stop, drive),
    //     // Step 3: Spin up shooter and fire for 3 seconds
    //     Commands.run(() -> shooter.run(ShooterConstants.kShooterPresetHigh), shooter)
    //             .withTimeout(3.0),
    //     // Step 4: Stop shooter
    //     Commands.runOnce(shooter::stop, shooter)
    // );

    return Commands.none(); // Remove this line when enabling auto above
  }

  /**
   * Drive forward, lock onto a vision target, then shoot at a speed calculated from distance.
   *
   * SETUP REQUIRED before enabling:
   *   1. Enable the Vision subsystem in Vision.java and fill in its physical constants.
   *   2. Add a Vision instance to RobotContainer and pass it here.
   *   3. Uncomment this method body and the Vision import at the top of this file.
   */
  // public static Command visionDriveAndShoot(Drive drive, Shooter shooter, Vision vision) {
  //   return new SequentialCommandGroup(
  //       // Step 1: Drive forward until a target is visible (max 3 seconds)
  //       Commands.run(() -> drive.tankDrive(0.4, 0.4), drive)
  //               .until(vision::hasTarget)
  //               .withTimeout(3.0),
  //       // Step 2: Stop driving
  //       Commands.runOnce(drive::stop, drive),
  //       // Step 3: Shoot at speed calculated from distance, for 3 seconds
  //       //         Falls back to preset high speed if no target is found
  //       Commands.run(() -> {
  //           double speed = vision.getCalculatedShooterSpeed();
  //           shooter.run(speed > 0 ? speed : ShooterConstants.kShooterPresetHigh);
  //       }, shooter).withTimeout(3.0),
  //       // Step 4: Stop shooter
  //       Commands.runOnce(shooter::stop, shooter)
  //   );
  // }

  private Autos() {
    throw new UnsupportedOperationException("This is a utility class!");
  }
}
