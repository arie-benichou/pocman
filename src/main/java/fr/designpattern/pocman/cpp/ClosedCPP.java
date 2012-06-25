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

import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import com.google.common.base.Supplier;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

import fr.designpattern.pocman.cpp.graph.UndirectedGraph;
import fr.designpattern.pocman.cpp.graph.WeightedEdge;
import fr.designpattern.pocman.cpp.graph.algo.EulerianTrail;
import fr.designpattern.pocman.cpp.graph.algo.MinimumWeightPerfectMatching;

public final class ClosedCPP<T> {

    private final UndirectedGraph<T> graph;

    private final Double lowerBoundCost;

    public Double getLowerBoundCost() {
        return this.lowerBoundCost;
    }

    private Map<WeightedEdge<T>, Integer> edgeInstances;

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

    //TODO unit tests
    public static class Solution<TT> {

        /*
        private final UndirectedGraph<TT> graph;

        public UndirectedGraph<TT> getGraph() {
            return this.graph;
        }
        */

        public TT getStartingVertex() {
            return this.trail.get(0);
        }

        private final List<TT> trail;

        public List<TT> getTrail() {
            return this.trail;
        }

        private final Double lowerBoundCost;

        public Double getLowerBoundCost() {
            return this.lowerBoundCost;
        }

        private final Double upperBoundCost;

        public Double getUpperBoundCost() {
            return this.upperBoundCost;
        }

        @Override
        public int hashCode() { // TODO à la construction
            int hashcode = 17;
            hashcode += this.getLowerBoundCost().hashCode();
            hashcode *= 31;
            hashcode += this.getUpperBoundCost().hashCode();
            hashcode *= 31;
            hashcode += this.getStartingVertex().hashCode();
            hashcode *= 31;
            hashcode += this.getTrail().size();
            hashcode *= 31;
            return hashcode;
        }

        @Override
        public boolean equals(final Object object) {
            if (object == null) return false;
            if (object == this) return true;
            if (!(object instanceof Solution)) return false;
            final Solution<?> that = (Solution<?>) object;
            if (!that.getLowerBoundCost().equals(this.getLowerBoundCost())) return false;
            if (!that.getUpperBoundCost().equals(this.getUpperBoundCost())) return false;
            if (!that.getStartingVertex().equals(this.getStartingVertex())) return false;
            //if (!that.getTrail().equals(this.getTrail())) return false;
            if (that.getTrail().size() != this.getTrail().size()) return false;
            if (!Sets.newHashSet(that.getTrail()).equals(Sets.newHashSet(this.getTrail()))) return false;
            return true;
        }

        public Solution(final UndirectedGraph<TT> graph, final List<TT> trail, final Double lowerBoundCost, final Double upperBoundCost) {
            //this.graph = graph;
            this.trail = ImmutableList.copyOf(trail);
            this.lowerBoundCost = lowerBoundCost;
            this.upperBoundCost = upperBoundCost;

            Double c = 0.0;
            for (int i = 0; i < trail.size() - 1; ++i) {
                c += graph.getEdge(trail.get(i), trail.get(i + 1)).getWeight();
            }

            Preconditions.checkState(upperBoundCost >= lowerBoundCost);
            Preconditions.checkState(c.equals(upperBoundCost));

        }

        public Solution(final Double lowerBoundCost, final Double upperBoundCost, final List<TT> trail) {
            this.trail = trail;
            this.lowerBoundCost = lowerBoundCost;
            this.upperBoundCost = upperBoundCost;
            Preconditions.checkState(upperBoundCost >= lowerBoundCost);
        }

        @Override
        public String toString() {
            return Objects.toStringHelper(this)
                    //.add("Graph", this.graph.toString())
                    .add("Lower-Bound Cost", this.lowerBoundCost)
                    .add("Upper-Bound Cost", this.upperBoundCost)
                    .add("Extra Cost", this.upperBoundCost - this.lowerBoundCost)
                    .add("Starting Vertex", this.trail.get(0))
                    .add("Trail", this.trail)
                    .toString();
        }

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

        final List<T> trail = Lists.newArrayList();
        EulerianTrail.apply(vertex, this.graph, Maps.newHashMap(this.edgeInstances), trail);

        return new Solution<T>(this.graph, trail, this.lowerBoundCost, this.upperBoundCost);
    }

}