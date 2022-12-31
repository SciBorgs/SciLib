package frc.sciborgs.math

import edu.wpi.first.wpilibj.Timer
import frc.sciborgs.scilib.filter.Filter
import frc.sciborgs.scilib.math.delta

/** Derivative filter with respect to system time */
class Derivative(var last: Double = 0.0) : Filter {

  private var delta = 0.0
  private val time = Timer()

  override fun calculate(input: Double): Double {
    delta = input - last
    last = input
    return delta / time.delta()
  }
}

/** Right Riemann sum based on system time */
class Integral(var sum: Double = 0.0) : Filter {

  private val time = Timer()

  override fun calculate(input: Double): Double {
    sum += input * time.delta()
    return sum
  }
}
