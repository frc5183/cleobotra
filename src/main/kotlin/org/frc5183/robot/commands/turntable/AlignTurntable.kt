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
) : Command() {
    init {
        addRequirements(turntable)
    }

    private var aligned = false

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

        val target = targets.minBy { TurntableTarget.byId(it.fiducialId).weight }

        val yaw = target.yaw

        if (Math.abs(yaw) < AutoConstants.SHOOTER_ALIGN_DEADBAND.`in`(Units.Degrees)) {
            turntable.stop()
            aligned = true
        } else {
            val turnPower = yaw * AutoConstants.SHOOTER_ALIGN_KP
            turntable.setSpeed(turnPower)
        }
    }

    private fun oscillate() {
        if (turntable.leftLimitReached) turntable.setSpeed(-1.0)
        if (turntable.rightLimitReached) turntable.setSpeed(1.0)
    }

    override fun end(interrupted: Boolean) {
        turntable.stop()
    }

    override fun isFinished(): Boolean = aligned
}
