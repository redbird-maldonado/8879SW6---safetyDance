
package frc.robot.subsystems.elevator;

import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class elevator extends SubsystemBase {

  private final elevatorIO io;
  // private final ElevatorIOInputsAutoLogged inputs = new ElevatorIOInputsAutoLogged();

  // Constructor
  public elevator(elevatorIO io) {
    this.io = io;
  }

  // Method to set power for the elevator
  public void setVoltage(double voltage) {
    System.out.println("Elevator position: " + getPosition());
    io.set(voltage);
  }

  // Method to stop the elevator
  public void stop() {
    io.stop();
  }

  // Set the elevator to a specific position
  public void setPosition(double position) {
    // System.out.println("Elevator position: " + getPosition());
    io.setPosition(position);
  }

  // Periodic method called in every cycle (e.g., 20ms)
  // We're not loggin at this point
  // @Override
  // public void periodic() {
  //   io.updateInputs(inputs);
  // }

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
