package org.frc5183.robot.subsystems.shooter.io

import org.frc5183.robot.logging.AutoLogInputs

class ShooterIOInputs : AutoLogInputs() {
    var shooterSpeed by log(0.0)
    var intakeSpeed by log(0.0)
    var feederSpeed by log(0.0)
}
