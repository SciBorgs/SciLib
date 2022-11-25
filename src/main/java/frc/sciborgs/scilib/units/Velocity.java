// package frc.sciborgs.scilib.units;

// import edu.wpi.first.math.controller.PIDController;
// import edu.wpi.first.math.controller.SimpleMotorFeedforward;
// import frc.sciborgs.scilib.config.FFConfig;
// import frc.sciborgs.scilib.config.PIDConfig;
// import frc.sciborgs.scilib.control.Controller;
// import frc.sciborgs.scilib.math.DeltaTime;
// import frc.sciborgs.scilib.math.Delta;

// public class Velocity extends Unit {
//     public static Controller<Velocity> getPID(PIDConfig pid) {
//         PIDController controller = PIDConfig.getPID(pid);
//         return (measurement, setpoint) -> controller.calculate(measurement, setpoint);
//     }

//     public static Controller<Velocity> getFF(FFConfig ff) {
//         SimpleMotorFeedforward controller = FFConfig.getFF(ff);
//         Delta dv = new Delta(0); // distance
//         DeltaTime dt = new DeltaTime(); // time
//         return (measurement, setpoint) -> controller.calculate(setpoint, dv.update(setpoint) / dt.update());
//     }
// }
