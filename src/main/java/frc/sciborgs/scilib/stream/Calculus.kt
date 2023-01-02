@file:JvmName("Calculus")

package frc.sciborgs.scilib.stream

import edu.wpi.first.wpilibj.Timer
import frc.sciborgs.scilib.util.delta

/**
 * Derivative filter with respect to system time
 *
 * @constructor creates a new [Derivative] with a starting value of [last]
 * @see Stream.differentiate
 * @author Asa Paparo
 */
class Derivative(private var last: Double = 0.0) : (Double) -> Double {

  private val time = Timer()

  override fun invoke(input: Double): Double {
    val delta = input - last
    last = input
    return delta / time.delta()
  }
}

/**
 * Right Riemann sum with respect to system time
 *
 * @constructor creates a new [Integral] with a starting sum of [sum]
 * @see Stream.integrate
 * @author Asa Paparo
 */
class Integral(private var sum: Double = 0.0) : (Double) -> Double {

  private val time = Timer()

  override fun invoke(input: Double): Double {
    sum += input * time.delta()
    return sum
  }
}

/**
 * Maps a [Stream] by a new [Derivative]
 *
 * Equivalent to `stream.map(Derivative())`
 */
fun Stream.differentiate() = map(Derivative())

/**
 * Maps a [Stream] by a new [Integral]
 *
 * Equivalent to `stream.map(Integral())`
 */
fun Stream.integrate() = map(Integral())
