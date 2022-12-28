package frc.sciborgs.scilib.config;

import com.revrobotics.RelativeEncoder
import com.revrobotics.CANSparkMax
import edu.wpi.first.wpilibj.Encoder
import edu.wpi.first.wpilibj.CounterBase
/*
  Configurator set all desired value for encoders
*/
data class EncoderConfig(
  var inverted: Boolean = false,
  var gearRatio: Double = 1.0,
  var countsPerRev: Int = 0,
) {
  constructor(inverted: Boolean, gearRatio: Double) : this(inverted, gearRatio, 0)
/*
  Builders,

Used to build different encoders depending what encoders are used


*/
//For encoders built into NEO motors
  fun buildBuiltInEncoders(motor: CANSparkMax): RelativeEncoder {
    val encoder = motor.getEncoder()
    encoder.inverted = inverted
    encoder.velocityConversionFactor = gearRatio
    
    return encoder
  }
//For Encoders that are connected to CANSparkMaxes via their encoder port
  fun buildAlternateCanEncoders(motor: CANSparkMax): RelativeEncoder {
    val encoder = motor.getAlternateEncoder(countsPerRev)
    encoder.inverted = inverted
    encoder.velocityConversionFactor = gearRatio
    
    return encoder
  }
//For Encoders that are completely seperate from the CANSparkMax and are connected directly to the roborio
  fun buildEncoderFromRio(firstPin: Int, secondPin: Int ): Encoder{
    val encoder = Encoder(firstPin, secondPin, inverted)
    encoder.reset()
    encoder.setDistancePerPulse(gearRatio)
    
    return encoder

  }
    
  }


