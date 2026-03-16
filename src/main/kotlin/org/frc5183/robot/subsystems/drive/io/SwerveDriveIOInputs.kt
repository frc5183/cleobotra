package org.frc5183.robot.subsystems.drive.io

import edu.wpi.first.math.geometry.Pose2d
import edu.wpi.first.math.kinematics.ChassisSpeeds
import edu.wpi.first.math.kinematics.SwerveDriveKinematics
import edu.wpi.first.math.kinematics.SwerveModuleState
import org.frc5183.robot.logging.AutoLogInputs

class SwerveDriveIOInputs : AutoLogInputs() {
    var pose by log(Pose2d())
    var robotVelocity by log(ChassisSpeeds())
    var fieldVelocity by log(ChassisSpeeds())
    var moduleStates by log(emptyArray<SwerveModuleState>())
    var kinematics by log(SwerveDriveKinematics())
}
