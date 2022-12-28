package frc.sciborgs.scilib.config

import com.ctre.phoenix.motorcontrol.NeutralMode
import com.ctre.phoenix.motorcontrol.can.WPI_TalonFX
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX
import com.revrobotics.CANSparkMax
import com.revrobotics.CANSparkMaxLowLevel.MotorType

/**
 * MotorConfig is a builder class for standardizing vendor motor controllers.
 *
 * <p>Example usage for a CANSparkMax differential drive:
 *
 * <pre><code> var leftMotor = new MotorConfig() .setNeutralBehavior(NeutralBehavior.BRAKE)
 * .setCurrentLimit(20);
 *
 * var rightMotor = new MotorConfig(leftMotor) .setInverted(true);
 *
 * int[] leftPorts = {1, 2, 3}; int[] rightPorts = {4, 5, 6};
 *
 * var leftGroup = new MotorControllerGroup(leftMotor.getCanSparkMax(leftPorts)); var rightGroup =
 * new MotorControllerGroup(rightMotor.getCanSparkMax(rightPorts));
 *
 * var driveTrain = new DifferentialDrive(leftGroup, rightGroup); </pre></code>
 */
data class MotorConfig(
    var inverted: Boolean = false,
    var neutralBehavior: NeutralBehavior = NeutralBehavior.COAST,
    var openLoopRampRate: Double = 0.0,
    var currentLimit: Int = 80,
    var pidConstants: PIDConstants? = null,
) {

  enum class NeutralBehavior(private val coast: Boolean) {
    COAST(true),
    BRAKE(false);

    fun rev() = if (coast) CANSparkMax.IdleMode.kCoast else CANSparkMax.IdleMode.kBrake

    fun ctre() = if (coast) NeutralMode.Coast else NeutralMode.Brake
  }

  // MOTOR BUILDERS
  /**
   * Creates a CANSparkMax based on configured values
   *
   * @param id the motor controller's device id
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
   * Creates an array of CANSparkMax objects based on their configured values.
   *
   * <p>One motor controller will be created per id, in order</p>
   *
   * @param ids
   * @return array of CANSparkMax objects
   */
  fun buildCanSparkMax(vararg ids: Int, motorType: MotorType) =
      Array(ids.size) { buildCanSparkMax(it, motorType) }

  fun buildTalonSRX(id: Int): WPI_TalonSRX {
    val motor = WPI_TalonSRX(id)
    motor.configFactoryDefault()
    motor.inverted = this.inverted
    motor.setNeutralMode(neutralBehavior.ctre())
    motor.configOpenloopRamp(openLoopRampRate)
    motor.configPeakCurrentLimit(currentLimit)
    motor.enableCurrentLimit(true)
    pidConstants?.also {
      motor.config_kP(0, it.kp)
      motor.config_kI(0, it.ki)
      motor.config_kD(0, it.kd)
    }
    return motor
  }

  fun buildTalonSRX(vararg ids: Int) = Array(ids.size) { buildTalonSRX(it) }

  fun buildTalonFX(id: Int): WPI_TalonFX {
    val motor = WPI_TalonFX(id)
    motor.configFactoryDefault()
    motor.inverted = inverted
    motor.setNeutralMode(neutralBehavior.ctre())
    motor.configOpenloopRamp(openLoopRampRate)
    pidConstants?.also {
      motor.config_kP(0, it.kp)
      motor.config_kI(0, it.ki)
      motor.config_kD(0, it.kd)
    }
    return motor
  }

  fun buildTalonFX(vararg ids: Int) = Array(ids.size) { buildTalonFX(it) }
}
