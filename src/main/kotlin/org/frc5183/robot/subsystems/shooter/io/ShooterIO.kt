package org.frc5183.robot.subsystems.shooter.io

interface ShooterIO {
    fun updateInputs(inputs: ShooterIOInputs)

    fun runShooter(speed: Double)

    fun runIntake(speed: Double)

    fun runFeeder(speed: Double)

    fun stopShooter()

    fun stopIntake()

    fun stopFeeder()
}
