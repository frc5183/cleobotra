package org.frc5183.robot.commands.collector

import edu.wpi.first.wpilibj2.command.Command
import org.frc5183.robot.subsystems.collector.CollectorSubsystem
import org.frc5183.robot.subsystems.shooter.ShooterSubsystem

class IntakeCommand(
    val collector: CollectorSubsystem,
) : Command() {
    init {
        addRequirements(collector)
    }

    override fun initialize() {
        collector.runIntake(1.0)
    }

    override fun end(interrupted: Boolean) {
        collector.stopIntake()
    }

    override fun isFinished(): Boolean = false
}
