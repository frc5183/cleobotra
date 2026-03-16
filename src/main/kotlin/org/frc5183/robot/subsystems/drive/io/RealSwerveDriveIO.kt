package org.frc5183.robot.subsystems.drive.io

import edu.wpi.first.math.Matrix
import edu.wpi.first.math.geometry.Pose2d
import edu.wpi.first.math.geometry.Rotation2d
import edu.wpi.first.math.geometry.Translation2d
import edu.wpi.first.math.kinematics.ChassisSpeeds
import edu.wpi.first.math.kinematics.SwerveModuleState
import edu.wpi.first.math.numbers.N1
import edu.wpi.first.math.numbers.N3
import edu.wpi.first.units.measure.Force
import edu.wpi.first.units.Units
import org.frc5183.robot.constants.PhysicalConstants
import org.frc5183.robot.constants.swerve.SwerveConstants
import swervelib.SwerveDrive
import swervelib.math.SwerveMath

open class RealSwerveDriveIO(
    private val drive: SwerveDrive,
) : SwerveDriveIO {
    init {
        drive.headingCorrection = false
        drive.setCosineCompensator(SwerveConstants.COSINE_COMPENSATOR)
    }

    override fun updateInputs(inputs: SwerveDriveIOInputs) {
        inputs.pose = drive.pose
        inputs.robotVelocity = drive.robotVelocity
        inputs.fieldVelocity = drive.fieldVelocity
        inputs.moduleStates = drive.states
        inputs.kinematics = drive.kinematics
    }

    override fun stopOdometryThread() = drive.stopOdometryThread()

    override fun updateOdometry() = drive.updateOdometry()

    override fun addVisionMeasurement(
        pose: Pose2d,
        timestampSeconds: Double,
        standardDeviations: Matrix<N3, N1>,
    ) = drive.addVisionMeasurement(pose, timestampSeconds, standardDeviations)

    override fun setMotorBrake(brake: Boolean) = drive.setMotorIdleMode(brake)

    override fun resetPose(pose: Pose2d) = drive.resetOdometry(pose)

    override fun getTargetSpeeds(
        x: Double,
        y: Double,
        headingX: Double,
        headingY: Double,
    ): ChassisSpeeds =
        drive.swerveController.getTargetSpeeds(x, y, headingX, headingY, drive.pose.rotation.radians, TODO("requires constants"))

    override fun drive(
        translation: Translation2d,
        rotation: Double,
        fieldOriented: Boolean,
        openLoop: Boolean,
    ) = drive.drive(translation, rotation, fieldOriented, openLoop)

    override fun drive(
        robotRelativeVelocity: ChassisSpeeds,
        states: Array<out SwerveModuleState>,
        feedforwardForces: Array<out Force>,
    ) = drive.drive(robotRelativeVelocity, states, feedforwardForces)

    override fun driveFieldOriented(speeds: ChassisSpeeds) = drive.driveFieldOriented(speeds)

    override fun driveRobotOriented(speeds: ChassisSpeeds) = drive.drive(speeds)
}
