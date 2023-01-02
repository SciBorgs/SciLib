package frc.sciborgs.scilib.config

import com.ctre.phoenix.motorcontrol.NeutralMode
import com.ctre.phoenix.motorcontrol.can.WPI_TalonFX
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX
import com.revrobotics.CANSparkMax
import com.revrobotics.CANSparkMaxLowLevel.MotorType

/**
 * MotorConfig is a builder class for standardizing vendor motor controllers.
 *
 * ### Example usage for a CANSparkMax differential drive
 *
 * ```kotlin
 * val leftMotor = MotorConfig(neutralBehavior = NeutralBehavior.BRAKE, currentLimit = 80)
 * val rightMotor = leftMotor.copy(inverted = true)
 *
 * val leftPorts = intArrayOf(1, 2, 3)
 * val rightPorts = intArrayOf(4, 5, 6)
 *
 * val leftGroup = MotorControllerGroup(leftMotor.buildCanSparkMax(*leftPorts, motorType = MotorType.kBrushless))
 * val rightGroup = MotorControllerGroup(rightMotor.buildCanSparkMax(*rightPorts, motorType = MotorType.kBrushless))
 * val driveTrain = DifferentialDrive(leftGroup, rightGroup)
 * ```
 *
 * @author Asa Paparo
 */
data class MotorConfig(
    var inverted: Boolean = false,
    var neutralBehavior: NeutralBehavior = NeutralBehavior.COAST,
    var openLoopRampRate: Double = 0.0,
    var currentLimit: Int = 80,
    var pidConstants: PIDConstants? = null,
) {

  /**
   * Creates a CANSparkMax based on configured values
   *
   * @param id the motor controller's device id
   * @param motorType the [MotorType] of the physical motor ***This could break your motor if it is
   * not set correctly***
   * @return a new CANSparkMax object
   */
  fun buildCanSparkMax(id: Int, motorType: MotorType): CANSparkMax {
    val motor = CANSparkMax(id, motorType)
    motor.restoreFactoryDefaults()
    motor.inverted = inverted
    motor.idleMode = neutralBehavior.rev()
    motor.openLoopRampRate = openLoopRampRate
    motor.setSmartCurrentLimit(currentLimit)
    pidConstants?.also {
      val pid = motor.pidController
      pid.p = it.kp
      pid.i = it.ki
      pid.d = it.kd
    }
    motor.burnFlash()

    return motor
  }

  /**
   * Creates an array of CANSparkMax objects based on configured values.
   *
   * One motor controller will be created per id, in order
   *
   * @param ids any number of motor ids
   * @param motorType the [MotorType] of the physical motors ***This could break your motors if it
   * is not set correctly***
   * @return array of CANSparkMax objects
   */
  fun buildCanSparkMax(vararg ids: Int, motorType: MotorType) =
      Array(ids.size) { buildCanSparkMax(it, motorType) }

  /**
   * Creates a TalonSRX based on configured values
   *
   * @param id the motor controller's device id
   * @param pidSlot the pidSlot of the controller
   * @return a new WPI_TalonSRX object
   */
  fun buildTalonSRX(id: Int, pidSlot: Int = 0): WPI_TalonSRX {
    val motor = WPI_TalonSRX(id)
    motor.configFactoryDefault()
    motor.inverted = this.inverted
    motor.setNeutralMode(neutralBehavior.ctre())
    motor.configOpenloopRamp(openLoopRampRate)
    motor.configPeakCurrentLimit(currentLimit)
    motor.enableCurrentLimit(true)
    pidConstants?.also {
      motor.config_kP(pidSlot, it.kp)
      motor.config_kI(pidSlot, it.ki)
      motor.config_kD(pidSlot, it.kd)
    }
    return motor
  }

  /**
   * Creates an array of TalonSRX objects based on configured values
   *
   * One motor controller will be created per id, in order
   *
   * @param ids any number of motor ids
   * @param pidSlot the pidSlot of the controller
   * @return array of WPI_TalonSRX objects
   */
  fun buildTalonSRX(vararg ids: Int, pidSlot: Int = 0) =
      Array(ids.size) { buildTalonSRX(it, pidSlot) }

  /**
   * Creates a TalonFX based on configured values
   *
   * @param id the motor controller's device id
   * @param pidSlot the pidSlot of the controller
   * @return a new WPI_TalonFX object
   */
  fun buildTalonFX(id: Int, pidSlot: Int = 0): WPI_TalonFX {
    val motor = WPI_TalonFX(id)
    motor.configFactoryDefault()
    motor.inverted = inverted
    motor.setNeutralMode(neutralBehavior.ctre())
    motor.configOpenloopRamp(openLoopRampRate)
    pidConstants?.also {
      motor.config_kP(pidSlot, it.kp)
      motor.config_kI(pidSlot, it.ki)
      motor.config_kD(pidSlot, it.kd)
    }
    return motor
  }

  /**
   * Creates an array of TalonFX objects based on configured values
   *
   * One motor controller will be created per id, in order
   *
   * @param ids any number of motor ids
   * @param pidSlot the pidSlot of the controller
   * @return array of WPI_TalonFX objects
   */
  fun buildTalonFX(vararg ids: Int, pidSlot: Int = 0) =
      Array(ids.size) { buildTalonFX(it, pidSlot) }
}

/** Enum to represent a generic neutral behvavior */
enum class NeutralBehavior(private val coast: Boolean) {
  COAST(true),
  BRAKE(false);

  /** Gets the rev compatible neutral mode */
  fun rev() = if (coast) CANSparkMax.IdleMode.kCoast else CANSparkMax.IdleMode.kBrake

  /** Gets the ctre compatible neutral mode */
  fun ctre() = if (coast) NeutralMode.Coast else NeutralMode.Brake
}
