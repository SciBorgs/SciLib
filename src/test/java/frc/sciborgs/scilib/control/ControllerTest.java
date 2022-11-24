package frc.sciborgs.scilib.control;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ControllerTest {
    @Test void operationTest() {
        Controller base = (x, y) -> x - y;
        Controller addition = base.add((x, y) -> x * y);
        Controller other = (x, y) -> x - y + x * y;
        assertEquals(addition.applyAsDouble(0.01, 3), other.applyAsDouble(0.01, 3));
        Controller subtraction = base.sub((x, y) -> x * y);
        other = (x, y) -> x - y - x * y;
        assertEquals(subtraction.applyAsDouble(0.01, 3), other.applyAsDouble(0.01, 3));
    }
}
