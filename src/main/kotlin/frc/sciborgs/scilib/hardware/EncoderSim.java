package frc.sciborgs.scilib.hardware;

import edu.wpi.first.hal.SimDouble;
import edu.wpi.first.hal.simulation.SimDeviceDataJNI.SimValueInfo;
import edu.wpi.first.wpilibj.simulation.SimDeviceSim;

/**
 * Sim class for CAN Encoders that uses JNI to interact with real Encoder instances. Thank you 6738!
 */
public class EncoderSim {
  private final SimDouble position, velocity, appliedOutput;

  /** Utility function to print the JNI values of the device */
  public static void printValues(SimDeviceSim device) {
    for (SimValueInfo val : device.enumerateValues()) {
      System.out.println("name: " + val.name + " handle: " + val.handle);
    }
  }

  public EncoderSim(int deviceID) {
    SimDeviceSim device = new SimDeviceSim("SPARK MAX [" + deviceID + "]");
    position = device.getDouble("Position");
    velocity = device.getDouble("Velocity");
    appliedOutput = device.getDouble("Applied Output");
  }

  public void setVelocity(double v) {
    velocity.set(v);
  }

  public double getVelocity() {
    return velocity.get();
  }

  public void setPosition(double pos) {
    position.set(pos);
  }

  public double getPosition() {
    return position.get();
  }

  public double getAppliedOutput() {
    return appliedOutput.get();
  }
}
