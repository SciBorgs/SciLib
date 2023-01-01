package frc.sciborgs.scilib.filter

import edu.wpi.first.math.MathUtil
import org.junit.jupiter.api.Test

class StreamTest {

  // helper class for testing
  class Inc : Stream {
    var last = 0.0
    override fun get(): Double {
      last += 1
      return last - 1
    }
  }

  @Test
  fun filtersWork() {
    val filter = Filter { it / 2.0 }
    assert(filter.calculate(4.0) == 2.0)
    assert(filter.calculate(2.0) == 1.0)

    val clamp = Filter { MathUtil.clamp(it, -1.0, 1.0) }
    assert(clamp.calculate(100.0) == 1.0)

    val one = Filter { it }
    val two = Filter { 2.0 * it }
    val three = one + two
    assert(three.calculate(1.0) == 3.0)
  }

  @Test
  fun streamsWork() {
    val two = Stream { 2.0 }
    assert(two.get() == 2.0)

    val inc = Inc()

    assert(inc.get() == 0.0)
    assert(inc.get() == 1.0)

    val one = Stream { 1.0 }
    val three = one + two
    assert(three.get() == 3.0)
  }

  // helper function for tests
  private fun getter() = 3.0

  @Test
  fun filterStream() {
    val s = Stream { 2.0 }
    val f = s.filter { it / 2.0 }
    assert(f.get() == 1.0)

    var gStream = ::getter.stream().filter { it * 10 }
    assert(gStream.get() == 30.0)

    val inc = Inc()
    // inc is already a stream but i'm streaming the stream to test if a getter can be streamed
    val incStream = inc::get.stream().filter { it * 10 }
    assert(incStream.get() == 0.0)
    assert(incStream.get() == 10.0)
    assert(incStream.get() == 20.0)

    val obj =
        object {
          val x = 11.55
        }
    val xStream = obj::x.stream()
    assert(xStream.get() == 11.55)
  }

  @Test
  fun composition() {
    val f1 = Filter { it / 2 }
    val f2 = Filter { it * 2 }
    val f3 = f1 then f2
    assert(f3.calculate(2.0) == 2.0)

    val cleanThing = Stream { 2.0 }.filter { MathUtil.clamp(it, -1.0, 1.0) }
    assert(cleanThing.get() == 1.0)
  }
}
