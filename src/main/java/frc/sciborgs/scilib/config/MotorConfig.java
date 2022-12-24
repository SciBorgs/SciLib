package frc.sciborgs.scilib.config;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.motorcontrol.MotorControllerGroup;

/**
 * MotorConfig is a builder class for standardization of vendor motor
 * controllers.
 * 
 * <p>
 * Example usage for a CANSparkMax differential drive:
 * </p>
 * 
 * <pre><code>
 * var leftMotor = new MotorConfig()
 *  .setNeutralBehavior(NeutralBehavior.BRAKE)
 *  .setCurrentLimit(20);
 * 
 * var rightMotor = new MotorConfig(leftMotor)
 *  .setInverted(true);
 * 
 * int[] leftPorts = {1, 2, 3};
 * int[] rightPorts = {4, 5, 6};
 * 
 * var leftGroup = new MotorControllerGroup(leftMotor.getCanSparkMax(leftPorts));
 * var rightGroup = new MotorControllerGroup(rightMotor.getCanSparkMax(rightPorts));
 * 
 * var driveTrain = new DifferentialDrive(leftGroup, rightGroup);
 * </pre></code>
 */
public final class MotorConfig {

    public enum MotorType {
        BRUSHED(true),
        BRUSHLESS(false);

        private final boolean brushed;

        private MotorType(boolean brushed) {
            this.brushed = brushed;
        }

        public CANSparkMaxLowLevel.MotorType getREV() {
            return brushed ? CANSparkMaxLowLevel.MotorType.kBrushed : CANSparkMaxLowLevel.MotorType.kBrushless;
        }
    }
    
    public enum NeutralBehavior {
        COAST(true),
        BRAKE(false);

        private final boolean coast;

        private NeutralBehavior(boolean coast) {
            this.coast = coast;
        }

        public CANSparkMax.IdleMode getREV() {
            return coast ? CANSparkMax.IdleMode.kCoast : CANSparkMax.IdleMode.kBrake;
        }
    }
    
    private boolean inverted;
    private MotorType motorType;
    private NeutralBehavior neutralBehavior;
    private double openLoopRampRate;
    private int currentLimit;
    private PIDConstants pidConstants;
    // private EncoderConfig eConfig = new EncoderConfig();

    public MotorConfig() {
        setInverted(false);
        setMotorType(MotorType.BRUSHLESS);
        setNeutralBehavior(NeutralBehavior.COAST);
        // setOpenLoopRampRate(openLoopRampRate)
        // setCurrentLimit(currentLimit)
    }

    public MotorConfig(MotorConfig base) {
        this.inverted = base.isInverted();
        this.motorType = base.getMotorType();
        this.neutralBehavior = base.getNeutralBehavior();
        this.openLoopRampRate = base.getOpenLoopRampRate();
        this.currentLimit = base.getCurrentLimit();
        this.pidConstants = base.getPidConstants();
    }

    /**
     * Creates a CANSparkMax based on configured values
     * @param id the motor controller's device id
     * @return a new CANSparkMax object 
     */
    public CANSparkMax getCanSparkMax(int id){
        var motor = new CANSparkMax(id, motorType.getREV());
        motor.setInverted(inverted);
        motor.setIdleMode(neutralBehavior.getREV());
        motor.setOpenLoopRampRate(openLoopRampRate);
        motor.setSmartCurrentLimit(currentLimit);
        if (pidConstants != null) {
            var pid = motor.getPIDController();
            pid.setP(pidConstants.kp());
            pid.setI(pidConstants.ki());
            pid.setD(pidConstants.kd());
        }
        return motor;
    }

    /**
     * Creates a list of CANSparkMax objects based on their configured values
     * One motor controller will be created per id, in order
     * @param ids
     * @return array of CANSparkMax objects
     */
    public CANSparkMax[] getCanSparkMax(int...ids) {
        var res = new CANSparkMax[ids.length];
        for (int i = 0; i < ids.length; i++) {
            res[i] = getCanSparkMax(ids[i]);
        }
        return res;
    }

    public boolean isInverted() {
        return inverted;
    }

    public MotorConfig setInverted(boolean inverted) {
        this.inverted = inverted;
        return this;
    }

    public MotorType getMotorType() {
        return motorType;
    }

    public MotorConfig setMotorType(MotorType motorType) {
        this.motorType = motorType;
        return this;
    }

    public NeutralBehavior getNeutralBehavior() {
        return neutralBehavior;
    }

    public MotorConfig setNeutralBehavior(NeutralBehavior neutralBehavior) {
        this.neutralBehavior = neutralBehavior;
        return this;
    }

    public double getOpenLoopRampRate() {
        return openLoopRampRate;
    }

    public MotorConfig setOpenLoopRampRate(double openLoopRampRate) {
        this.openLoopRampRate = openLoopRampRate;
        return this;
    }

    public int getCurrentLimit() {
        return currentLimit;
    }

    public MotorConfig setCurrentLimit(int currentLimit) {
        this.currentLimit = currentLimit;
        return this;
    }

    public PIDConstants getPidConstants() {
        return pidConstants;
    }

    public MotorConfig setPidConstants(PIDConstants pidConstants) {
        this.pidConstants = pidConstants;
        return this;
    }
    
}
