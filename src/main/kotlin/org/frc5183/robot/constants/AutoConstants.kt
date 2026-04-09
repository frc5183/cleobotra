package org.frc5183.robot.constants

import com.pathplanner.lib.config.RobotConfig
import edu.wpi.first.units.Units
import org.frc5183.robot.constants.swerve.modules.*

object AutoConstants {
    val PATHPLANNER_CONFIG: RobotConfig =
        RobotConfig(
            PhysicalConstants.MASS,
            PhysicalConstants.MOI,
            SwerveModulePhysicalConstants.PATHPLANNER,
            FLSwerveModuleConstants.LOCATION,
            FRSwerveModuleConstants.LOCATION,
            BLSwerveModuleConstants.LOCATION,
            BRSwerveModuleConstants.LOCATION,
        )

    val USE_FEED_FORWARD: Boolean = false

    val SHOOTER_ALIGN_KP = 0.04
    val SHOOTER_ALIGN_KI = 0.00
    val SHOOTER_ALIGN_KD = 0.00
    val SHOOTER_ALIGN_DEADBAND = Units.Degrees.of(1.0)
}
