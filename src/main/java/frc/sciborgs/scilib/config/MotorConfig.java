package frc.sciborgs.scilib.config;

import com.revrobotics.CANSparkMax;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.wpilibj.Encoder;


public class MotorConfig {
    private CANSparkMax canSparkMax;
    private RelativeEncoder encoder;
    private EncoderConfig eConfig = new EncoderConfig();
    
    public MotorConfig(int id, MotorType motorType, boolean isInverted){
        canSparkMax = new CANSparkMax(id, motorType);
        canSparkMax.setInverted(isInverted);
        eConfig.encoderConfigurator(canSparkMax.getEncoder(), 10.71);
        encoder = canSparkMax.getEncoder();
    }
    public MotorConfig(int id, MotorType motorType){
        this(id, motorType, false);
    }
    public CANSparkMax getCanSparkMax(){
        return canSparkMax;
    }
    public void setVoltage(int Voltage){
        canSparkMax.setVoltage(Voltage);
    }
    public double getVelocity(){
        return encoder.getVelocity();
    }
    
}
