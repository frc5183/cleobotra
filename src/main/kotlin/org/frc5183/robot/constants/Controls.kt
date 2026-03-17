package org.frc5183.robot.constants

import edu.wpi.first.wpilibj2.command.Command
import edu.wpi.first.wpilibj2.command.CommandScheduler
import edu.wpi.first.wpilibj2.command.button.CommandXboxController
import org.frc5183.robot.commands.drive.TeleopDriveCommand
import org.frc5183.robot.subsystems.drive.SwerveDriveSubsystem
import swervelib.SwerveInputStream
import kotlin.math.absoluteValue

object Controls {
    val DRIVER_CONTROLLER: CommandXboxController = CommandXboxController(0)
    val OPERATOR_CONTROLLER: CommandXboxController = CommandXboxController(1)

    lateinit var DRIVE_COMMAND: Command

    fun registerControls(drive: SwerveDriveSubsystem) {
        CommandScheduler.getInstance().activeButtonLoop.clear()

        drive.defaultCommand = TELEOP_DRIVE_COMMAND
    }
}
