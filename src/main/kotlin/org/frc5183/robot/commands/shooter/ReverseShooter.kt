package org.frc5183.robot.commands.shooter

import edu.wpi.first.wpilibj2.command.Command
import org.frc5183.robot.subsystems.shooter.ShooterSubsystem

class ReverseShooter(private val shooter: ShooterSubsystem) : Command() {
    init {
        addRequirements(shooter)
    }

    override fun initialize() {
        shooter.run(-1.0)
    }

    override fun end(interrupted: Boolean) {
        shooter.stop()
    }

    override fun isFinished(): Boolean = false
}
