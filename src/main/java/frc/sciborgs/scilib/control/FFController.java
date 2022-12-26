package frc.sciborgs.scilib.control;

import edu.wpi.first.util.sendable.SendableBuilder;
import frc.sciborgs.scilib.math.Derivative;

public class FFController extends Controller {

  private double ks, kv, ka;

  private Derivative accel;

  /**
   * Position based ff, achieved by differentiating setpoint Do not use this alone, as it will
   * result in massive voltage spikes. Instead, use with a motion profile.
   *
   * @param ks
   * @param kv
   * @param ka
   * @return A position based DC motor feedforward model
   * @see Profile
   */
  public static Controller position(double ks, double kv, double ka) {
    return new FFController(ks, kv, ka).differentiateInputs();
  }

  /**
   * @param ks
   * @param kv
   * @param ka
   * @return A velocity based DC motor feedforward model
   */
  public static Controller velocity(double ks, double kv, double ka) {
    return new FFController(ks, kv, ka);
  }

  /**
   * @param ks
   * @param kv
   * @param ka
   * @param kcos
   * @return A position based arm feedforward implementation
   */
  public static Controller arm(double ks, double kv, double ka, double kcos) {
    // this works because Arm is position based
    return position(ks, kv, ka).add(new Arm(kcos));
  }

  /**
   * @param ks
   * @param kv
   * @param ka
   * @param kg
   * @return A position based elevator feedforward implementation
   */
  public static Controller elevator(double ks, double kv, double ka, double kg) {
    return position(ks, kv, ka).add(new Elevator(kg));
  }

  public FFController(double ks, double kv, double ka) {
    this.ks = ks;
    this.kv = kv;
    this.ka = ka;

    accel = new Derivative();
  }

  @Override
  public double apply(double setpoint, double _measurement) {
    var x = new frc.sciborgs.scilib.control.Motor(1);
    // assuming setpoint is velocity
    // position based ff is handled with a derivative setpointFilter
    return ks * Math.signum(setpoint) + kv * setpoint + ka * accel.calculate(setpoint);
  }

  public double getS() {
    return ks;
  }

  public double getV() {
    return kv;
  }

  public double getA() {
    return ka;
  }

  public void setS(double ks) {
    this.ks = ks;
  }

  public void setV(double kv) {
    this.kv = kv;
  }

  public void setA(double ka) {
    this.ka = ka;
  }

  @Override
  public void initSendable(SendableBuilder builder) {
    builder.addDoubleProperty("s", this::getS, this::setS);
    builder.addDoubleProperty("v", this::getV, this::setV);
    builder.addDoubleProperty("a", this::getA, this::setA);
  }
}

class Arm extends Controller {

  private double kcos;

  public Arm(double kcos) {
    this.kcos = kcos;
  }

  public double getCos() {
    return kcos;
  }

  public void setCos(double kcos) {
    this.kcos = kcos;
  }

  @Override
  public double apply(double position, double _measurement) {
    return kcos * Math.cos(position);
  }

  @Override
  public void initSendable(SendableBuilder builder) {
    builder.addDoubleProperty("cos", this::getCos, this::setCos);
  }
}

class Elevator extends Controller {

  private double kg;

  public Elevator(double kg) {
    this.kg = kg;
  }

  public double getG() {
    return kg;
  }

  public void setG(double kg) {
    this.kg = kg;
  }

  @Override
  public double apply(double _setpoint, double _measurement) {
    return kg;
  }

  @Override
  public void initSendable(SendableBuilder builder) {
    builder.addDoubleProperty("g", this::getG, this::setG);
  }
}
