
package fr.designpattern.pocman.cpp.graph;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;

/**
 * Ripped from Keith Schwarz (htiek@cs.stanford.edu)
 * http://www.keithschwarz.com/interesting/code/?dir=edmonds-matching
 */
public final class MutableUndirectedGraph<T> implements UndirectedGraphInterface<T> {

    private final Map<T, Set<T>> mGraph = new HashMap<T, Set<T>>();

    @Override
    public boolean addVertex(final T node) {
        if (this.mGraph.containsKey(node))
            return false;
        this.mGraph.put(node, new HashSet<T>());
        return true;
    }

    @Override
    public void addEdge(final T one, final T two) {
        if (!this.mGraph.containsKey(one) || !this.mGraph.containsKey(two))
            throw new NoSuchElementException("Both nodes must be in the graph.");
        this.mGraph.get(one).add(two);
        this.mGraph.get(two).add(one);
    }

    @Override
    public void removeEdge(final T one, final T two) {
        if (!this.mGraph.containsKey(one) || !this.mGraph.containsKey(two))
            throw new NoSuchElementException("Both nodes must be in the graph.");
        this.mGraph.get(one).remove(two);
        this.mGraph.get(two).remove(one);
    }

    @Override
    public boolean edgeExists(final T one, final T two) {
        if (!this.mGraph.containsKey(one) || !this.mGraph.containsKey(two))
            throw new NoSuchElementException("Both nodes must be in the graph.");
        return this.mGraph.get(one).contains(two);
    }

    @Override
    public Set<T> edgesFrom(final T node) {
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
    public boolean isEmpty() {
        return this.mGraph.isEmpty();
    }

    @Override
    public List<WeightedEdge<T>> getEdges(final T vertex) {
        return null; // TODO
    }

}