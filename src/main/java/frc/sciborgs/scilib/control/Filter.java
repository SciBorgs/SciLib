package frc.sciborgs.scilib.control;

import java.util.function.DoublePredicate;

import edu.wpi.first.math.MathUtil;
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
public interface Filter {

    static Filter identity() {
        return v -> v;
    }

    /*
     * Rename method from applyAsDouble to calculate
     */

    double calculate(double value);

    /*
     * Operations with another filter.
     */

    default Filter compose(Filter before) {
        return value -> calculate(before.calculate(value));
    }

    default Filter andThen(Filter after) {
        return value -> after.calculate(calculate(value));
    }

    default Filter add(Filter other) {
        return value -> calculate(value) + other.calculate(value);
    }

    default Filter sub(Filter other) {
        return value -> calculate(value) - other.calculate(value);
    }

    default Filter mul(Filter other) {
        return value -> calculate(value) * other.calculate(value);
    }

    default Filter div(Filter other) {
        return value -> calculate(value) / other.calculate(value);
    }

    /*
     * Factory methods for adapting WPILib-provided filters.
     */

    static Filter fromLinearFilter(LinearFilter filter) {
        return value -> filter.calculate(value);
    }

    static Filter fromMedianFilter(MedianFilter filter) {
        return value -> filter.calculate(value);
    }

    static Filter fromSlewRateLimiter(SlewRateLimiter filter) {
        return value -> filter.calculate(value);
    }

    /*
     * Operations on the filter.
     */

    static Filter scale(double alpha) {
        return value -> alpha * value;
    }

    static Filter clamp(double low, double high) {
        return value -> MathUtil.clamp(value, low, high);
    }

    static Filter deadband(double deadband) {
        return value -> MathUtil.applyDeadband(value, deadband);
    }

    default DoublePredicate eval(DoublePredicate predicate) {
        return value -> predicate.test(calculate(value));
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
