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
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

public class MinimumWeightPerfectMatching {

    private MinimumWeightPerfectMatching() {}

    private static <T> List<T> oddVertices(final UndirectedGraph<T> graph) {
        final List<T> oddVertices = Lists.newArrayList();
        for (final T MazeNode : graph)
            if (graph.getConnectedVerticeSet(MazeNode).size() % 2 == 1) oddVertices.add(MazeNode);
        return oddVertices;
    }

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
        if (oddVertices.size() % 2 != 0) throw new RuntimeException("Number of odd vertices should be even.");
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

    // TODO degreeByVertice
    // TODO ?  réduire les noeuds de type corner
    public static <T> Map<WeightedEdge<T>, Integer> computeOptimalEulerization(final UndirectedGraph<T> originalGraph) {
        Preconditions.checkState(!originalGraph.isEulerian());

        final Set<T> oddVertices = originalGraph.getOddVertices();
        /*
        System.out.println(oddVertices.size());

        System.out.println();
        */
        final NodeDegreeFunctions<T> nodeDegreeVisitor = NodeDegreeFunctions.from(originalGraph);
        final Map<T, Integer> nodesWithDegree1 = nodeDegreeVisitor.getNodesWithDegree(1);
        /*
        System.out.println(nodesWithDegree1.size());
        for (final Entry<T, Integer> entry : nodesWithDegree1.entrySet()) {
            System.out.println(entry);
        }
        */

        final NodeOfDegree1Pruning<T> nodeOfDegree1Pruning = NodeOfDegree1Pruning.from(nodeDegreeVisitor);

        /*
        System.out.println();
        */

        final Set<WeightedEdge<T>> doubledEdges = nodeOfDegree1Pruning.getDoubledEdges();
        /*
        for (final WeightedEdge<T> weightedEdge : doubledEdges) {
            System.out.println(weightedEdge); // TODO permetrait d'affiner le lower bound
        }

        System.out.println();
        */

        final Set<T> remainingOddVertices = nodeOfDegree1Pruning.getRemainingOddVertices();

        /*
        System.out.println(remainingOddVertices.size());
        for (final T oddVertex : remainingOddVertices) {
            System.out.println(oddVertex);
        }

        System.out.println();

        System.out.println(nodeOfDegree1Pruning.hasStillNodeWithOddDegree());

        System.out.println();
        */

        if (nodeOfDegree1Pruning.hasStillNodeWithOddDegree()) {
            final Map<WeightedEdge<T>, Integer> eulerization =
                                                               computeOptimalEulerization(originalGraph, Lists.newArrayList(remainingOddVertices),
                                                                       new HashMap<T, T>());

            /*
            for (final Entry<WeightedEdge<T>, Integer> entry : eulerization.entrySet()) {
                System.out.println(entry);
            }
            */

            final HashMap<WeightedEdge<T>, Integer> map = Maps.newHashMap(eulerization);
            for (final WeightedEdge<T> edge : doubledEdges) {
                map.put(edge, 2);
            }

            /*
            System.out.println();

            for (final Entry<WeightedEdge<T>, Integer> entry : map.entrySet()) {
                System.out.println(entry);
            }
            */

            //System.exit(0);

            return map;
        }
        throw new RuntimeException("Not implemented yet"); // TODO
    }
}