package frc.sciborgs.scilib.filter

import edu.wpi.first.math.filter.LinearFilter
import edu.wpi.first.util.sendable.Sendable
import edu.wpi.first.util.sendable.SendableBuilder

open class Filter(private val f: (Double) -> Double) : Sendable {

  companion object {
    fun identity() = Filter { x -> x }

    fun linearFilter(filter: LinearFilter) = Filter { filter.calculate(it) }
  }

  var last = 0.0

  fun calculate(value: Double): Double {
    last = f(value)
    return last
  }

  fun compose(before: Filter): Filter = ComposedFilter(before, this)

  fun andThen(after: Filter): Filter = ComposedFilter(this, after)

  fun aggregate(other: Filter, aggregator: (Double, Double) -> Double): Filter =
      AggregateFilter(this, other, aggregator)

  // shorthand for conversion and then composing

  fun compose(before: (Double) -> Double): Filter = compose(Filter(before))

  fun andThen(after: (Double) -> Double): Filter = andThen(Filter(after))

  fun aggregate(other: (Double) -> Double, aggregator: (Double, Double) -> Double): Filter =
      aggregate(other, aggregator)

  override fun initSendable(builder: SendableBuilder) {
    builder.addDoubleProperty(this.toString(), this::last, null)
  }
}

class ComposedFilter(private val before: Filter, private val after: Filter) :
    Filter({ x -> after.calculate(before.calculate(x)) }) {

  override fun initSendable(builder: SendableBuilder) {
    before.initSendable(builder)
    builder.addDoubleProperty(this.toString(), this::last, null)
  }
}

class AggregateFilter(
    private val first: Filter,
    private val second: Filter,
    private val aggregator: (Double, Double) -> Double
) : Filter({ x -> aggregator(first.calculate(x), second.calculate(x)) }) {
  override fun initSendable(builder: SendableBuilder) {
    first.initSendable(builder)
    second.initSendable(builder)
    builder.addDoubleProperty(this.toString(), this::last, null)
  }
}
