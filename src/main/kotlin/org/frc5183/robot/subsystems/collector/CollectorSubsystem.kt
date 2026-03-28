package org.frc5183.robot.subsystems.collector

import com.revrobotics.spark.SparkMax
import edu.wpi.first.wpilibj.DigitalInput
import edu.wpi.first.wpilibj2.command.SubsystemBase
import org.littletonrobotics.junction.Logger
import kotlin.math.sign

class CollectorSubsystem(
    val arm: SparkMax,
    val intake: SparkMax,
    val topLimitSwitch: DigitalInput,
    val bottomLimitSwitch: DigitalInput,
) : SubsystemBase() {
    val armSpeed: Double
        get() = arm.get()

    val armVelocity: Double
        get() = arm.encoder.velocity

    val intakeSpeed: Double
        get() = intake.get()

    val topLimit: Boolean
        get() = topLimitSwitch.get()

    val bottomLimit: Boolean
        get() = bottomLimitSwitch.get()

    init {
        zeroArm()
    }

    override fun periodic() {
        Logger.recordOutput("Collector/ArmSpeed", armSpeed)
        Logger.recordOutput("Collector/ArmVelocity", armVelocity)

        Logger.recordOutput("Collector/IntakeSpeed", intakeSpeed)

        Logger.recordOutput("Collector/Bottom", bottomLimit)
        Logger.recordOutput("Collector/Top", topLimit)

        if (topLimit && speedIsUp(armSpeed)) stopArm()
        if (bottomLimit && speedIsDown(armSpeed)) stopArm()
    }

    fun runArm(speed: Double) {
        if (topLimit && speedIsUp(speed)) return
        if (bottomLimit && speedIsDown(speed)) return
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

    fun zeroArm() {
        arm.encoder.setPosition(0.0)
    }

    fun speedIsUp(speed: Double): Boolean = speed.sign == 1.0

    fun speedIsDown(speed: Double): Boolean = speed.sign == -1.0
}
