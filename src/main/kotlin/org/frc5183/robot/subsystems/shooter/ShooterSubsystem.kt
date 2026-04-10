package org.frc5183.robot.subsystems.shooter

import com.revrobotics.spark.SparkBase
import com.revrobotics.spark.SparkMax
import edu.wpi.first.units.Units
import edu.wpi.first.units.measure.AngularVelocity
import edu.wpi.first.wpilibj2.command.SubsystemBase
import org.littletonrobotics.junction.Logger

class ShooterSubsystem(
    val shooter: SparkMax,
    val intake: SparkMax,
    val feeder: SparkMax,
) : SubsystemBase() {
    override fun periodic() {
        Logger.recordOutput("Shooter/ShooterSpeed", shooter.get())
        Logger.recordOutput("Shooter/ShooterVelocity", shooter.encoder.velocity)
        Logger.recordOutput("Shooter/IntakeSpeed", intake.get())
        Logger.recordOutput("Shooter/FeederSpeed", feeder.get())
    }

    fun run(speed: Double) {
        runShooter(speed)
        runIntake(speed)
        runFeeder(speed)
    }

    fun shoot(speed: Double) {
        runShooter(speed)
        runIntake(1.0)
        runFeeder(1.0)
    }

    fun shoot(velocity: AngularVelocity) {
        runShooterVelocity(velocity)
        runIntake(-1.0)
        runFeeder(-1.0)
    }

    fun runShooter(speed: Double) = shooter.set(-speed)
    fun runShooterVelocity(velocity: AngularVelocity) = shooter.closedLoopController.setSetpoint(velocity.`in`(Units.RPM), SparkBase.ControlType.kVelocity)
    fun runIntake(speed: Double) = intake.set(-speed)
    fun runFeeder(speed: Double) = feeder.set(-speed)

    fun stop() {
        stopShooter()
        stopIntake()
        stopFeeder()
    }

    fun stopShooter() = shooter.stopMotor()
    fun stopIntake() = intake.stopMotor()
    fun stopFeeder() = feeder.stopMotor()
}
