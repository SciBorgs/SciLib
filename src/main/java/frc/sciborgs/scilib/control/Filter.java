package frc.sciborgs.scilib.control;

import java.util.function.DoublePredicate;
import java.util.function.DoubleSupplier;
import java.util.function.DoubleUnaryOperator;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.filter.LinearFilter;
import edu.wpi.first.math.filter.MedianFilter;
import edu.wpi.first.math.filter.SlewRateLimiter;

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
     * Creates a derivative filter that differentiates with respect to values
     * provided by the time supplier.
     * 
     * @param tSupplier the supplier of time (or other system variable)
     * @param delay     number of samples to look back
     * @return a filter representing the derivative with respect to time
     */
    default Filter Dt(DoubleSupplier tSupplier, int delay) {
        UpdateDelayFilter delayedU = new UpdateDelayFilter(delay); // value filter
        UpdateDelayFilter delayedT = new UpdateDelayFilter(delay); // time filter
        // At least two samples needed for derivative
        return value -> {
            double time = tSupplier.getAsDouble();
            if (delayedT.getNumSamples() == 0) {
                delayedU.calculate(value);
                delayedT.calculate(time);
                return Double.NaN;
            }
            return (value - delayedU.calculate(value)) / (time - delayedT.calculate(time));
        };
    }

    default Filter It(DoubleSupplier tSupplier) {
        return new Filter() {
            double timePrev = Double.NaN;
            double integrator = 0;

            public double calculate(double value) {
                double time = tSupplier.getAsDouble();
                if (!Double.isNaN(timePrev * value)) {
                    integrator += value * (time - timePrev);
                }
                timePrev = time;
                return integrator;
            }
        };
    }

}
