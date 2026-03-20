package org.frc5183.robot.subsystems.climber

import com.revrobotics.spark.SparkMax
import edu.wpi.first.units.Units
import edu.wpi.first.units.measure.Angle
import edu.wpi.first.wpilibj2.command.SubsystemBase
import org.frc5183.robot.constants.Constants
import org.littletonrobotics.junction.Logger

class ClimberSubsystem(
    val motor: SparkMax
) : SubsystemBase() {
    val climberSpeed: Double
        get() = motor.get()

    val climberPosition: Angle
        get() = Units.Rotations.of(motor.encoder.position)

    val topLimit: Boolean
        get() = climberPosition >= Constants.CLIMBER_MAX

    val bottomLimit: Boolean
        get() = motor.encoder.position <= 0.0

    init {
        motor.encoder.position = 0.0
    }

    override fun periodic() {
        Logger.recordOutput("Climber/Speed", climberSpeed)
        Logger.recordOutput("Climber/Position", climberPosition)

//        if (topLimit && speedGoesUp(climberSpeed)) {
//            stopMotor()
//        }

//        if (bottomLimit && speedGoesDown(climberSpeed)) {
//            climberPosition <= Units.Rotations.of(0.0)
//        }
    }

    fun runMotor(speed: Double) {
//        if (speedGoesUp(speed) && topLimit) return
//        if (speedGoesDown(speed) && bottomLimit) return
        motor.set(speed)
    }

    fun stopMotor() = motor.stopMotor()

    fun speedGoesDown(speed: Double): Boolean = speed < 0.0
    fun speedGoesUp(speed: Double): Boolean = speed < 0.0
}