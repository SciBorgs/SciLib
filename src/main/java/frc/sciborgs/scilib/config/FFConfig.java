package frc.sciborgs.scilib.config;

import edu.wpi.first.math.controller.SimpleMotorFeedforward;

public record FFConfig(double kS, double kV, double kA) {
    public static SimpleMotorFeedforward getFF(FFConfig config) {
        return new SimpleMotorFeedforward(config.kS, config.kV, config.kA);
    }
}
