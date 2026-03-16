package org.frc5183.robot.subsystems.drive.io

import swervelib.SwerveDrive

class SimulatedSwerveDriveIO(
    drive: SwerveDrive,
) : RealSwerveDriveIO(drive) {
    init {
        drive.headingCorrection = false
        drive.setCosineCompensator(false)
    }
}
