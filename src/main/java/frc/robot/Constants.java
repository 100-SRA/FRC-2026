// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

/**
 * The Constants class provides a convenient place for teams to hold robot-wide numerical or boolean
 * constants. This class should not be used for any other purpose. All constants should be declared
 * globally (i.e. public static). Do not put anything functional in this class.
 *
 * <p>It is advised to statically import this class (or one of its inner classes) wherever the
 * constants are needed, to reduce verbosity.
 */
public final class Constants {
  public static class OperatorConstants {
    public static final int kDriverControllerPort = 0;
  }

  public static class DriveConstants {
    // SparkMax CAN IDs - Adjust these to match your robot's wiring
    public static final int kLeftFrontMotorId = 1;
    public static final int kLeftMiddleMotorId = 2;
    public static final int kLeftBackMotorId = 3;
    public static final int kRightFrontMotorId = 4;
    public static final int kRightMiddleMotorId = 5;
    public static final int kRightBackMotorId = 6;

    // Motor inversions - Tune based on actual robot behavior
    // Left side typically needs to be inverted for correct drive direction
    public static final boolean kLeftMotorsInverted = true;
    public static final boolean kRightMotorsInverted = false;

    // Control parameters
    public static final double kJoystickDeadband = 0.05;  // Ignore small joystick movements
    public static final double kTriggerDeadband = 0.05;   // Ignore light trigger presses
    public static final double kMaxSpeed = 1.0;           // Maximum speed multiplier (0.0 to 1.0)
    public static final double kTriggerScale = 0.8;       // Triggers run at 80% power for fine control
  }
}
