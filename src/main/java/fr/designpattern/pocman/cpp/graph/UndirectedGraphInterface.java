
package fr.designpattern.pocman.cpp.graph;

import java.util.List;
import java.util.Set;

public interface UndirectedGraphInterface<T> extends Iterable<T> { // TODO à revoir

    boolean addVertex(final T vertex);

    void addEdge(final T one, final T two);

    void removeEdge(final T one, final T two);

    boolean edgeExists(final T one, final T two);

    Set<T> getConnectedVerticeSet(final T vertex);

    List<WeightedEdge<T>> getEdges(final T vertex);

    boolean isEmpty();

}