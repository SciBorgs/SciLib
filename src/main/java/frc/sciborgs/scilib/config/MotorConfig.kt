package frc.sciborgs.scilib.config

import com.revrobotics.CANSparkMax
import com.revrobotics.CANSparkMaxLowLevel
import com.revrobotics.CANSparkMaxLowLevel.MotorType
import edu.wpi.first.wpilibj.motorcontrol.MotorControllerGroup

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
    var motorType: MotorType,
    var neutralBehavior: NeutralBehavior = NeutralBehavior.COAST,
    var openLoopRampRate: Double = 0.0,
    var currentLimit: Int = 80,
    var pidConstants: PIDConstants? = null,
) {

  enum class MotorType(val brushed: Boolean) {
    BRUSHED(true),
    BRUSHLESS(false);

    fun getREV(): CANSparkMaxLowLevel.MotorType =
        if (brushed) CANSparkMaxLowLevel.MotorType.kBrushed
        else CANSparkMaxLowLevel.MotorType.kBrushless
  }

  enum class NeutralBehavior(val coast: Boolean) {
    COAST(true),
    BRAKE(false);

    fun getREV(): CANSparkMax.IdleMode =
        if (coast) CANSparkMax.IdleMode.kCoast else CANSparkMax.IdleMode.kBrake
  }

  /**
   * Creates a CANSparkMax based on configured values
   *
   * @param id the motor controller's device id
   * @return a new CANSparkMax object
   */
  fun buildCanSparkMax(id: Int): CANSparkMax {
    val motor = CANSparkMax(id, motorType.getREV())
    motor.restoreFactoryDefaults()
    motor.setInverted(inverted)
    motor.setIdleMode(neutralBehavior.getREV())
    motor.setOpenLoopRampRate(openLoopRampRate)
    motor.setSmartCurrentLimit(currentLimit)
    pidConstants?.also {
      val pid = motor.getPIDController()
      pid.setP(it.kp)
      pid.setI(it.ki)
      pid.setD(it.kd)
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
  fun getCanSparkMaxArray(vararg ids: Int): Array<CANSparkMax> =
      Array(ids.size) { buildCanSparkMax(it) }

  /**
   * Creates a MotorControllerGroup from CANSparkMax objects based on their configured values.
   *
   * <p>Shorthand for MotorControllerGroup(getCanSparkMaxArray(ids))</p>
   */
  fun getCANSparkMaxMotorControllerGroup(vararg ids: Int) =
      MotorControllerGroup(getCanSparkMaxArray(*ids))
}
