package frc.sciborgs.scilib.control;

import java.util.function.DoubleBinaryOperator;

import edu.wpi.first.math.controller.ArmFeedforward;
import edu.wpi.first.math.controller.ElevatorFeedforward;
import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.controller.SimpleMotorFeedforward;

public abstract class Controller {

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

    /*
     * WPILib wrappers
     */

    public static Controller fromPID(PIDController pid) {
        return Controller.from((measurement, setpoint) -> pid.calculate(measurement, setpoint)).setAllowableError(pid.getPositionTolerance());
    }

    public static Controller fromPositionFF(SimpleMotorFeedforward ff) {
        Filter velocity = Filter.Dt(0);
        return Controller.from((_measurement, setpoint) -> ff.calculate(velocity.calculate(setpoint)));
    }

    public static Controller fromVelocityFF(SimpleMotorFeedforward ff) {
        Filter accel = Filter.Dt(0);
        return Controller.from((_measurement, setpoint) -> ff.calculate(setpoint, accel.calculate(setpoint)));
    }

    // public static Controller fromREVPID() {

    // }

    public Controller setAllowableError(double allowableError) {
        this.allowableError = allowableError;
        return this;
    }

    public final double calculate(double measurement, double setpoint) {
        this.measurement = measuremenFilter.calculate(measurement);
        this.setpoint = setpointFilter.calculate(setpoint);
        this.output = outputFilter.calculate(apply(measurement, setpoint));
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
        outputFilter = outputFilter.andThen(filter);
        return this;
    }

    public Controller addSetpointFilter(Filter filter) {
        setpointFilter = setpointFilter.andThen(filter);
        return this;
    }

    public Controller addMeasurementFilter(Filter filter) {
        measuremenFilter = measuremenFilter.andThen(filter);
        return this;
    }

    public boolean atSetpoint() {
        return Math.abs(getError()) < allowableError;
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
