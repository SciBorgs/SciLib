package frc.sciborgs.scilib.units;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.controller.SimpleMotorFeedforward;
import frc.sciborgs.scilib.control.Controller;

public class Position extends Unit {
    public static Controller<Position> getPID(PIDController pid) {
        return (measurement, setpoint) -> pid.calculate(measurement, setpoint);
    }

    // TODO implement velocit/derivatives in math/
    // public static Controller<Position> getFF(SimpleMotorFeedforward ff) {
    //     return (measurement, setpoint) -> ff.calculate(0);
    // }
}
