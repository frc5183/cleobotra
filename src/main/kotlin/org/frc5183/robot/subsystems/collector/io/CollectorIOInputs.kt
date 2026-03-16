package org.frc5183.robot.subsystems.collector.io

import org.frc5183.robot.logging.AutoLogInputs

class CollectorIOInputs : AutoLogInputs() {
    var collectorArmSpeed by log(0.0)
    var collectorIntakeSpeed by log(0.0)
}
