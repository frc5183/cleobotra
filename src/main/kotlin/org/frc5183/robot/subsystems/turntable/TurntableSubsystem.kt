package org.frc5183.robot.subsystems.turntable

import com.revrobotics.spark.SparkMax
import edu.wpi.first.wpilibj2.command.SubsystemBase
import org.littletonrobotics.junction.Logger
import org.photonvision.PhotonCamera
import org.photonvision.targeting.PhotonTrackedTarget

class TurntableSubsystem(
    val motor: SparkMax,
    val camera: PhotonCamera,
) : SubsystemBase() {
    val targets: Array<PhotonTrackedTarget>
        get() =
            camera.allUnreadResults
                .map { it.targets }
                .flatten()
                .toTypedArray()

    override fun periodic() {
        Logger.recordOutput("Turntable/Speed", motor.get())
        Logger.recordOutput("Turntable/Targets", targets.map { it.fiducialId }.toIntArray())
    }

    fun setSpeed(speed: Double) {
        motor.set(speed)
    }

    fun stop() {
        motor.stopMotor()
    }
}
