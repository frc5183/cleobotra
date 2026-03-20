package org.frc5183.robot.commands.turntable

import edu.wpi.first.units.Units
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
) : Command() {
    init {
        addRequirements(turntable)
    }

    var oscDirection = 0.25

    override fun execute() {
        val targets =
            turntable.targets.filter {
                TurntableTarget.hubIds.contains(it.fiducialId)
            }

        println("hi")

        // We can't see any targets, just spin until we can.
        if (targets.isEmpty()) {
            oscillate()
            println("emty")
            return
        }

        val target = targets.minBy { TurntableTarget.byId(it.fiducialId).weight }

        val yaw = target.yaw

        if (Math.abs(yaw) < AutoConstants.SHOOTER_ALIGN_DEADBAND.`in`(Units.Degrees)) {
            turntable.stop()
            println("stopping")
        } else {
            val turnPower = yaw * AutoConstants.SHOOTER_ALIGN_KP
            turntable.setSpeed(turnPower)
            println("setting speed $turnPower")
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
