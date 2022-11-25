package frc.sciborgs.scilib.units;

import edu.wpi.first.math.controller.PIDController;
import frc.sciborgs.scilib.control.Controller;

public class Angle extends Unit {
    public static Controller<Angle> getPID(PIDController pid) {
        return (measurement, setpoint) -> pid.calculate(measurement, setpoint);
    }

    public static Controller<Angle> getPID(PIDController pid, double minInput, double maxInput) {
        pid.enableContinuousInput(minInput, maxInput);
        return (measurement, setpoint) -> pid.calculate(measurement, setpoint);
    }
}
