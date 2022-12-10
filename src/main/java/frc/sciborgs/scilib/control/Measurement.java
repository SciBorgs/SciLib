package frc.sciborgs.scilib.control;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("unused")
public abstract sealed class Measurement {

    private Measurement() {}
    
    private static final Map<Class<? extends Measurement>, Type> classToEnum;
    private static final Map<Type, Class<? extends Measurement>> enumToClass;
    static {
        classToEnum = new HashMap<>();

        // Ensure that inner enum class is instantiated.
        // TODO: verify if this is correct
        var __ = Type.POSITION;
        
        enumToClass = new EnumMap<>(Type.class);
    }

    static Type getType(Class<? extends Measurement> typeClass) {
        return classToEnum.get(typeClass);
    }

    static Class<? extends Measurement> getTypeClass(Type type) {
        return enumToClass.get(type);
    }

    public enum Type {
        POSITION (Position.class),
        VELOCITY (Velocity.class),
        ANGLE    (Angle.class);

        private final Class<? extends Measurement> associatedClass;
        private Type(Class<? extends Measurement> associatedClass) {
            this.associatedClass = associatedClass;
        }

        /* Note that a static block of an enum class is run AFTER enum constants are initialized */
        static {
            for (Type t : Type.values()) {
                classToEnum.put(t.associatedClass, t);
                enumToClass.put(t, t.associatedClass);
            }
        }
    }

    /* final inner classes */
    public final class Position extends Measurement {}
    public final class Velocity extends Measurement {}
    public final class Angle    extends Measurement {}

}
