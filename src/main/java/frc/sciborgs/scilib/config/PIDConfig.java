package frc.sciborgs.scilib.config;

import edu.wpi.first.math.controller.PIDController;

public record PIDConfig(double kP, double kI, double kD) {
    public static PIDController getPID(PIDConfig config) {
        return new PIDController(config.kP, config.kI, config.kD);
    }
}
