package frc.sciborgs.scilib.math;

import edu.wpi.first.wpilibj.Timer;

public class DeltaTime {

    private Slot slot;

    public DeltaTime() {
        slot = new Slot(Timer.getFPGATimestamp());
    }

    public double update() {
        return slot.update(Timer.getFPGATimestamp());
    }
}
