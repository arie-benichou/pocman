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

package cpp;

import graph.Path;
import graph.UndirectedGraph;
import graph.WeightedEdge;
import graph.features.connectivity.ConnectivityFeature;
import graph.features.connectivity.ConnectivityInterface;
import graph.features.degree.DegreeFeature;
import graph.features.degree.DegreeInterface;
import graph.features.routing.RoutingFeature;
import graph.features.routing.RoutingInterface;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.annotation.concurrent.ThreadSafe;

import matching.Matches;
import matching.MatchingAlgorithmInterface;

import com.google.common.base.Preconditions;
import com.google.common.base.Supplier;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMap.Builder;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

// TODO ? réduire les noeuds de type corner
@ThreadSafe
public final class ClosedCPP<T> {

    public final static MatchingAlgorithmInterface DEFAULT_MATCHING_ALGORITHM = new matching.edmonds1.Matching();

    public static <T> ClosedCPP<T> from(final UndirectedGraph<T> graph, final MatchingAlgorithmInterface matchingAlgorithm) {
        Preconditions.checkArgument(graph != null);
        Preconditions.checkArgument(matchingAlgorithm != null);
        final ConnectivityInterface<T> feature = graph.fetch(ConnectivityFeature.class).up();
        Preconditions.checkState(feature.isConnected(), "Graph must be connected.");
        return new ClosedCPP<T>(graph, matchingAlgorithm);
    }

    public static <T> ClosedCPP<T> from(final Supplier<UndirectedGraph<T>> graphSupplier, final MatchingAlgorithmInterface matchingAlgorithm) {
        Preconditions.checkArgument(graphSupplier != null);
        return ClosedCPP.from(graphSupplier.get(), matchingAlgorithm);
    }

    public static <T> ClosedCPP<T> from(final UndirectedGraph<T> graph) {
        return ClosedCPP.from(graph, DEFAULT_MATCHING_ALGORITHM);
    }

    public static <T> ClosedCPP<T> from(final Supplier<UndirectedGraph<T>> graphSupplier) {
        return ClosedCPP.from(graphSupplier, DEFAULT_MATCHING_ALGORITHM);
    }

    private final UndirectedGraph<T> graph;

    public UndirectedGraph<T> getGraph() {
        return this.graph;
    }

    private final Double lowerBoundCost;

    private Double getLowerBoundCost() {
        return this.lowerBoundCost;
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

    private static <T> UndirectedGraph<T> buildResidualGraph(final UndirectedGraph<T> originalGraph, final Set<T> oddVertices) {
        final UndirectedGraph.Builder<T> residualGraphBuilder = new UndirectedGraph.Builder<T>(oddVertices.size(), UndirectedGraph.SUPERVISER_MODE);
        final Set<WeightedEdge<T>> edges = Sets.newHashSet();

        final RoutingInterface<T> pathFeature = originalGraph.fetch(RoutingFeature.class).up();

        for (final T endPoint1 : oddVertices)
            for (final T endPoint2 : oddVertices)
                if (!endPoint1.equals(endPoint2)) { // TODO contains(u, v, w)
                    final Path<T> shortestPath = pathFeature.getShortestPath(endPoint1, endPoint2);
                    final WeightedEdge<T> edge = WeightedEdge.from(endPoint1, endPoint2, shortestPath.getWeight());
                    //if (!residualGraphBuilder.contains(edge)) residualGraphBuilder.addEdge(edge);
                    if (!edges.contains(edges)) edges.add(edge);
                }

        final List<WeightedEdge<T>> sortedEdges = Lists.newArrayList(edges);
        Collections.sort(sortedEdges);
        for (final WeightedEdge<T> weightedEdge : sortedEdges)
            residualGraphBuilder.addEdge(weightedEdge);

        return residualGraphBuilder.build();
    }

    private static <T> Map<WeightedEdge<T>, Integer> computeTraversalByEdge(final UndirectedGraph<T> originalGraph, final Map<T, T> matching) {
        final Set<WeightedEdge<T>> edges = Sets.newHashSet();
        for (final T MazeNode : originalGraph)
            edges.addAll(originalGraph.getEdgesFrom(MazeNode));
        final Map<WeightedEdge<T>, Integer> map = Maps.newHashMap();
        for (final WeightedEdge<T> edge : edges)
            map.put(edge, 1);

        final RoutingInterface<T> pathFeature = originalGraph.fetch(RoutingFeature.class).up();

        for (final Entry<T, T> entry : matching.entrySet()) {
            final T endPoint1 = entry.getKey();
            final T endPoint2 = entry.getValue();
            final Path<T> path = pathFeature.getShortestPath(endPoint1, endPoint2);
            for (final WeightedEdge<T> edge : path.getEdges()) {
                map.put(edge, (map.get(edge) + 1) % 2 == 0 ? 2 : 1);
            }
        }
        return map;
    }

    private static <T> Map<WeightedEdge<T>, Integer> computeOptimalEulerization(final MatchingAlgorithmInterface matchingAlgorithm, final UndirectedGraph<T> graph) {

        final DegreeInterface<T> degreeInterface = graph.fetch(DegreeFeature.class).up();
        final Map<T, Integer> nodesWithOddDegree = degreeInterface.getNodesWithOddDegree();
        Preconditions.checkState(nodesWithOddDegree.size() % 2 == 0, "Number of odd vertices should be even.");

        final NodeOfDegree1Pruning<T> nodeOfDegree1Pruning = NodeOfDegree1Pruning.from(graph);
        final Set<T> remainingOddVertices = nodeOfDegree1Pruning.getRemainingOddVertices();

        Preconditions.checkState(remainingOddVertices.size() % 2 == 0, "Number of remaining odd vertices should be even.");

        final Map<WeightedEdge<T>, Integer> eulerization;

        if (remainingOddVertices.isEmpty()) {
            eulerization = Maps.newHashMap();
            for (final T endPoint1 : graph)
                for (final T endPoint2 : graph.getConnectedEndPoints(endPoint1))
                    eulerization.put(graph.getEdge(endPoint1, endPoint2), 1);
        }
        else {
            matchingAlgorithm.setOriginalGraph(graph); // TODO !! virer ce  quick & dirty fix
            final Matches<T> matches = matchingAlgorithm.from(buildResidualGraph(graph, remainingOddVertices));
            eulerization = computeTraversalByEdge(graph, matches.get());
        }

        final Builder<WeightedEdge<T>, Integer> builder = new ImmutableMap.Builder<WeightedEdge<T>, Integer>();
        final Set<WeightedEdge<T>> doubledEdges = nodeOfDegree1Pruning.getDoubledEdges();
        for (final Entry<WeightedEdge<T>, Integer> entry : eulerization.entrySet()) {
            final WeightedEdge<T> edge = entry.getKey();
            if (doubledEdges.contains(edge)) builder.put(edge, 2);
            else builder.put(entry);
        }

        return builder.build();
    }

    private volatile Map<WeightedEdge<T>, Integer> traversalByEdge = null;

    private Map<WeightedEdge<T>, Integer> getTraversalByEdge() {
        Map<WeightedEdge<T>, Integer> value = this.traversalByEdge;
        if (value == null) {
            synchronized (this) {
                if ((value = this.traversalByEdge) == null)
                    this.traversalByEdge = value = computeOptimalEulerization(this.getMatchingAlgorithm(), this.getGraph());
            }
        }
        return value;
    }

    private final MatchingAlgorithmInterface matchingAlgorithm;

    public MatchingAlgorithmInterface getMatchingAlgorithm() {
        return this.matchingAlgorithm;
    }

    private ClosedCPP(final UndirectedGraph<T> graph, final MatchingAlgorithmInterface matchingAlgorithm) {
        this.graph = graph;
        this.matchingAlgorithm = matchingAlgorithm;
        // TODO à revoir
        double lowerBoundCost = 0;
        for (final WeightedEdge<T> edge : this.graph.getSetOfEdges())
            lowerBoundCost += edge.getWeight();
        this.lowerBoundCost = lowerBoundCost;
    }

    public ClosedCPPSolution<T> solve() {
        final Map<WeightedEdge<T>, Integer> traversalByEdge = this.getTraversalByEdge();
        return new ClosedCPPSolution<T>(this.matchingAlgorithm, this.graph, traversalByEdge, this.getLowerBoundCost(), computeCost(traversalByEdge)); // TODO à revoir
    }

}