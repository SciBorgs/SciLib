package frc.sciborgs.scilib.filter

import edu.wpi.first.util.sendable.Sendable
import edu.wpi.first.util.sendable.SendableBuilder
import java.util.function.DoubleUnaryOperator

fun interface Filter : DoubleUnaryOperator {

  override fun applyAsDouble(operand: Double): Double = calculate(operand)

  fun calculate(input: Double): Double

  // compose

  infix fun compose(before: Filter): Filter = Filter { calculate(before.calculate(it)) }

  infix fun then(after: Filter): Filter = Filter { after.calculate(calculate(it)) }

  // operate

  fun operate(other: Filter, op: (Double, Double) -> Double): Filter = Filter {
    op(calculate(it), other.calculate(it))
  }

  operator fun plus(other: Filter) = operate(other) { a, b -> a + b }

  operator fun minus(other: Filter) = operate(other) { a, b -> a - b }

  operator fun times(other: Filter) = operate(other) { a, b -> a * b }

  operator fun div(other: Filter) = operate(other) { a, b -> a / b }

  fun cache(): CachedFilter = CachedFilter(this)
}

open class CachedFilter(private val filter: Filter) : Filter, Sendable {

  var out: Double = 0.0 // last output value
    get

  override fun calculate(input: Double): Double {
    out = filter.calculate(input)
    return out
  }

  override infix fun compose(before: Filter): Filter =
      ComposedCachedFilter(before as CachedFilter, this)

  override infix fun then(after: Filter): Filter = ComposedCachedFilter(this, after as CachedFilter)

  override fun operate(other: Filter, op: (Double, Double) -> Double): Filter =
      AggregateCachedFilter(this, other as CachedFilter, op)

  override fun initSendable(builder: SendableBuilder) =
      builder.addDoubleProperty(this.toString(), this::out, null)
}

private class ComposedCachedFilter(
    private val before: CachedFilter,
    private val after: CachedFilter,
) : CachedFilter({ after.calculate(before.calculate(it)) }) {

  override fun initSendable(builder: SendableBuilder) {
    before.initSendable(builder)
    builder.addDoubleProperty(this.toString(), this::out, null)
  }
}

private class AggregateCachedFilter(
    private val first: CachedFilter,
    private val second: CachedFilter,
    private val aggregator: (Double, Double) -> Double
) : CachedFilter({ aggregator(first.calculate(it), second.calculate(it)) }) {
  override fun initSendable(builder: SendableBuilder) {
    first.initSendable(builder)
    second.initSendable(builder)
    builder.addDoubleProperty(this.toString(), this::out, null)
  }
}
