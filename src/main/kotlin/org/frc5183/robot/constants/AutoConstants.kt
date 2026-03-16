package org.frc5183.robot.constants

import com.pathplanner.lib.config.RobotConfig
import org.frc5183.robot.constants.swerve.modules.*

object AutoConstants {
    val PATHPLANNER_CONFIG: RobotConfig =
        RobotConfig(
            PhysicalConstants.MASS,
            PhysicalConstants.MOI,
            SwerveModulePhysicalConstants.PATHPLANNER,
            FLSwerveModuleConstants.LOCATION,
            FRSwerveModuleConstant.LOCATION,
            BLSwerveModuleConstant.LOCATION,
            BRSwerveModuleConstant.LOCATION,
        )
}
