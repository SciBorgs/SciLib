package frc.sciborgs.scilib.control;

public abstract class Measurement {

    // java is crying: incredibly jank abuse of generics, polymorphism, and the type system

    public enum Type {
        DISTANCE,
        VELOCITY,
        ANGLE,
    }

    public abstract Type type();

    public static class Distance extends Measurement {
        
        @Override
        public Type type() {
            return Type.DISTANCE;
        }

    }
    
    public static class Velocity extends Measurement {

        @Override
        public Type type() {
            return Type.VELOCITY;
        }
    
    }

    public static class Angle extends Measurement {

        @Override
        public Type type() {
            return Type.ANGLE;
        }
    
    }

}
