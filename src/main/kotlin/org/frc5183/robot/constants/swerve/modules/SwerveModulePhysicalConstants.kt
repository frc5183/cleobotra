package org.frc5183.robot.constants.swerve.modules

import com.pathplanner.lib.config.ModuleConfig
import edu.wpi.first.math.system.plant.DCMotor
import edu.wpi.first.units.Units
import edu.wpi.first.units.measure.Current
import edu.wpi.first.units.measure.Distance
import edu.wpi.first.units.measure.MomentOfInertia
import edu.wpi.first.units.measure.Voltage
import org.frc5183.robot.constants.PhysicalConstants
import swervelib.parser.SwerveModulePhysicalCharacteristics
import swervelib.parser.json.modules.AngleConversionFactorsJson
import swervelib.parser.json.modules.ConversionFactorsJson
import swervelib.parser.json.modules.DriveConversionFactorsJson

object SwerveModulePhysicalConstants {
    val MOTOR_TYPE: DCMotor = DCMotor.getKrakenX60(1)

    val WHEEL_COF: Double = 1.0
    val DRIVE_CURRENT_LIMIT: Current = Units.Amps.of(40.0)
    val ANGLE_CURRENT_LIMIT: Current = Units.Amps.of(30.0)
    val DRIVE_MOTOR_RAMP_RATE: Double = 0.25 // TODO
    val ANGLE_MOTOR_RAMP_RATE: Double = 0.25 // TODO
    val DRIVE_MINIMUM_VOLTAGE: Voltage = Units.Millivolts.of(300.0) // TODO
    val ANGLE_MINIMUM_VOLTAGE: Voltage = Units.Millivolts.of(200.0) // TODO
    val STEER_ROTATIONAL_INERTIA: MomentOfInertia = Units.KilogramSquareMeters.of(0.03) // TODO

    val DRIVE_GEAR_RATIO: Double = 5.9
    val ANGLE_GEAR_RATIO: Double = 18.75
    val WHEEL_DIAMETER: Distance = Units.Inches.of(4.0)

    val MAX_DRIVE_VELOCITY = Units.MetersPerSecond.of(5.0) // TODO: https://pathplanner.dev/robot-config.html#module-config-options

    val CONVERSION_FACTORS: ConversionFactorsJson =
        ConversionFactorsJson().apply {
            val driveConversionFactors = DriveConversionFactorsJson()
            driveConversionFactors.gearRatio = DRIVE_GEAR_RATIO
            driveConversionFactors.diameter = WHEEL_DIAMETER.`in`(Units.Inches)
            driveConversionFactors.calculate()

            val angleConversionFactors = AngleConversionFactorsJson()
            angleConversionFactors.gearRatio = ANGLE_GEAR_RATIO
            angleConversionFactors.calculate()

            this.drive = driveConversionFactors
            this.angle = angleConversionFactors
        }

    val YAGSL: SwerveModulePhysicalCharacteristics =
        SwerveModulePhysicalCharacteristics(
            CONVERSION_FACTORS,
            WHEEL_COF,
            PhysicalConstants.OPTIMAL_VOLTAGE.`in`(Units.Volts),
            DRIVE_CURRENT_LIMIT.`in`(Units.Amps).toInt(),
            ANGLE_CURRENT_LIMIT.`in`(Units.Amps).toInt(),
            DRIVE_MOTOR_RAMP_RATE,
            ANGLE_MOTOR_RAMP_RATE,
            DRIVE_MINIMUM_VOLTAGE.`in`(Units.Volts),
            ANGLE_MINIMUM_VOLTAGE.`in`(Units.Volts),
            STEER_ROTATIONAL_INERTIA.`in`(Units.KilogramSquareMeters),
            PhysicalConstants.MASS.`in`(Units.Kilograms),
        )

    val PATHPLANNER: ModuleConfig
        get() =
            ModuleConfig(
                WHEEL_DIAMETER.div(2.0),
                MAX_DRIVE_VELOCITY,
                WHEEL_COF,
                MOTOR_TYPE,
                DRIVE_CURRENT_LIMIT,
                1,
            )
}
