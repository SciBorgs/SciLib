package frc.sciborgs.scilib.control;

import java.util.function.DoubleBinaryOperator;

public abstract class Controller {
    
    private double allowableError;

    private Filter measuremenFilter;
    private Filter setpointFilter;
    private Filter outputFilter;

    private double measurement;
    private double setpoint;
    private double output;

    public Controller() {
        this(0, 0);
    }

    public Controller(double allowableError, double startingValue) {
        this.allowableError = allowableError;

        measuremenFilter = Filter.identity();
        setpointFilter = Filter.identity();
        outputFilter = Filter.identity();

        measurement = startingValue;
        setpoint = startingValue;
        output = startingValue;
    }

    public static Controller from(DoubleBinaryOperator op) {
        return new Controller() {
            @Override
            public double apply(double measurement, double setpoint) {
                return op.applyAsDouble(measurement, setpoint);
            }
        };
    }

    public static Controller getPID(double kP, double kI, double kD) {
        Filter integral = Filter.IT(0);
        Filter derivative = Filter.DT(0);
        return from(
                (measurement, setpoint) -> {
                    double error = setpoint - measurement;
                    return kP * error + kI * integral.calculate(error) + kD * derivative.calculate(error);
                });
    }

    public static Controller getVelocityFF(double kS, double kV, double kA) {
        Filter acceleration = Filter.DT(0);
        return from(
                (measurement, _setpoint) -> {
                    return kS * Math.signum(measurement) + kV * measurement + kA * acceleration.calculate(measurement);
                });
    }

    public static Controller getPositionFF(double kS, double kV, double kA) {
        Filter velocity = Filter.DT(0);
        return from(
                (measurement, _setpoint) -> {
                    return kS * Math.signum(measurement) + kV * velocity.calculate(measurement);
                });
    }

    public final double calculate(double measurement, double setpoint) {
        this.measurement = measuremenFilter.applyAsDouble(measurement);
        this.setpoint = setpointFilter.applyAsDouble(setpoint);
        this.output = outputFilter.applyAsDouble(apply(measurement, setpoint));
        return output;
    }

    public abstract double apply(double measurement, double setpoint);

    public Controller add(Controller other) {
        Controller t = this;
        return new Controller() {
            @Override
            public double apply(double measurement, double setpoint) {
                return t.apply(measurement, setpoint) + other.calculate(measurement, setpoint);
            }
        };
    }

    public Controller sub(Controller other) {
        Controller t = this;
        return new Controller() {
            @Override
            public double apply(double measurement, double setpoint) {
                return t.apply(measurement, setpoint) - other.calculate(measurement, setpoint);
            }
        };
    }

    public Controller addOutputFilter(Filter filter) {
        outputFilter = filter;
        return this;
    }

    public Controller addSetpointFilter(Filter filter) {
        setpointFilter = filter;
        return this;
    }

    public Controller addMeasurementFilter(Filter filter) {
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
