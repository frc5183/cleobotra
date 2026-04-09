package org.frc5183.robot.commands.turntable

import edu.wpi.first.units.Units
import edu.wpi.first.wpilibj.Timer
import edu.wpi.first.wpilibj2.command.Command
import org.frc5183.robot.constants.AutoConstants
import org.frc5183.robot.subsystems.turntable.TurntableSubsystem
import org.frc5183.robot.target.TurntableTarget

/**
 * A command that will constantly align the turntable to be centered on the middle hub targets.
 * @param turntable The turntable subsystem to use.
 *
 * @see AlignTurntable
 */
class ConstantAlignTurntable(
    private val turntable: TurntableSubsystem,
    private val kP: Double = AutoConstants.SHOOTER_ALIGN_KP,
    private val kI: Double = AutoConstants.SHOOTER_ALIGN_KI,
    private val kD: Double = AutoConstants.SHOOTER_ALIGN_KD,
) : Command() {
    init {
        addRequirements(turntable)
    }

    var startLoss = true
    val lossTimer = Timer()
    var oscDirection = 0.25

    var integral = 0.0
    var previousError = 0.0
    val dt = 0.02 // loop time (20ms typical for FRC)

    override fun initialize() {
        startLoss = true
        lossTimer.reset()
        lossTimer.start()

        integral = 0.0
        previousError = 0.0
        oscDirection = 0.25
    }

    override fun execute() {
        val targets =
            turntable.targets.filter {
                TurntableTarget.hubIds.contains(it.fiducialId)
            }

        // We can't see any targets, just spin until we can.
        if (targets.isEmpty() && (startLoss || lossTimer.hasElapsed(4.0))) {
            oscillate()
            return
        } else if (targets.isEmpty()) {
            lossTimer.restart()
            turntable.stop()
            return
        }

        val target = targets.minByOrNull { TurntableTarget.byId(it.fiducialId)?.weight ?: 0 }

        if (target == null) {
            println("Target is null, this is not normal")
            return
        }

        startLoss = false

        val yaw = target.yaw

        if (Math.abs(yaw) < AutoConstants.SHOOTER_ALIGN_DEADBAND.`in`(Units.Degrees)) {
            turntable.stop()
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

    override fun isFinished(): Boolean = false
}
