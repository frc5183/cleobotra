package org.frc5183.robot.constants

import edu.wpi.first.units.Units
import edu.wpi.first.units.measure.AngularVelocity
import edu.wpi.first.units.measure.LinearVelocity
import edu.wpi.first.units.measure.Mass
import edu.wpi.first.units.measure.Voltage

object PhysicalConstants {
    val MASS: Mass = Units.Pounds.of(90.0) // TODO
    val OPTIMAL_VOLTAGE: Voltage = Units.Volts.of(12.0)

    val MAX_VELOCITY: LinearVelocity = Units.MetersPerSecond.of(4.0) // TODO
    val MAX_ANGULAR_VELOCITY: AngularVelocity = Units.DegreesPerSecond.of(360.0) // TODO
}