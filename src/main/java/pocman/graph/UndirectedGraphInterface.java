
package pocman.graph;

import java.util.Set;

public interface UndirectedGraphInterface<T> extends Iterable<T> {

    int getOrder();

    boolean hasEndPoint(final T endPoint);

    boolean hasEdge(final T endPoint1, final T endPoint2);

    Set<T> getConnectedEndPoints(final T endPoint);

    Set<WeightedEdge<T>> getEdgesFrom(final T endPoint);

    Set<T> getSetOfEndPoints();

    Set<WeightedEdge<T>> getSetOfEdges();

    WeightedEdge<T> getEdge(final T endPoint1, final T endPoint2);

}