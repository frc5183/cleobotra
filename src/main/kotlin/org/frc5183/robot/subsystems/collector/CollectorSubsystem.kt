package org.frc5183.robot.subsystems.collector

import edu.wpi.first.wpilibj2.command.SubsystemBase
import org.frc5183.robot.subsystems.collector.io.CollectorIO
import org.frc5183.robot.subsystems.collector.io.CollectorIOInputs

class CollectorSubsystem(
    private val io: CollectorIO,
) : SubsystemBase() {
    private val ioInputs = CollectorIOInputs()

    override fun periodic() {
        io.updateInputs(ioInputs)
    }

    fun runArm(speed: Double) {
        io.runArm(speed)
    }

    fun runIntake(speed: Double) {
        io.runIntake(speed)
    }

    fun stopArm() {
        io.stopArm()
    }

    fun stopIntake() {
        io.stopIntake()
    }
}
