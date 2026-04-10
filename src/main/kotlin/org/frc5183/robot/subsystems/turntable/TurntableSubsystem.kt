package org.frc5183.robot.subsystems.turntable

import com.revrobotics.spark.SparkMax
import edu.wpi.first.units.Units
import edu.wpi.first.units.measure.Angle
import edu.wpi.first.wpilibj.DigitalInput
import edu.wpi.first.wpilibj2.command.SubsystemBase
import org.frc5183.robot.constants.DeviceConstants
import org.littletonrobotics.junction.Logger
import org.photonvision.PhotonCamera

class TurntableSubsystem(
    val motor: SparkMax,
    val camera: PhotonCamera,
    val limitSwitch: DigitalInput,
) : SubsystemBase() {
    var safetyOverride: Boolean = false

    val speed: Double
        get() = motor.get()

    val angle: Angle
        get() = Units.Rotations.of(-motor.encoder.position / DeviceConstants.TURNTABLE_GEAR_RATIO) - DeviceConstants.TURNTABLE_ANGLE_OFFSET

    var leftLimitReached: Boolean = false
        private set

    var rightLimitReached: Boolean = false
        private set

    override fun periodic() {
        Logger.recordOutput("Turntable/Speed", motor.get())
        Logger.recordOutput("Turntable/Angle", angle.`in`(Units.Degrees))

        Logger.recordOutput("Turntable/Hit Limit", limitSwitch.get())
        Logger.recordOutput("Turntable/Left Limit", leftLimitReached)
        Logger.recordOutput("Turntable/Right Limit", rightLimitReached)

        checkLimitSwitches()
    }

    private fun checkLimitSwitches() {
        if (!safetyOverride && limitSwitch.get()) {
            if (speedGoesLeft(speed) && !rightLimitReached) {
                leftLimitReached = true
                stop()
                zero()
            } else if (speedGoesRight(speed) && !leftLimitReached) {
                rightLimitReached = true
                stop()
            }
        }

        if (!limitSwitch.get()) {
            leftLimitReached = false
            rightLimitReached = false
        }
    }

    fun setSpeed(speed: Double) {
        if (!safetyOverride && leftLimitReached && speedGoesLeft(speed)) return
        if (!safetyOverride && rightLimitReached && speedGoesRight(speed)) return

        motor.set(speed)
    }

    fun stop() {
        motor.stopMotor()
    }

    fun zero() {
        motor.encoder.position = 0.0
    }

    fun speedGoesLeft(speed: Double) = speed > 0

    fun speedGoesRight(speed: Double) = speed < 0
}
