package org.frc5183.robot.commands.climber

import edu.wpi.first.wpilibj2.command.Command
import org.frc5183.robot.subsystems.climber.ClimberSubsystem

class RaiseClimber(
    val climber: ClimberSubsystem,
) : Command() {
    init {
        addRequirements(climber)
    }

    override fun initialize() {
        climber.runMotor(.5)
    }

    override fun end(interrupted: Boolean) {
        climber.stopMotor()
    }

    override fun isFinished(): Boolean = false
}
