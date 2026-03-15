package org.frc5183.robot.subsystems.drive.io

import edu.wpi.first.math.Matrix
import edu.wpi.first.math.geometry.Pose2d
import edu.wpi.first.math.geometry.Translation2d
import edu.wpi.first.math.kinematics.ChassisSpeeds
import edu.wpi.first.math.kinematics.SwerveModuleState
import edu.wpi.first.math.numbers.N1
import edu.wpi.first.math.numbers.N3
import edu.wpi.first.units.measure.Force

interface SwerveDriveIO {
    /**
     * Updates the IO inputs with current values to be logged.
     *
     * @param inputs The input object to be updated.
     */
    fun updateInputs(inputs: SwerveDriveIOInputs)

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
     * Get the chassis speeds based on controller input of 2 joysticks
     */
    fun getTargetSpeeds(
        x: Double,
        y: Double,
        headingX: Double,
        headingY: Double,
    ): ChassisSpeeds

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
     * Drive the robot using [SwerveModuleState]s.
     * @param robotRelativeVelocity The robot relative velocity to drive with.
     * @param states The swerve module states to drive with.
     * @param feedforwardForces The feedforward forces to apply to each module.
     */
    fun drive(
        robotRelativeVelocity: ChassisSpeeds,
        states: List<SwerveModuleState>,
        feedforwardForces: List<Force>,
    )

    /**
     * Drive the robot given a chassis field oriented velocity.
     * @param speeds Field oriented chassis speeds to drive with.
     */
    fun driveFieldOriented(speeds: ChassisSpeeds)

    /**
     * Drive the robot given a chassis robot oriented velocity.
     * @param speeds Robot oriented chassis speeds to drive with.
     */
    fun driveRobotOriented(speeds: ChassisSpeeds)
}
