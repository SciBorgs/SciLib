package frc.sciborgs.scilib.config;

import java.util.ResourceBundle.Control;

import com.revrobotics.CANSparkMax;
import com.revrobotics.SparkMaxPIDController;
import com.revrobotics.SparkMaxPIDController.AccelStrategy;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.controller.ProfiledPIDController;
import edu.wpi.first.math.controller.SimpleMotorFeedforward;
import edu.wpi.first.math.trajectory.TrapezoidProfile;
import edu.wpi.first.math.trajectory.TrapezoidProfile.Constraints;
import frc.sciborgs.scilib.control.Controller;
import frc.sciborgs.scilib.control.Filter;

public class ControllerConfig {
    enum ControlType {
        Position,
        Velocity,
        Angle,
    }

    private ControlType controlType;

    private double kP;
    private double kI;
    private double kD;

    private double kS;
    private double kV;
    private double kA;

    private Constraints constraints;

    private boolean continuousInput = false;
    private double min;
    private double max;

    private double posTolerance;
    private double velTolerance;

    private boolean wrap;

    public Controller configureController(Controller controller) {
        
    }

    public PIDController getPID(PIDController pid) {
        pid.setP(kP);
        pid.setI(kI);
        pid.setD(kD);
        pid.setTolerance(posTolerance, velTolerance);
        if (continuousInput) {
            pid.enableContinuousInput(min, max);
        }
        return pid;
    }

    public Controller configureREVPID(CANSparkMax motorController) {
        SparkMaxPIDController pid = motorController.getPIDController();
        pid.setP(kP);
        pid.setI(kI);
        pid.setD(kD);

        switch (controlType) {
            case Velocity:
                return Controller.from((measurement,
                        setpoint) -> pid.setReference(setpoint, CANSparkMax.ControlType.kSmartVelocity, 0).value);
            default:
                return Controller.from((measurement,
                        setpoint) -> pid.setReference(setpoint, CANSparkMax.ControlType.kPosition, 0).value);
        }
    }

    public ProfiledPIDController getProfiledPID() {
        ProfiledPIDController pid = new ProfiledPIDController(kP, kI, kD, constraints);
        pid.setTolerance(posTolerance, velTolerance);
        if (continuousInput) {
            pid.enableContinuousInput(min, max);
        }
        return pid;
    }

    public SimpleMotorFeedforward getFF() {
        return new SimpleMotorFeedforward(kS, kV, kA);
    }

}
