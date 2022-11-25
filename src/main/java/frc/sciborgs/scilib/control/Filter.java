package frc.sciborgs.scilib.control;

import java.util.function.DoublePredicate;
import java.util.function.DoubleUnaryOperator;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.filter.LinearFilter;
import edu.wpi.first.math.filter.MedianFilter;
import edu.wpi.first.math.filter.SlewRateLimiter;
import frc.sciborgs.scilib.units.Position;
import frc.sciborgs.scilib.units.Unit;
import frc.sciborgs.scilib.units.Velocity;

public interface Filter<T extends Unit> extends DoubleUnaryOperator {
    
    public default Filter<T> add(Filter<T> other) {
        return measurement -> applyAsDouble(measurement) + other.applyAsDouble(measurement);
    }

    public default Filter<T> sub(Filter<T> other) {
        return measurement -> applyAsDouble(measurement) - other.applyAsDouble(measurement);
    }

    public default DoublePredicate eval(DoublePredicate predicate) {
        return measurement -> predicate.test(applyAsDouble(measurement));
    }

    public static <T extends Unit> Filter<T> fromLinearFilter(LinearFilter filter) {
        return measurement -> filter.calculate(measurement);
    }

    public static <T extends Unit> Filter<T> fromMedianFilter(MedianFilter filter) {
        return measurement -> filter.calculate(measurement);
    }

    public static <T extends Unit> Filter<T> fromSlewRateLimiter(SlewRateLimiter filter) {
        return measurement -> filter.calculate(measurement);
    }

    public static <T extends Unit> Filter<T> clamp(double low, double high) {
        return measurement -> MathUtil.clamp(measurement, low, high);
    }

    public static <T extends Unit> Filter<T> deadband(double value, double deadband) {
        return measurement -> MathUtil.applyDeadband(value, deadband);
    }
}
