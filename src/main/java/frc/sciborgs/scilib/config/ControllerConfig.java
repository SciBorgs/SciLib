package frc.sciborgs.scilib.config;

import edu.wpi.first.math.controller.ArmFeedforward;
import edu.wpi.first.math.controller.ElevatorFeedforward;
import edu.wpi.first.math.controller.SimpleMotorFeedforward;
import edu.wpi.first.math.trajectory.TrapezoidProfile.Constraints;

public class ControllerConfig {

    public enum ControlType {
        Position,
        Velocity,
        Angle,
    }

    public enum MechanismType {
        Motor,
        Arm,
        Elevator,
    }

    private double kP, kI, kD;
    private double kS, kV, kA, kG, kCos;

    private ControlType controlType;
    private MechanismType mechanismType;

    private Constraints constraints;

    private boolean continuousInput = false;
    private double min;
    private double max;

    private double posTolerance;
    private double velTolerance;

    // FEEDFORWARD

    public void setSimpleFF(double kS, double kV, double kA) {
        if (mechanismType != null) {
            throw new IllegalStateException("You already set FF");
        }
        mechanismType = MechanismType.Motor;

        this.kS = kS;
        this.kV = kV;
        this.kA = kA;
    }

    public void setElevatorFF(double kS, double kV, double kA, double kG) {
        if (mechanismType != null) {
            throw new IllegalStateException("You already set FF");
        }
        mechanismType = MechanismType.Elevator;

        this.kS = kS;
        this.kV = kV;
        this.kA = kA;
        this.kG = kG;
    }

    public void setArmFF(double kS, double kV, double kA, double kCos) {
        if (mechanismType != null) {
            throw new IllegalStateException("You already set FF");
        }
        mechanismType = MechanismType.Arm;

        this.kS = kS;
        this.kV = kV;
        this.kA = kA;
        this.kCos = kCos;
    }

    public SimpleMotorFeedforward buildSimpleFF() {
        if (mechanismType != MechanismType.Motor) {
            throw new IllegalStateException("Not set to the correct mechanism type");
        }
        return new SimpleMotorFeedforward(kS, kV, kA);
    }

    public ArmFeedforward buildArmFF() {
        if (mechanismType != MechanismType.Arm) {
            throw new IllegalStateException("Not set to the correct mechanism type");
        }
        return new ArmFeedforward(kS, kCos, kV, kA);
    }

    public ElevatorFeedforward buildElevatorFeedforward() {
        if (mechanismType != MechanismType.Elevator) {
            throw new IllegalStateException("Not set to the correct mechanism type");
        }
        return new ElevatorFeedforward(kS, kG, kV, kA);
    }

    public void setConstraints(Constraints constraints) {
        this.constraints = constraints;
    }

    // public void setConstraintsFromVelocity(double maxVoltage, double velocity) {
    //     double accel = 0;
    //     switch (mechanismType) {
    //         case Motor:
    //             accel = buildSimpleFF().maxAchievableAcceleration(maxVoltage, velocity);
    //         case Arm:
    //             accel = buildArmFF().maxAchievableAcceleration(maxVoltage, angle, velocity);
    //         case Elevator:
    //             accel = buildElevatorFeedforward().maxAchievableAcceleration(maxVoltage, velocity);
    //         default:
    //             throw new IllegalStateException("Not set to an ff model");
    //     }
    // }
}
