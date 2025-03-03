package frc.robot.subsystems.climber;

// import com.revrobotics.spark.config.ClosedLoopConfig.FeedbackSensor;
import com.revrobotics.spark.config.SparkBaseConfig.IdleMode;
import com.revrobotics.spark.config.SparkMaxConfig;

import frc.robot.Constants;

import com.revrobotics.spark.SparkBase.ControlType;
import com.revrobotics.spark.SparkBase.PersistMode;
import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.SparkMax;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.spark.SparkBase.ResetMode;

public class climberIOSparkMax implements climberIO {

	/* VARIABLES */
	SparkMax climberMotor1;
	SparkMax climberMotor2;
	RelativeEncoder climberEncoder1;
	RelativeEncoder climberEncoder2;

	public climberIOSparkMax() {
		// RUN PHOENIX TUNER AND ASSIGN DEVICE IDs THEN UPDATE HERE
		climberMotor1 = new SparkMax(22, MotorType.kBrushless); // <------- THING 1
		climberMotor2 = new SparkMax(23, MotorType.kBrushless); // <------- THING 2
		climberEncoder1 = climberMotor1.getEncoder();
		climberEncoder2 = climberMotor2.getEncoder();


		/* CONFIG FOR MOTOR 1 ARMS (PID) */
		SparkMaxConfig configArm = new SparkMaxConfig();
		configArm
				.inverted(false) // MAYBE SPIN THE MOTOR UNCOUPLED TO TEST DIRECTION; YOU DECIDE :::shrug:::
				.smartCurrentLimit(40) // change ele to 40
				.idleMode(IdleMode.kBrake);
		configArm.encoder
				.positionConversionFactor(Constants.kElevatorPositionFactor)
				.velocityConversionFactor(Constants.kElevatorVelocityFactor);
		configArm.closedLoop
				.pidf(.55, 0, 0, 0.00375); // CHECK THESE VALS -- ADJUST AS NEEDED :-D
		climberMotor1.configure(configArm, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);

		/* CONFIG FOR MOTOR 2 SCREW (PID) */
		SparkMaxConfig configScrew = new SparkMaxConfig();
		configScrew
				.inverted(true)
				.smartCurrentLimit(50)
				.idleMode(IdleMode.kBrake);
		configScrew.encoder
				.positionConversionFactor(Constants.kElevatorPositionFactor)
				.velocityConversionFactor(Constants.kElevatorVelocityFactor);
		configScrew.closedLoop.pidf(.55, 0, 0, 0.00375); // CHECK PIDF VALUES was 0.55 on P was .00375 on ff
		climberMotor2.configure(configScrew, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);
	}

	/*
	 * SparkMax climberMotor1;
	SparkMax climberMotor2;
	RelativeEncoder climberEncoder1;
	RelativeEncoder climberEncoder2;
	 */

	@Override
	public double getPosition() {
		// Get the position from the encoder
		return climberEncoder1.getPosition();
	}

	@Override
	public void set(double voltage) {
		// Set the power to the main motor
		climberMotor1.set(voltage);
		climberMotor2.set(voltage);
	}


	@Override
	public double getVelocity() {
		// Get the velocity from the encoder
		return climberEncoder2.getVelocity();
	}

	@Override
	public void resetPosition() {
		// Reset the encoder to the specified position
		climberEncoder1.setPosition(0);
	}

	@Override
	public void setPosition(double position) {
		climberMotor1.getClosedLoopController().setReference(position, ControlType.kPosition);
		climberMotor2.getClosedLoopController().setReference(position, ControlType.kPosition);
	}

	@Override
	public void stop() {
		climberMotor1.setVoltage(0);
	}

}
