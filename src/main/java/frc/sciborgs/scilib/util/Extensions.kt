package frc.sciborgs.scilib.util

import edu.wpi.first.wpilibj.Timer

fun Timer.delta(): Double {
  val diff = this.get()
  this.reset()
  return diff
}
