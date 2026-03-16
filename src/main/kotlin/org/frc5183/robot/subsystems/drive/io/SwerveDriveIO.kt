package org.frc5183.robot.subsystems.drive.io

import edu.wpi.first.math.Matrix
import edu.wpi.first.math.geometry.Pose2d
import edu.wpi.first.math.geometry.Rotation2d
import edu.wpi.first.math.geometry.Translation2d
import edu.wpi.first.math.kinematics.ChassisSpeeds
import edu.wpi.first.math.kinematics.SwerveModuleState
import edu.wpi.first.math.numbers.N1
import edu.wpi.first.math.numbers.N3
import org.frc5183.robot.logging.AutoLogInputs

interface SwerveDriveIO {
    class SwerveDriveIOInputs : AutoLogInputs() {
        var pose by log(Pose2d())
        var velocity by log(ChassisSpeeds())
        var moduleStates by log(emptyArray<SwerveModuleState>())
    }

    /**
     * Updates the IO inputs with current values to be logged.
     *
     * @param inputs The input object to be updated.
     */
    fun updateInputs(inputs: SwerveDriveIOInputs)

    /**
     * The current pose of the robot according to odometry/vision estimates.
     */
    val pose: Pose2d

    /**
     * The current robot-relative speed/velocity of the robot according to odometry.
     */
    val velocity: ChassisSpeeds

    /**
     * Stops YAGSL's odometry thread.
     * If this is stopped then [updateOdometry] must be called.
     */
    fun stopOdometryThread()

    /**
     * Adds a vision measurement to the swerve drive.
     * @param pose The pose to add.
     * @param timestampSeconds The timestamp of the measurement.
     * @param standardDeviations The standard deviations of the measurement.
     */
    fun addVisionMeasurement(
        pose: Pose2d,
        timestampSeconds: Double,
        standardDeviations: Matrix<N3, N1>,
    )

    /**
     * Updates the robot's odometry using YAGSL.
     * This should only be run when the odometry thread is stopped (which should
     * only be when vision pose estimation is enabled).
     */
    fun updateOdometry()

    /**
     * Sets the drive motors to brake/coast mode.
     * @param brake Whether to set the motors to brake mode (true) or coast mode (false).
     */
    fun setMotorBrake(brake: Boolean)

    /**
     * Resets the robot's pose to [pose].
     */
    fun resetPose(pose: Pose2d = Pose2d.kZero)

    /**
     * Returns a [ChassisSpeeds] object based off x and y inputs from -1 to 1.
     */
    fun getTargetSpeeds(x: Double, y: Double, angle: Rotation2d): ChassisSpeeds

    /**
     * Drives the robot with the given translation and rotation.
     * @param translation The translation to drive with.
     * @param rotation The rotation to drive with.
     * @param fieldOriented Whether the translation is field-oriented.
     * @param openLoop Whether the drive is open-loop.
     */
    fun drive(
        translation: Translation2d,
        rotation: Double,
        fieldOriented: Boolean,
        openLoop: Boolean,
    )

    /**
     * Drives the robot with the given chassis speeds.
     * @param speeds The chassis speeds to drive with.
     */
    fun drive(speeds: ChassisSpeeds)
}
