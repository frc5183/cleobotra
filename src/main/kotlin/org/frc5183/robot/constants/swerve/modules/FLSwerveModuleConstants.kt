package org.frc5183.robot.constants.swerve.modules

import edu.wpi.first.math.geometry.Translation2d
import edu.wpi.first.units.Units
import edu.wpi.first.units.measure.Angle
import swervelib.encoders.CANCoderSwerve
import swervelib.encoders.SwerveAbsoluteEncoder
import swervelib.motors.SwerveMotor
import swervelib.motors.TalonFXSwerve

object FLSwerveModuleConstants : SwerveModuleConstants {
    override val NAME: String = "frontleft"
    override val LOCATION: Translation2d = Translation2d(Units.Inches.of(9.3125), Units.Inches.of(12.75))

    override val DRIVE_MOTOR: SwerveMotor = TalonFXSwerve(11, true, SwerveModulePhysicalConstants.MOTOR_TYPE)
    override val DRIVE_MOTOR_INVERTED: Boolean = false

    override val ANGLE_MOTOR: SwerveMotor = TalonFXSwerve(12, false, SwerveModulePhysicalConstants.MOTOR_TYPE)
    override val ANGLE_MOTOR_INVERTED: Boolean = false

    override val ABSOLUTE_ENCODER: SwerveAbsoluteEncoder = CANCoderSwerve(13)
    override val ABSOLUTE_ENCODER_OFFSET: Angle = Units.Rotations.of(-0.300)
    override val ABSOLUTE_ENCODER_INVERTED: Boolean = false
}
