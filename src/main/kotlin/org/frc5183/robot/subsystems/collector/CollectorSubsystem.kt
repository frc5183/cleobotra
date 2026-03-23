package org.frc5183.robot.subsystems.collector

import com.revrobotics.spark.SparkMax
import edu.wpi.first.wpilibj2.command.SubsystemBase
import org.littletonrobotics.junction.Logger

class CollectorSubsystem(
    val arm: SparkMax,
    val intake: SparkMax,
) : SubsystemBase() {
    override fun periodic() {
        Logger.recordOutput("Collector/ArmSpeed", arm.get())
        Logger.recordOutput("Collector/IntakeSpeed", intake.get())
    }

    fun runArm(speed: Double) {
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
}
