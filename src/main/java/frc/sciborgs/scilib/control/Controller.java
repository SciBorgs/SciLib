package frc.sciborgs.scilib.control;

import java.util.function.DoubleBinaryOperator;

import frc.sciborgs.scilib.units.Unit;

public interface Controller<T extends Unit> extends DoubleBinaryOperator {

    public default Controller<T> add(Controller<T> other) {
        return (measurement, setpoint) -> applyAsDouble(measurement, setpoint) + other.applyAsDouble(measurement, setpoint);
    }

    public default Controller<T> sub(Controller<T> other) {
        return (measurement, setpoint) -> applyAsDouble(measurement, setpoint) - other.applyAsDouble(measurement, setpoint);
    }

    public default Controller<T> filterOutput(Filter filter) {
        return (measurement, setpoint) -> filter.applyAsDouble(applyAsDouble(measurement, setpoint));
    }

    public default Controller<T> filterInput(Filter filter) {
        return (measurement, setpoint) -> applyAsDouble(filter.applyAsDouble(setpoint), measurement);
    }

    public default Controller<T> filterMeasurement(Filter filter) {
        return (measurement, setpoint) -> applyAsDouble(setpoint, filter.applyAsDouble(measurement));
    }
}
