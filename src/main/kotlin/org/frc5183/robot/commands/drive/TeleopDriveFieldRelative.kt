package org.frc5183.robot.commands.drive

import edu.wpi.first.wpilibj2.command.Command
import org.frc5183.robot.subsystems.drive.SwerveDriveSubsystem
import swervelib.SwerveController

/**
 * A command that allows the driver to control the robot's translation and rotation using the joystick.
 */
class TeleopDriveFieldRelative(
    /**
     * The [SwerveDriveSubsystem] to control.
     */
    val drive: SwerveDriveSubsystem,
    /**
     * A function that returns the X translation input for the robot. This should be in the range [-1, 1] and will
     * have [translationCurve] applied, then scaled to [PhysicalConstants.MAX_SPEED].
     */
    val xInput: () -> Double,
    /**
     * A function that returns the Y translation input for the robot. This should be in the range [-1, 1] and will
     * have [translationCurve] applied, then scaled to [PhysicalConstants.MAX_SPEED].
     */
    val yInput: () -> Double,
    /**
     * A function that returns the rotation input for the robot. This should be in the range [-1, 1] and will
     * have [rotationCurve] applied, then scaled to [PhysicalConstants.MAX_ANGULAR_VELOCITY].
     */
    val rotationInput: () -> Double,
) : Command() {
    init {
        addRequirements(drive)
    }

    override fun execute() {
        val desiredSpeeds = drive.getTargetSpeeds(xInput(), yInput())

        val translation = SwerveController.getTranslation2d(desiredSpeeds)
        // todo: limit our velocity/acceleration to prevent tipping
        //  idk if this is going to be a problem though because our center of gravity seems low

        drive.drive(translation, rotationInput(), true)
    }

    override fun isFinished(): Boolean = false
}
