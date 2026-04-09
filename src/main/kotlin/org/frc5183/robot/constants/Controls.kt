package org.frc5183.robot.constants

import edu.wpi.first.wpilibj2.command.CommandScheduler
import edu.wpi.first.wpilibj2.command.InstantCommand
import edu.wpi.first.wpilibj2.command.button.CommandXboxController
import org.frc5183.robot.commands.climber.LowerClimber
import org.frc5183.robot.commands.climber.RaiseClimber
import org.frc5183.robot.commands.collector.DriveCollector
import org.frc5183.robot.commands.collector.IntakeCommand
import org.frc5183.robot.commands.collector.LowerCollector
import org.frc5183.robot.commands.collector.RaiseCollector
import org.frc5183.robot.commands.shooter.AlignAndShoot
import org.frc5183.robot.commands.shooter.LockedShoot
import org.frc5183.robot.commands.shooter.ReverseShooter
import org.frc5183.robot.commands.shooter.ShootCommand
import org.frc5183.robot.commands.turntable.ConstantAlignTurntable
import org.frc5183.robot.commands.turntable.DriveTurntable
import org.frc5183.robot.commands.turntable.HomeTurntable
import org.frc5183.robot.subsystems.climber.ClimberSubsystem
import org.frc5183.robot.subsystems.collector.CollectorSubsystem
import org.frc5183.robot.subsystems.drive.SwerveDriveSubsystem
import org.frc5183.robot.subsystems.shooter.ShooterSubsystem
import org.frc5183.robot.subsystems.turntable.TurntableSubsystem
import swervelib.SwerveInputStream

object Controls {
    val DRIVER_CONTROLLER: CommandXboxController = CommandXboxController(0)
    val OPERATOR_CONTROLLER: CommandXboxController = CommandXboxController(1)

    enum class ControlProfiles(
        val displayName: String,
    ) {
        AUTONOMOUS("Autonomous"),
        MANUAL("Manual"),
        ;

        override fun toString(): String = displayName
    }

    fun registerCommonTeleopControls(
        drive: SwerveDriveSubsystem,
        shooter: ShooterSubsystem,
        collector: CollectorSubsystem,
        turntable: TurntableSubsystem,
        climber: ClimberSubsystem,
    ) {
        val driveInput =
            SwerveInputStream
                .of(
                    drive.drive,
                    { -DRIVER_CONTROLLER.leftY },
                    { DRIVER_CONTROLLER.leftX },
                ).withControllerRotationAxis { DRIVER_CONTROLLER.rightX }
                .deadband(0.2)
                .scaleTranslation(0.8)
                .robotRelative(true)

        // SwerveInputStream always supplies field oriented chassis speeds it seems.
        drive.defaultCommand = drive.driveFieldOriented(driveInput)

        OPERATOR_CONTROLLER.x().toggleOnTrue(ReverseShooter(shooter))

        OPERATOR_CONTROLLER.rightBumper().onTrue(LowerCollector(collector))
        OPERATOR_CONTROLLER.leftBumper().onTrue(RaiseCollector(collector))
        OPERATOR_CONTROLLER.y().toggleOnTrue(IntakeCommand(collector))
        OPERATOR_CONTROLLER.leftStick().toggleOnTrue(DriveCollector(collector) { OPERATOR_CONTROLLER.leftY })
        OPERATOR_CONTROLLER.rightStick().toggleOnTrue(DriveTurntable(turntable) { OPERATOR_CONTROLLER.rightX })

        OPERATOR_CONTROLLER.leftTrigger().whileTrue(LowerClimber(climber))
        OPERATOR_CONTROLLER.rightTrigger().whileTrue(RaiseClimber(climber))

        DRIVER_CONTROLLER.b().onTrue(InstantCommand({ CommandScheduler.getInstance().cancelAll() }))
        OPERATOR_CONTROLLER.b().onTrue(InstantCommand({ CommandScheduler.getInstance().cancelAll() }))
    }

    /**
     * These controls are used in teleoperated mode when the robot has working
     * autonomous code for its subsystems (shooter, turntable, etc.) this removes
     * some of the operator's manual control in place for autonomous control only.
     */
    fun registerControlsAutonomousProfile(
        drive: SwerveDriveSubsystem,
        shooter: ShooterSubsystem,
        collector: CollectorSubsystem,
        turntable: TurntableSubsystem,
        climber: ClimberSubsystem,
    ) {
        registerCommonTeleopControls(drive, shooter, collector, turntable, climber)

        OPERATOR_CONTROLLER.a().toggleOnTrue(AlignAndShoot(shooter, turntable, { drive.robotPose }))
        OPERATOR_CONTROLLER.povUp().toggleOnTrue(LockedShoot(shooter, turntable, drive))

        OPERATOR_CONTROLLER.povLeft().toggleOnTrue(ConstantAlignTurntable(turntable, { drive.robotPose }))

        OPERATOR_CONTROLLER.povRight().onTrue(HomeTurntable(turntable))
    }

    /**
     * These controls are used in teleoperated mode when the robot does not have
     * working autonomous code for its subsystems (shooter, turntable, etc.) this ensures
     * that the operator has manual control over all subsystems, in case something were to break.
     */
    fun registerControlsManualProfile(
        drive: SwerveDriveSubsystem,
        shooter: ShooterSubsystem,
        collector: CollectorSubsystem,
        turntable: TurntableSubsystem,
        climber: ClimberSubsystem,
    ) {
        registerCommonTeleopControls(drive, shooter, collector, turntable, climber)

        OPERATOR_CONTROLLER.povUp().toggleOnTrue(ShootCommand(shooter, 0.65))
        OPERATOR_CONTROLLER.povUpRight().toggleOnTrue(ShootCommand(shooter, 0.70))
        OPERATOR_CONTROLLER.povRight().toggleOnTrue(ShootCommand(shooter, 0.75))
        OPERATOR_CONTROLLER.povDownRight().toggleOnTrue(ShootCommand(shooter, 0.80))
        OPERATOR_CONTROLLER.povDown().toggleOnTrue(ShootCommand(shooter, 0.85))
        OPERATOR_CONTROLLER.povDownLeft().toggleOnTrue(ShootCommand(shooter, 0.90))
        OPERATOR_CONTROLLER.povLeft().toggleOnTrue(ShootCommand(shooter, 0.95))
        OPERATOR_CONTROLLER.povUpLeft().toggleOnTrue(ShootCommand(shooter, 1.0))
    }

    fun registerTestingControls(
        drive: SwerveDriveSubsystem,
        shooter: ShooterSubsystem,
        collector: CollectorSubsystem,
        turntable: TurntableSubsystem,
        climber: ClimberSubsystem,
    ) {
        // This is intentionally left blank for feature branches to use as needed.
    }
}
