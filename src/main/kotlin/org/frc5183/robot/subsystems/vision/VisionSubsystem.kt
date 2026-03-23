package org.frc5183.robot.subsystems.vision

import edu.wpi.first.wpilibj2.command.SubsystemBase
import org.littletonrobotics.junction.Logger
import org.photonvision.EstimatedRobotPose
import org.photonvision.targeting.PhotonTrackedTarget

class VisionSubsystem(
    val frontCamera: FixedCamera,
    val backCamera: FixedCamera,
) : SubsystemBase() {
    val frontTargets: List<PhotonTrackedTarget>
        get() = frontCamera.targets

    val backTargets: List<PhotonTrackedTarget>
        get() = backCamera.targets

    val frontRobotPose: EstimatedRobotPose?
        get() = frontCamera.estimatedRobotPose

    val backRobotPose: EstimatedRobotPose?
        get() = backCamera.estimatedRobotPose

    override fun periodic() {
        frontCamera.periodic()
        backCamera.periodic()

        Logger.recordOutput("Vision/Targets/Front", frontTargets.map { it.fiducialId }.toIntArray())
        Logger.recordOutput("Vision/Targets/Back", backTargets.map { it.fiducialId }.toIntArray())

        Logger.recordOutput(
            "Vision/EstimatedRobotPoses/Front",
            frontCamera.estimatedRobotPose?.estimatedPose?.toPose2d()
        )
        Logger.recordOutput("Vision/EstimatedRobotPoses/Back", backCamera.estimatedRobotPose?.estimatedPose?.toPose2d())
    }
}
