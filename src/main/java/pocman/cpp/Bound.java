
package pocman.cpp;

import com.google.common.base.Preconditions;

public class Bound {

    private final double lowerBound;
    private final double upperBound;

    public Bound(final double lowerBound, final double upperBound) {
        Preconditions.checkState(Double.compare(lowerBound, upperBound) < 1);
        this.lowerBound = lowerBound;
        this.upperBound = upperBound;
    }

    public double getLowerBound() {
        return this.lowerBound;
    }

    public double getUpperBound() {
        return this.upperBound;
    }

}