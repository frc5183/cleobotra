package org.frc5183.robot.commands.collector

import edu.wpi.first.wpilibj2.command.Command
import org.frc5183.robot.subsystems.collector.CollectorSubsystem
import org.frc5183.robot.subsystems.shooter.ShooterSubsystem

class RaiseCollector(
    val collector: CollectorSubsystem,
) : Command() {
    init {
        addRequirements(collector)
    }

    override fun initialize() {
        collector.runArm(1.0)
    }

    override fun end(interrupted: Boolean) {
        collector.stopArm()
    }

    override fun isFinished(): Boolean = collector.atTop
}
