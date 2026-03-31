package org.frc5183.robot.constants

import com.revrobotics.spark.SparkLowLevel
import com.revrobotics.spark.SparkMax
import com.studica.frc.AHRS.NavXComType
import edu.wpi.first.units.Units
import edu.wpi.first.wpilibj.DigitalInput
import org.photonvision.PhotonCamera

object DeviceConstants {
    val IMUPort: NavXComType = NavXComType.kMXP_SPI
    val SHOOTER = SparkMax(51, SparkLowLevel.MotorType.kBrushless)

    val SHOOTER_INTAKE = SparkMax(52, SparkLowLevel.MotorType.kBrushless)
    val FEEDER = SparkMax(54, SparkLowLevel.MotorType.kBrushless)

    val COLLECTOR_ARM = SparkMax(55, SparkLowLevel.MotorType.kBrushed)
    val COLLECTOR_INTAKE = SparkMax(56, SparkLowLevel.MotorType.kBrushed)
    val COLLECTOR_TOP_LIMIT_SWITCH = DigitalInput(2)
    val COLLECTOR_OTHER_TOP_LIMIT_SWITCH = DigitalInput(0)
    val COLLECTOR_BOTTOM_LIMIT_SWITCH = DigitalInput(4)
    val TURNTABLE_MOTOR = SparkMax(53, SparkLowLevel.MotorType.kBrushed)

    val TURNTABLE_CAMERA = PhotonCamera("Turntable")
    val TURNTABLE_CAMERA_HEIGHT = Units.Inches.of(20.0)
    val TURNTABLE_CAMERA_PITCH = Units.Degrees.of(2.75)
    val TURNTABLE_LIMIT_SWITCH = DigitalInput(1)

    val CLIMBER_MOTOR: SparkMax = SparkMax(57, SparkLowLevel.MotorType.kBrushless)
}
