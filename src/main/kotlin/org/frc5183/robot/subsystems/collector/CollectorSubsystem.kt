package org.frc5183.robot.subsystems.collector

import com.revrobotics.spark.SparkMax
import edu.wpi.first.units.Units
import edu.wpi.first.wpilibj2.command.SubsystemBase
import org.frc5183.robot.constants.Constants
import org.littletonrobotics.junction.Logger
import kotlin.math.abs
import kotlin.math.sign

class CollectorSubsystem(
    val arm: SparkMax,
    val intake: SparkMax,
) : SubsystemBase() {
    val armSpeed: Double
        get() = arm.get()

    val intakeSpeed: Double
        get() = intake.get()

    val atBottom: Boolean
        get() =
            abs(Constants.COLLECTOR_ARM_BOTTOM.`in`(Units.Rotations) - arm.absoluteEncoder.position) <=
                Constants.COLLECTOR_ARM_DELTA.`in`(Units.Rotations)

    val atTop: Boolean
        get() =
            abs(Constants.COLLECTOR_ARM_TOP.`in`(Units.Rotations) - arm.absoluteEncoder.position) <=
                Constants.COLLECTOR_ARM_DELTA.`in`(Units.Rotations)

    override fun periodic() {
        Logger.recordOutput("Collector/ArmSpeed", arm.get())
        Logger.recordOutput("Collector/IntakeSpeed", intake.get())

        Logger.recordOutput("Collector/Bottom", atBottom)
        Logger.recordOutput("Collector/Top", atTop)

        if (atTop && speedIsUp(armSpeed)) stopArm()
        if (atBottom && speedIsUp(armSpeed)) stopArm()
    }

    fun runArm(speed: Double) {
        if (atBottom && speedIsDown(speed)) return
        if (atTop && speedIsUp(speed)) return
        arm.set(speed)
    }

    fun runIntake(speed: Double) {
        intake.set(speed)
    }

    fun stopArm() {
        arm.stopMotor()
    }

    fun stopIntake() {
        intake.stopMotor()
    }

    fun speedIsUp(speed: Double): Boolean = speed.sign == 1.0

    fun speedIsDown(speed: Double): Boolean = speed.sign == -1.0
}
