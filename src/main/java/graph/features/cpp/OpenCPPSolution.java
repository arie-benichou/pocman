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

package graph.features.cpp;

import graph.UndirectedGraph;
import graph.WeightedEdge;

import java.util.Map;


import com.google.common.base.Objects;
import com.google.common.collect.ImmutableMap;

public final class OpenCPPSolution<T> {

    private final Double lowerBoundCost;

    public Double getLowerBoundCost() {
        return this.lowerBoundCost;
    }

    private final Double upperBoundCost;

    public Double getUpperBoundCost() {
        return this.upperBoundCost;
    }

    private final Map<WeightedEdge<T>, Integer> traversalByEdge;
    public final UndirectedGraph<T> graph;

    public UndirectedGraph<T> getGraph() {
        return this.graph;
    }

    private final T node;

    public Map<WeightedEdge<T>, Integer> getTraversalByEdge() {
        return this.traversalByEdge;
    }

    @Override
    public int hashCode() { // TODO à la construction
        int hashcode = 17;
        hashcode += this.getTraversalByEdge().hashCode(); // TODO ? sortedMap
        hashcode *= 31;
        hashcode += this.getUpperBoundCost().hashCode();
        hashcode *= 31;
        hashcode += this.getLowerBoundCost().hashCode();
        hashcode *= 31;
        return hashcode;
    }

    @Override
    public boolean equals(final Object object) {
        if (object == null) return false;
        if (object == this) return true;
        if (!(object instanceof OpenCPPSolution)) return false;
        final OpenCPPSolution<?> that = (OpenCPPSolution<?>) object;
        if (!that.getLowerBoundCost().equals(this.getLowerBoundCost())) return false;
        if (!that.getUpperBoundCost().equals(this.getUpperBoundCost())) return false;
        return that.getTraversalByEdge().equals(this.getTraversalByEdge()); // TODO ? comparer uniquement la taille des maps
    }

    public OpenCPPSolution(
            final T node,
            final UndirectedGraph<T> graph,
            final Map<WeightedEdge<T>, Integer> traversalByEdge,
            final Double lowerBoundCost,
            final Double upperBoundCost) {
        //Preconditions.checkArgument(traversalByEdge != null);
        //Preconditions.checkArgument(lowerBoundCost != null);
        //Preconditions.checkArgument(upperBoundCost != null);
        this.graph = graph;
        this.node = node;
        if (traversalByEdge != null) this.traversalByEdge = ImmutableMap.copyOf(traversalByEdge);
        else this.traversalByEdge = null;
        this.lowerBoundCost = lowerBoundCost;
        this.upperBoundCost = upperBoundCost;
        //Preconditions.checkState(this.upperBoundCost >= this.lowerBoundCost, "Cost upper-bound must be greater than lower-bound.");
    }

    public OpenCPPSolution(
            final Map<WeightedEdge<T>, Integer> traversalByEdge,
            final Double lowerBoundCost,
            final Double upperBoundCost) {
        //Preconditions.checkArgument(traversalByEdge != null);
        //Preconditions.checkArgument(lowerBoundCost != null);
        //Preconditions.checkArgument(upperBoundCost != null);
        this.graph = null;
        this.node = null;
        if (traversalByEdge != null) this.traversalByEdge = ImmutableMap.copyOf(traversalByEdge);
        else this.traversalByEdge = null;
        this.lowerBoundCost = lowerBoundCost;
        this.upperBoundCost = upperBoundCost;
        //Preconditions.checkState(this.upperBoundCost >= this.lowerBoundCost, "Cost upper-bound must be greater than lower-bound.");
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(this)
                .addValue(this.getEndPoint())
                .addValue(this.lowerBoundCost)
                .addValue(this.upperBoundCost)
                //.add("Extra Cost", this.upperBoundCost - this.lowerBoundCost)
                //.add("Traversal By Edge", this.traversalByEdge)
                .toString();
    }

    public T getEndPoint() {
        return this.node;
    }

}