@file:JvmName("Logging")

package frc.sciborgs.scilib.stream

import edu.wpi.first.util.sendable.Sendable
import edu.wpi.first.util.sendable.SendableBuilder

/**
 * A class implementing [Stream] and [Sendable], allowing for the logging of composed streams
 *
 * This implementation overrides all default methods of [Stream] to return appropriate
 * [SendableStream] instances. It internally uses [CompositeStream] and [AggregateStream] to
 * appropriately override [initSendable].
 *
 * @constructor creates a new [SendableStream] from the provided [Stream]
 * @see Stream
 * @see Stream.log
 * @see CompositeStream
 * @see AggregateStream
 * @author Asa Paparo
 */
open class SendableStream(private val stream: Stream) : Stream, Sendable {

  /** Last output */
  var out = 0.0

  override fun get(): Double {
    out = stream.get()
    return out
  }

  override fun map(mapper: (Double) -> Double): SendableStream = CompositeStream(this, mapper)

  override operator fun plus(other: Stream): SendableStream =
      AggregateStream(this, other as SendableStream)

  override operator fun minus(other: Stream): SendableStream =
      AggregateStream(this, (-1.0 * other) as SendableStream)

  override operator fun times(scalar: Double): SendableStream =
      CompositeStream(this) { it * scalar }

  override operator fun div(scalar: Double): SendableStream = CompositeStream(this) { it / scalar }

  override fun initSendable(builder: SendableBuilder?) {
    builder?.addDoubleProperty(javaClass.simpleName, ::out, null)
  }
}

/**
 * A class that extends [SendableStream] for internal use
 *
 * It overrides [initSendable] to add [stream] and the mapped result
 *
 * @constructor creates an instance a [mapper function] and a [SendableStream]
 * @see SendableStream
 * @see SendableStream.times
 * @see SendableStream.div
 */
private class CompositeStream(
    private val stream: SendableStream,
    private val mapper: (Double) -> Double,
) : SendableStream({ mapper(stream.get()) }) {

  override fun initSendable(builder: SendableBuilder?) {
    stream.initSendable(builder)
    builder?.addDoubleProperty(javaClass.simpleName, ::out, null)
  }
}

/**
 * A class that extends [SendableStream] for internal use
 *
 * It overrides [initSendable] to add [first], [second], and their sum
 *
 * @constructor creates an instance from the sum of two SendableStreams
 * @see SendableStream
 * @see SendableStream.plus
 * @see SendableStream.minus
 */
private class AggregateStream(
    private val first: SendableStream,
    private val second: SendableStream,
) : SendableStream({ first.get() + second.get() }) {

  override fun initSendable(builder: SendableBuilder?) {
    first.initSendable(builder)
    second.initSendable(builder)
    builder?.addDoubleProperty(javaClass.simpleName, ::out, null)
  }
}

/**
 * Converts a [Stream] into a [SendableStream]
 *
 * Equivalent to `SendableStream(stream)`
 */
fun Stream.log() = SendableStream(this)

/**
 * Scalar pre-multiplication operator between a [scalar][Double] and a [SendableStream]
 *
 * Scalar pre-multiplication and post-multiplication are equivalent, but separate operator functions
 *
 * @param stream the [SendableStream]
 * @return a [SendableStream] scaled by this
 */
operator fun Double.times(stream: SendableStream): SendableStream =
    CompositeStream(stream) { this * it }

/**
 * Scalar division operator between a [SendableStream] and a [scalar][Double]
 *
 * @param stream the [SendableStream]
 * @return a [SendableStream] that [gets][get] this divided by the original value
 */
operator fun Double.div(stream: SendableStream): SendableStream =
    CompositeStream(stream) { this / it }
