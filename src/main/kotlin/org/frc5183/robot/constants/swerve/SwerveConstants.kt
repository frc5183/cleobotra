package org.frc5183.robot.constants.swerve

import edu.wpi.first.units.Units
import org.frc5183.robot.constants.DeviceConstants
import org.frc5183.robot.constants.PhysicalConstants
import org.frc5183.robot.constants.swerve.SwervePIDConstants.HEADING_PID
import org.frc5183.robot.constants.swerve.modules.*
import swervelib.imu.NavXSwerve
import swervelib.imu.SwerveIMU
import swervelib.parser.SwerveControllerConfiguration
import swervelib.parser.SwerveDriveConfiguration
import swervelib.telemetry.SwerveDriveTelemetry

object SwerveConstants {
    val VERBOSITY: SwerveDriveTelemetry.TelemetryVerbosity = SwerveDriveTelemetry.TelemetryVerbosity.HIGH

    val IMU: SwerveIMU = NavXSwerve(DeviceConstants.IMUPort)
    val IMU_INVERTED: Boolean = false

    val COSINE_COMPENSATOR: Boolean = false

    val YAGSL: SwerveDriveConfiguration =
        SwerveDriveConfiguration(
            listOf(
                FLSwerveModuleConstants.YAGSL,
                FRSwerveModuleConstants.YAGSL,
                BLSwerveModuleConstants.YAGSL,
                BRSwerveModuleConstants.YAGSL,
            ).toTypedArray(),
            IMU,
            IMU_INVERTED,
            SwerveModulePhysicalConstants.YAGSL,
        )

    val YAGSL_CONTROLLER_CONFIG: SwerveControllerConfiguration =
        SwerveControllerConfiguration(
            YAGSL,
            HEADING_PID,
            PhysicalConstants.MAX_VELOCITY.`in`(Units.MetersPerSecond),
        )
}
