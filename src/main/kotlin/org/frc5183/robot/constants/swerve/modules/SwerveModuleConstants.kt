package org.frc5183.robot.constants.swerve.modules

import edu.wpi.first.math.geometry.Translation2d
import edu.wpi.first.units.Units
import edu.wpi.first.units.measure.Angle
import org.frc5183.robot.constants.swerve.SwerveConstants
import org.frc5183.robot.constants.swerve.SwervePIDConstants
import swervelib.encoders.SwerveAbsoluteEncoder
import swervelib.motors.SwerveMotor
import swervelib.parser.SwerveModuleConfiguration

interface SwerveModuleConstants {
    val NAME: String
    val LOCATION: Translation2d

    val DRIVE_MOTOR: SwerveMotor
    val DRIVE_MOTOR_INVERTED: Boolean

    val ANGLE_MOTOR: SwerveMotor
    val ANGLE_MOTOR_INVERTED: Boolean

    val ABSOLUTE_ENCODER: SwerveAbsoluteEncoder
    val ABSOLUTE_ENCODER_OFFSET: Angle
    val ABSOLUTE_ENCODER_INVERTED: Boolean

    val YAGSL: SwerveModuleConfiguration
        get() =
            SwerveModuleConfiguration(
                DRIVE_MOTOR,
                ANGLE_MOTOR,
                SwerveModulePhysicalConstants.CONVERSION_FACTORS,
                ABSOLUTE_ENCODER,
                ABSOLUTE_ENCODER_OFFSET.`in`(Units.Degrees),
                LOCATION.measureX.`in`(Units.Meters),
                LOCATION.measureY.`in`(Units.Meters),
                SwervePIDConstants.ANGLE_PID,
                SwervePIDConstants.DRIVE_PID,
                SwerveModulePhysicalConstants.YAGSL,
                ABSOLUTE_ENCODER_INVERTED,
                DRIVE_MOTOR_INVERTED,
                ANGLE_MOTOR_INVERTED,
                NAME,
                SwerveConstants.COSINE_COMPENSATOR,
            )
}
