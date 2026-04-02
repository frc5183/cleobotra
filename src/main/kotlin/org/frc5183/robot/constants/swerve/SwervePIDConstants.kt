package org.frc5183.robot.constants.swerve

import swervelib.parser.PIDFConfig

object SwervePIDConstants {
    val DRIVE_PID: PIDFConfig =
        PIDFConfig(
            1.0, // p
            0.0, // i
            0.0, // d
            0.0, // f
            0.0, // iz
        )

    val ANGLE_PID: PIDFConfig =
        PIDFConfig(
            50.0, // p
            0.0, // i
            0.32, // d
            0.0, // f
            0.0, // iz
        )

    val HEADING_PID: PIDFConfig =
        PIDFConfig(
            0.4, // p
            0.0, // i
            0.01, // d
            0.0, // f
            0.0, // iz
        )
}
