package org.frc5183.robot.target

import edu.wpi.first.math.geometry.Translation2d
import edu.wpi.first.units.Units
import edu.wpi.first.wpilibj.DriverStation
import kotlin.jvm.optionals.getOrNull

enum class HubTarget(val translation: Translation2d) {
    RED(Translation2d(Units.Meters.of(11.925), Units.Meters.of(4.025))),
    BLUE(Translation2d(Units.Meters.of(4.625), Units.Meters.of(4.025)));

    companion object {
        fun get(): HubTarget = if (DriverStation.getAlliance().getOrNull() == DriverStation.Alliance.Red) RED else BLUE
    }
}