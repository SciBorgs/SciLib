package frc.sciborgs.scilib.config;

import com.revrobotics.RelativeEncoder;

public class EncoderConfig {
  RelativeEncoder encoder;
  // if you wanna use the class only to configure existing encoders
  public EncoderConfig() {}

  public EncoderConfig(RelativeEncoder encoder, double gearRatio) {
    this.encoder = encoder;
    this.encoder.setVelocityConversionFactor(gearRatio);
  }

  public EncoderConfig(RelativeEncoder encoder, double gearRatio, int wheelCircumfrence) {
    this(encoder, gearRatio);
    this.encoder.setPositionConversionFactor(wheelCircumfrence * gearRatio);
  }

  public void encoderConfigurator(RelativeEncoder encoder, double gearRatio, boolean isInverted) {
    encoder.setVelocityConversionFactor(gearRatio);
    encoder.setInverted(isInverted);
  }

  public void encoderConfigurator(RelativeEncoder encoder, double gearRatio) {
    encoderConfigurator(encoder, gearRatio, false);
  }

  public RelativeEncoder getEncoder() {
    return encoder;
  }
}
