package frc.sciborgs.scilib.control;

import edu.wpi.first.math.controller.PIDController;

public interface Controller {
    public double calculate(double setpoint, double measurement);

    public static Controller fromPID(PIDController pid) {
        return (setpoint, measurement) -> pid.calculate(setpoint, measurement);
    }

    public default Controller filterOutput(Filter filter) {
        return (setpoint, measurement) -> filter.calculate(calculate(setpoint, measurement));
    }

    public default Controller filterInput(Filter filter) {
        return (setpoint, measurement) -> calculate(filter.calculate(setpoint), measurement);
    }

    public default Controller filterMeasurement(Filter filter) {
        return (setpoint, measurement) -> calculate(setpoint, filter.calculate(measurement));
    }

    public default Controller add(Controller other) {
        return (setpoint, measurement) -> calculate(setpoint, measurement) + other.calculate(setpoint, measurement);
    }

    public default Controller sub(Controller other) {
        return (setpoint, measurement) -> calculate(setpoint, measurement) - other.calculate(setpoint, measurement);
    }
}
