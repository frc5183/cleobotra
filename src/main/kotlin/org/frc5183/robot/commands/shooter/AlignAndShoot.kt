package org.frc5183.robot.commands.shooter

import edu.wpi.first.wpilibj2.command.ParallelCommandGroup
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup
import org.frc5183.robot.commands.turntable.AlignTurntable
import org.frc5183.robot.commands.turntable.ConstantAlignTurntable
import org.frc5183.robot.subsystems.shooter.ShooterSubsystem
import org.frc5183.robot.subsystems.turntable.TurntableSubsystem

class AlignAndShoot(
    shooter: ShooterSubsystem,
    turntable: TurntableSubsystem,
) : ParallelCommandGroup() {
    init {
        addRequirements(shooter)
        addRequirements(turntable)

        addCommands(
            ShootByDistance(shooter, { turntable.distanceToTarget }),
            ConstantAlignTurntable(turntable),
        )
    }
}
