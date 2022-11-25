package frc.sciborgs.scilib.units;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.controller.SimpleMotorFeedforward;
import frc.sciborgs.scilib.control.Controller;
import frc.sciborgs.scilib.math.DeltaTime;
import frc.sciborgs.scilib.math.Slot;

public class Velocity extends Unit {
    public static Controller<Velocity> getPID(PIDController pid) {
        return (measurement, setpoint) -> pid.calculate(measurement, setpoint);
    }

    public static Controller<Velocity> getFF(SimpleMotorFeedforward ff) {
        Slot dv = new Slot(0); // distance
        DeltaTime dt = new DeltaTime(); // time
        return (measurement, setpoint) -> ff.calculate(setpoint, dv.update(setpoint) / dt.update());
    }
}
