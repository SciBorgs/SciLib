package frc.sciborgs.scilib.math;

public class Delta {
    private double last;

    public Delta(double initialValue) {
        last = initialValue;
    }

    public double update(double newValue) {
        double old = newValue - last;
        last = newValue;
        return old;
    }
}
