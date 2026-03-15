// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import org.photonvision.PhotonCamera;
import org.photonvision.targeting.PhotonTrackedTarget;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

/**
 * Vision subsystem using PhotonVision to detect targets and estimate distance.
 */
public class Vision extends SubsystemBase {

  // -------------------------------------------------------------------------
  // Physical constants — measured on the actual robot
  // -------------------------------------------------------------------------

  /**
   * Name of the camera as shown in the PhotonVision dashboard (case-sensitive).
   * Open http://photonvision.local:5800 and confirm the exact name before competition.
   */
  private static final String CAMERA_NAME = "YOUR_CAMERA_NAME";

  /** Height of the camera lens above the floor, in meters. (17 inches) */
  private static final double CAMERA_HEIGHT_METERS = 0.4318;

  /**
   * Camera mounting angle above horizontal, in degrees.
   * Camera is mounted parallel to the ground, so this is 0.
   * Distance formula: distance = (targetHeight - cameraHeight) / tan(targetPitch)
   */
  private static final double CAMERA_PITCH_DEGREES = 0.0;

  /**
   * Height of the target center above the floor, in meters.
   * Hub is 71-72 inches tall; using 71.5 in (midpoint) = 1.8161 m.
   */
  private static final double TARGET_HEIGHT_METERS = 1.8161;

  // -------------------------------------------------------------------------
  // Shooter speed tuning — adjust based on field testing
  // -------------------------------------------------------------------------

  /** Closest distance you expect to shoot from, in meters. */
  private static final double MIN_DISTANCE_METERS = 1.0;

  /** Farthest distance you expect to shoot from, in meters. */
  private static final double MAX_DISTANCE_METERS = 4.0;

  /** Shooter speed at MIN_DISTANCE_METERS (0.0 to 1.0). */
  private static final double SPEED_AT_MIN_DISTANCE = 0.5;

  /** Shooter speed at MAX_DISTANCE_METERS (0.0 to 1.0). */
  private static final double SPEED_AT_MAX_DISTANCE = 1.0;

  // -------------------------------------------------------------------------

  private final PhotonCamera m_camera;

  public Vision() {
    m_camera = new PhotonCamera(CAMERA_NAME);
  }

  /** Returns true if the camera currently sees a target. */
  public boolean hasTarget() {
    var result = m_camera.getLatestResult();
    return result.hasTargets();
  }

  /**
   * Estimates distance to the best visible target using camera geometry.
   * Returns -1 if no target is visible.
   *
   * Camera is parallel to ground (pitch = 0), so the formula simplifies to:
   *   distance = (targetHeight - cameraHeight) / tan(targetPitch)
   */
  public double getDistanceMeters() {
    var result = m_camera.getLatestResult();
    if (!result.hasTargets()) return -1.0;

    PhotonTrackedTarget target = result.getBestTarget();
    double targetPitchRadians = Math.toRadians(target.getPitch());
    double cameraPitchRadians = Math.toRadians(CAMERA_PITCH_DEGREES);
    double heightDelta        = TARGET_HEIGHT_METERS - CAMERA_HEIGHT_METERS;

    return heightDelta / Math.tan(cameraPitchRadians + targetPitchRadians);
  }

  /**
   * Maps the estimated distance to a shooter speed using linear interpolation.
   * Clamps to [SPEED_AT_MIN_DISTANCE, SPEED_AT_MAX_DISTANCE] for safety.
   * Returns -1 if no target is visible.
   */
  public double getCalculatedShooterSpeed() {
    double distance = getDistanceMeters();
    if (distance < 0) return -1.0;

    double t = (distance - MIN_DISTANCE_METERS) / (MAX_DISTANCE_METERS - MIN_DISTANCE_METERS);
    t = Math.max(0.0, Math.min(1.0, t)); // clamp to [0, 1]

    return SPEED_AT_MIN_DISTANCE + t * (SPEED_AT_MAX_DISTANCE - SPEED_AT_MIN_DISTANCE);
  }

  @Override
  public void periodic() {
    double distance = getDistanceMeters();
    SmartDashboard.putBoolean("Vision/HasTarget",      hasTarget());
    SmartDashboard.putNumber("Vision/DistanceMeters",  distance);
    SmartDashboard.putNumber("Vision/ShooterSpeed",    getCalculatedShooterSpeed());
  }
}
