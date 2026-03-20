package org.frc5183.robot.commands.auto

import com.pathplanner.lib.path.PathConstraints
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup
import org.frc5183.robot.subsystems.drive.SwerveDriveSubsystem

/**
 * Automatically drives to the climb position with the correct alliance.
 * @param drive The [SwerveDriveSubsystem] to control.
 * @param side The side of the climb to drive to.
 * @param constraints The [PathConstraints] to use for the pathfinding.
 */
class DriveToClimb(
    private val drive: SwerveDriveSubsystem,
    private val constraints: PathConstraints = TODO("requires constants"),
) : SequentialCommandGroup() {
    init {
        addRequirements(drive)

        addCommands(
            DriveToPose(
                drive,
                TODO()
            ),
        )
    }
}
