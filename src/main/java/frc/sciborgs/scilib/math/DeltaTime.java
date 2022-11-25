package frc.sciborgs.scilib.math;

import edu.wpi.first.wpilibj.Timer;

public class DeltaTime extends Delta {

    public DeltaTime() {
        super(Timer.getFPGATimestamp());
    }

    public double update() {
        return super.update(Timer.getFPGATimestamp());
    }
}
