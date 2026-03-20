package org.frc5183.robot.commands.shooter

import edu.wpi.first.wpilibj2.command.Command
import org.frc5183.robot.subsystems.shooter.ShooterSubsystem

class Shoot95Command(
    private val shooter: ShooterSubsystem,
) : Command() {
    init {
        addRequirements(shooter)
    }

    override fun initialize() {
        shooter.runShooter(0.95)
        shooter.runFeeder(1.0)
        shooter.runIntake(1.0)
    }

    override fun end(interrupted: Boolean) {
        shooter.stop()
    }

    override fun isFinished(): Boolean = false
}
