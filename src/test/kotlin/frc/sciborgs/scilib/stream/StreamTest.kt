package frc.sciborgs.scilib.stream

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

  // helper method for testing
  fun getter() = 3.0

  // note that the calculus streams are difficult to test due to them using wpilib's timer

  @Test
  fun mapTest() {
    val s = Stream { 2.0 }
    val f = s.map { it / 2.0 }
    assert(f.get() == 1.0)

    var gStream = ::getter.stream().map { it * 10 }
    assert(gStream.get() == 30.0)

    val inc = Inc()
    // inc is already a stream but i'm streaming the stream to test if a getter can be streamed
    val incStream = inc::get.stream().map { it * 10 }
    assert(incStream.get() == 0.0)
    assert(incStream.get() == 10.0)
    assert(incStream.get() == 20.0)

    val obj =
        object {
          val x = 11.55
        }
    val xStream = obj::x.stream()
    assert(xStream.get() == 11.55)

    val cleanThing = Stream { 2.0 }.map { MathUtil.clamp(it, -1.0, 1.0) }
    assert(cleanThing.get() == 1.0)
  }

  @Test
  fun operationTest() {
    val one = Stream { 1.0 }

    val two = one + one
    assert(two.get() == 2.0)

    val three = one + two
    assert(three.get() == 3.0)

    assert((three - one).get() == 2.0)

    assert((three / 2.0).get() == 1.5)

    assert((10.0 * two).get() == 20.0)
  }

  @Test
  fun SendableStreamTest() {
    // SendableStreams should be further tested in robot sim
    val x = Inc().log().map { it * 2 } * 2.0
    assert(x is SendableStream)
  }
}
