package org.frc5183.robot.commands.collector

import edu.wpi.first.wpilibj2.command.Command
import org.frc5183.robot.subsystems.collector.CollectorSubsystem

class IntakeCommand(
    val collector: CollectorSubsystem,
) : Command() {
    init {
        addRequirements(collector)
    }

    override fun initialize() {
        collector.runIntake(-0.5)
    }

    override fun end(interrupted: Boolean) {
        collector.stopIntake()
    }

    override fun isFinished(): Boolean = false
}
