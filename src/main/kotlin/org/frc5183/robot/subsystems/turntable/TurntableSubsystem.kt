package org.frc5183.robot.subsystems.turntable

import com.revrobotics.spark.SparkMax
import edu.wpi.first.wpilibj.DigitalInput
import edu.wpi.first.wpilibj2.command.SubsystemBase
import org.littletonrobotics.junction.Logger
import org.photonvision.PhotonCamera
import org.photonvision.targeting.PhotonPipelineResult
import org.photonvision.targeting.PhotonTrackedTarget
import kotlin.math.max

class TurntableSubsystem(
    val motor: SparkMax,
    val camera: PhotonCamera,
    val limitSwitch: DigitalInput,
) : SubsystemBase() {
    var safetyOverride: Boolean = false

    val speed: Double
        get() = motor.get()

    var resultsList: MutableList<PhotonPipelineResult> = mutableListOf()
        private set

    var targets: List<PhotonTrackedTarget> = emptyList()
        private set

    var leftLimitReached: Boolean = false
        private set

    var rightLimitReached: Boolean = false
        private set

    override fun periodic() {
        Logger.recordOutput("Turntable/Speed", motor.get())
        Logger.recordOutput("Turntable/Targets", targets.map { it.fiducialId }.toIntArray())

        Logger.recordOutput("Turntable/Hit Limit", limitSwitch.get())
        Logger.recordOutput("Turntable/Left Limit", leftLimitReached)
        Logger.recordOutput("Turntable/Right Limit", rightLimitReached)

        if (!safetyOverride && limitSwitch.get()) {
            if (speedGoesLeft(speed) && !rightLimitReached) {
                leftLimitReached = true
                stop()
            } else if (speedGoesRight(speed) && !leftLimitReached) {
                rightLimitReached = true
                stop()
            }
        }

        if (!limitSwitch.get()) {
            leftLimitReached = false
            rightLimitReached = false
        }

        updateUnreadResults()
    }

    private fun updateUnreadResults() {
        var mostRecentTimestamp = resultsList.firstOrNull()?.timestampSeconds ?: 0.0

        for (result in resultsList) {
            mostRecentTimestamp = max(mostRecentTimestamp, result.timestampSeconds)
        }

        resultsList = camera.allUnreadResults
        resultsList.sortByDescending { it.timestampSeconds }

        targets = resultsList.map { it.targets }.flatten()
    }

    fun setSpeed(speed: Double) {
        if (!safetyOverride && leftLimitReached && speedGoesLeft(speed)) return
        if (!safetyOverride && rightLimitReached && speedGoesRight(speed)) return

        motor.set(speed)
    }

    fun stop() {
        motor.stopMotor()
    }

    fun speedGoesLeft(speed: Double) = speed > 0
    fun speedGoesRight(speed: Double) = speed < 0
}
