
package old;

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
public final class UndirectedGraph<T> implements Iterable<T> {

    private final Map<T, Set<T>> mGraph = new HashMap<T, Set<T>>();

    public boolean addNode(final T node) {
        if (this.mGraph.containsKey(node)) return false;
        this.mGraph.put(node, new HashSet<T>());
        return true;
    }

    public boolean nodeExists(final T node) {
        return this.mGraph.containsKey(node);
    }

    public void addEdge(final T one, final T two) {
        if (!this.mGraph.containsKey(one) || !this.mGraph.containsKey(two)) throw new NoSuchElementException("Both nodes must be in the graph.");
        this.mGraph.get(one).add(two);
        this.mGraph.get(two).add(one);
    }

    public void removeEdge(final T one, final T two) {
        if (!this.mGraph.containsKey(one) || !this.mGraph.containsKey(two)) throw new NoSuchElementException("Both nodes must be in the graph.");
        this.mGraph.get(one).remove(two);
        this.mGraph.get(two).remove(one);
    }

    public boolean edgeExists(final T one, final T two) {
        if (!this.mGraph.containsKey(one) || !this.mGraph.containsKey(two)) throw new NoSuchElementException("Both nodes must be in the graph.");
        return this.mGraph.get(one).contains(two);
    }

    public Set<T> edgesFrom(final T node) {
        final Set<T> arcs = this.mGraph.get(node);
        if (arcs == null) throw new NoSuchElementException("Source node does not exist.");
        return Collections.unmodifiableSet(arcs);
    }

    public boolean containsNode(final T node) {
        return this.mGraph.containsKey(node);
    }

    @Override
    public Iterator<T> iterator() {
        return this.mGraph.keySet().iterator();
    }

    public int size() {
        return this.mGraph.size();
    }

    public boolean isEmpty() {
        return this.mGraph.isEmpty();
    }

}