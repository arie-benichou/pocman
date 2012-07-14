
package graph.features.shortestPath;

import graph.WeightedEdge;

import java.util.List;

public interface PathInterface<T> {

    T getEndPoint1();

    T getEndPoint2();

    double getWeight();

    int getNumberOfEdges();

    List<WeightedEdge<T>> getEdges();

    WeightedEdge<T> getLastEdge();

    PathInterface<T> reverse();

    PathInterface<T> add(WeightedEdge<T> edge);

    PathInterface<T> add(PathInterface<T> path);

}