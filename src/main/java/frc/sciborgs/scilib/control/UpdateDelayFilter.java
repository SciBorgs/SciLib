package frc.sciborgs.scilib.control;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Objects;

/**
 * Delays the reporting of values by a set number of updates. This function is
 * internally achieved by feeding provided values into one end of a
 * {@code Deque<Double>} and returning the value at the other end.
 * <p>
 * When the size of the {@code Deque<Double>} is less than the specified delay,
 * the first value passed to this filter is returned.
 */
public class UpdateDelayFilter implements Filter {
    public Deque<Double> queue;
    private int delay;

    /**
     * Creates an UpdateDelayFilter with the specified delay.
     * 
     * @param delay number of updates to delay values by
     */
    public UpdateDelayFilter(int delay) {
        this.delay = delay;
        queue = new ArrayDeque<>(delay + 1);
    }

    /**
     * Adds the specified value to the internal deque and returns the value at
     * the other end.
     * 
     * @param value value to update this filter with
     * @return the value at the other end of the deque
     */
    @Override
    public double calculate(double value) {
        double old = (queue.size() == delay + 1) ? queue.pop() : Objects.requireNonNullElse(queue.peek(), value);
        queue.addLast(value);
        return old;
    }

    public int getNumSamples() {
        return queue.size();
    }
}
