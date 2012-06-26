/*
 * Copyright 2012 Arie Benichou
 * 
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * this program. If not, see <http://www.gnu.org/licenses/>.
 */

package fr.designpattern.pocman.cpp;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

import fr.designpattern.pocman.graph.Path;
import fr.designpattern.pocman.graph.UndirectedGraph;
import fr.designpattern.pocman.graph.WeightedEdge;

public class MinimumWeightPerfectMatching {

    private MinimumWeightPerfectMatching() {}

    private static <T> List<T> oddVertices(final UndirectedGraph<T> graph) {
        final List<T> oddVertices = Lists.newArrayList();
        for (final T vertex : graph)
            if (graph.getConnectedVerticeSet(vertex).size() % 2 == 1) oddVertices.add(vertex);
        return oddVertices;
    }

    private static <T> MutableUndirectedGraph<T> buildResidualGraph(final UndirectedGraph<T> graph, final List<T> oddVertices) {
        final MutableUndirectedGraph<T> residualGraph = new MutableUndirectedGraph<T>();
        for (final T vertice : oddVertices)
            residualGraph.addVertex(vertice);
        for (final T endPoint1 : oddVertices) {
            for (final T endPoint2 : oddVertices) {
                if (!endPoint1.equals(endPoint2)) {
                    final Path<T> shortestPath = graph.getShortestPathBetween(endPoint1, endPoint2);
                    residualGraph.addEdge(shortestPath.getEndPoint1(), shortestPath.getEndPoint2());
                }
            }
        }
        return residualGraph;
    }

    private static <T> boolean isPerfect(final MutableUndirectedGraph<T> maximumMatching) {
        for (final T vertex : maximumMatching)
            if (maximumMatching.getConnectedVerticeSet(vertex).size() != 1) return false;
        return true;
    }

    private static <T> Map<T, T> buildMatchingMap(final MutableUndirectedGraph<T> maximumMatching) {
        final Map<T, T> matching = Maps.newHashMap();
        final Set<T> set = Sets.newHashSet(); // TODO ? ou bien ajouter 0.5 deux fois
        for (final T endPoint1 : maximumMatching) {
            final Set<T> endPoints = maximumMatching.getConnectedVerticeSet(endPoint1);
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
            final Path<T> path = originalGraph.getShortestPathBetween(endPoint1, endPoint2);
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
            Map<T, T> matching)
    {
        if (oddVertices.size() % 2 != 0) throw new RuntimeException("Number of odd vertices should be even.");
        final MutableUndirectedGraph<T> residualGraph = buildResidualGraph(originalGraph, oddVertices);
        MutableUndirectedGraph<T> maximumMatching = EdmondsMatching.maximumMatching(residualGraph);
        Map<T, T> bestPerfectMatching = null;
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

    public static <T> Map<WeightedEdge<T>, Integer> computeOptimalEulerization(final UndirectedGraph<T> originalGraph) {
        final List<T> oddVertices = oddVertices(originalGraph);
        final Map<T, T> matching = Maps.newHashMap();
        return computeOptimalEulerization(originalGraph, oddVertices, matching);
    }

}