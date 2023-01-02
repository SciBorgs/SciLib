@file:JvmName("StreamUtils")

package frc.sciborgs.scilib.stream

import java.util.function.DoubleSupplier

/**
 * A functional interface that extends DoubleSupplier
 *
 * Its abstract method is [get], which has the signature `() -> Double`
 *
 * Streams are lazy, meaning any calculations are ran when [get] is called.
 *
 * Streams can be modified using [map], which applies a `(Double) -> Double` function to the output
 * of the stream
 *
 * [plus] and [minus] are operators for two streams, [times] and [div] are scalar multiplication
 * operators
 *
 * @author Asa Paparo
 */
fun interface Stream : DoubleSupplier {

  /**
   * Equivalent to [get]
   *
   * @return the stream's output
   */
  override fun getAsDouble() = get()

  /**
   * Gets the stream's output
   *
   * @return the output
   */
  fun get(): Double

  /**
   * Addition operator between two [Stream]
   *
   * @param other the other addend
   * @return a stream that [gets][get] the sum of this and [other]
   */
  operator fun plus(other: Stream) = Stream { get() + other.get() }

  /**
   * Subtraction operator between two [Stream]
   *
   * @param other the subtrahend
   * @return a stream that [gets][get] the difference of this and [other]
   */
  operator fun minus(other: Stream) = Stream { get() - other.get() }

  /**
   * Scalar post-multiplication operator between a [scalar][Double] and a [Stream]
   *
   * Scalar pre-multiplication and post-multiplication are equivalent, but separate operator
   * functions
   *
   * @param scalar the scalar double
   * @return a stream that [gets][get] this scaled by [scalar]
   */
  operator fun times(scalar: Double) = Stream { get() * scalar }

  /**
   * Scalar division operator between a [scalar][Double] and a [Stream]
   *
   * @param scalar the scalar divisor
   * @return a stream that [gets][get] this scaled by 1/[scalar]
   */
  operator fun div(scalar: Double) = Stream { get() / scalar }

  /**
   * Applies the provided function to the output of this [Stream]
   *
   * @param mapper the function to be applied to this stream
   * @return a stream which has its return value mapped by [mapper]
   */
  fun map(mapper: (Double) -> Double): Stream = Stream { mapper(get()) }
}

/**
 * Scalar pre-multiplication operator between a [scalar][Double] and a [Stream]
 *
 * Scalar pre-multiplication and post-multiplication are equivalent, but separate operator functions
 *
 * @param stream the stream
 * @return a stream scaled by this
 */
operator fun Double.times(stream: Stream) = Stream { this * stream.get() }

/**
 * Scalar division operator between a [Stream] and a [scalar][Double]
 *
 * @param stream the stream
 * @return a stream that [gets][get] this divided by the original value
 */
operator fun Double.div(stream: Stream) = Stream { this / stream.get() }

/**
 * Converts a function represented by the signature `(Double) -> Double` into a [Stream]
 *
 * Equivalent to `Stream(function)`
 */
fun (() -> Double).stream(): Stream = Stream { this.invoke() }
