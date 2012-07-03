
package pocman.matching.edmonds1;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;

/**
 * Ripped from Keith Schwarz (htiek@cs.stanford.edu)
 * http://www.keithschwarz.com/interesting/code/?dir=edmonds-matching
 */
public final class MutableUndirectedGraph<T> implements Iterable<T> {

    private final Map<T, Set<T>> mGraph = new HashMap<T, Set<T>>();

    public boolean isEmpty() {
        return this.mGraph.isEmpty();
    }

    public boolean addEndPoint(final T node) {
        if (this.mGraph.containsKey(node))
            return false;
        this.mGraph.put(node, new HashSet<T>());
        return true;
    }

    public void addEdge(final T one, final T two) {
        if (!this.mGraph.containsKey(one) || !this.mGraph.containsKey(two))
            throw new NoSuchElementException("Both nodes must be in the graph.");
        this.mGraph.get(one).add(two);
        this.mGraph.get(two).add(one);
    }

    public void removeEdge(final T one, final T two) {
        if (!this.mGraph.containsKey(one) || !this.mGraph.containsKey(two))
            throw new NoSuchElementException("Both nodes must be in the graph.");
        this.mGraph.get(one).remove(two);
        this.mGraph.get(two).remove(one);
    }

    public boolean contains(final T one, final T two) {
        if (!this.mGraph.containsKey(one) || !this.mGraph.containsKey(two))
            throw new NoSuchElementException("Both nodes must be in the graph.");
        return this.mGraph.get(one).contains(two);
    }

    public Set<T> getEndPoints(final T node) {
        final Set<T> arcs = this.mGraph.get(node);
        if (arcs == null)
            throw new NoSuchElementException("Source node does not exist.");
        return Collections.unmodifiableSet(arcs);
    }

    @Override
    public Iterator<T> iterator() {
        return this.mGraph.keySet().iterator();
    }

    @Override
    public int hashCode() {
        return this.mGraph.hashCode();
    }

    @Override
    public boolean equals(final Object object) {
        if (object == null) return false;
        if (object == this) return true;
        if (!(object instanceof MutableUndirectedGraph)) return false;
        final MutableUndirectedGraph<?> that = (MutableUndirectedGraph<?>) object;
        return that.mGraph.equals(this.mGraph);
    }

}