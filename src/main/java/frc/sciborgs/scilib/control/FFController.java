package frc.sciborgs.scilib.control;

import edu.wpi.first.util.sendable.Sendable;
import edu.wpi.first.util.sendable.SendableBuilder;
import frc.sciborgs.scilib.control.Measurement.Angle;
import frc.sciborgs.scilib.control.Measurement.Position;
import frc.sciborgs.scilib.math.Derivative;
import frc.sciborgs.scilib.math.Integral;

public class FFController<M extends Measurement> extends Controller<M> {

    private double ks, kv, ka;
    
    private Derivative derivative; // velocity or accel, depending on mode

    /* Look at those two yellow squiggly lines. That's what we have locked ourselves into */
    public static <M extends Measurement> Controller<M> armFF(double ks, double kv, double ka, double kcos, Class<M> type) {
        return new FFController<M>(ks, kv, ka, type).add((Controller<M>) new Arm(kcos));
    }

    public static <M extends Measurement> Controller<M> elevatorFF(double ks, double kv, double ka, double kg, Class<M> type) {
        return new FFController<M>(ks, kv, ka, type).add((Controller<M>) new Elevator(kg));
    }

    public FFController(double ks, double kv, double ka, Class<M> type) {
        super(type);

        this.ks = ks;
        this.kv = kv;
        this.ka = ka;

        derivative = new Derivative();
    }

    public FFController(double ks, double kv, Class<M> type) {
        this(ks, kv, 0, type);
    }

    public double ff(double velocity, double acceleration) {
        return ks * Math.signum(velocity) + kv * velocity + ka * acceleration;
    }
    
    @Override
    public double apply(double setpoint, double _measurement) {
        Measurement.Type typeEnum = Measurement.getType(type);
        if (typeEnum == null) throw new RuntimeException();
        return switch (typeEnum) {
            case POSITION, ANGLE -> ff(derivative.calculate(setpoint), 0);
            case VELOCITY        -> ff(setpoint, derivative.calculate(setpoint));
        };
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
        super(Angle.class);
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
        super(Position.class);
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