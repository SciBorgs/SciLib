package frc.sciborgs.scilib.control;

import java.util.function.DoubleBinaryOperator;

import edu.wpi.first.util.sendable.Sendable;
import edu.wpi.first.util.sendable.SendableBuilder;

public abstract class Controller<M extends Measurement> implements Sendable {
    
    protected Class<M> type;

    /* unnecessary instance variables? */
    private double setpoint;
    private double measurement;
    private double output;

    private Filter setpointFilter;
    private Filter measurementFilter;
    private Filter outputFilter;

    public Controller(Class<M> type) {
        this.type = type;
        
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

    public Controller<M> add(Controller<M> elevator) {
        return op(elevator, Double::sum);
    }

    public Controller<M> sub(Controller<M> other) {
        return op(other, (a, b) -> a - b);
    }

    public Controller<M> op(Controller<M> other, DoubleBinaryOperator op) {
        return new Controller<M>(this.type) {

            @Override
            public double apply(double setpoint, double measurement) {
                return op.applyAsDouble(
                    Controller.this.apply(setpoint, measurement), 
                    other.apply(setpoint, measurement));
            }

            @Override
            public boolean atSetpoint() {
                return Controller.this.atSetpoint() && other.atSetpoint();
            }

            @Override
            public void initSendable(SendableBuilder builder) {
                Controller.this.initSendable(builder);
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
        this.setpointFilter = this.setpointFilter.andThen(filter);
    }

    public void addMeasurementFilter(Filter filter) {
        this.measurementFilter = this.measurementFilter.andThen(filter);
    }

    public void addOutputFilter(Filter filter) {
        this.outputFilter = this.outputFilter.andThen(filter);
    }

}
