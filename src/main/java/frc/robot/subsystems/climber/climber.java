
package frc.robot.subsystems.climber;

import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class climber extends SubsystemBase {

	private final climberIO io;

	// Constructor
	public climber(climberIO io) {
		this.io = io;
	}

	// Method to set power for the climber
	public void setVoltage(double voltage) {
		io.set(voltage);
	}

	// Method to stop the climber
	public void stop() {
		io.stop();
	}

	// Set the climber to a specific position
	public void setPosition(double position) {
		io.setPosition(position);
	}

	// Periodic method called in every cycle (e.g., 20ms)
	@Override
	public void periodic() {
		// io.updateInputs(inputs);
	}

	public double getPosition() {
		return io.getPosition();
	}

	public double getVelocity() {
		return io.getVelocity();
	}

	public void resetPosition() {
		io.resetPosition();
	}
}
