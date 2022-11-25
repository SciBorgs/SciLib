// package frc.sciborgs.scilib.units;

// import edu.wpi.first.math.controller.PIDController;
// import frc.sciborgs.scilib.config.PIDConfig;
// import frc.sciborgs.scilib.control.Controller;

// public class Angle extends Unit {
//     public static Controller<Angle> getPID(PIDConfig pid) {
//         PIDController controller = PIDConfig.getPID(pid);
//         return new Controller<Angle>((measurement, setpoint) -> controller.calculate(measurement, setpoint));
//     }

//     public static Controller<Angle> getPID(PIDConfig pid, double minInput, double maxInput) {
//         PIDController controller = PIDConfig.getPID(pid);
//         controller.enableContinuousInput(minInput, maxInput);
//         return new Controller<Angle>((measurement, setpoint) -> controller.calculate(measurement, setpoint));
//     }
// }
