package frc.sciborgs.scilib.math;

public class Delta {
    private double last;

    public Delta(double initialValue) {
        last = initialValue;
    }

    public double update(double newValue) {
        double delta = newValue - last;
        last = newValue;
        return delta;
    }
}
