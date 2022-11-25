package frc.sciborgs.scilib.units;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.controller.SimpleMotorFeedforward;
import frc.sciborgs.scilib.control.Controller;

public class Velocity extends Unit {
    public static Controller<Velocity> getPID(PIDController pid) {
        return (measurement, setpoint) -> pid.calculate(measurement, setpoint);
    }

    // TODO implement timed controller for acceleration
    public static Controller<Velocity> getFF(SimpleMotorFeedforward ff) {
        return (measurement, setpoint) -> ff.calculate(setpoint);
    }
}
