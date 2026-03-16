package org.frc5183.robot.subsystems.collector.io

import com.revrobotics.spark.SparkMax

class RealCollectorIO(
    val arm: SparkMax,
    val intake: SparkMax,
) : CollectorIO {
    override fun updateInputs(inputs: CollectorIOInputs) {
        inputs.collectorArmSpeed = arm.get()
        inputs.collectorIntakeSpeed = intake.get()
    }

    override fun runArm(speed: Double) {
        arm.set(speed)
    }

    override fun runIntake(speed: Double) {
        intake.set(speed)
    }

    override fun stopArm() {
        arm.stopMotor()
    }

    override fun stopIntake() {
        intake.stopMotor()
    }
}
