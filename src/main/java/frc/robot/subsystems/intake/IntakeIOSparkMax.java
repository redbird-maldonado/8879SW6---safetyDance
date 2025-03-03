package frc.robot.subsystems.intake;

import com.revrobotics.spark.SparkBase.ControlType;
import com.revrobotics.spark.SparkBase.PersistMode;
import com.revrobotics.spark.config.SparkBaseConfig.IdleMode;
import com.revrobotics.spark.config.SparkMaxConfig;
import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.SparkMax;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.spark.SparkBase.ResetMode;
import frc.robot.Constants;


public class IntakeIOSparkMax implements IntakeIO {
    SparkMax algaeMotor1;
    SparkMax algaeMotor2;
    SparkMax coralIntake;
    SparkMax coralWrist;
    RelativeEncoder wristEncoder;
    RelativeEncoder coralEncoder;

    public IntakeIOSparkMax() {
        algaeMotor1 = new SparkMax(19, MotorType.kBrushless);
        algaeMotor2 = new SparkMax(20, MotorType.kBrushless);
        coralIntake = new SparkMax(17, MotorType.kBrushless);
        coralWrist = new SparkMax(18, MotorType.kBrushless);
        wristEncoder = coralWrist.getEncoder();
        coralEncoder = coralIntake.getEncoder(); // DOES THE INTAKE NEED PID?

        // CONFIG 1 ALGAE
        SparkMaxConfig config1 = new SparkMaxConfig();
        config1
                .inverted(false)
                .idleMode(IdleMode.kBrake)
                .smartCurrentLimit(15);
        config1.encoder
                .positionConversionFactor(1000)
                .velocityConversionFactor(1000);
        // APPLY CONFIG TO MOTOR 1 (1&2 ARE JUST RUNNING WIDE OPEN)
        algaeMotor1.configure(config1, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);

        // CONFIG MOTOR 2
        SparkMaxConfig config2 = new SparkMaxConfig();
        config2
                .inverted(false)
                .idleMode(IdleMode.kBrake)
                .smartCurrentLimit(15);
        config2.encoder
                .positionConversionFactor(1000)
                .velocityConversionFactor(1000);
        algaeMotor2.configure(config2, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);

        // CONFIG FOR WRIST
        SparkMaxConfig configWrist = new SparkMaxConfig();
        configWrist
                .inverted(true)
                .smartCurrentLimit(50)
                .idleMode(IdleMode.kBrake);
        configWrist.encoder
                .positionConversionFactor(Constants.kElevatorPositionFactor)
				.velocityConversionFactor(Constants.kElevatorVelocityFactor);
        configWrist.closedLoop.pidf(.1, 0, 0, 0.00375); // CHECK PIDF VALUES was 0.55 on P was .00375 on ff
        coralWrist.configure(configWrist, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);

        // CONFIG FOR CORAL INTAKE (IF PID; ELSE, MOVE CORAL INTAKE UP TO MOTOR 1&2
        // WHICH'RE W/O PID)
        SparkMaxConfig config = new SparkMaxConfig();
        config
                .inverted(false)
                .idleMode(IdleMode.kBrake)
                .smartCurrentLimit(15);
        config.encoder
                .positionConversionFactor(1000)
                .velocityConversionFactor(1000);
        coralIntake.configure(config, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);
    }

    // @Override
    // public void updateInputs(IntakeIOInputs inputs) {
    // inputs.coralWristCurrent = coralWrist.getOutputCurrent();
    // inputs.coralWristVelocity = coralWrist.getEncoder().getVelocity();
    // inputs.coralWristPosition = coralWrist.getEncoder().getPosition();
    // }

    @Override
    public void setAlgaeVoltage(double voltage) {
        algaeMotor1.setVoltage(voltage);
        algaeMotor2.setVoltage(-voltage);
    }

    // @Override
    // public void setCoralIntakeVoltage(double voltage) {
    // coralIntake.setVoltage(voltage);
    // coralIntake.setVoltage(-voltage);
    // }

    // HELPER
    @Override
    public double getWristPosition() {
        return wristEncoder.getPosition();
    }

    
    @Override
    public void adjustAngle(double angleRadians) {
            coralWrist.getEncoder().setPosition(coralWrist.getEncoder().getPosition() + angleRadians);
        }

    @Override
    public void wristAngle(double position) {
            coralWrist.getClosedLoopController().setReference(position, ControlType.kPosition);
        }

    @Override
    public void setWristVoltage(double voltage) {
        coralWrist.set(voltage);
    }

    @Override
    public void setCoralIntakeVoltage(double voltage) {
        coralIntake.setVoltage(voltage);
    }

    // @Override
    // public void stop() {
    //     coralWrist.setVoltage(0);
    // }

    // HELPER
    // public double getCoralVelocity() {
    // return coralEncoder.getVelocity(); // RETURNS RPM OF THE MOTOR;
    // }
}
