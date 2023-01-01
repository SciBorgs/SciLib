package frc.sciborgs.scilib.stream

import edu.wpi.first.util.sendable.Sendable
import edu.wpi.first.util.sendable.SendableBuilder

open class SendableStream(private val stream: Stream) : Stream, Sendable {

  var out = 0.0 // last output

  override fun get(): Double {
    out = stream.get()
    return out
  }

  override fun map(mapper: (Double) -> Double): SendableStream = MappedStream(this, mapper)

  override operator fun plus(other: Stream): SendableStream =
      AggregatedStream(this, other as SendableStream)

  override operator fun minus(other: Stream): SendableStream =
      AggregatedStream(this, (-1.0 * other) as SendableStream)

  override operator fun times(scalar: Double): SendableStream = MappedStream(this) { it * scalar }

  override operator fun div(scalar: Double): SendableStream = MappedStream(this) { it / scalar }

  override fun initSendable(builder: SendableBuilder?) {
    builder?.addDoubleProperty(javaClass.simpleName, ::out, null)
  }
}

private class MappedStream(
    private val stream: SendableStream,
    private val mapper: (Double) -> Double,
) : SendableStream({ mapper(stream.get()) }) {

  override fun initSendable(builder: SendableBuilder?) {
    stream.initSendable(builder)
    builder?.addDoubleProperty(javaClass.simpleName, ::out, null)
  }
}

private class AggregatedStream(
    private val first: SendableStream,
    private val second: SendableStream,
) : SendableStream({ first.get() + second.get() }) {

  override fun initSendable(builder: SendableBuilder?) {
    first.initSendable(builder)
    second.initSendable(builder)
    builder?.addDoubleProperty(javaClass.simpleName, ::out, null)
  }
}

fun Stream.log() = SendableStream(this)

operator fun Double.times(stream: SendableStream): SendableStream =
    MappedStream(stream) { this * it }

operator fun Double.div(stream: SendableStream): SendableStream = MappedStream(stream) { this / it }
