package frc.sciborgs.scilib.filter

import java.util.function.DoubleSupplier

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

  fun filter(filter: Filter = Filter { it }): FilteredStream = FilteredStream(this, filter)
}

class FilteredStream(private val stream: Stream, private val filter: Filter) : Stream {

  override fun get(): Double = filter.calculate(stream.get())
}
