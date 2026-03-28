package org.frc5183.robot.subsystems.climber

import com.revrobotics.spark.SparkMax
import edu.wpi.first.wpilibj2.command.SubsystemBase
import org.littletonrobotics.junction.Logger

class ClimberSubsystem(
    val motor: SparkMax,
) : SubsystemBase() {
    val climberSpeed: Double
        get() = motor.get()

    override fun periodic() {
        Logger.recordOutput("Climber/Speed", climberSpeed)
    }

    fun runMotor(speed: Double) {
        motor.set(speed)
    }

    fun stopMotor() = motor.stopMotor()
}
