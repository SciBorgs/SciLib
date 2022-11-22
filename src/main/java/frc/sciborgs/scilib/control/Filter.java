package frc.sciborgs.scilib.control;

import java.util.function.DoublePredicate;
import java.util.function.DoubleUnaryOperator;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.filter.LinearFilter;
import edu.wpi.first.math.filter.MedianFilter;
import edu.wpi.first.math.filter.SlewRateLimiter;

public interface Filter extends DoubleUnaryOperator {

    public default Filter add(Filter other) {
        return measurement -> applyAsDouble(measurement) + other.applyAsDouble(measurement);
    }

    public default Filter sub(Filter other) {
        return measurement -> applyAsDouble(measurement) - other.applyAsDouble(measurement);
    }

    public default DoublePredicate eval(DoublePredicate predicate) {
        return measurement -> predicate.test(applyAsDouble(measurement));
    }

    public static Filter fromLinearFilter(LinearFilter filter) {
        return measurement -> filter.calculate(measurement);
    }

    public static Filter fromMedianFilter(MedianFilter filter) {
        return measurement -> filter.calculate(measurement);
    }

    public static Filter fromSlewRateLimiter(SlewRateLimiter filter) {
        return measurement -> filter.calculate(measurement);
    }

    public static Filter clamp(double low, double high) {
        return measurement -> MathUtil.clamp(measurement, low, high);
    }

    public static Filter deadband(double value, double deadband) {
        return measurement -> MathUtil.applyDeadband(value, deadband);
    }
}
