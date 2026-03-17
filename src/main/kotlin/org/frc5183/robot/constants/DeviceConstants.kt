package org.frc5183.robot.constants

import com.revrobotics.spark.SparkLowLevel
import com.revrobotics.spark.SparkMax
import com.studica.frc.AHRS.NavXComType
import edu.wpi.first.wpilibj.DigitalInput
import org.photonvision.PhotonCamera

object DeviceConstants {
    val IMUPort: NavXComType = NavXComType.kMXP_SPI

    val SHOOTER = SparkMax(51, SparkLowLevel.MotorType.kBrushless)
    val SHOOTER_INTAKE = SparkMax(52, SparkLowLevel.MotorType.kBrushless)
    val FEEDER = SparkMax(54, SparkLowLevel.MotorType.kBrushless)

    val COLLECTOR_ARM = SparkMax(55, SparkLowLevel.MotorType.kBrushless)
    val COLLECTOR_INTAKE = SparkMax(56, SparkLowLevel.MotorType.kBrushed)

    val TURNTABLE_MOTOR = SparkMax(53, SparkLowLevel.MotorType.kBrushed)
    val TURNTABLE_CAMERA = PhotonCamera("Turntable")
    val TURNTABLE_LIMIT_SWITCH = DigitalInput(1)
}
