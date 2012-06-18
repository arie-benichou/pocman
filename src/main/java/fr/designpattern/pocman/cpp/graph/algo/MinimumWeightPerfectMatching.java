
package fr.designpattern.pocman.cpp.graph.algo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

import fr.designpattern.pocman.cpp.graph.MutableUndirectedGraph;
import fr.designpattern.pocman.cpp.graph.Path;
import fr.designpattern.pocman.cpp.graph.UndirectedGraph;
import fr.designpattern.pocman.cpp.graph.Vertex;
import fr.designpattern.pocman.cpp.graph.WeightedEdge;

public class MinimumWeightPerfectMatching {

    private MinimumWeightPerfectMatching() {}

    private static <T> List<T> oddVertices(final UndirectedGraph<T> graph) {
        final List<T> oddVertices = Lists.newArrayList();
        for (final T vertex : graph)
            if (graph.edgesFrom(vertex).size() % 2 == 1) oddVertices.add(vertex);
        return oddVertices;
    }

    private static <T> MutableUndirectedGraph<T> buildResidualGraph(final UndirectedGraph<T> graph, final List<T> oddVertices) {
        final MutableUndirectedGraph<T> residualGraph = new MutableUndirectedGraph<T>();
        for (final T vertice : oddVertices)
            residualGraph.addVertex(vertice);
        for (final T endPoint1 : oddVertices) {
            for (final T endPoint2 : oddVertices) {
                if (!endPoint1.equals(endPoint2)) {
                    final Path<T> shortestPath = graph.shortestPathBetween(endPoint1, endPoint2);
                    residualGraph.addEdge(shortestPath.getEndPoint1(), shortestPath.getEndPoint2());
                }
            }
        }
        return residualGraph;
    }

    private static <T> boolean isPerfect(final MutableUndirectedGraph<T> maximumMatching) {
        for (final T vertex : maximumMatching)
            if (maximumMatching.edgesFrom(vertex).size() != 1) return false;
        return true;
    }

    private static <T> HashMap<T, T> buildMatchingMap(final MutableUndirectedGraph<T> maximumMatching) {
        final HashMap<T, T> matching = Maps.newHashMap();
        final Set<T> set = Sets.newHashSet(); // TODO ? ou bien ajouter 0.5 deux fois
        for (final T endPoint1 : maximumMatching) {
            final Set<T> endPoints = maximumMatching.edgesFrom(endPoint1);
            if (endPoints.size() == 1) {
                final T endPoint2 = endPoints.iterator().next();
                if (!set.contains(endPoint2)) {
                    set.add(endPoint1);
                    set.add(endPoint2);
                    matching.put(endPoint1, endPoint2);
                }
            }
        }
        return matching;
    }

    private static <T> Map<WeightedEdge<T>, Integer> eulerize(final UndirectedGraph<T> originalGraph, final Map<T, T> matching) {
        final Set<WeightedEdge<T>> edges = Sets.newHashSet();
        for (final T vertex : originalGraph)
            edges.addAll(originalGraph.getEdges(vertex));
        final Map<WeightedEdge<T>, Integer> map = Maps.newHashMap();
        for (final WeightedEdge<T> edge : edges)
            map.put(edge, 1);
        for (final Entry<T, T> entry : matching.entrySet()) {
            final T endPoint1 = entry.getKey();
            final T endPoint2 = entry.getValue();
            final Path<T> path = originalGraph.shortestPathBetween(endPoint1, endPoint2);
            for (final WeightedEdge<T> edge : path.getEdges())
                map.put(edge, (map.get(edge) + 1) % 2 == 0 ? 2 : 1);
        }
        return map;
    }

    private static <T> double computeCost(final Map<WeightedEdge<T>, Integer> edgeInstances) {
        double cost = 0;
        for (final Entry<WeightedEdge<T>, Integer> entry : edgeInstances.entrySet()) {
            final WeightedEdge<T> edge = entry.getKey();
            final Integer k = entry.getValue();
            cost += k * edge.getWeight();
        }
        return cost;
    }

    private static <T> Map<WeightedEdge<T>, Integer> computeOptimalEulerization(final UndirectedGraph<T> originalGraph, final List<T> oddVertices,
            HashMap<T, T> matching)
            throws Exception {
        if (oddVertices.size() % 2 != 0) throw new RuntimeException("Number of odd vertices should be even.");
        final MutableUndirectedGraph<T> residualGraph = buildResidualGraph(originalGraph, oddVertices);
        MutableUndirectedGraph<T> maximumMatching = EdmondsMatching.maximumMatching(residualGraph);
        HashMap<T, T> bestPerfectMatching = null;
        if (isPerfect(maximumMatching)) {
            double bestPerfectMatchingWeight = Double.POSITIVE_INFINITY;
            do {
                matching = buildMatchingMap(maximumMatching);
                final Map<WeightedEdge<T>, Integer> eulerized = eulerize(originalGraph, matching);
                final double cost = computeCost(eulerized);
                if (cost < bestPerfectMatchingWeight) {
                    bestPerfectMatchingWeight = cost;
                    bestPerfectMatching = Maps.newHashMap(matching);
                }
                for (final Entry<T, T> entry : matching.entrySet())
                    residualGraph.removeEdge(entry.getKey(), entry.getValue());
                maximumMatching = EdmondsMatching.maximumMatching(residualGraph);
            }
            while (isPerfect(maximumMatching));
        }
        return eulerize(originalGraph, bestPerfectMatching);
    }

    public static Map<WeightedEdge<Vertex>, Integer> computeOptimalEulerization(final UndirectedGraph<Vertex> originalGraph) throws Exception {
        final List<Vertex> oddVertices = oddVertices(originalGraph);
        final HashMap<Vertex, Vertex> matching = Maps.newHashMap();
        return computeOptimalEulerization(originalGraph, oddVertices, matching);
    }

}