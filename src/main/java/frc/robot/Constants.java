package frc.robot;

public class Constants {
    public static final double tau = 2.0 * Math.PI;
//
    public static final double kElevatorGearRatio = 0.05; // 20:1 gear ratio on the two elevator motors
    public static final double kElevatorPositionFactor = kElevatorGearRatio * tau;
    public static final double kElevatorVelocityFactor = kElevatorPositionFactor / 60.0;

    public static final double kWristGearRatio = 0.05; // 20:1 gear ratio on the wrist drive
    public static final double kWristPositionFactor = kWristGearRatio * tau;
    public static final double kWristVelocityFactor = kWristPositionFactor / 60.0;

    public static final double kClimberArmGearRatio = 0.04; // 25:1 gear ratio on the climber arm drive
    public static final double kClimberArmPositionFactor = kClimberArmGearRatio * tau;
    public static final double kClimberArmVelocityFactor = kClimberArmPositionFactor / 60.0;

    public static final double kClimberScrewGearRatio = 0.0277; // 36:1 gear ratio on the screw drive
    public static final double kClimberScrewPositionFactor = kClimberScrewGearRatio * tau;
    public static final double kClimberScrewVelocityFactor = kClimberScrewPositionFactor / 60.0;

    public static final double PROCESSOR_HEIGHT = 0;
    public static final double SOURCE_HEIGHT = 4.35;
    public static final double L0_HEIGHT = -0.5;
    public static final double L1_HEIGHT = 7.75; // 5
    public static final double L2_HEIGHT = 16; // 10
    public static final double L3_HEIGHT = 23; //18
    public static final double L4_HEIGHT = 21;
    
    public static final double L1_HEIGHT_ALGAE = 10.15; // 9.25
    public static final double L2_HEIGHT_ALGAE = 17; // 10
    
    public static final double ZERO_ANGLE = 0;

    // public static final double ZERO_ELEVATOR_INCREMENTAL = -.5;
    // MR LORBER TALK TOO MUCH ACCCORDING TO HIS 6 PERIOD MEATHEADS
    // NOT NONESENSE
    public static final double ARM_HEIGHT = 0.6; // NEED TO SET
    public static final double CLIMB_HEIGHT = 10; // NEED TO SET

    public static final double PROCESSOR_ANGLE = 0.5; // TEST VALUE FOR THE WRIST (1 was about 50°)///////////////////
    public static final double SOURCE_ANGLE = 0.8;
    public static final double L0_ANGLE = 1.56;    // 1.3
    public static final double L1_ANGLE = 1.60;    // 1.85
    public static final double L2_ANGLE = 1.50; //1.85
    public static final double L3_ANGLE = 1.35; //.5
    public static final double L4_ANGLE = 0.26; //.26
    public static final double TOP_ALGAE_ANGLE = 0;
}
