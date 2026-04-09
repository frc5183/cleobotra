package org.frc5183.robot.commands.shooter

import edu.wpi.first.math.geometry.Pose2d
import edu.wpi.first.wpilibj2.command.ParallelCommandGroup
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup
import org.frc5183.robot.commands.turntable.AlignTurntable
import org.frc5183.robot.commands.turntable.ConstantAlignTurntable
import org.frc5183.robot.subsystems.shooter.ShooterSubsystem
import org.frc5183.robot.subsystems.turntable.TurntableSubsystem

class AlignAndShoot(
    shooter: ShooterSubsystem,
    turntable: TurntableSubsystem,
    poseSupplier: () -> Pose2d,
) : SequentialCommandGroup() {
    init {
        addRequirements(shooter)
        addRequirements(turntable)

        addCommands(
            AlignTurntable(turntable),
            ParallelCommandGroup(
                ShootByDistance(shooter, { turntable.distanceToTarget }),
                ConstantAlignTurntable(turntable, poseSupplier),
            ),
        )
    }
}
