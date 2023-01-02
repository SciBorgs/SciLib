# Config

## Features

`MotorConfig` is a data class to standardize the configuration of motor controller objects.
It provides builder methods for CANSparkMax, TalonSRX, and TalonFX motors.

Example creation of a WPILib drivetrain using MotorConfig (provided in the documentation)

```kotlin
val leftMotor = MotorConfig(neutralBehavior = NeutralBehavior.BRAKE, currentLimit = 80)
val rightMotor = leftMotor.copy(inverted = true)

val leftPorts = intArrayOf(1, 2, 3)
val rightPorts = intArrayOf(4, 5, 6)

val leftGroup = MotorControllerGroup(leftMotor.buildCanSparkMax(*leftPorts, motorType = MotorType.kBrushless))
val rightGroup = MotorControllerGroup(rightMotor.buildCanSparkMax(*rightPorts, motorType = MotorType.kBrushless))
val driveTrain = DifferentialDrive(leftGroup, rightGroup)
```
