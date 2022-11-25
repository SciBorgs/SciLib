package frc.sciborgs.scilib.config;

import edu.wpi.first.math.controller.SimpleMotorFeedforward;
import frc.sciborgs.scilib.control.Controller;

public record FFConfig(double kS, double kV, double kA) {}
