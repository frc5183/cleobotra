package org.frc5183.robot.subsystems.collector.io

interface CollectorIO {
    fun updateInputs(inputs: CollectorIOInputs)

    fun runArm(speed: Double)

    fun runIntake(speed: Double)

    fun stopArm()

    fun stopIntake()
}
