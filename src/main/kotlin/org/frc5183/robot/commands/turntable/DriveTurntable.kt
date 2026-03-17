package org.frc5183.robot.commands.turntable

import edu.wpi.first.wpilibj2.command.Command
import org.frc5183.robot.subsystems.turntable.TurntableSubsystem

class DriveTurntable(
    val turntable: TurntableSubsystem,
    val input: () -> Double,
) : Command() {
    init {
        addRequirements(turntable)
    }

    override fun execute() {
        turntable.setSpeed(input())
    }

    override fun end(interrupted: Boolean) {
        turntable.stop()
    }

    override fun isFinished(): Boolean = false
}
