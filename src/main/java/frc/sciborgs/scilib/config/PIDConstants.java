package frc.sciborgs.scilib.config;

import edu.wpi.first.math.controller.PIDController;

public record PIDConstants(double kp, double ki, double kd) {

    public static PIDController pidController(PIDConstants constants) {
        return new PIDController(constants.kp, constants.ki, constants.kd);
    }

}
