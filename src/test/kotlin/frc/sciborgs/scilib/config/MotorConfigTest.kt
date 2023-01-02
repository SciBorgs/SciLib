package frc.sciborgs.scilib.config

import com.revrobotics.CANSparkMaxLowLevel

class MotorConfigTest {

  // @Test annotations are currently removed because CANSparkMax objects cannot be created without
  // HAL

  // TODO get hal working

  fun canSparkMaxTest() {
    val cfg = MotorConfig(inverted = true, neutralBehavior = NeutralBehavior.BRAKE)
    val motor = cfg.buildCanSparkMax(1, CANSparkMaxLowLevel.MotorType.kBrushless)
    assert(motor.inverted == cfg.inverted)
    assert(motor.idleMode == cfg.neutralBehavior.rev())
    assert(motor.openLoopRampRate == cfg.openLoopRampRate)
  }

  fun pidTest() {
    val cfg = MotorConfig(pidConstants = PIDConstants(1.0, 0.0, 0.1))
    val motor = cfg.buildCanSparkMax(2, CANSparkMaxLowLevel.MotorType.kBrushless)
    val pid = motor.pidController
    assert(pid.p == cfg.pidConstants?.kp)
    assert(pid.i == cfg.pidConstants?.ki)
    assert(pid.d == cfg.pidConstants?.kd)
  }
}
