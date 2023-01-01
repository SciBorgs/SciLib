# SciLib

[![CI](https://github.com/SciBorgs/SciLib/actions/workflows/main.yml/badge.svg)](https://github.com/SciBorgs/SciLib/actions/workflows/main.yml)

SciLib is a library by FRC team 1155 intended for use with [WPILibJ](https://github.com/wpilibsuite/allwpilib). It is designed to standardize and simplify some tedious aspects of writing robot code.

*It is currently under development.*

## Project Scope

italicized features may not be added

General

- [x] General motor builders
- [ ] Stream api
- [ ] *Vision wrappers*

Kotlin specific

- [ ] Units
- [ ] *Coroutines in command based*

## Motor Config Example

```kotlin
// Constants.kt
object Ports {
    const val flywheelLeft = 20
    const val flywheelRight = 21
}

object Motors {
    val shooter = MotorConfig(currentLimit = 20)
}

// Shooter.kt
class Shooter : SubsystemBase() {

    val lead = Motors.shooter.buildCanSparkMax(Ports.flywheelLeft, MotorType.kBrushless)
    val follow = Motors.shooter.buildCanSparkMax(Ports.flywheelRight, MotorType.kBrushless)
        // ...
    init {
        follow.follow(lead, true)
    }
        // ...
}
```
