package frc.sciborgs.scilib.control;

public abstract sealed class Measurement {

    // java tomfoolery
    
    public final class Position extends Measurement {}
    public final class Velocity extends Measurement {}
    public final class Angle    extends Measurement {}

}
