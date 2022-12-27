@file:JvmName("Config")

package frc.sciborgs.scilib.config

import edu.wpi.first.math.controller.PIDController

data class PIDConstants(val kp: Double = 0.0, val ki: Double = 0.0, val kd: Double = 0.0)

data class FFConstants(val ks: Double = 0.0, val kv: Double = 0.0, val ka: Double = 0.0)

fun createPID(constants: PIDConstants): PIDController =
    PIDController(constants.kp, constants.ki, constants.kd)
