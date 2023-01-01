package frc.sciborgs.math

import edu.wpi.first.wpilibj.Timer
import frc.sciborgs.scilib.stream.Stream
import frc.sciborgs.scilib.util.delta

/** Derivative filter with respect to system time */
class Derivative(private var last: Double = 0.0) : (Double) -> Double {

  private val time = Timer()

  override fun invoke(input: Double): Double {
    val delta = input - last
    last = input
    return delta / time.delta()
  }
}

/** Right Riemann sum with respect to system time */
class Integral(private var sum: Double = 0.0) : (Double) -> Double {

  private val time = Timer()

  override fun invoke(input: Double): Double {
    sum += input * time.delta()
    return sum
  }
}

fun Stream.differentiate() = map(Derivative())

fun Stream.integrate() = map(Integral())
