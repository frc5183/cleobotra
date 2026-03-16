package org.frc5183.robot.commands.drive

import edu.wpi.first.math.geometry.Rotation2d
import edu.wpi.first.math.geometry.Translation2d
import edu.wpi.first.units.Units
import edu.wpi.first.wpilibj.DriverStation
import edu.wpi.first.wpilibj2.command.Command
import org.frc5183.robot.constants.PhysicalConstants
import org.frc5183.robot.subsystems.drive.SwerveDriveSubsystem

/**
 * A command that allows the driver to control the robot's translation and rotation using the joystick.
 */
class TeleopDriveCommand(
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
    /**
     * Whether to use field-relative control.
     */
    val fieldRelative: Boolean,
) : Command() {
    init {
        addRequirements(drive)
    }

    override fun execute() {
        drive.drive(Translation2d(PhysicalConstants.MAX_VELOCITY.`in`(Units.MetersPerSecond) * xInput(), PhysicalConstants.MAX_VELOCITY.`in`(Units.MetersPerSecond) * yInput()), PhysicalConstants.MAX_ANGULAR_VELOCITY.`in`(Units.RadiansPerSecond) * rotationInput(), false, false)
    }

    fun applyAllianceAwareTranslation(fieldRelativeTranslation: Translation2d): Translation2d =
        if (fieldRelative && DriverStation.getAlliance().orElseGet {
                DriverStation.Alliance.Red
            } == DriverStation.Alliance.Blue
        ) { // todo: This was changed from 2025 codebase, make sure that this is correct BEFORE going to competition.
            // Otherwise revert to 2025 code to at least keep the issues consistent with last year.
            fieldRelativeTranslation.rotateBy(Rotation2d.k180deg)
        } else {
            fieldRelativeTranslation
        }

    override fun isFinished(): Boolean = false
}
