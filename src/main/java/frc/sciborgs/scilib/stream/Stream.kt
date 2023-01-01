package frc.sciborgs.scilib.stream

import java.util.function.DoubleSupplier

/** A Stream functional interface */
fun interface Stream : DoubleSupplier {

  override fun getAsDouble() = get()

  fun get(): Double

  operator fun plus(other: Stream) = Stream { get() + other.get() }

  operator fun minus(other: Stream) = Stream { get() - other.get() }

  operator fun times(scalar: Double) = Stream { get() * scalar }

  operator fun div(scalar: Double) = Stream { get() / scalar }
  fun map(mapper: (Double) -> Double): Stream = Stream { mapper(get()) }
}

operator fun Double.times(stream: Stream) = Stream { this * stream.get() }

operator fun Double.div(stream: Stream) = Stream { this / stream.get() }

fun (() -> Double).stream(): Stream = Stream { this.invoke() }
