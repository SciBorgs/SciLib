package frc.sciborgs.scilib.filter

import java.util.function.DoubleSupplier

/** A Stream functional interface */
fun interface Stream : DoubleSupplier {

  override fun getAsDouble() = get()

  fun get(): Double

  fun operate(other: Stream, op: (Double, Double) -> Double): Stream = Stream {
    op(get(), other.get())
  }

  operator fun plus(other: Stream) = operate(other) { a, b -> a + b }

  operator fun minus(other: Stream) = operate(other) { a, b -> a - b }

  operator fun times(other: Stream) = operate(other) { a, b -> a * b }

  operator fun div(other: Stream) = operate(other) { a, b -> a / b }

  fun map(mapper: (Double) -> Double = { it }): Stream = Stream { mapper(get()) }
}
