package frc.sciborgs.scilib.config;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.trajectory.TrapezoidProfile.Constraints;

public class PIDConfig {
    private double kP;
    private double kI;
    private double kD;

    private Constraints constraints;
    
    public PIDController getWPILIB(PIDConfig config) {
        return new PIDController(config.kP, config.kI, config.kD);
    }
}
