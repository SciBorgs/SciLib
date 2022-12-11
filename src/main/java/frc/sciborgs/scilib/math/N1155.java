package frc.sciborgs.scilib.math;

import edu.wpi.first.math.Nat;
import edu.wpi.first.math.Num;

/**
 * This class exists as a joke
 * ... unless you want a matrix with dimensions of 1155
 */
public final class N1155 extends Num implements Nat<N1155> {

    private N1155() {}

    @Override
    public int getNum() {
        return 1155;
    }

    public static final N1155 instance = new N1155();
    
}
