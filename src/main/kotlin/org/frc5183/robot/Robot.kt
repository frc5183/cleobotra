package org.frc5183.robot

import com.pathplanner.lib.auto.AutoBuilder
import com.pathplanner.lib.auto.NamedCommands
import com.pathplanner.lib.commands.PathPlannerAuto
import edu.wpi.first.hal.FRCNetComm
import edu.wpi.first.hal.HAL
import edu.wpi.first.math.geometry.Pose2d
import edu.wpi.first.math.geometry.Rotation2d
import edu.wpi.first.units.Units
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard
import edu.wpi.first.wpilibj.util.WPILibVersion
import edu.wpi.first.wpilibj2.command.Command
import edu.wpi.first.wpilibj2.command.CommandScheduler
import edu.wpi.first.wpilibj2.command.Commands
import org.frc5183.robot.commands.collector.IntakeCommand
import org.frc5183.robot.commands.collector.LowerCollector
import org.frc5183.robot.commands.collector.RaiseCollector
import org.frc5183.robot.commands.shooter.AlignAndShoot
import org.frc5183.robot.commands.shooter.LockedShoot
import org.frc5183.robot.constants.*
import org.frc5183.robot.constants.swerve.SwerveConstants
import org.frc5183.robot.subsystems.climber.ClimberSubsystem
import org.frc5183.robot.subsystems.collector.CollectorSubsystem
import org.frc5183.robot.subsystems.drive.SwerveDriveSubsystem
import org.frc5183.robot.subsystems.shooter.ShooterSubsystem
import org.frc5183.robot.subsystems.turntable.TurntableSubsystem
import org.littletonrobotics.junction.LoggedRobot
import org.littletonrobotics.junction.Logger
import org.littletonrobotics.junction.networktables.NT4Publisher
import org.littletonrobotics.junction.wpilog.WPILOGWriter
import swervelib.SwerveDrive
import kotlin.jvm.optionals.getOrNull

object Robot : LoggedRobot() {
    private val drive: SwerveDriveSubsystem
    private val shooter: ShooterSubsystem
    private val collector: CollectorSubsystem
    private val turntable: TurntableSubsystem
    private val climber: ClimberSubsystem

    private val autoChooser: SendableChooser<Command>

    init {
        HAL.report(
            FRCNetComm.tResourceType.kResourceType_Language,
            FRCNetComm.tInstances.kLanguage_Kotlin,
            0,
            WPILibVersion.Version,
        )

        HAL.report(FRCNetComm.tResourceType.kResourceType_Framework, FRCNetComm.tInstances.kFramework_AdvantageKit)

        Logger.recordMetadata("ProjectName", MAVEN_NAME)
        Logger.recordMetadata("BuildDate", BUILD_DATE)
        Logger.recordMetadata("BuildDateUnix", BUILD_UNIX_TIME.toString())
        Logger.recordMetadata("GitSHA", GIT_SHA)
        Logger.recordMetadata("GitDate", GIT_DATE)
        Logger.recordMetadata("GitBranch", GIT_BRANCH)

        when (DIRTY) {
            0 -> Logger.recordMetadata("GitDirty", "All changes committed")
            1 -> Logger.recordMetadata("GitDirty", "Uncommitted changes")
            else -> Logger.recordMetadata("GitDirty", "Unknown")
        }

        if (isReal()) {
            Logger.addDataReceiver(WPILOGWriter())
            Logger.addDataReceiver(NT4Publisher())
        }

        Logger.start()

        drive =
            SwerveDriveSubsystem(
                SwerveDrive(
                    SwerveConstants.YAGSL,
                    SwerveConstants.YAGSL_CONTROLLER_CONFIG,
                    PhysicalConstants.MAX_VELOCITY.`in`(Units.MetersPerSecond),
                    Pose2d(5.0, 5.0, Rotation2d.kZero),
                ),
                null,
            )

        shooter =
            ShooterSubsystem(
                DeviceConstants.SHOOTER,
                DeviceConstants.SHOOTER_INTAKE,
                DeviceConstants.FEEDER,
            )

        collector =
            CollectorSubsystem(
                DeviceConstants.COLLECTOR_ARM,
                DeviceConstants.COLLECTOR_INTAKE,
                DeviceConstants.COLLECTOR_TOP_LIMIT_SWITCH,
                DeviceConstants.COLLECTOR_OTHER_TOP_LIMIT_SWITCH,
                DeviceConstants.COLLECTOR_BOTTOM_LIMIT_SWITCH,
            )

        turntable =
            TurntableSubsystem(
                DeviceConstants.TURNTABLE_MOTOR,
                DeviceConstants.TURNTABLE_CAMERA,
                DeviceConstants.TURNTABLE_LIMIT_SWITCH,
            )

        climber =
            ClimberSubsystem(
                DeviceConstants.CLIMBER_MOTOR,
            )

        NamedCommands.registerCommand("Lower Collector", LowerCollector(collector))
        NamedCommands.registerCommand("Raise Collector", RaiseCollector(collector))
        NamedCommands.registerCommand("Run Collector Intake", IntakeCommand(collector))
        NamedCommands.registerCommand("Turret Shoot", AlignAndShoot(shooter, turntable))
        NamedCommands.registerCommand("Locked Shoot", LockedShoot(shooter, turntable, drive))

        autoChooser = AutoBuilder.buildAutoChooser()
        SmartDashboard.putData("Auto Chooser", autoChooser)

        CommandScheduler.getInstance().onCommandInitialize { println("${it.name} initialized") }
        CommandScheduler.getInstance().onCommandFinish { println("${it.name} finished") }
        CommandScheduler.getInstance().onCommandInterrupt {
            command,
            action,
            ->
            println("${command.name} interrupted by ${action.getOrNull()?.name}")
        }
    }

    override fun robotPeriodic() {
        CommandScheduler.getInstance().run()
    }

    override fun teleopInit() {
        CommandScheduler.getInstance().cancelAll()
        Controls.registerControls(drive, shooter, collector, turntable, climber)
    }

    override fun teleopPeriodic() {
//        SmartDashboard.putBoolean("Turntable/Safety Override", turntable.safetyOverride)
//        turntable.safetyOverride = SmartDashboard.getBoolean("Turntable/Safety Override", false)
    }

    var autoCommand: PathPlannerAuto? = null

    override fun autonomousInit() {
        CommandScheduler.getInstance().cancelAll()

        autoCommand = PathPlannerAuto(autoChooser.selected)
        autoCommand?.isRunning?.whileTrue(Commands.print("${autoChooser.selected.name} started"))

        autoCommand?.schedule()
    }

    override fun autonomousPeriodic() {
    }

    override fun testInit() {
        CommandScheduler.getInstance().cancelAll()
        Controls.registerTestingControls(drive, shooter, collector, turntable, climber)
    }

    override fun printWatchdogEpochs() {
        return
    }
}
