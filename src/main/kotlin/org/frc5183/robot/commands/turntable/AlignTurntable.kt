package org.frc5183.robot.commands.turntable

import edu.wpi.first.units.Units
import edu.wpi.first.wpilibj2.command.Command
import org.frc5183.robot.constants.AutoConstants
import org.frc5183.robot.subsystems.turntable.TurntableSubsystem
import org.frc5183.robot.target.TurntableTarget

/**
 * A command that will align the turntable to be centered on the middle hub targets and then stop.
 * @param turntable The turntable subsystem to use.
 *
 * @see ConstantAlignTurntable
 */
class AlignTurntable(
    private val turntable: TurntableSubsystem,
    private val kP: Double = AutoConstants.SHOOTER_ALIGN_KP,
    private val kI: Double = AutoConstants.SHOOTER_ALIGN_KI,
    private val kD: Double = AutoConstants.SHOOTER_ALIGN_KD
) : Command() {
    init {
        addRequirements(turntable)
    }

    private var aligned = false
    var oscDirection = 0.25

    var integral = 0.0
    var previousError = 0.0
    val dt = 0.02  // loop time (20ms typical for FRC)

    override fun initialize() {
        aligned = false
    }

    override fun execute() {
        val targets =
            turntable.targets.filter {
                TurntableTarget.hubIds.contains(it.fiducialId)
            }

        // We can't see any targets, just spin until we can.
        if (targets.isEmpty()) {
            oscillate()
            return
        }

        val target = targets.minByOrNull { TurntableTarget.byId(it.fiducialId).weight }

        if (target == null) {
            println("Target is null, this is not normal")
            return
        }

        val yaw = target.yaw

        if (Math.abs(yaw) < AutoConstants.SHOOTER_ALIGN_DEADBAND.`in`(Units.Degrees)) {
            turntable.stop()
            aligned = true
        } else {
            val error = yaw

            integral += error * dt

            val integralLimit = 1.0
            integral = integral.coerceIn(-integralLimit, integralLimit)

            val derivative = (error - previousError) / dt

            val turnPower = -(kP * error + kI * integral + kD * derivative)

            turntable.setSpeed(turnPower)

            previousError = error
        }
    }

    private fun oscillate() {
        if (turntable.leftLimitReached) oscDirection = -0.25
        if (turntable.rightLimitReached) oscDirection = 0.25
        turntable.setSpeed(oscDirection)
    }
    override fun end(interrupted: Boolean) {
        turntable.stop()
    }

    override fun isFinished(): Boolean = aligned
}
