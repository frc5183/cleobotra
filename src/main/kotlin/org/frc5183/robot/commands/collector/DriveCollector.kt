package org.frc5183.robot.commands.collector

import edu.wpi.first.wpilibj2.command.Command
import org.frc5183.robot.subsystems.collector.CollectorSubsystem

class DriveCollector(
    val collector: CollectorSubsystem,
    val input: () -> Double,
) : Command() {
    init {
        addRequirements(collector)
    }

    override fun execute() {
        collector.runArm(input())
    }

    override fun end(interrupted: Boolean) {
        collector.stopArm()
    }

    override fun isFinished(): Boolean = false
}
