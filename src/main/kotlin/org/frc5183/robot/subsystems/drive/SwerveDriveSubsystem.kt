package org.frc5183.robot.subsystems.drive

import com.pathplanner.lib.auto.AutoBuilder
import com.pathplanner.lib.commands.PathfindingCommand
import com.pathplanner.lib.controllers.PPHolonomicDriveController
import com.pathplanner.lib.pathfinding.Pathfinding
import com.pathplanner.lib.util.DriveFeedforwards
import edu.wpi.first.math.Matrix
import edu.wpi.first.math.geometry.Pose2d
import edu.wpi.first.math.geometry.Translation2d
import edu.wpi.first.math.kinematics.ChassisSpeeds
import edu.wpi.first.math.kinematics.SwerveDriveKinematics
import edu.wpi.first.math.kinematics.SwerveModuleState
import edu.wpi.first.math.numbers.N1
import edu.wpi.first.math.numbers.N3
import edu.wpi.first.units.Units
import edu.wpi.first.units.measure.Force
import edu.wpi.first.wpilibj.DriverStation
import edu.wpi.first.wpilibj2.command.Command
import edu.wpi.first.wpilibj2.command.CommandScheduler
import edu.wpi.first.wpilibj2.command.SubsystemBase
import org.frc5183.robot.constants.AutoConstants
import org.frc5183.robot.constants.PhysicalConstants
import org.frc5183.robot.constants.swerve.SwerveConstants
import org.frc5183.robot.constants.swerve.SwervePIDConstants
import org.frc5183.robot.constants.swerve.toPathPlannerPIDConstants
import org.frc5183.robot.math.pathfinding.LocalADStarAK
import org.frc5183.robot.subsystems.vision.VisionSubsystem
import swervelib.SwerveDrive
import swervelib.math.SwerveMath
import swervelib.telemetry.SwerveDriveTelemetry
import java.util.function.Supplier
import kotlin.jvm.optionals.getOrNull

class SwerveDriveSubsystem(
    val drive: SwerveDrive,
    val vision: VisionSubsystem?,
) : SubsystemBase() {
    val robotPose: Pose2d
        get() = drive.pose

    val robotVelocity: ChassisSpeeds
        get() = drive.robotVelocity

    val fieldVelocity: ChassisSpeeds
        get() = drive.fieldVelocity

    val moduleStates: Array<out SwerveModuleState>
        get() = drive.states

    val kinematics: SwerveDriveKinematics
        get() = drive.kinematics

    init {
        SwerveDriveTelemetry.verbosity = SwerveConstants.VERBOSITY

        drive.headingCorrection = false
        drive.setCosineCompensator(false)
        drive.setAngularVelocityCompensation(true, false, 0.1)
        drive.setModuleEncoderAutoSynchronize(true, 1.0)

        AutoBuilder.configure(
            { robotPose },
            this::resetPose,
            { robotVelocity },
            { robotRelativeSpeeds: ChassisSpeeds, feedforwards: DriveFeedforwards ->
                if (AutoConstants.USE_FEED_FORWARD) {
                    drive(
                        robotRelativeSpeeds,
                        kinematics.toSwerveModuleStates(robotRelativeSpeeds),
                        feedforwards.linearForces(),
                    )
                } else driveRobotOriented(robotRelativeSpeeds)
            },
            PPHolonomicDriveController(
                SwervePIDConstants.DRIVE_PID.toPathPlannerPIDConstants(),
                SwervePIDConstants.ANGLE_PID.toPathPlannerPIDConstants(),
            ),
            AutoConstants.PATHPLANNER_CONFIG,
            { DriverStation.getAlliance().getOrNull() == DriverStation.Alliance.Red },
            this,
        )

        Pathfinding.setPathfinder(LocalADStarAK())

        // https://pathplanner.dev/pplib-follow-a-single-path.html#java-warmup
        CommandScheduler.getInstance().schedule(PathfindingCommand.warmupCommand())

        if (vision != null) {
            drive.stopOdometryThread()
        }
    }

    override fun periodic() {
        if (vision != null) {
            vision.frontRobotPose?.let {
                addVisionMeasurement(it.estimatedPose.toPose2d(), it.timestampSeconds, vision.frontCamera.currentStandardDeviations)
            }

            vision.backRobotPose?.let {
                addVisionMeasurement(it.estimatedPose.toPose2d(), it.timestampSeconds, vision.backCamera.currentStandardDeviations)
            }

            drive.updateOdometry()
        }
    }

    private fun addVisionMeasurement(
        pose: Pose2d,
        timestampSeconds: Double,
        standardDeviations: Matrix<N3, N1>,
    ) = drive.addVisionMeasurement(pose, timestampSeconds, standardDeviations)

    fun setMotorBrake(brake: Boolean) = drive.setMotorIdleMode(brake)

    fun resetPose(pose: Pose2d = Pose2d.kZero) = drive.resetOdometry(pose)

    fun getTargetSpeeds(
        x: Double,
        y: Double,
        headingX: Double = 0.0,
        headingY: Double = 0.0,
    ): ChassisSpeeds {
        val scaledInputs = SwerveMath.cubeTranslation(Translation2d(x, y))
        return drive.swerveController.getTargetSpeeds(
            scaledInputs.x,
            scaledInputs.y,
            headingX,
            headingY,
            drive.pose.rotation.radians,
            PhysicalConstants.MAX_VELOCITY.`in`(Units.MetersPerSecond),
        )
    }

    fun drive(
        translation: Translation2d,
        rotation: Double,
        fieldOriented: Boolean,
        openLoop: Boolean = false,
    ) = drive.drive(translation, rotation, fieldOriented, openLoop)

    fun drive(
        robotRelativeVelocity: ChassisSpeeds,
        states: Array<out SwerveModuleState>,
        feedforwardForces: Array<out Force>,
    ) = drive.drive(robotRelativeVelocity, states, feedforwardForces)

    fun driveFieldOriented(speeds: ChassisSpeeds) = drive.driveFieldOriented(speeds)

    fun driveFieldOriented(speedsSupplier: Supplier<ChassisSpeeds>): Command = run { driveFieldOriented(speedsSupplier.get()) }

    fun driveRobotOriented(speeds: ChassisSpeeds) = drive.drive(speeds)

    fun driveRobotOriented(speedsSupplier: Supplier<ChassisSpeeds>): Command = run { driveRobotOriented(speedsSupplier.get()) }
}
