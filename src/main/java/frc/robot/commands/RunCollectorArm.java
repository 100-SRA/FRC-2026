// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.Constants.CollectorArmConstants;
import frc.robot.subsystems.CollectorArm;
import java.util.function.Supplier;

/** Drives the collector arm motor in RETRACT, STOP, or EXTEND mode. */
public class RunCollectorArm extends Command {

  /** The three operating modes for the collector arm. */
  public enum CollectorArmMode {
    RETRACT,
    STOP,
    EXTEND
  }

  private final CollectorArm               m_arm;
  private final Supplier<CollectorArmMode> m_modeSupplier;
  private double                           m_rampedExtendSpeed = 0.0;

  public RunCollectorArm(CollectorArm arm, Supplier<CollectorArmMode> modeSupplier) {
    m_arm          = arm;
    m_modeSupplier = modeSupplier;
    addRequirements(arm);
  }

  @Override
  public void execute() {
    switch (m_modeSupplier.get()) {
      case RETRACT -> {
        m_rampedExtendSpeed = 0.0;  // Reset ramp so next extend starts fresh
        m_arm.retract();
      }
      case EXTEND -> {
        // Ramp from 0 toward the target extend speed each scheduler cycle (~20ms)
        // so the arm accelerates gradually instead of jerking to full speed
        double target = CollectorArmConstants.kCollectorArmExtendSpeed;
        m_rampedExtendSpeed = Math.max(m_rampedExtendSpeed - CollectorArmConstants.kCollectorArmExtendRampRate, target);
        m_arm.setSpeed(m_rampedExtendSpeed);
      }
      default -> {
        m_rampedExtendSpeed = 0.0;
        m_arm.stop();
      }
    }
  }

  @Override
  public void end(boolean interrupted) {
    m_rampedExtendSpeed = 0.0;
    m_arm.stop();
  }

  @Override
  public boolean isFinished() {
    return false;
  }
}
