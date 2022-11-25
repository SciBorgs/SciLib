package frc.sciborgs.scilib.control;

import java.util.function.DoublePredicate;
import java.util.function.DoubleUnaryOperator;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.filter.LinearFilter;
import edu.wpi.first.math.filter.MedianFilter;
import edu.wpi.first.math.filter.SlewRateLimiter;

/**
 * Interface representing a transformation on a stream of doubles, and providing
 * operations on those transformations. This interface also includes static factory
 * methods to enable compatibilty with classes defined in WPILib.
 * @see Controller
 */
@FunctionalInterface
public interface Filter extends DoubleUnaryOperator {

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
        return measurement -> applyAsDouble(measurement) + other.applyAsDouble(measurement);
    }

    default Filter sub(Filter other) {
        return measurement -> applyAsDouble(measurement) - other.applyAsDouble(measurement);
    }

    default DoublePredicate eval(DoublePredicate predicate) {
        return measurement -> predicate.test(applyAsDouble(measurement));
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

    static Filter clamp(double low, double high) {
        return measurement -> MathUtil.clamp(measurement, low, high);
    }

    static Filter deadband(double value, double deadband) {
        return measurement -> MathUtil.applyDeadband(value, deadband);
    }
}
