
package pocman.matching;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import com.google.common.base.Supplier;
import com.google.common.collect.ImmutableMap;

public class Matches<T> implements Supplier<Map<T, T>>, Iterable<Entry<T, T>> {

    private final Map<T, T> matches;

    @Override
    public Map<T, T> get() {
        return this.matches;
    }

    private final Double cost;

    public Double getCost() {
        return this.cost;
    }

    public Matches(final Map<T, T> matches, final Double cost) {
        this.matches = ImmutableMap.copyOf(matches);
        this.cost = cost;
    }

    @Override
    public Iterator<Entry<T, T>> iterator() {
        return this.matches.entrySet().iterator();
    }

}