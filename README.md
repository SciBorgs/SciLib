# SciLib

[![CI](https://github.com/SciBorgs/SciLib/actions/workflows/main.yml/badge.svg)](https://github.com/SciBorgs/SciLib/actions/workflows/main.yml)

SciLib is a library by FRC team 1155 intended for use with [WPILibJ](https://github.com/wpilibsuite/allwpilib). It is designed to standardize and simplify some tedius aspects of writing robot code.

*It is currently under development.*

## Features

SciLib deals with problems in the simplest way possible, so instead of wrapper classes, it uses functional interfaces and factories.

## Example
```java
// Constants.java
public static final PIDConfig flywheelFB = new PIDConfig(0.003, 0.001, 0);
public static final FFConfig flywheelFF = new FFConfig(0.13419, 0.0017823, 0.00028074);

// Shooter.java
private final Controller flywheelController = PIDConfig.getPID(flywheelFB)
        .add(FFConfig.getFF(flywheelFF))
        // don't actually filter the measurement of a flywheel, this will cause it to ramp slowly
        .filterMeasurement(Filter.fromLinearFilter(LinearFilter.movingAverage(10)));
```
