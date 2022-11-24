package frc.sciborgs.scilib.config;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.sciborgs.scilib.control.Controller;

public record PIDConfig(double kP, double kI, double kD) {
    public static Controller getPID(PIDConfig config) {
        PIDController pid = new PIDController(config.kP, config.kI, config.kD);
        SmartDashboard.putData(pid);
        return Controller.fromPID(pid);
    }
}
