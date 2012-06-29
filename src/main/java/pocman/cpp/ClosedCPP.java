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

import pocman.graph.UndirectedGraph;
import pocman.graph.WeightedEdge;
import pocman.matching.MinimumWeightPerfectMatching;

import com.google.common.base.Preconditions;
import com.google.common.base.Supplier;
import com.google.common.collect.Maps;

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

    // TODO élaguer les noeuds dont le degré vaut 1 (endway): doubler d'emblée les arêtes...
    // TODO ? retourner un objet solution
    private static <T> Map<WeightedEdge<T>, Integer> computeOptimalEulerization(final UndirectedGraph<T> graph) {
        if (graph.isEulerian()) {
            final Map<WeightedEdge<T>, Integer> map = Maps.newHashMap();
            for (final T MazeNode : graph)
                for (final T connectedMazeNode : graph.getConnectedVerticeSet(MazeNode))
                    map.put(graph.getEdge(MazeNode, connectedMazeNode), 1);
            return map;
        }
        return MinimumWeightPerfectMatching.computeOptimalEulerization(graph); // TODO pouvoir changer d'implémentation (naive, ...)
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
        for (final T MazeNode : this.graph)
            for (final T connectedMazeNode : this.graph.getConnectedVerticeSet(MazeNode))
                lowerBoundCost += this.graph.getEdge(MazeNode, connectedMazeNode).getWeight();
        this.lowerBoundCost = lowerBoundCost / 2; // TODO exposer un set of edges immutable depuis le graphe
    }

    public Solution<T> solve() {
        if (this.solution == null)
            this.solution = new Solution<T>(null, this.getGraph(), this.getTraversalByEdge(), this.getLowerBoundCost(), this.getUpperBoundCost());
        return this.solution;
    }

}