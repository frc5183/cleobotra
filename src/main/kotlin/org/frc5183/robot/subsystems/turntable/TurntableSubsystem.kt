package org.frc5183.robot.subsystems.turntable

import com.revrobotics.spark.SparkMax
import edu.wpi.first.wpilibj.DigitalInput
import edu.wpi.first.wpilibj2.command.SubsystemBase
import org.littletonrobotics.junction.Logger
import org.photonvision.PhotonCamera
import org.photonvision.targeting.PhotonTrackedTarget

class TurntableSubsystem(
    val motor: SparkMax,
    val camera: PhotonCamera,
    val limitSwitch: DigitalInput,
) : SubsystemBase() {
    val speed: Double
        get() = motor.get()

    val targets: Array<PhotonTrackedTarget>
        get() =
            camera.allUnreadResults
                .map { it.targets }
                .flatten()
                .toTypedArray()

    var leftLimitReached: Boolean = false
        private set

    var rightLimitReached: Boolean = false
        private set

    override fun periodic() {
        Logger.recordOutput("Turntable/Speed", motor.get())
        Logger.recordOutput("Turntable/Targets", targets.map { it.fiducialId }.toIntArray())

        Logger.recordOutput("Turntable/Hit Limit", limitSwitch.get())

        if (speedGoesLeft(speed) && limitSwitch.get()) {
            leftLimitReached = true
            stop()
        }

        if (speedGoesRight(speed) && limitSwitch.get()) {
            rightLimitReached = true
            stop()
        }

        if (!limitSwitch.get()) {
            leftLimitReached = false
            rightLimitReached = false
        }
    }

    fun setSpeed(speed: Double) {
        if (leftLimitReached && speedGoesLeft(speed)) return
        if (rightLimitReached && speedGoesRight(speed)) return

        motor.set(speed)
    }

    fun stop() {
        motor.stopMotor()
    }

    fun speedGoesLeft(speed: Double) = speed < 0
    fun speedGoesRight(speed: Double) = speed > 0
}
