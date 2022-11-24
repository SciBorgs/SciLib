package frc.sciborgs.scilib.config;

import edu.wpi.first.math.controller.SimpleMotorFeedforward;
import frc.sciborgs.scilib.control.Controller;

public record FFConfig(double kS, double kV, double kA) {
    public static Controller getFF(FFConfig config) {
        SimpleMotorFeedforward ff = new SimpleMotorFeedforward(config.kS, config.kV, config.kA);
        return Controller.fromFF(ff);
    }
}
