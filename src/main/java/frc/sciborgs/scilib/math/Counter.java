package frc.sciborgs.scilib.math;

public class Counter {
    private double value;

    public Counter(double initialValue) {
        value = initialValue;
    }

    public double increase(double amount) {
        return value += amount;
    }
}
