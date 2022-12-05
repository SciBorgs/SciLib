package frc.sciborgs.scilib.math;

import frc.sciborgs.scilib.control.Filter;

/**
 * This is a right Riemann sum based on system time, not an integral
 */
public class Integral implements Filter {

    private final ElapsedTime dt;

    private double sum;

    public Integral() {
        dt = new ElapsedTime();
        sum = 0;
    }

    @Override
    public double calculate(double value) {
        sum += value * dt.reset();
        return sum;
    }

    public double get() {
        return sum;
    }
}
