
package fr.designpattern.pocman.cpp.graph.algo;

import java.util.List;
import java.util.Map;

import fr.designpattern.pocman.cpp.graph.UndirectedGraph;
import fr.designpattern.pocman.cpp.graph.Vertex;
import fr.designpattern.pocman.cpp.graph.WeightedEdge;

public final class EulerianTrail {

    /**
     * Fleury algorithm
     */
    public static void apply(final Vertex startingVertex, final UndirectedGraph<Vertex> eulerianGraph, final Map<WeightedEdge<Vertex>, Integer> map,
            final List<Vertex> trail) {
        //TODO ! vérifier que le graphe est eulerien
        final List<WeightedEdge<Vertex>> edges = eulerianGraph.getEdges(startingVertex);
        for (final WeightedEdge<Vertex> edge : edges) {
            final Integer integer = map.get(edge);
            if (integer < 1) continue;
            map.put(edge, integer - 1);
            Vertex nextVertex;
            if (edge.getEndPoint1().equals(startingVertex)) nextVertex = edge.getEndPoint2();
            else if (edge.getEndPoint2().equals(startingVertex)) nextVertex = edge.getEndPoint1();
            else throw new RuntimeException(edge.toString());

            apply(nextVertex, eulerianGraph, map, trail);
        }
        trail.add(startingVertex);
    }

    private EulerianTrail() {}

}