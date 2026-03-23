package org.frc5183.robot.target

import edu.wpi.first.units.Units
import edu.wpi.first.units.measure.Distance
import edu.wpi.first.wpilibj.DriverStation
import kotlin.jvm.optionals.getOrNull

enum class TurntableTarget(
    val redId: Int,
    val blueId: Int,
    val weight: Int,
    val heightFromFloor: Distance = Units.Inches.of(44.25),
) {
    HUB_MIDDLE_RIGHT(10, 26, 3),
    HUB_MIDDLE_LEFT(9, 25, 3),
    HUB_LEFT_FAR(5, 21, 1),
    HUB_LEFT_CLOSE(8, 24, 2),
    HUB_RIGHT_FAR(2, 18, 1),
    HUB_RIGHT_CLOSE(11, 27, 2),
    ;

    val id: Int
        get() =
            if ((DriverStation.getAlliance().getOrNull() ?: DriverStation.Alliance.Red) == DriverStation.Alliance.Red) {
                redId
            } else {
                blueId
            }

    companion object {
        val hubIds: IntArray
            get() =
                intArrayOf(
                    HUB_MIDDLE_LEFT.id,
                    HUB_MIDDLE_RIGHT.id,
                    HUB_RIGHT_FAR.id,
                    HUB_RIGHT_CLOSE.id,
                    HUB_LEFT_FAR.id,
                    HUB_LEFT_CLOSE.id,
                )

        fun byId(id: Int): TurntableTarget = values().first { it.id == id }
    }
}
