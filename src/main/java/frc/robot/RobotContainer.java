package frc.robot;

import static edu.wpi.first.units.Units.*;
import com.ctre.phoenix6.swerve.SwerveModule.DriveRequestType;
import com.ctre.phoenix6.swerve.SwerveRequest;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.RunCommand;
import edu.wpi.first.wpilibj2.command.StartEndCommand;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
// import edu.wpi.first.wpilibj2.command.sysid.SysIdRoutine.Direction;
import frc.robot.subsystems.climber.climber;
// import frc.robot.subsystems.climber.climberIO;
import frc.robot.subsystems.climber.climberIOSparkMax;
import frc.robot.subsystems.intake.Intake;
// import frc.robot.subsystems.intake.IntakeIO;
import frc.robot.subsystems.intake.IntakeIOSparkMax;
import frc.robot.subsystems.elevator.elevator;
// import frc.robot.subsystems.elevator.elevatorIO;
import frc.robot.subsystems.elevator.elevatorIOSparkMax;
// import frc.robot.Constants;
import frc.robot.generated.TunerConstants;
import frc.robot.subsystems.CommandSwerveDrivetrain;

public class RobotContainer {
	private final climber climber;
	private final elevator elevator;
	private final Intake intake;
	private double MaxSpeed = TunerConstants.kSpeedAt12Volts.in(MetersPerSecond); // kSpeedAt12Volts desired top speed
	private double MaxAngularRate = RotationsPerSecond.of(0.75).in(RadiansPerSecond); // 3/4 of a rotation per second max
																																										// angular velocity

	/* Setting up bindings for necessary control of the swerve drive platform */
	private final SwerveRequest.FieldCentric drive = new SwerveRequest.FieldCentric()
			.withDeadband(MaxSpeed * 0.1).withRotationalDeadband(MaxAngularRate * 0.1) // Add a 10% deadband
			.withDriveRequestType(DriveRequestType.OpenLoopVoltage); // Use open-loop control for drive motors
	private final SwerveRequest.SwerveDriveBrake brake = new SwerveRequest.SwerveDriveBrake();
	private final SwerveRequest.PointWheelsAt point = new SwerveRequest.PointWheelsAt();
	private final Telemetry logger = new Telemetry(MaxSpeed);
	private final CommandXboxController driverController = new CommandXboxController(0);
	private final CommandXboxController operatorController = new CommandXboxController(1);
	public final CommandSwerveDrivetrain drivetrain = TunerConstants.createDrivetrain();

	public RobotContainer() {
		climber = new climber(new climberIOSparkMax());
		elevator = new elevator(new elevatorIOSparkMax());
		intake = new Intake(new IntakeIOSparkMax());
		configureBindings();
	}

	private void configureBindings() {
		// THE JOYSTICK CONTROLS ARE HERE: https://tinyurl.com/25xw7zu7
		// NB: X is defined as forward and Y is defined as to the left, according to
		// WPILib convention

		// BEGIN MANUAL LIFT
		/*********** CHECK THAT THIS DOESN'T TRIGGER PROCESSOR OR SOURCE HEIGHT */
		// Command manualLift = new RunCommand(() -> elevator.setVoltage(-operatorController.getLeftY() * 0.5), elevator);
		// Command manualWrist = new RunCommand(() -> intake.setWristVoltage(operatorController.getRightY() * 0.25), intake);
		// ParallelCommandGroup manualCommandGroup = new ParallelCommandGroup(manualLift, manualWrist);
		// operatorController.start().whileTrue(manualCommandGroup);
		/// END MANUAL LIFT

		drivetrain.setDefaultCommand(
				// Drivetrain will execute this command periodically
				drivetrain.applyRequest(() -> drive
						.withVelocityX(-driverController.getLeftY() * MaxSpeed)
						// Drive forward with negative Y (forward)
						.withVelocityY(-driverController.getLeftX() * MaxSpeed)
						// Drive left with negative X (left)
						.withRotationalRate(-driverController.getRightX() * MaxAngularRate)
				// Drive counterclockwise with negative X (left)
				));

		driverController.b().whileTrue(drivetrain.applyRequest(() -> brake));
		// driverController.leftTrigger().whileTrue(drivetrain.applyRequest(
		// 		() -> point.withModuleDirection(new Rotation2d(
		// 				-driverController.getLeftY(),
		// 				-driverController.getLeftX()))));

		// SOOO TIRED ZZZZZ IMA JUST COMMENT OUT THIS ROUTINE
		// Run SysId routines when holding back/start and X/Y.
		// Note that each routine should be run exactly once in a single log.
		// driverController.back().and(driverController.y())
		// .whileTrue(drivetrain.sysIdDynamic(Direction.kForward));
		// driverController.back().and(driverController.x())
		// .whileTrue(drivetrain.sysIdDynamic(Direction.kReverse));
		// driverController.start().and(driverController.y())
		// .whileTrue(drivetrain.sysIdQuasistatic(Direction.kForward));
		// driverController.start().and(driverController.x())
		// .whileTrue(drivetrain.sysIdQuasistatic(Direction.kReverse));

		// I'M GOING TO COMMENT OUT HERE, THEN LIFT THIS COMMAND TO THE START OF
		// THE ROBOT BINDINGS CONFIGURATION -- REMOVING THE COMMAND FROM THE DRIVER'S
		// CONTROLLER
		// SO IT RUNS ONCE WHEN THE ROBOT INITIALIZES... THE ASSUMPTION IS (JUST USE THE
		// FORCE, LUKE :P)
		// reset the field-centric heading on left bumper press
		// driverController.leftBumper().onTrue(drivetrain.runOnce(() ->
		// drivetrain.seedFieldCentric()));

		drivetrain.registerTelemetry(logger::telemeterize);

		/* ELEVATOR STATES */

		/*
		 * LISTEN FOR OVERCURRENT ON CAN - WHAT IS THE MESSAGE FROM THE MOTOR IF IT'S
		 * STALLING
		 */
		/* STOP THE INTAKE */
		/* .andThen(run the intake) *********** time the eject */
		/* MOVE THE COMMANDS ABOVE THE ELVATOR STATES IF WE USE THE ANDTHEN */

		/* SOURCE COMMANDS */
		Command liftToSourceCommand = new RunCommand(() -> elevator.setPosition(Constants.SOURCE_HEIGHT), elevator);
		Command wristToSourceCommand = new RunCommand(() -> intake.wristAngle(Constants.SOURCE_ANGLE), intake);
		ParallelCommandGroup sourceCommandGroup = new ParallelCommandGroup(liftToSourceCommand, wristToSourceCommand);
		driverController.leftBumper().onTrue(sourceCommandGroup);

		// zero the wrist
		Command wristZeroCommand = new RunCommand(() -> intake.wristAngle(Constants.ZERO_ANGLE), intake);
		driverController.x().onTrue(wristZeroCommand);

		// zero ele incremental
		// Command zero_ele_inc = new RunCommand(() -> elevator.setPosition(elevator.getPosition()-0.5), elevator);
		// driverController.a().onTrue(zero_ele_inc);

		// L0 state
		Command liftToL0Command = new RunCommand(() -> elevator.setPosition(Constants.L0_HEIGHT), elevator);
		Command wristToL0Command = new RunCommand(() -> intake.wristAngle(Constants.L0_ANGLE), intake);
		ParallelCommandGroup l0CommandGroup = new ParallelCommandGroup(liftToL0Command, wristToL0Command);
		operatorController.a().onTrue(l0CommandGroup);

		// L1 state
		Command liftToL1Command = new RunCommand(() -> elevator.setPosition(Constants.L1_HEIGHT), elevator);
		Command wristToL1Command = new RunCommand(() -> intake.wristAngle(Constants.L1_ANGLE), intake);
		ParallelCommandGroup l1CommandGroup = new ParallelCommandGroup(liftToL1Command, wristToL1Command);
		operatorController.b().onTrue(l1CommandGroup);

		// L2 state
		Command liftToL2Command = new RunCommand(() -> elevator.setPosition(Constants.L2_HEIGHT), elevator);
		Command wristToL2Command = new RunCommand(() -> intake.wristAngle(Constants.L2_ANGLE), intake);
		ParallelCommandGroup l2CommandGroup = new ParallelCommandGroup(liftToL2Command, wristToL2Command);
		operatorController.x().onTrue(l2CommandGroup);

		// L3 state
		Command liftToL3Command = new RunCommand(() -> elevator.setPosition(Constants.L3_HEIGHT), elevator);
		Command wristToL3Command = new RunCommand(() -> intake.wristAngle(Constants.L3_ANGLE), intake);
		ParallelCommandGroup l3CommandGroup = new ParallelCommandGroup(liftToL3Command, wristToL3Command);
		operatorController.y().onTrue(l3CommandGroup);

		// algae L-1 state
		Command algae_lift_1 = new RunCommand(() -> elevator.setPosition(Constants.L1_HEIGHT_ALGAE), elevator);
		driverController.leftTrigger().onTrue(algae_lift_1);
		
		// algae L-2 state
		Command algae_lift_2 = new RunCommand(() -> elevator.setPosition(Constants.L2_HEIGHT_ALGAE), elevator);
		driverController.rightTrigger().onTrue(algae_lift_2);

		// L4 state
		// Command liftToL4Command = new RunCommand(() -> elevator.setPosition(Constants.L4_HEIGHT), elevator);
		// Command wristToL4Command = new RunCommand(() -> intake.wristAngle(Constants.L4_ANGLE), intake);
		// ParallelCommandGroup l4CommandGroup = new ParallelCommandGroup(liftToL4Command, wristToL4Command);
		// operatorController.y().onTrue(l4CommandGroup);

		// NICE TO HAVE - A COMMAND THAT PICKS UP BOTH THE ALGAE AND CORAL

		/* ALGAE COMMANDS */
		Command intakeAlgaeCommand = new StartEndCommand(() -> intake.setAlgaeVoltage(-12), () -> intake.setAlgaeVoltage(0),
				intake);
		operatorController.rightBumper().whileTrue(intakeAlgaeCommand);

		Command ejectAlgaeCommand = new StartEndCommand(() -> intake.setAlgaeVoltage(12), () -> intake.setAlgaeVoltage(0),
				intake);
		operatorController.rightTrigger().whileTrue(ejectAlgaeCommand);

		/* CORAL COMMANDS */
		Command intakeCoralCommand = new StartEndCommand(() -> intake.setCoralIntakeVoltage(-6),
				() -> intake.setCoralIntakeVoltage(0), intake);
		operatorController.leftBumper().whileTrue(intakeCoralCommand);

		Command ejectCoralCommand = new StartEndCommand(() -> intake.setCoralIntakeVoltage(6),
				() -> intake.setCoralIntakeVoltage(0), intake);
		operatorController.leftTrigger().whileTrue(ejectCoralCommand);

		/* PROCESSOR COMMANDS */
		// Command liftToProcessorCommand = new RunCommand(() -> elevator.setPosition(Constants.PROCESSOR_HEIGHT), elevator);

		// Command wristToProcessorCommand = new RunCommand(() -> intake.wristAngle(Constants.PROCESSOR_ANGLE), intake);
		// ParallelCommandGroup processorCommandGroup = new ParallelCommandGroup(liftToProcessorCommand,
		// 		wristToProcessorCommand);
		// driverController.rightBumper().onTrue(processorCommandGroup);


		// zero elevator



		// TEST THE WRIST ON ITS OWN
		// LEFT STICK
		// 		Command liftToL4Command = new RunCommand(() -> elevator.setPosition(Constants.L4_HEIGHT), elevator);

		// Command wristToProcessorCommand = new RunCommand(() -> intake.wristAngle(Constants.PROCESSOR_ANGLE), intake);
		// operatorController.a().onTrue(wristToProcessorCommand);

		/* CLIMBER COMMANDS */
		// NEED TO FIRST LATCH ON, THEN, WHEN COMPLETE, START THE CLIMB (NOT BEFORE!)
		// BOTH OF THESE ARE RUNNING THE SAME
		// Command deployArm = new RunCommand(() -> climber.setPosition(Constants.ARM_HEIGHT), climber);
		// driverController.a().onTrue(deployArm);
		// andThen()
		// Command climb = new RunCommand(() -> climber.setPosition(Constants.CLIMB_HEIGHT), climber);
		// driverController.b().onTrue(climb);


		// Command latchCommand = new RunCommand(() ->
		/// latcher.setPosition(Constants.CLIMB_HEIGHT), elevator);
		// Command climbCommand = new RunCommand(() ->
		/// climber.setPosition(Constants.CLIMB_HEIGHT), elevator);
		// ParallelCommandGroup climbCommandGroup = new
		// ParallelCommandGroup(latchCommand, climbCommand);

		// POV (D-PAD) IS ANALOG. NEED DIGITAL TO RUN ONCE .: DON'T USE B/C WON'T TRIGGER
		// driverController.povUp().onTrue(climbCommandGroup);
		// driverController.povDown().onTrue(climbCommandGroup);
		// driverController.povLeft().onTrue(climbCommandGroup);
		// driverController.povRight().onTrue(climbCommandGroup);
	}

	/* AUTONOMY */
	public Command getAutonomousCommand() {
		return Commands.print("No autonomous command configured");
	}
}
