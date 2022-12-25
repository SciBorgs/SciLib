package frc.sciborgs.scilib.config;

import org.junit.jupiter.api.Test;

import com.revrobotics.CANSparkMax;

class ConfigTest {
    @Test void nothing() {
        MotorConfig cfg = new MotorConfig();
        cfg.setInverted(true);
        CANSparkMax motor = cfg.getCanSparkMax(1);
        assert true;
    }
}
