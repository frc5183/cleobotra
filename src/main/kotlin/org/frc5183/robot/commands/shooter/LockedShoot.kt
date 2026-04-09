package org.frc5183.robot.commands.shooter

import edu.wpi.first.wpilibj2.command.Commands
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup
import org.frc5183.robot.subsystems.drive.SwerveDriveSubsystem
import org.frc5183.robot.subsystems.shooter.ShooterSubsystem
import org.frc5183.robot.subsystems.turntable.TurntableSubsystem

class LockedShoot(
    shooter: ShooterSubsystem,
    turntable: TurntableSubsystem,
    drive: SwerveDriveSubsystem,
) : SequentialCommandGroup() {
    init {
        addRequirements(shooter)
        addRequirements(turntable)
        addRequirements(drive)

        addCommands(
            Commands.run({ drive.lockWheels() }, drive),
            AlignAndShoot(shooter, turntable, { drive.robotPose }),
        )
    }
}
