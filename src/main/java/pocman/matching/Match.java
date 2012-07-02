
package pocman.matching;

import java.util.Map;

public class Match<T> {

    private final Map<T, T> matches;

    public Map<T, T> getMatches() {
        return this.matches; // TODO
    }

    private final Double cost;

    public Double getCost() {
        return this.cost;
    }

    public Match(final Map<T, T> matches, final Double cost) {
        this.matches = matches;
        this.cost = cost;
    }

}