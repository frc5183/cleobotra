package org.frc5183.robot.commands.turntable

import edu.wpi.first.wpilibj2.command.Command
import org.frc5183.robot.subsystems.turntable.TurntableSubsystem

class HomeTurntable(val turntable: TurntableSubsystem) : Command() {
    init {
        addRequirements(turntable)
    }

    override fun initialize() {
        turntable.setSpeed(0.35)
    }

    override fun end(interrupted: Boolean) {
        turntable.stop()
    }

    override fun isFinished(): Boolean = turntable.leftLimitReached
}
