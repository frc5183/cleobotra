package org.frc5183.robot.commands.turntable

import edu.wpi.first.math.geometry.Pose2d
import edu.wpi.first.math.geometry.Translation2d
import edu.wpi.first.units.Units
import edu.wpi.first.wpilibj.DriverStation
import edu.wpi.first.wpilibj2.command.Command
import org.frc5183.robot.constants.AutoConstants
import org.frc5183.robot.subsystems.turntable.TurntableSubsystem
import org.littletonrobotics.junction.Logger
import kotlin.jvm.optionals.getOrNull
import kotlin.math.atan2

/**
 * A command that will constantly align the turntable to be centered on the middle hub targets.
 * @param turntable The turntable subsystem to use.
 *
 * @see AlignTurntable
 */
class ConstantAlignTurntable(
    private val turntable: TurntableSubsystem,
    private val poseSupplier: () -> Pose2d,
    private val kP: Double = AutoConstants.SHOOTER_ALIGN_KP,
    private val kI: Double = AutoConstants.SHOOTER_ALIGN_KI,
    private val kD: Double = AutoConstants.SHOOTER_ALIGN_KD,
) : Command() {
    private val BLUE_HUB = Translation2d(Units.Meters.of(4.625), Units.Meters.of(4.025))
    private val RED_HUB = Translation2d(Units.Meters.of(11.925), Units.Meters.of(4.025))

    init {
        addRequirements(turntable)
    }

    var integral = 0.0
    var previousError = 0.0
    val dt = 0.02 // loop time (20ms typical for FRC)

    override fun initialize() {
        integral = 0.0
        previousError = 0.0
    }

    override fun execute() {
        val pose = poseSupplier()
        val hub = if (DriverStation.getAlliance().getOrNull() == DriverStation.Alliance.Red) RED_HUB else BLUE_HUB

        val dx = hub.x - pose.x // m
        val dy = hub.y - pose.y // m
        val targetAngleFieldRelative = Units.Radians.of(atan2(dy, dx)) // rad
        val targetAngleRobotRelative = targetAngleFieldRelative - pose.rotation.measure
        val error = targetAngleRobotRelative - turntable.angle

        Logger.recordOutput("Turntable/Align/dx", dx)
        Logger.recordOutput("Turntable/Align/dy", dy)
        Logger.recordOutput("Turntable/Align/targetAngle/field", targetAngleFieldRelative)
        Logger.recordOutput("Turntable/Align/targetAngle/robot", targetAngleRobotRelative)
        Logger.recordOutput("Turntable/Align/targetAngle/error", error)

        val errorDegrees = error.`in`(Units.Degrees)

        if (Math.abs(errorDegrees) < AutoConstants.SHOOTER_ALIGN_DEADBAND.`in`(Units.Degrees)) {
            turntable.stop()
        } else {
            integral += errorDegrees * dt

            val integralLimit = 1.0
            integral = integral.coerceIn(-integralLimit, integralLimit)

            val derivative = (errorDegrees - previousError) / dt

            val turnPower = -(kP * errorDegrees + kI * integral + kD * derivative)

            turntable.setSpeed(turnPower)

            previousError = errorDegrees
        }
    }

    override fun end(interrupted: Boolean) {
        turntable.stop()
    }

    override fun isFinished(): Boolean = false
}
