package frc.robot.subsystems.elevator;

import com.revrobotics.spark.SparkBase.ControlType;
import com.revrobotics.spark.SparkBase.PersistMode;
import com.revrobotics.spark.config.SparkBaseConfig.IdleMode;
import com.revrobotics.spark.config.SparkMaxConfig;
import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.SparkClosedLoopController;
import com.revrobotics.spark.SparkMax;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.spark.SparkBase.ResetMode;
import frc.robot.Constants;

public class elevatorIOSparkMax implements elevatorIO {
	private final SparkMax leadMotor;
	public final SparkMax followerMotor;
	private final RelativeEncoder leadEncoder;
	private final RelativeEncoder followerEncoder;
	public final SparkClosedLoopController leadPIDController;

	// Constructor
	public elevatorIOSparkMax() {
		leadMotor = new SparkMax(15, MotorType.kBrushless);
		followerMotor = new SparkMax(16, MotorType.kBrushless);
		leadEncoder = leadMotor.getEncoder();
		followerEncoder = followerMotor.getEncoder();

		this.leadPIDController = this.leadMotor.getClosedLoopController();

		SparkMaxConfig leadConfig = new SparkMaxConfig();
		leadConfig
				.inverted(true) // Inverting lead motor because positive makes elevator go down
				.smartCurrentLimit(50)
				.idleMode(IdleMode.kBrake)
						// .voltageCompensation(12) // limit controller max V; can adjust if nec.
						.encoder
				.positionConversionFactor(Constants.kElevatorPositionFactor)
				.velocityConversionFactor(Constants.kElevatorVelocityFactor);

		// .encoder.positionConversionFactor(360d *
		// ABSOLUTE_DEGREES_PER_RELATIVE_DEGREES / 762.183 *
		// METERS_ASCENDED_PER_ROTATION)
		SparkMaxConfig followConfig = new SparkMaxConfig();
		followConfig
				.follow(15, true) // OKAY, SO... the lead motor is inverted ON PURPOSE. The follower also gets
													// inverted ON PURPOSE (they have to be in phase/sync).
				.smartCurrentLimit(50)
				.idleMode(IdleMode.kBrake);

		leadConfig.closedLoop.pidf(.027, 0, 0, .0085) // I THINK THESE ARE TOO SLOW -- PLZ CHECK TYVM
				.outputRange(-.8, .8); // THESE MAY ALSO NEED TO BE ADJUSTED
		leadMotor.configure(leadConfig, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);

		followerMotor.configure(followConfig, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);

		leadEncoder.setPosition(0);

		followerEncoder.setPosition(0);
	}

	@Override
	public void set(double voltage) {
		// Set the power to the main motor
		leadMotor.set(voltage);
	}

	@Override
	public double getPosition() {
		// Get the position from the encoder
		return leadEncoder.getPosition();
	}

	@Override
	public double getVelocity() {
		// Get the velocity from the encoder
		return leadEncoder.getVelocity();
	}

	@Override
	public void resetPosition() {
		// Reset the encoder to the specified position
		leadEncoder.setPosition(0);
	}

	@Override
	public void setPosition(double position) {
		leadMotor.getClosedLoopController().setReference(position, ControlType.kPosition);
	}

	@Override
	public void stop() {
		leadMotor.setVoltage(0);
	}
}