package frc.sciborgs.scilib.math;

import edu.wpi.first.wpilibj.Timer;

public class DiffTimer {

    private double start;

    public DiffTimer() {
        start = Timer.getFPGATimestamp();
    }

    public double get() {
        return Timer.getFPGATimestamp() - start;
    }

    public double reset() {
        double old = start;
        start = Timer.getFPGATimestamp();
        return old;
    }
}
