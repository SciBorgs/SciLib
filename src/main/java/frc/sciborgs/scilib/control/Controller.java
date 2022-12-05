package frc.sciborgs.scilib.control;

import java.util.function.DoubleBinaryOperator;

public interface Controller {

    public double calculate(double setpoint, double measurement);

    public default boolean atSetpoint() {
        return true;
    }

    public default Controller add(Controller other) {
        return op(other, (a, b) -> a + b);
    }

    public default Controller sub(Controller other) {
        return op(other, (a, b) -> a - b);
    }

    public default Controller op(Controller other, DoubleBinaryOperator op) {
        Controller t = this;
        return new Controller() {

            @Override
            public double calculate(double setpoint, double measurement) {
                return op.applyAsDouble(t.calculate(setpoint, measurement), other.calculate(setpoint, measurement));
            }

            @Override
            public boolean atSetpoint() {
                return this.atSetpoint() && other.atSetpoint();
            }
            
        };
    }

}
