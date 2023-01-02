@file:JvmName("Filters")

package frc.sciborgs.scilib.stream

import edu.wpi.first.math.filter.LinearFilter

// Filters.kt provides concise interop with WPILib's LinearFilter class

/**
 * Maps a [Stream] by a [LinearFilter]
 *
 * Equivalent to `stream.map { linearFilter.calculate(it) }`
 */
fun Stream.map(filter: LinearFilter) = this.map { filter.calculate(it) }

// Reimplementing LinearFilter static methods

/** @see LinearFilter.singlePoleIIR */
fun Stream.singlePoleIIR(timeConstant: Double, period: Double) =
    this.map(LinearFilter.singlePoleIIR(timeConstant, period))

/** @see LinearFilter.highPass */
fun Stream.highPass(timeConstant: Double, period: Double) =
    this.map(LinearFilter.highPass(timeConstant, period))

/** @see LinearFilter.movingAverage */
fun Stream.movingAverage(taps: Int) = this.map(LinearFilter.movingAverage(taps))

/** @see LinearFilter.finiteDifference */
fun Stream.finiteDifference(derivative: Int, samples: Int, stencil: IntArray, period: Double) =
    this.map(LinearFilter.finiteDifference(derivative, samples, stencil, period))

/** @see LinearFilter.backwardFiniteDifference */
fun Stream.backwardsFiniteDifference(derivative: Int, samples: Int, period: Double) =
    this.map(LinearFilter.backwardFiniteDifference(derivative, samples, period))
