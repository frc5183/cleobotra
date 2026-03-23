package org.frc5183.robot.subsystems.vision

import edu.wpi.first.apriltag.AprilTagFieldLayout
import edu.wpi.first.math.Matrix
import edu.wpi.first.math.VecBuilder
import edu.wpi.first.math.geometry.Transform3d
import edu.wpi.first.math.numbers.N1
import edu.wpi.first.math.numbers.N3
import edu.wpi.first.networktables.NetworkTablesJNI
import edu.wpi.first.units.Units.Microseconds
import edu.wpi.first.units.Units.Seconds
import org.frc5183.robot.constants.VisionConstants
import org.photonvision.EstimatedRobotPose
import org.photonvision.PhotonCamera
import org.photonvision.PhotonPoseEstimator
import org.photonvision.targeting.PhotonPipelineResult
import org.photonvision.targeting.PhotonTrackedTarget
import kotlin.jvm.optionals.getOrNull
import kotlin.math.max

/**
 * A camera placed at a fixed, known [Transform3d] that therefore can be used for pose estimation.
 */
class FixedCamera(
    /**
     * The nickname of the [FixedCamera] (found in the PhotonVision UI).
     */
    cameraName: String,
    /**
     * The [Transform3d] of the [FixedCamera] in relation to the robot's origin.
     */
    val transform: Transform3d,
    /**
     * The single April Tag standard deviations of estimated poses from the
     * [FixedCamera].
     */
    private val singleTagStandardDeviations: Matrix<N3, N1>,
    /**
     * The multi April Tag standard deviations of estimated poses from the
     * [FixedCamera].
     */
    private val multiTagStandardDeviations: Matrix<N3, N1>,
) : PhotonCamera(cameraName) {
    /**
     * The current visible targets of the camera.
     */
    var targets: List<PhotonTrackedTarget> = emptyList()

    /**
     * Estimated robot pose.
     */
    var estimatedRobotPose: EstimatedRobotPose? = null

    /**
     * Pose estimator for camera.
     */
    private val poseEstimator =
        PhotonPoseEstimator(AprilTagFieldLayout.loadField(VisionConstants.APRIL_TAG_LAYOUT), transform)

    /**
     * Current standard deviations used.
     */
    var currentStandardDeviations: Matrix<N3, N1> = singleTagStandardDeviations

    /**
     * Results list to be updated periodically and cached to avoid unnecessary queries.
     */
    private var resultsList = mutableListOf<PhotonPipelineResult>()

    /**
     * Last read from the camera timestamp to prevent lag due to slow data fetches.
     */
    private var lastReadTimestamp: Double = Microseconds.of(NetworkTablesJNI.now().toDouble()).`in`(Seconds)

    fun periodic() {
        updateUnreadResults()
    }

    private fun updateUnreadResults() {
        var mostRecentTimestamp = resultsList.firstOrNull()?.timestampSeconds ?: 0.0

        for (result in resultsList) {
            mostRecentTimestamp = max(mostRecentTimestamp, result.timestampSeconds)
        }

        resultsList = allUnreadResults
        resultsList.sortByDescending { it.timestampSeconds }

        targets = resultsList.map { it.targets }.flatten()

        if (!resultsList.isEmpty()) {
            updateEstimatedGlobalPose()
        }
    }

    private fun updateEstimatedGlobalPose() {
        var visionEst: EstimatedRobotPose? = null
        for (change in resultsList) {
            visionEst = poseEstimator.estimateCoprocMultiTagPose(change).getOrNull()

            // Fallback estimation if multi tag fails.
            if (visionEst == null) {
                visionEst = poseEstimator.estimateLowestAmbiguityPose(change).getOrNull()
            }

            updateEstimationStdDevs(visionEst, change.getTargets())
        }
        estimatedRobotPose = visionEst
    }

    private fun updateEstimationStdDevs(
        estimatedPose: EstimatedRobotPose?,
        targets: MutableList<PhotonTrackedTarget>,
    ) {
        if (estimatedPose == null) {
            // No pose input. Default to single-tag std devs
            currentStandardDeviations = singleTagStandardDeviations
        } else {
            // Pose present. Start running Heuristic
            var estStdDevs = singleTagStandardDeviations
            var numTags = 0
            var avgDist = 0.0

            // Precalculation - see how many tags we found, and calculate an average-distance metric
            for (target in targets) {
                val tagPose = poseEstimator.fieldTags.getTagPose(target.getFiducialId()).getOrNull() ?: continue
                numTags++
                avgDist += tagPose.toPose2d().translation.getDistance(estimatedPose.estimatedPose.toPose2d().translation)
            }

            if (numTags == 0) {
                // No tags visible. Default to single-tag std devs
                currentStandardDeviations = singleTagStandardDeviations
            } else {
                // One or more tags visible, run the full heuristic.
                avgDist /= numTags.toDouble()
                // Decrease std devs if multiple targets are visible
                if (numTags > 1) {
                    estStdDevs = multiTagStandardDeviations
                }
                // Increase std devs based on (average) distance
                if (numTags == 1 && avgDist > 4) {
                    estStdDevs =
                        VecBuilder.fill(
                            Double.MAX_VALUE,
                            Double.MAX_VALUE,
                            Double.MAX_VALUE,
                        )
                } else {
                    estStdDevs = estStdDevs.times(1 + (avgDist * avgDist / 30))
                }
                currentStandardDeviations = estStdDevs
            }
        }
    }
}
