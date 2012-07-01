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

package pocman.matching;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import pocman.graph.Path;
import pocman.graph.UndirectedGraph;
import pocman.graph.WeightedEdge;
import pocman.graph.functions.NodeDegreeFunctions;
import pocman.graph.functions.NodeOfDegree1Pruning;

import com.google.common.base.Preconditions;
import com.google.common.base.Stopwatch;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMap.Builder;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

// TODO ? réduire les noeuds de type corner
public final class MinimumWeightPerfectMatching {

    private static <T> MutableUndirectedGraph<T> buildResidualGraph(final UndirectedGraph<T> graph, final List<T> oddVertices) {
        final MutableUndirectedGraph<T> residualGraph = new MutableUndirectedGraph<T>();
        for (final T vertice : oddVertices)
            residualGraph.addMazeNode(vertice);
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
        for (final T MazeNode : maximumMatching)
            if (maximumMatching.getConnectedVerticeSet(MazeNode).size() != 1) return false;
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
        for (final T MazeNode : originalGraph)
            edges.addAll(originalGraph.getEdges(MazeNode));
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
            Map<T, T> matching) {
        Preconditions.checkState(oddVertices.size() % 2 == 0, "Number of odd vertices should be even.");
        final MutableUndirectedGraph<T> residualGraph = buildResidualGraph(originalGraph, oddVertices);
        MutableUndirectedGraph<T> maximumMatching = EdmondsMatching.maximumMatching(residualGraph);
        Map<T, T> bestPerfectMatching = null;
        if (isPerfect(maximumMatching)) {
            double bestPerfectMatchingWeight = Double.POSITIVE_INFINITY;
            final Stopwatch stopwatch = new Stopwatch();
            do {
                stopwatch.start();
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
                stopwatch.reset();
            }
            while (isPerfect(maximumMatching));
        }
        return eulerize(originalGraph, bestPerfectMatching);
    }

    public static <T> Map<WeightedEdge<T>, Integer> computeOptimalEulerization(final UndirectedGraph<T> originalGraph) {
        Preconditions.checkState(!originalGraph.isEulerian());
        final Builder<WeightedEdge<T>, Integer> builder = new ImmutableMap.Builder<WeightedEdge<T>, Integer>();
        final NodeOfDegree1Pruning<T> nodeOfDegree1Pruning = NodeOfDegree1Pruning.from(NodeDegreeFunctions.from(originalGraph));
        if (nodeOfDegree1Pruning.hasStillNodeWithOddDegree()) {
            final Map<WeightedEdge<T>, Integer> eulerization = computeOptimalEulerization(
                    originalGraph,
                    Lists.newArrayList(nodeOfDegree1Pruning.getRemainingOddVertices()),
                    new HashMap<T, T>());
            for (final WeightedEdge<T> edge : nodeOfDegree1Pruning.getDoubledEdges())
                eulerization.put(edge, 2);
            builder.putAll(eulerization);
        }
        else for (final WeightedEdge<T> edge : nodeOfDegree1Pruning.getDoubledEdges())
            builder.put(edge, 2);
        return builder.build();
    }

    private MinimumWeightPerfectMatching() {}

}