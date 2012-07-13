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

package matching.edmonds1;

import graph.Feature;
import graph.Path;
import graph.UndirectedGraph;
import graph.WeightedEdge;
import graph.features.degree.DegreeInterface;
import graph.features.routing.RoutingInterface;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import matching.Matches;
import matching.MatchingAlgorithm;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMap.Builder;
import com.google.common.collect.Lists;
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

        final RoutingInterface<T> pathFeature = originalGraph.getFeature(Feature.ROUTING);

        double cost = 0;
        for (final Entry<T, T> entry : matching.entrySet())
            cost += pathFeature.getShortestPath(entry.getKey(), entry.getValue()).getWeight();
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
            edges.addAll(originalGraph.getEdgesFrom(vertex));

        final Map<WeightedEdge<T>, Integer> map = Maps.newHashMap();
        for (final WeightedEdge<T> edge : edges)
            map.put(edge, 1);

        final DegreeInterface<T> degreeInterface = originalGraph.getFeature(Feature.DEGREE);

        final Set<T> nodesWithDegree1 = degreeInterface.getNodesHavingDegree(1).keySet();

        for (final T t : nodesWithDegree1) {
            final WeightedEdge<T> endWayEdge = originalGraph.getEdgesFrom(t).iterator().next();
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

        final RoutingInterface<T> pathFeature = originalGraph.getFeature(Feature.ROUTING);

        for (final Entry<T, T> entry : matching.entrySet()) {
            final T endPoint1 = entry.getKey();
            final T endPoint2 = entry.getValue();
            final Path<T> path = pathFeature.getShortestPath(endPoint1, endPoint2);
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

    private <T> MutableUndirectedGraph<T> copyGraph(final UndirectedGraph<T> residualGraph) {
        final MutableUndirectedGraph<T> mutableResidualGraph = new MutableUndirectedGraph<T>();
        for (final T endPoint : residualGraph)
            mutableResidualGraph.addEndPoint(endPoint);
        for (final T endPoint1 : residualGraph)
            for (final T endPoint2 : residualGraph.getConnectedEndPoints(endPoint1))
                mutableResidualGraph.addEdge(endPoint1, endPoint2);
        return mutableResidualGraph;
    }

    private <T> UndirectedGraph<T> copyGraph(final MutableUndirectedGraph<T> graph) {
        final UndirectedGraph.Builder<T> residualGraphBuilder = new UndirectedGraph.Builder<T>(graph.getOrder(), UndirectedGraph.SUPERVISER_MODE);
        final Set<WeightedEdge<T>> edges = Sets.newHashSet();
        for (final T endPoint1 : graph)
            for (final T endPoint2 : graph)
                if (!endPoint1.equals(endPoint2)) { // TODO contains(u, v, w)
                    final WeightedEdge<T> edge = WeightedEdge.from(endPoint1, endPoint2, 1);
                    if (!edges.contains(edges)) edges.add(edge);
                }
        final List<WeightedEdge<T>> sortedEdges = Lists.newArrayList(edges);
        Collections.sort(sortedEdges);
        for (final WeightedEdge<T> weightedEdge : sortedEdges)
            residualGraphBuilder.addEdge(weightedEdge);
        return residualGraphBuilder.build();
    }

    // TODO ? pouvoir donner un traversal by edge en option
    @Override
    public <T> Matches<T> from(final UndirectedGraph<T> residualGraph) {
        final MutableUndirectedGraph<T> mutableResidualGraph = this.copyGraph(residualGraph);
        return this.bestMatching(residualGraph, mutableResidualGraph, new Matches<T>(new HashMap<T, T>(), Double.POSITIVE_INFINITY), 0);
    }

    public <T> void enumeration(final UndirectedGraph<T> residualGraph, final int i) {
        System.out.println(i);
        final MutableUndirectedGraph<T> mutableResidualGraph = this.copyGraph(residualGraph);
        MutableUndirectedGraph<T> maximumMatching;
        maximumMatching = EdmondsAlgorithm.maximumMatching(mutableResidualGraph);
        if (!isPerfect(maximumMatching)) return;
        final MutableUndirectedGraph<T> mutableUndirectedGraph = new MutableUndirectedGraph<T>(mutableResidualGraph);
        final T node = maximumMatching.iterator().next();
        mutableUndirectedGraph.removeEdge(node, maximumMatching.getEndPoints(node).iterator().next());
        final UndirectedGraph<T> nextResidualGraph = this.copyGraph(mutableUndirectedGraph);
        this.enumeration(nextResidualGraph, i + 1);
    }

    private <T> Matches<T> _bestMatching( // TODO à revoir...
            final UndirectedGraph<T> residualGraph,
            final MutableUndirectedGraph<T> mutableResidualGraph,
            Matches<T> bestMatch,
            final int level) {

        //System.out.println(level);

        final MutableUndirectedGraph<T> maximumMatching = EdmondsAlgorithm.maximumMatching(mutableResidualGraph);

        if (!isPerfect(maximumMatching)) return bestMatch;

        /*
        System.out.println();
        System.out.println("Perfect Matching");
        for (final T t : maximumMatching) {
            System.out.println(t + " - " + maximumMatching.getEndPoints(t).iterator().next());
        }
        System.out.println();
        */

        final Map<T, T> matching = buildMatchingMap(maximumMatching);

        System.out.println();
        System.out.println("Perfect Matching");
        for (final Entry<T, T> entry : matching.entrySet()) {
            System.out.println(entry);
        }
        System.out.println();

        //final double cost = computeCost(residualGraph, matching);
        //System.out.println(cost);
        final double cost = computeCost(eulerize((UndirectedGraph<T>) this.originalGraph, matching));

        //System.err.println(cost + " | " + bestMatch.getCost());
        if (Double.compare(cost, bestMatch.getCost()) == -1) bestMatch = new Matches<T>(matching, cost);

        for (final Entry<T, T> entry : matching.entrySet()) {
            final MutableUndirectedGraph<T> nextMutableResidualGraph = new MutableUndirectedGraph<T>(mutableResidualGraph);
            nextMutableResidualGraph.removeEdge(entry.getKey(), entry.getValue());
            bestMatch = this.bestMatching(residualGraph, nextMutableResidualGraph, bestMatch, level + 1);
        }

        /*
        final MutableUndirectedGraph<T> nextMutableResidualGraph = new MutableUndirectedGraph<T>(mutableResidualGraph);
        for (final Entry<T, T> entry : matching.entrySet()) {
            nextMutableResidualGraph.removeEdge(entry.getKey(), entry.getValue());
        }
        bestMatch = this.bestMatching(residualGraph, nextMutableResidualGraph, bestMatch, level + 1);
        */

        /*
        System.out.println("ok");
        final MutableUndirectedGraph<T> nextMutableResidualGraph = new MutableUndirectedGraph<T>(mutableResidualGraph);
        for (final Entry<T, T> entry : matching.entrySet())
            nextMutableResidualGraph.removeEdge(entry.getKey(), entry.getValue());
        bestMatch = this.bestMatching(residualGraph, nextMutableResidualGraph, bestMatch, level + 1);
        */

        return bestMatch;
    }

    private <T> Matches<T> bestMatching(
            final UndirectedGraph<T> residualGraph,
            final MutableUndirectedGraph<T> mutableResidualGraph,
            Matches<T> bestMatch,
            final int level) {

        final MutableUndirectedGraph<T> maximumMatching = EdmondsAlgorithm.maximumMatching(mutableResidualGraph);
        if (!isPerfect(maximumMatching)) return bestMatch;
        final Map<T, T> matching = buildMatchingMap(maximumMatching);
        //final double cost = computeCost(residualGraph, matching);
        final double cost = computeCost(eulerize((UndirectedGraph<T>) this.originalGraph, matching));
        if (Double.compare(cost, bestMatch.getCost()) == -1) bestMatch = new Matches<T>(matching, cost);
        final MutableUndirectedGraph<T> nextMutableResidualGraph = new MutableUndirectedGraph<T>(mutableResidualGraph);
        for (final Entry<T, T> entry : matching.entrySet()) {
            //final MutableUndirectedGraph<T> nextMutableResidualGraph = new MutableUndirectedGraph<T>(mutableResidualGraph);
            nextMutableResidualGraph.removeEdge(entry.getKey(), entry.getValue());
            //bestMatch = this.bestMatching(residualGraph, nextMutableResidualGraph, bestMatch, level + 1);
        }
        bestMatch = this.bestMatching(residualGraph, nextMutableResidualGraph, bestMatch, level + 1);
        return bestMatch;
    }

    @Override
    public <T> void setOriginalGraph(final UndirectedGraph<T> graph) {
        this.originalGraph = graph;
    }
}