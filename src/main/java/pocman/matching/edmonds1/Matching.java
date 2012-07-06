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

package pocman.matching.edmonds1;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import pocman.graph.Path;
import pocman.graph.UndirectedGraph;
import pocman.graph.WeightedEdge;
import pocman.graph.functions.NodeDegreeFunctions;
import pocman.matching.Matches;
import pocman.matching.MatchingAlgorithm;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMap.Builder;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

/**
 * Minimum Weight Perfect Matching Algorithm
 */
public final class Matching implements MatchingAlgorithm {

    private static <T> Map<T, T> buildMatchingMap(final MutableUndirectedGraph<T> maximumMatching) {
        final Builder<T, T> builder = new ImmutableMap.Builder<T, T>();
        final Set<T> set = Sets.newHashSet();
        for (final T endPoint1 : maximumMatching) {
            final T endPoint2 = maximumMatching.getEndPoints(endPoint1).iterator().next();
            if (!set.contains(endPoint2)) {
                set.add(endPoint1);
                set.add(endPoint2);
                builder.put(endPoint1, endPoint2);
            }
        }
        return builder.build();
    }

    private static <T> double computeCost(final UndirectedGraph<T> originalGraph, final Map<T, T> matching) {
        if (matching.isEmpty()) return Double.POSITIVE_INFINITY;
        double cost = 0;
        for (final Entry<T, T> entry : matching.entrySet())
            cost += originalGraph.getShortestPathBetween(entry.getKey(), entry.getValue()).getWeight();
        return cost;
    }

    private static <T> boolean isPerfect(final MutableUndirectedGraph<T> maximumMatching) {
        for (final T MazeNode : maximumMatching)
            if (maximumMatching.getEndPoints(MazeNode).size() != 1) return false;
        return true;
    }

    private UndirectedGraph<?> originalGraph;

    private static <T> Map<WeightedEdge<T>, Integer> buildMap(final UndirectedGraph<T> originalGraph) {

        final Set<WeightedEdge<T>> edges = Sets.newHashSet();
        for (final T vertex : originalGraph)
            edges.addAll(originalGraph.getEdges(vertex));

        final Map<WeightedEdge<T>, Integer> map = Maps.newHashMap();
        for (final WeightedEdge<T> edge : edges)
            map.put(edge, 1);

        final NodeDegreeFunctions<T> nodeDegreeFunctions = NodeDegreeFunctions.from(originalGraph); // TODO
        final Set<T> nodesWithDegree1 = nodeDegreeFunctions.getNodesWithDegree(1).keySet();

        for (final T t : nodesWithDegree1) {
            final WeightedEdge<T> endWayEdge = originalGraph.getEdges(t).iterator().next();
            map.put(endWayEdge, 2);
        }

        return map;
    }

    private static <T> Map<WeightedEdge<T>, Integer> eulerize(final UndirectedGraph<T> originalGraph, final Map<T, T> matching) {

        final Map<WeightedEdge<T>, Integer> map = buildMap(originalGraph);

        /*
        for (final Entry<WeightedEdge<T>, Integer> t : map.entrySet()) {
            System.out.println(t);
        }
        */

        for (final Entry<T, T> entry : matching.entrySet()) {
            final T endPoint1 = entry.getKey();
            final T endPoint2 = entry.getValue();
            final Path<T> path = originalGraph.getShortestPathBetween(endPoint1, endPoint2);
            for (final WeightedEdge<T> edge : path.getEdges()) {
                map.put(edge, (map.get(edge) + 1) % 2 == 0 ? 2 : 1);
                //map.put(edge, 2);
            }
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

    // TODO ? pouvoir donner un traversal by edge en option
    @Override
    public <T> Matches<T> from(final UndirectedGraph<T> residualGraph) {

        final MutableUndirectedGraph<T> mutableResidualGraph = new MutableUndirectedGraph<T>();

        for (final T endPoint : residualGraph)
            mutableResidualGraph.addEndPoint(endPoint);

        for (final T endPoint1 : residualGraph)
            for (final T endPoint2 : residualGraph.getEndPoints(endPoint1))
                mutableResidualGraph.addEdge(endPoint1, endPoint2);

        return this.bestMatching(residualGraph, mutableResidualGraph, new Matches<T>(new HashMap<T, T>(), Double.POSITIVE_INFINITY), 0);
    }

    private <T> Matches<T> bestMatching( // TODO à revoir...
            final UndirectedGraph<T> residualGraph,
            final MutableUndirectedGraph<T> mutableResidualGraph,
            Matches<T> bestMatch,
            final int level) {

        //System.out.println(level);

        final MutableUndirectedGraph<T> maximumMatching = EdmondsAlgorithm.maximumMatching(mutableResidualGraph);

        if (!isPerfect(maximumMatching)) return bestMatch;

        final Map<T, T> matching = buildMatchingMap(maximumMatching);

        //final double cost = computeCost(residualGraph, matching);
        final double cost = computeCost(eulerize((UndirectedGraph<T>) this.originalGraph, matching));

        System.err.println(cost + " | " + bestMatch.getCost());
        if (Double.compare(cost, bestMatch.getCost()) == -1) bestMatch = new Matches<T>(matching, cost);

        for (final Entry<T, T> entry : matching.entrySet()) {

            final MutableUndirectedGraph<T> nextMutableResidualGraph = new MutableUndirectedGraph<T>(mutableResidualGraph);
            nextMutableResidualGraph.removeEdge(entry.getKey(), entry.getValue());

            //System.out.println(entry.getKey() + "-" + entry.getValue());

            bestMatch = this.bestMatching(residualGraph, nextMutableResidualGraph, bestMatch, level + 1);
        }

        return bestMatch;
    }

    @Override
    public <T> void setOriginalGraph(final UndirectedGraph<T> graph) {
        this.originalGraph = graph;
    }
}