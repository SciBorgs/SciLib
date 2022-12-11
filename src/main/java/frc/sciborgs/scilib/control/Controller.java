package frc.sciborgs.scilib.control;

import java.util.function.DoubleBinaryOperator;

import edu.wpi.first.util.sendable.Sendable;
import edu.wpi.first.util.sendable.SendableBuilder;
import frc.sciborgs.scilib.math.Derivative;
import frc.sciborgs.scilib.math.Integral;

public abstract class Controller implements Sendable {

    private double setpoint, measurement, output;

    private Filter setpointFilter, measurementFilter, outputFilter;

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

    public Controller add(Controller other) {
        return op(other, (a, b) -> a + b);
    }

    public Controller sub(Controller other) {
        return op(other, (a, b) -> a - b);
    }

    /**
     * 
     * @param other
     * @param op
     * @return
     */
    public Controller op(Controller other, DoubleBinaryOperator op) {
        return new Controller() {

            @Override
            public double apply(double setpoint, double measurement) {
                return op.applyAsDouble(
                    // questionable workaround for expected behavior
                    // calculate retains filters, rather than removing all previous ones
                    Controller.this.calculate(setpoint, measurement), 
                    other.calculate(setpoint, measurement));
            }

            @Override
            public boolean atSetpoint() {
                // atSetpoint() returns true by default, so this makes sure all controllers agree
                return Controller.this.atSetpoint() && other.atSetpoint();
            }

            @Override
            public void initSendable(SendableBuilder builder) {
                Controller.this.initSendable(builder);
                other.initSendable(builder);
            }
            
        };

    }

    // same as op(), but for changing units
    /**
     * This is a potentially unsafe operation
     * @param <N> Derivative you are changing to (Position would be 0, Velocity would be 1, etc)
     * @return new controller, with the dimensions you specified
     */
    // public <N extends Measurement> Controller<N> changeUnits() {
    //     return new Controller<N>() {

    //         @Override
    //         public double apply(double setpoint, double measurement) {
    //             return Controller.this.calculate(setpoint, measurement);
    //         }

    //         @Override
    //         public boolean atSetpoint() {
    //             return Controller.this.atSetpoint();
    //         }

    //         @Override
    //         public void initSendable(SendableBuilder builder) {
    //             Controller.this.initSendable(builder);
    //         }

    //     };

    // }

    /**
     * 
     * @param filter
     * @return
     */
    public Controller addSetpointFilter(Filter filter) {
        this.setpointFilter = this.setpointFilter.andThen(filter);
        return this;
    }

    /**
     * 
     * @param filter
     * @return
     */
    public Controller addMeasurementFilter(Filter filter) {
        this.measurementFilter = this.measurementFilter.andThen(filter);
        return this;
    }

    /**
     * 
     * @param filter
     * @return
     */
    public Controller addOutputFilter(Filter filter) {
        this.outputFilter = this.outputFilter.andThen(filter);
        return this;
    }

    /**
     * <b>WARNING</b> VERY UNSAFE,
     * ONLY USE THIS IF YOU KNOW WHAT YOU'RE DOING
     * Useful for position based ff control, for example.
     * 
     * @return self: with differentiated measurements and setpoints
     */
    public Controller differentiateInputs() {
        addMeasurementFilter(new Derivative());
        addSetpointFilter(new Derivative());
        return this;
    }

    /**
     * <b>WARNING</b> VERY UNSAFE,
     * ONLY USE THIS IF YOU KNOW WHAT YOU'RE DOING
     * 
     * @return self: with integrated measurements and setpoints
     */
    public Controller integrateInputs() {
        addMeasurementFilter(new Integral());
        addSetpointFilter(new Integral());
        return this;
    }

}
