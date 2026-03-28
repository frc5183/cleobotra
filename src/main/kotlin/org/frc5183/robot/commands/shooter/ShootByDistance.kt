package org.frc5183.robot.commands.shooter

import edu.wpi.first.units.Units
import edu.wpi.first.units.measure.Distance
import edu.wpi.first.wpilibj2.command.Command
import org.frc5183.robot.subsystems.shooter.ShooterSubsystem

class ShootByDistance(
    val shooter: ShooterSubsystem,
    val distanceSupplier: () -> Distance?,
) : Command() {
    init {
        addRequirements(shooter)
    }

    override fun execute() {
        shooter.runShooter(distanceToSpeed(distanceSupplier() ?: return))
        shooter.runFeeder(1.0)
        shooter.runIntake(1.0)
    }

    private fun distanceToSpeed(distance: Distance): Double {
        val distanceMeters = distance.`in`(Units.Meters)
        return ((0.0447387 * distanceMeters) + 0.640909).coerceIn(0.75, 1.0)
    }

    override fun end(interrupted: Boolean) {
        shooter.stop()
    }

    override fun isFinished(): Boolean = false
}
