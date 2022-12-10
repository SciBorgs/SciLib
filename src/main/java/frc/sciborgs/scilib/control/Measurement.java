package frc.sciborgs.scilib.control;

public class Measurement {

    // java is crying: incredibly jank abuse of generics, polymorphism, and the type system

    public static class Distance extends Measurement {}
    
    public static class Velocity extends Measurement {}

    public static class Angle extends Measurement {}

}
