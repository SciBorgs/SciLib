package frc.sciborgs.scilib.units;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.controller.SimpleMotorFeedforward;
import frc.sciborgs.scilib.control.Controller;
import frc.sciborgs.scilib.math.DeltaTime;
import frc.sciborgs.scilib.math.Slot;

public class Position extends Unit {
    public static Controller<Position> getPID(PIDController pid) {
        return (measurement, setpoint) -> pid.calculate(measurement, setpoint);
    }

    public static Controller<Position> getFF(SimpleMotorFeedforward ff) {
        Slot dd = new Slot(0); // distance
        DeltaTime dt = new DeltaTime(); // time
        return (measurement, setpoint) -> ff.calculate(dd.update(setpoint) / dt.update());
    }
}
