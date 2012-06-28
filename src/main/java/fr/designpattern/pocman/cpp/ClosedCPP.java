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

import java.util.Map;
import java.util.Map.Entry;

import com.google.common.base.Preconditions;
import com.google.common.base.Supplier;
import com.google.common.collect.Maps;

import fr.designpattern.pocman.graph.UndirectedGraph;
import fr.designpattern.pocman.graph.WeightedEdge;

public final class ClosedCPP<T> {

    public static <T> ClosedCPP<T> from(final UndirectedGraph<T> graph) {
        Preconditions.checkArgument(graph != null);
        Preconditions.checkState(graph.isConnected(), "Graph must be connected.");
        return new ClosedCPP<T>(graph);
    }

    public static <T> ClosedCPP<T> from(final Supplier<UndirectedGraph<T>> graphSupplier) {
        Preconditions.checkArgument(graphSupplier != null);
        return ClosedCPP.from(graphSupplier.get());
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

    // TODO ? retourner un objet solution
    private static <T> Map<WeightedEdge<T>, Integer> computeOptimalEulerization(final UndirectedGraph<T> graph) {
        if (graph.isEulerian()) {
            final Map<WeightedEdge<T>, Integer> map = Maps.newHashMap();
            for (final T vertex : graph)
                for (final T connectedVertex : graph.getConnectedVerticeSet(vertex))
                    map.put(graph.getEdge(vertex, connectedVertex), 1);
            return map;
        }
        return MinimumWeightPerfectMatching.computeOptimalEulerization(graph);
    }

    private Map<WeightedEdge<T>, Integer> edgeInstances = null;

    public Map<WeightedEdge<T>, Integer> getTraversalByEdge() {
        if (this.edgeInstances == null) this.edgeInstances = computeOptimalEulerization(this.getGraph());
        return this.edgeInstances;
    }

    private Solution<T> solution = null;

    private ClosedCPP(final UndirectedGraph<T> graph) {
        this.graph = graph;
        double lowerBoundCost = 0;
        for (final T vertex : this.graph)
            for (final T connectedVertex : this.graph.getConnectedVerticeSet(vertex))
                lowerBoundCost += this.graph.getEdge(vertex, connectedVertex).getWeight();
        this.lowerBoundCost = lowerBoundCost / 2; // TODO exposer un set of edges immutable depuis le graphe
    }

    public Solution<T> solve() {
        if (this.solution == null)
            this.solution = new Solution<T>(this.getGraph(), this.getTraversalByEdge(), this.getLowerBoundCost(), this.getUpperBoundCost());
        return this.solution;
    }

}