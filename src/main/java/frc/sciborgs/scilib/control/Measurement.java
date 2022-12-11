package frc.sciborgs.scilib.control;

public abstract sealed class Measurement {

    // java tomfoolery

    // this might be useful, but servers absolutely no purpose rn
    // public abstract Num getDerivative();

    public static final class Position extends Measurement {}
    
    public static final class Velocity extends Measurement {}

    public static final class Acceleratioon extends Measurement {}

    public static final class Jerk extends Measurement {}

}
