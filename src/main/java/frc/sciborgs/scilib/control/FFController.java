package frc.sciborgs.scilib.control;

import edu.wpi.first.util.sendable.SendableBuilder;
import frc.sciborgs.scilib.control.Measurement.Angle;
import frc.sciborgs.scilib.control.Measurement.Position;
import frc.sciborgs.scilib.control.Measurement.Velocity;
import frc.sciborgs.scilib.math.Derivative;
import frc.sciborgs.scilib.math.Integral;

public class FFController<M extends Measurement> extends Controller<M> {

    private double ks, kv, ka;
    
    private Derivative accel;

    public static Controller<Position> position(double ks, double kv, double ka) {
        return new FFController<Position>(ks, kv, ka).addSetpointFilter(new Derivative());
    }

    public static Controller<Velocity> velocity(double ks, double kv, double ka) {
        return new FFController<Velocity>(ks, kv, ka);
    }

    public static Controller<Angle> angle(double ks, double kv, double ka) {
        return new FFController<Angle>(ks, kv, ka).addSetpointFilter(new Derivative());
    }

    public static Controller<Angle> armFF(double ks, double kv, double ka, double kcos) {
        // arm is a position based controller, rather than an angle derivative (anglocity?) based controller
        return new FFController<Angle>(ks, kv, ka).addSetpointFilter(new Derivative()).add(new Arm(kcos));
    }

    public static Controller<Position> elevatorFF(double ks, double kv, double ka, double kg) {
        return new FFController<Position>(ks, kv, ka).add(new Elevator(kg)).addSetpointFilter(new Derivative());
    }

    public FFController(double ks, double kv, double ka) {
        this.ks = ks;
        this.kv = kv;
        this.ka = ka;

        accel = new Derivative();
    }
    
    @Override
    public double apply(double setpoint, double _measurement) {
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

class Arm extends Controller<Angle> {

    private double kcos;
    private Integral position;

    public Arm(double kcos) {
        this.kcos = kcos;
        this.position = new Integral();
    }

    public double getCos() {
        return kcos;
    }

    public void setCos(double kcos) {
        this.kcos = kcos;
    }

    @Override
    public double apply(double setpoint, double measurement) {
        return kcos * Math.cos(position.calculate(setpoint));
    }

    @Override
    public void initSendable(SendableBuilder builder) {
        builder.addDoubleProperty("cos", this::getCos, this::setCos);
    }

}

class Elevator extends Controller<Position> {

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
    public double apply(double setpoint, double measurement) {
        return kg;
    }
    
    @Override
    public void initSendable(SendableBuilder builder) {
        builder.addDoubleProperty("g", this::getG, this::setG);
    }
}