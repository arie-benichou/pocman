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

package pocman.cpp;

import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import pocman.graph.Path;
import pocman.graph.UndirectedGraph;
import pocman.graph.WeightedEdge;
import pocman.graph.functions.NodeDegreeFunctions;
import pocman.graph.functions.NodeOfDegree1Pruning;
import pocman.matching.Match;
import pocman.matching.MatchingAlgorithm;
import pocman.matching.MutableUndirectedGraph;

import com.google.common.base.Preconditions;
import com.google.common.base.Supplier;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMap.Builder;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

public final class ClosedCPP<T> {

    public final static MatchingAlgorithm DEFAULT_MATCHING_ALGORITHM = new pocman.matching.edmonds1.Matching();

    public static <T> ClosedCPP<T> from(final UndirectedGraph<T> graph, final MatchingAlgorithm matchingAlgorithm) {
        Preconditions.checkArgument(graph != null);
        Preconditions.checkArgument(matchingAlgorithm != null);
        Preconditions.checkState(graph.isConnected(), "Graph must be connected.");
        return new ClosedCPP<T>(graph, matchingAlgorithm);
    }

    public static <T> ClosedCPP<T> from(final Supplier<UndirectedGraph<T>> graphSupplier, final MatchingAlgorithm matchingAlgorithm) {
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

    public Double getLowerBoundCost() {
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

    private Double upperBoundCost = null;

    public Double getUpperBoundCost() {
        if (this.upperBoundCost == null)
            this.upperBoundCost = computeCost(this.getTraversalByEdge());
        return this.upperBoundCost;
    }

    public Double getExtraCost() {
        return this.getUpperBoundCost() - this.getLowerBoundCost();
    }

    private static <T> MutableUndirectedGraph<T> buildResidualGraph(final UndirectedGraph<T> graph, final Set<T> oddVertices) {
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

    private static <T> Map<WeightedEdge<T>, Integer> computeTraversalByEdge(final UndirectedGraph<T> originalGraph, final Map<T, T> matching) {
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
            for (final WeightedEdge<T> edge : path.getEdges()) {
                //System.out.println(edge);
                map.put(edge, (map.get(edge) + 1) % 2 == 0 ? 2 : 1);
            }
        }
        //System.exit(0);
        return map;
    }

    // TODO ? réduire les noeuds de type corner
    private Map<WeightedEdge<T>, Integer> computeOptimalEulerization(final UndirectedGraph<T> graph, final Map<T, Integer> nodesWithOddDegree) {

        Preconditions.checkState(nodesWithOddDegree.size() % 2 == 0, "Number of odd vertices should be even.");

        final NodeOfDegree1Pruning<T> nodeOfDegree1Pruning = NodeOfDegree1Pruning.from(this.nodeDegreeFunctions);
        final Set<T> remainingOddVertices = nodeOfDegree1Pruning.getRemainingOddVertices();

        Preconditions.checkState(remainingOddVertices.size() % 2 == 0, "Number of remaining odd vertices should be even.");

        final Map<WeightedEdge<T>, Integer> eulerization;

        if (remainingOddVertices.isEmpty()) {
            eulerization = Maps.newHashMap();
            for (final T endPoint1 : graph)
                for (final T endPoint2 : graph.getEndPoints(endPoint1))
                    eulerization.put(graph.getEdge(endPoint1, endPoint2), 1);
        }
        else {
            final Match<T> matching = this.matchingAlgorithm.from(graph, buildResidualGraph(graph, remainingOddVertices));
            eulerization = computeTraversalByEdge(graph, matching.getMatches());
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

    private Map<WeightedEdge<T>, Integer> traversalByEdge = null;

    public Map<WeightedEdge<T>, Integer> getTraversalByEdge() {
        if (this.traversalByEdge == null) this.traversalByEdge = this.computeOptimalEulerization(this.getGraph(), this.getNodesWithOddDegree());
        return this.traversalByEdge;
    }

    private final MatchingAlgorithm matchingAlgorithm;

    public MatchingAlgorithm getMatchingAlgorithm() {
        return this.matchingAlgorithm;
    }

    private final NodeDegreeFunctions<T> nodeDegreeFunctions;

    public Map<T, Integer> getNodesWithOddDegree() {
        return this.nodeDegreeFunctions.getNodesWithOddDegree();
    }

    private Solution<T> solution = null;

    private ClosedCPP(final UndirectedGraph<T> graph, final MatchingAlgorithm matchingAlgorithm) {
        this.graph = graph;
        this.matchingAlgorithm = matchingAlgorithm;
        double lowerBoundCost = 0;
        for (final T MazeNode : this.graph)
            for (final T connectedMazeNode : this.graph.getEndPoints(MazeNode))
                lowerBoundCost += this.graph.getEdge(MazeNode, connectedMazeNode).getWeight();
        this.lowerBoundCost = lowerBoundCost / 2; // TODO exposer un set of edges immutable depuis le graphe

        this.nodeDegreeFunctions = NodeDegreeFunctions.from(this.getGraph());
    }

    public Solution<T> solve() {
        if (this.solution == null)
            this.solution = new Solution<T>(null, this.getGraph(), this.getTraversalByEdge(), this.getLowerBoundCost(), this.getUpperBoundCost());
        return this.solution;
    }

}