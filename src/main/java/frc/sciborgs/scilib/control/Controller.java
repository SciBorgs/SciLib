package frc.sciborgs.scilib.control;

import java.lang.reflect.InvocationTargetException;
import java.util.ResourceBundle.Control;
import java.util.function.DoubleBinaryOperator;

import edu.wpi.first.util.sendable.Sendable;
import edu.wpi.first.util.sendable.SendableBuilder;
import frc.sciborgs.scilib.control.Measurement.Distance;
import frc.sciborgs.scilib.control.Measurement.Type;

public abstract class Controller<M extends Measurement> implements Sendable {

    private Class<M> type;

    private double setpoint;
    private double measurement;
    private double output;

    private Filter setpointFilter;
    private Filter measurementFilter;
    private Filter outputFilter;

    public Controller() {
        setpoint = 0;
        measurement = 0;
        output = 0;

        setpointFilter = Filter.identity();
        measurementFilter = Filter.identity();
        outputFilter = Filter.identity();
    }

    public final double calculate(double setpoint, double measurement) {
        this.setpoint = setpointFilter.calculate(setpoint);
        this.measurement = measurementFilter.calculate(measurement);
        this.output = apply(this.setpoint, this.measurement);
        return this.output;
    }

    public abstract double apply(double setpoint, double measurement);

    public boolean atSetpoint() {
        return true;
    }

    public final Measurement.Type getType() {
        try {
            return type.getDeclaredConstructor().newInstance().type();
        } catch (Exception e) {
            System.err.println(e);
            return Type.DISTANCE; // TODO safety
        }
    }

    public Controller<M> add(Controller<M> other) {
        return op(other, (a, b) -> a + b);
    }

    public Controller<M> sub(Controller<M> other) {
        return op(other, (a, b) -> a - b);
    }

    public Controller<M> op(Controller<M> other, DoubleBinaryOperator op) {
        Controller<M> t = this;
        return new Controller<M>() {

            @Override
            public double apply(double setpoint, double measurement) {
                return op.applyAsDouble(t.apply(setpoint, measurement), other.apply(setpoint, measurement));
            }

            @Override
            public boolean atSetpoint() {
                return t.atSetpoint() && other.atSetpoint();
            }

            @Override
            public void initSendable(SendableBuilder builder) {
                t.initSendable(builder);
                other.initSendable(builder);
            }
            
        };
    }


    public void setSetpointFilter(Filter setpointFilter) {
        this.setpointFilter = setpointFilter;
    }

    public void setMeasurementFilter(Filter measurementFilter) {
        this.measurementFilter = measurementFilter;
    }

    public void setOutputFilter(Filter outputFilter) {
        this.outputFilter = outputFilter;
    }

    public void addSetpointFilter(Filter filter) {
        this.setpointFilter.andThen(filter);
    }

    public void addMeasurementFilter(Filter filter) {
        this.measurementFilter.andThen(filter);
    }

    public void addOutputFilter(Filter filter) {
        this.outputFilter.andThen(filter);
    }

}
