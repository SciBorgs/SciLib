package frc.sciborgs.scilib.config;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.sciborgs.scilib.control.Controller;
import frc.sciborgs.scilib.units.Unit;

public record PIDConfig(double kP, double kI, double kD) {}
