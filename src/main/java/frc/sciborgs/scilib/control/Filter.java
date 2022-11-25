package frc.sciborgs.scilib.control;

import java.util.function.DoubleBinaryOperator;
import java.util.function.DoublePredicate;
import java.util.function.DoubleSupplier;
import java.util.function.DoubleUnaryOperator;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.filter.LinearFilter;
import edu.wpi.first.math.filter.MedianFilter;
import edu.wpi.first.math.filter.SlewRateLimiter;
import frc.sciborgs.scilib.math.Counter;
import frc.sciborgs.scilib.math.Delta;
import frc.sciborgs.scilib.math.DeltaTime;

/**
 * Interface representing a transformation on a stream of doubles, and providing
 * operations on those transformations. This interface also includes static
 * factory
 * methods to enable compatibilty with classes defined in WPILib.
 * 
 * @see Controller
 */
@FunctionalInterface
public interface Filter extends DoubleUnaryOperator {

    static Filter identity() {
        return v -> v;
    }

    /*
     * Rename method from applyAsDouble to calculate
     */

    double calculate(double value);

    @Override
    default double applyAsDouble(double value) {
        return this.calculate(value);
    }

    /*
     * Operations with another filter.
     */

    default Filter add(Filter other) {
        return measurement -> calculate(measurement) + other.calculate(measurement);
    }

    default Filter sub(Filter other) {
        return measurement -> calculate(measurement) - other.calculate(measurement);
    }

    default Filter mul(Filter other) {
        return measurement -> calculate(measurement) * other.calculate(measurement);
    }

    default Filter div(Filter other) {
        return measurement -> calculate(measurement) / other.calculate(measurement);
    }

    /*
     * Factory methods for adapting WPILib-provided filters.
     */

    static Filter fromLinearFilter(LinearFilter filter) {
        return measurement -> filter.calculate(measurement);
    }

    static Filter fromMedianFilter(MedianFilter filter) {
        return measurement -> filter.calculate(measurement);
    }

    static Filter fromSlewRateLimiter(SlewRateLimiter filter) {
        return measurement -> filter.calculate(measurement);
    }

    /*
     * Operations on the filter.
     */

    static Filter scale(double alpha) {
        return measurement -> alpha * measurement;
    }

    static Filter clamp(double low, double high) {
        return measurement -> MathUtil.clamp(measurement, low, high);
    }

    static Filter deadband(double value, double deadband) {
        return measurement -> MathUtil.applyDeadband(value, deadband);
    }

    default DoublePredicate eval(DoublePredicate predicate) {
        return measurement -> predicate.test(calculate(measurement));
    }

    /**
     * Creates a derivative filter with respect to system time
     * @param initialValue the starting value
     * @return a derivative filter
     */
    static Filter DT(double initialValue) {
        Delta du = new Delta(initialValue);
        DeltaTime dt = new DeltaTime();
        return value -> du.update(value) / dt.update();
    }

    /**
     * Creates an integral filter with respect to system time
     * @param initialValue the starting value
     * @return an integral filter
     */
    static Filter IT(double initialValue) {
        Counter integrator = new Counter(initialValue);
        DeltaTime dt = new DeltaTime();
        return value -> integrator.increase(value * dt.update());
    }

}
