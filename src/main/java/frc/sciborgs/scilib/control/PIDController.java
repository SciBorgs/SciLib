package frc.sciborgs.scilib.control;

import edu.wpi.first.util.sendable.SendableBuilder;
import frc.sciborgs.scilib.math.Derivative;
import frc.sciborgs.scilib.math.Integral;

public class PIDController extends Controller {
  private double setpoint, measurement, output;
  private double kp, ki, kd;
  private double xTolerance,
      dxTolerance; // eg. position and velocity, velocity and acceleration, etc
  private final Derivative dError;
  private final Integral iError;

  public PIDController(double kp, double ki, double kd) {
    this.kp = kp;
    this.ki = ki;
    this.kd = kd;

    dError = new Derivative();
    iError = new Integral();
  }

  @Override
  public double apply(double setpoint, double measurement) {
    this.setpoint = setpoint;
    this.measurement = measurement;
    double error = setpoint - measurement;
    output = kp * error + ki * iError.calculate(error) + kd * dError.calculate(error);
    return output;
  }

  @Override
  public boolean atSetpoint() {
    double error = setpoint - measurement;
    return error < xTolerance && dError.get() < dxTolerance;
  }

  public double getD() {
    return kd;
  }

  public double getI() {
    return ki;
  }

  public double getP() {
    return kp;
  }

  public double getSetpoint() {
    return setpoint;
  }

  public double getMeasurement() {
    return measurement;
  }

  public double getError() {
    return dError.get();
  }

  public double getCumulativeError() {
    return iError.get();
  }

  public void setP(double kp) {
    this.kp = kp;
  }

  public void setI(double ki) {
    this.ki = ki;
  }

  public void setD(double kd) {
    this.kd = kd;
  }

  public void setSetpoint(double setpoint) {
    this.setpoint = setpoint;
  }

  @Override
  public void initSendable(SendableBuilder builder) {
    builder.addDoubleProperty("p", this::getP, this::setP);
    builder.addDoubleProperty("i", this::getI, this::setI);
    builder.addDoubleProperty("d", this::getD, this::setD);
    // builder.addDoubleProperty("setpoint", this::getSetpoint, this::setSetpoint); // unexpected
    // behavior when composed
  }
}
