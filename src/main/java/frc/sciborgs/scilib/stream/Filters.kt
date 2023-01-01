package frc.sciborgs.scilib.stream

import edu.wpi.first.math.filter.LinearFilter

// Filters.kt provides concise interop with WPILib's LinearFilter class

fun Stream.map(filter: LinearFilter) = this.map { filter.calculate(it) }

// Reimplimenting LinearFilter static methods

fun Stream.singlePoleIIR(timeConstant: Double, period: Double) =
    this.map(LinearFilter.singlePoleIIR(timeConstant, period))

fun Stream.highPass(timeConstant: Double, period: Double) =
    this.map(LinearFilter.highPass(timeConstant, period))

fun Stream.movingAverage(taps: Int) = this.map(LinearFilter.movingAverage(taps))

fun Stream.finiteDifference(derivative: Int, samples: Int, stencil: IntArray, period: Double) =
    this.map(LinearFilter.finiteDifference(derivative, samples, stencil, period))

fun Stream.backwardsFiniteDifference(derivative: Int, samples: Int, period: Double) =
    this.map(LinearFilter.backwardFiniteDifference(derivative, samples, period))
