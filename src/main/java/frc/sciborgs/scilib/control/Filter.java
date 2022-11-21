package frc.sciborgs.scilib.control;

import edu.wpi.first.math.filter.LinearFilter;

public interface Filter {
    public double calculate(double measurement);

    public static Filter fromLinearFilter(LinearFilter filter) {
        return measurement -> filter.calculate(measurement);
    }

    public default Filter with(Filter other) {
        return measurement -> calculate(other.calculate(measurement));
    }

    public default Filter add(Filter other) {
        return measurement -> calculate(measurement) + other.calculate(measurement);
    }

    public default Filter sub(Filter other) {
        return measurement -> calculate(measurement) - other.calculate(measurement);
    }
}
