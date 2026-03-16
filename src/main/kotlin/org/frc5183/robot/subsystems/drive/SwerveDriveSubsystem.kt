package org.frc5183.robot.subsystems.drive

import com.pathplanner.lib.auto.AutoBuilder
import com.pathplanner.lib.commands.PathfindingCommand
import com.pathplanner.lib.controllers.PPHolonomicDriveController
import com.pathplanner.lib.pathfinding.Pathfinding
import com.pathplanner.lib.util.DriveFeedforwards
import edu.wpi.first.math.geometry.Pose2d
import edu.wpi.first.math.geometry.Translation2d
import edu.wpi.first.math.kinematics.ChassisSpeeds
import edu.wpi.first.math.kinematics.SwerveDriveKinematics
import edu.wpi.first.math.kinematics.SwerveModuleState
import edu.wpi.first.units.measure.Force
import edu.wpi.first.wpilibj.DriverStation
import edu.wpi.first.wpilibj2.command.CommandScheduler
import edu.wpi.first.wpilibj2.command.SubsystemBase
import org.frc5183.robot.constants.AutoConstants
import org.frc5183.robot.constants.swerve.SwerveConstants
import org.frc5183.robot.constants.swerve.SwervePIDConstants
import org.frc5183.robot.constants.swerve.toPathPlannerPIDConstants
import org.frc5183.robot.math.pathfinding.LocalADStarAK
import org.frc5183.robot.subsystems.drive.io.SwerveDriveIO
import org.frc5183.robot.subsystems.drive.io.SwerveDriveIOInputs
import org.frc5183.robot.subsystems.vision.VisionSubsystem
import org.littletonrobotics.junction.Logger
import swervelib.math.SwerveMath
import swervelib.telemetry.SwerveDriveTelemetry
import kotlin.jvm.optionals.getOrNull

class SwerveDriveSubsystem(
    private val io: SwerveDriveIO,
    private val vision: VisionSubsystem? = null,
) : SubsystemBase() {
    private val ioInputs = SwerveDriveIOInputs()

    val robotPose: Pose2d
        get() = ioInputs.pose

    val robotVelocity: ChassisSpeeds
        get() = ioInputs.robotVelocity

    val fieldVelocity: ChassisSpeeds
        get() = ioInputs.fieldVelocity

    val moduleStates: Array<out SwerveModuleState>
        get() = ioInputs.moduleStates

    val kinematics: SwerveDriveKinematics
        get() = ioInputs.kinematics

    init {
        SwerveDriveTelemetry.verbosity = SwerveConstants.VERBOSITY

        AutoBuilder.configure(
            { robotPose },
            this::resetPose,
            { robotVelocity },
            { robotRelativeSpeeds: ChassisSpeeds, feedforwards: DriveFeedforwards ->
                drive(
                    robotRelativeSpeeds,
                    kinematics.toSwerveModuleStates(robotRelativeSpeeds),
                    feedforwards.linearForces(),
                )
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
    }

    override fun periodic() {
        io.updateInputs(ioInputs)
        Logger.processInputs("Swerve", ioInputs)

        vision?.estimatedRobotPoses?.forEach { (camera, pose) ->
            io.addVisionMeasurement(pose.estimatedPose.toPose2d(), pose.timestampSeconds, camera.currentStandardDeviations)
        }
    }

    fun setMotorBrake(brake: Boolean) = io.setMotorBrake(brake)

    fun resetPose(pose: Pose2d = Pose2d.kZero) = io.resetPose(pose)

    fun getTargetSpeeds(
        x: Double,
        y: Double,
        headingX: Double = 0.0,
        headingY: Double = 0.0,
    ): ChassisSpeeds {
        val scaledInputs = SwerveMath.cubeTranslation(Translation2d(x, y))
        return io.getTargetSpeeds(scaledInputs.x, scaledInputs.y, headingX, headingY)
    }

    fun drive(
        translation: Translation2d,
        rotation: Double,
        fieldOriented: Boolean,
        openLoop: Boolean = false,
    ) = io.drive(translation, rotation, fieldOriented, openLoop)

    fun drive(
        robotRelativeVelocity: ChassisSpeeds,
        states: Array<out SwerveModuleState>,
        feedforwardForces: Array<out Force>,
    ) = io.drive(robotRelativeVelocity, states, feedforwardForces)

    fun driveFieldOriented(speeds: ChassisSpeeds) = io.driveFieldOriented(speeds)

    fun driveRobotOriented(speeds: ChassisSpeeds) = io.driveRobotOriented(speeds)
}
