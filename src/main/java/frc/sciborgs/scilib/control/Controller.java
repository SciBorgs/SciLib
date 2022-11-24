package frc.sciborgs.scilib.control;

import java.util.function.DoubleBinaryOperator;

import edu.wpi.first.math.controller.BangBangController;
import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.controller.SimpleMotorFeedforward;

public interface Controller extends DoubleBinaryOperator {
    // TODO add stateful/timed controller abstract class for trapezoid profiles and accel

    public default Controller add(Controller other) {
        return (setpoint, measurement) -> applyAsDouble(setpoint, measurement) + other.applyAsDouble(setpoint, measurement);
    }

    public default Controller sub(Controller other) {
        return (setpoint, measurement) -> applyAsDouble(setpoint, measurement) - other.applyAsDouble(setpoint, measurement);
    }

    public static Controller fromPID(PIDController pid) {
        return (setpoint, measurement) -> pid.calculate(setpoint, measurement);
    }

    public static Controller fromFF(SimpleMotorFeedforward ff) {
        return (setpoint, measurement) -> ff.calculate(setpoint);
    }

    public static Controller fromBangBang(BangBangController bb) {
        return (setpoint, measurement) -> bb.calculate(measurement, setpoint);
    }

    public default Controller filterOutput(Filter filter) {
        return (setpoint, measurement) -> filter.applyAsDouble(applyAsDouble(setpoint, measurement));
    }

    public default Controller filterInput(Filter filter) {
        return (setpoint, measurement) -> applyAsDouble(filter.applyAsDouble(setpoint), measurement);
    }

    public default Controller filterMeasurement(Filter filter) {
        return (setpoint, measurement) -> applyAsDouble(setpoint, filter.applyAsDouble(measurement));
    }
}
