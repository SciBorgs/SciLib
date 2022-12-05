package frc.sciborgs.scilib.control;

import edu.wpi.first.math.controller.SimpleMotorFeedforward;
import edu.wpi.first.util.sendable.Sendable;
import edu.wpi.first.util.sendable.SendableBuilder;

public class FFController implements Controller, Sendable {

    public enum Mode {
        Simple,
        Arm,
        Elevator,
    }
    
    private double ks, kv, ka;
    private double kOther;

    public double ff(double velocity, double acceleration) {
        return ks * Math.signum(velocity) + kv * velocity + ka * acceleration;
    }
    
    @Override
    public double calculate(double setpoint, double _measurement) {
        // TODO Auto-generated method stub
        return 0;
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
