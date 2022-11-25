package frc.sciborgs.scilib.control;

import java.util.function.DoubleBinaryOperator;

import frc.sciborgs.scilib.units.Unit;

public abstract class Controller<T extends Unit> {
    
    private double allowableError;

    private Filter measuremenFilter;
    private Filter setpointFilter;
    private Filter outputFilter;

    private double measurement;
    private double setpoint;
    private double output;

    public static Controller<Unit> from(DoubleBinaryOperator op) {
        return new Controller<Unit>() {
            @Override
            public double apply(double measurement, double setpoint) {
                return op.applyAsDouble(measurement, setpoint);
            }
        };
    }

    public final double calculate(double measurement, double setpoint) {
        this.measurement = measuremenFilter.applyAsDouble(measurement);
        this.setpoint = setpointFilter.applyAsDouble(setpoint);
        this.output = outputFilter.applyAsDouble(apply(measurement, setpoint));
        return output;
    }

    public abstract double apply(double measurement, double setpoint);

    public Controller<T> add(DoubleBinaryOperator other) {
        Controller<T> t = this;
        return new Controller<T>() {
            @Override
            public double apply(double measurement, double setpoint) {
                return t.apply(measurement, setpoint) + other.applyAsDouble(measurement, setpoint);
            }
        };
    }

    public Controller<T> sub(DoubleBinaryOperator other) {
        Controller<T> t = this;
        return new Controller<T>() {
            @Override
            public double apply(double measurement, double setpoint) {
                return t.apply(measurement, setpoint) - other.applyAsDouble(measurement, setpoint);
            }
        };
    }

    public Controller<T> filterOutput(Filter filter) {
        outputFilter = filter;
        return this;
    }

    public Controller<T> filterSetpoint(Filter filter) {
        setpointFilter = filter;
        return this;
    }

    public Controller<T> filterMeasurement(Filter filter) {
        measuremenFilter = filter;
        return this;
    }

    public boolean atSetpoint() {
        return Math.abs(setpoint) < allowableError;
    }

    public double getError() {
        return setpoint - measurement;
    }

    public double getMeasurement() {
        return measurement;
    }

    public double getSetpoint() {
        return setpoint;
    }

    public double getOutput() {
        return output;
    }

    public void setMeasurement(double measurement) {
        this.measurement = measurement;
    }

    public void setSetpoint(double setpoint) {
        this.setpoint = setpoint;
    }

    public void setOutput(double output) {
        this.output = output;
    }
}
