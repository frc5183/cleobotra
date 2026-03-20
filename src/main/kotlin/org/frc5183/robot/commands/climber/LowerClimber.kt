package org.frc5183.robot.commands.climber

import edu.wpi.first.wpilibj2.command.Command
import org.frc5183.robot.subsystems.climber.ClimberSubsystem

class LowerClimber(
    val climber: ClimberSubsystem,
) : Command() {
    init {
        addRequirements(climber)
    }

    override fun initialize() {
        climber.runMotor(-1.0)
    }

    override fun end(interrupted: Boolean) {
        climber.stopMotor()
    }

    override fun isFinished(): Boolean = false
}
