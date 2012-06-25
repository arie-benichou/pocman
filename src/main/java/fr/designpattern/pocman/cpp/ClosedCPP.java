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

import fr.designpattern.pocman.cpp.graph.Solution;
import fr.designpattern.pocman.cpp.graph.UndirectedGraph;
import fr.designpattern.pocman.cpp.graph.WeightedEdge;
import fr.designpattern.pocman.cpp.graph.algo.MinimumWeightPerfectMatching;

public final class ClosedCPP<T> {

    private final UndirectedGraph<T> graph;

    public UndirectedGraph<T> getGraph() {
        return this.graph;
    }

    private final Double lowerBoundCost;

    public Double getUpperBoundCost() {
        return this.upperBoundCost;
    }

    public Double getLowerBoundCost() {
        return this.lowerBoundCost;
    }

    private Map<WeightedEdge<T>, Integer> edgeInstances;

    public Map<WeightedEdge<T>, Integer> getEdgeInstances() {
        return this.edgeInstances;
    }

    private Double upperBoundCost;

    public static <T> ClosedCPP<T> newSolver(final UndirectedGraph<T> graph) {
        Preconditions.checkArgument(graph != null);
        Preconditions.checkState(graph.isConnected(), "Graph must be connected.");
        return new ClosedCPP<T>(graph);
    }

    public static <T> ClosedCPP<T> newSolver(final Supplier<UndirectedGraph<T>> graphSupplier) {
        Preconditions.checkArgument(graphSupplier != null);
        return ClosedCPP.newSolver(graphSupplier.get());
    }

    private ClosedCPP(final UndirectedGraph<T> graph) {
        this.graph = graph;
        double lowerBoundCost = 0;
        for (final T vertex : this.graph)
            for (final T connectedVertex : this.graph.getConnectedVerticeSet(vertex))
                lowerBoundCost += this.graph.getEdge(vertex, connectedVertex).getWeight();
        this.lowerBoundCost = lowerBoundCost / 2; // TODO exposer un set of edges immutable depuis le graphe
    }

    private void computeOptimalEulerization() {
        if (this.graph.isEulerian()) {
            final Map<WeightedEdge<T>, Integer> map = Maps.newHashMap();
            for (final T vertex : this.graph)
                for (final T connectedVertex : this.graph.getConnectedVerticeSet(vertex))
                    map.put(this.graph.getEdge(vertex, connectedVertex), 1);
            this.edgeInstances = map; // TODO à revoir...
        }
        else this.edgeInstances = MinimumWeightPerfectMatching.computeOptimalEulerization(this.graph); // TODO retourner un objet solution
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

    public Solution<T> solveFrom(final T vertex) {

        Preconditions.checkArgument(vertex != null);

        if (this.edgeInstances == null) {
            this.computeOptimalEulerization();
            this.upperBoundCost = computeCost(this.edgeInstances);
        }

        return new Solution<T>(this.graph, vertex, this.edgeInstances, this.lowerBoundCost, this.upperBoundCost);
    }

}