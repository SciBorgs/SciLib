package frc.sciborgs.scilib.math;

import frc.sciborgs.scilib.filter.Filter;

/** Derivative filter with respect to system time */
public class Derivative implements Filter {

  private final ElapsedTime dt;

  private double last;
  private double delta;

  public Derivative() {
    this(0);
  }

  public Derivative(double initialValue) {
    dt = new ElapsedTime();
    last = initialValue;
    delta = 0;
  }

  @Override
  public double calculate(double value) {
    delta = value - last;
    last = value;
    return delta / dt.reset();
  }

  public double get() {
    return delta;
  }
}
