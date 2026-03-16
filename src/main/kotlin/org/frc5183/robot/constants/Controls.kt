package org.frc5183.robot.constants

import edu.wpi.first.wpilibj2.command.CommandScheduler
import edu.wpi.first.wpilibj2.command.button.CommandXboxController
import org.frc5183.robot.commands.drive.TeleopDriveCommand
import org.frc5183.robot.subsystems.drive.SwerveDriveSubsystem
import kotlin.math.absoluteValue

object Controls {
    val DRIVER_CONTROLLER: CommandXboxController = CommandXboxController(0)
    val OPERATOR_CONTROLLER: CommandXboxController = CommandXboxController(1)

    lateinit var TELEOP_DRIVE_COMMAND: TeleopDriveCommand

    fun registerControls(drive: SwerveDriveSubsystem) {
        CommandScheduler.getInstance().activeButtonLoop.clear()

        TELEOP_DRIVE_COMMAND =
            TeleopDriveCommand(
                drive,
                xInput = { if (DRIVER_CONTROLLER.leftX.absoluteValue < .2) 0.0 else DRIVER_CONTROLLER.leftX },
                yInput = { if (DRIVER_CONTROLLER.leftY.absoluteValue < .2) 0.0 else DRIVER_CONTROLLER.leftY },
                rotationInput = { 0.0 },
                fieldRelative = false,
            )

        drive.defaultCommand = TELEOP_DRIVE_COMMAND
    }
}
