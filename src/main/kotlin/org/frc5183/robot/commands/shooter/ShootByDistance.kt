package org.frc5183.robot.commands.shooter

import edu.wpi.first.math.geometry.Pose2d
import edu.wpi.first.units.Units
import edu.wpi.first.units.measure.AngularVelocity
import edu.wpi.first.units.measure.Distance
import edu.wpi.first.wpilibj.Timer
import edu.wpi.first.wpilibj2.command.Command
import org.frc5183.robot.constants.AutoConstants
import org.frc5183.robot.subsystems.shooter.ShooterSubsystem
import org.frc5183.robot.target.HubTarget
import org.littletonrobotics.junction.Logger
import kotlin.math.*

class ShootByDistance(
    val shooter: ShooterSubsystem,
    val poseSupplier: () -> Pose2d,
) : Command() {
    init {
        addRequirements(shooter)
    }

    val timer: Timer = Timer()

    override fun initialize() {
        timer.restart()
    }

    override fun execute() {
        val distance = calculateDistance()

        Logger.recordOutput("Shooter/DistanceEst", distance.`in`(Units.Inches))

        val velocity = requiredMotorVelocity(distance)

        if (distance == null || velocity == null) {
            shooter.stop()
            timer.restart()
            return
        }

        shooter.runShooterVelocity(velocity)
        
        if (!timer.hasElapsed(1.0)) {
            return
        }
        
        shooter.runFeeder(1.0)
        shooter.runIntake(1.0)
    }

    fun calculateDistance(): Distance {
        val pose = poseSupplier()
        val hub = HubTarget.get().translation

        val dx = hub.x - pose.x // m
        val dy = hub.y - pose.y // m
        
        return Units.Meters.of(hypot(dx, dy))
    }

    fun requiredMotorVelocity(
        distance: Distance?,
        efficiency: Double = 0.8
    ): AngularVelocity? {
        if (distance == null) return null
        
        val g = 9.81

        val distanceMeters = distance.`in`(Units.Meters)
        val hubHeightMeters = AutoConstants.HUB_HEIGHT.`in`(Units.Meters)
        val shooterHeightMeters = AutoConstants.SHOOTER_HEIGHT.`in`(Units.Meters)
        val wheelDiameterMeters = AutoConstants.SHOOTER_WHEEL_DIAMETER.`in`(Units.Meters)
        val angleRad = AutoConstants.SHOOTER_SHOOT_ANGLE.`in`(Units.Radians)

        val y = hubHeightMeters - shooterHeightMeters
        val r = wheelDiameterMeters / 2.0

        val denom = distanceMeters * tan(angleRad) - y
        if (denom <= 0) return null

        val v = sqrt((g * distanceMeters * distanceMeters) /
                (2 * cos(angleRad).pow(2) * denom))

        val adjustedV = v / efficiency

        val wheelRPM = (adjustedV / r) * (60.0 / (2.0 * Math.PI))
        return Units.Revolutions.per(Units.Minutes).of(wheelRPM) * 2.0
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
