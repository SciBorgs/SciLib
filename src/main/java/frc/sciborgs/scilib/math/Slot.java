package frc.sciborgs.scilib.math;

/**
 * Slot is a horrible class name,
 * it just stores the last inputted double
 */
public class Slot {
    private double last;

    public Slot(double initialValue) {
        last = initialValue;
    }

    public double update(double newValue) {
        double old = newValue - last;
        last = newValue;
        return old;
    }
}
