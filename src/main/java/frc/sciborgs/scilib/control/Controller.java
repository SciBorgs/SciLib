package frc.sciborgs.scilib.control;

import java.util.ResourceBundle.Control;
import java.util.function.DoubleBinaryOperator;

import com.revrobotics.CANSparkMax;

import edu.wpi.first.math.controller.BangBangController;
import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.controller.ProfiledPIDController;
import edu.wpi.first.math.controller.SimpleMotorFeedforward;
import edu.wpi.first.math.trajectory.TrapezoidProfile;
import frc.sciborgs.scilib.units.Position;
import frc.sciborgs.scilib.units.Unit;
import frc.sciborgs.scilib.units.Velocity;

public interface Controller<T extends Unit> extends DoubleBinaryOperator {

    public default Controller<T> add(Controller<T> other) {
        return (measurement, setpoint) -> applyAsDouble(measurement, setpoint) + other.applyAsDouble(measurement, setpoint);
    }

    public default Controller<T> sub(Controller<T> other) {
        return (measurement, setpoint) -> applyAsDouble(measurement, setpoint) - other.applyAsDouble(measurement, setpoint);
    }

    public default Controller<T> filterOutput(Filter filter) {
        return (measurement, setpoint) -> filter.applyAsDouble(applyAsDouble(measurement, setpoint));
    }

    public default Controller<T> filterInput(Filter filter) {
        return (measurement, setpoint) -> applyAsDouble(filter.applyAsDouble(setpoint), measurement);
    }

    public default Controller<T> filterMeasurement(Filter filter) {
        return (measurement, setpoint) -> applyAsDouble(setpoint, filter.applyAsDouble(measurement));
    }
}
