@file:JvmName("Config")

package frc.sciborgs.scilib.config

import com.revrobotics.CANSparkMax
import edu.wpi.first.math.controller.PIDController
import edu.wpi.first.math.controller.SimpleMotorFeedforward

data class PIDConstants(val kp: Double = 0.0, val ki: Double = 0.0, val kd: Double = 0.0)

data class FFConstants(val ks: Double = 0.0, val kv: Double = 0.0, val ka: Double = 0.0)

/** Creates a [PIDController] from [PIDConstants] */
fun createPID(constants: PIDConstants): PIDController =
    PIDController(constants.kp, constants.ki, constants.kd)

/** Creates a [SimpleMotorFeedForward] from [FFConstants] */
fun createSimpleMotorFF(constants: FFConstants) =
    SimpleMotorFeedforward(constants.ks, constants.kv, constants.ka)

/** Experimental CANSparkMax configuration DSL */
fun CANSparkMax.config(configurator: CANSparkMax.() -> Unit) {
  this.restoreFactoryDefaults()
  this.configurator()
  this.burnFlash()
}
