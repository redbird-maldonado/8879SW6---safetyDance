package frc.robot.subsystems.climber;

public interface climberIO {
	
	public static class ElevatorIOInputs {
		public double motorCurrent = 0;
		public double motorVoltage = 0;
		public double motorAngle = 0;
	}

	public default void updateInputs(ElevatorIOInputs inputs) {
	}

	// Sets the power to the climber motor
	public default void set(double voltage) {
	}

	// Gets the current position of the climber (in encoder units)
	public default double getPosition() {
		return 0;
	}

	public default void setPosition(double position) {
	}

	// Gets the current velocity of the climber
	public default double getVelocity() {
		return 0;
	}

	// Resets the encoder position to a specific value
	public default void resetPosition() {
	}

	public default void stop() {
	}
}
