package frc.sciborgs.scilib.math;

import edu.wpi.first.wpilibj.Timer;

public class DeltaTime {

    private Delta slot;

    public DeltaTime() {
        slot = new Delta(Timer.getFPGATimestamp());
    }

    public double update() {
        return slot.update(Timer.getFPGATimestamp());
    }
}
