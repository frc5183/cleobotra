package org.frc5183.robot.subsystems.shooter

import edu.wpi.first.wpilibj2.command.SubsystemBase
import org.frc5183.robot.subsystems.shooter.io.ShooterIO
import org.frc5183.robot.subsystems.shooter.io.ShooterIOInputs

class ShooterSubsystem(
    private val io: ShooterIO,
) : SubsystemBase() {
    private val ioInputs = ShooterIOInputs()

    val shooterSpeed: Double
        get() = ioInputs.shooterSpeed

    val intakeSpeed: Double
        get() = ioInputs.intakeSpeed

    val feederSpeed: Double
        get() = ioInputs.feederSpeed

    override fun periodic() {
        io.updateInputs(ioInputs)
    }

    fun run(speed: Double) {
        runShooter(speed)
        runIntake(speed)
        runFeeder(speed)
    }

    fun runShooter(speed: Double) = io.runShooter(speed)

    fun runIntake(speed: Double) = io.runIntake(speed)

    fun runFeeder(speed: Double) = io.runFeeder(speed)

    fun stop() {
        stopShooter()
        stopIntake()
        stopFeeder()
    }

    fun stopShooter() = io.stopShooter()

    fun stopIntake() = io.stopIntake()

    fun stopFeeder() = io.stopFeeder()
}
