package frc.sciborgs.scilib.filter

fun (() -> Double).stream(): Stream = Stream { this.invoke() }
