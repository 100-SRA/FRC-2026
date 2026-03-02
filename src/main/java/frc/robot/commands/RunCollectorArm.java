// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
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

  public RunCollectorArm(CollectorArm arm, Supplier<CollectorArmMode> modeSupplier) {
    m_arm          = arm;
    m_modeSupplier = modeSupplier;
    addRequirements(arm);
  }

  @Override
  public void execute() {
    switch (m_modeSupplier.get()) {
      case RETRACT -> m_arm.retract();
      case EXTEND  -> m_arm.extend();
      default      -> m_arm.stop();
    }
  }

  @Override
  public void end(boolean interrupted) {
    m_arm.stop();
  }

  @Override
  public boolean isFinished() {
    return false;
  }
}
