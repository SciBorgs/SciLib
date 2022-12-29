package frc.sciborgs.scilib.math

import edu.wpi.first.wpilibj.Timer

fun Timer.delta(): Double {
  val diff = this.get()
  this.reset()
  return diff
}
