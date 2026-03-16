package org.frc5183.robot.subsystems.shooter.io

import com.revrobotics.spark.SparkMax

class RealShooterIO(
    val shooter: SparkMax,
    val intake: SparkMax,
    val feeder: SparkMax,
) : ShooterIO {
    override fun updateInputs(inputs: ShooterIOInputs) {
        inputs.shooterSpeed = shooter.get()
        inputs.intakeSpeed = intake.get()
        inputs.feederSpeed = feeder.get()
    }

    override fun runShooter(speed: Double) = shooter.set(speed)

    override fun runIntake(speed: Double) = intake.set(speed)

    override fun runFeeder(speed: Double) = feeder.set(speed)

    override fun stopShooter() = shooter.stopMotor()

    override fun stopFeeder() = feeder.stopMotor()

    override fun stopIntake() = intake.stopMotor()
}
