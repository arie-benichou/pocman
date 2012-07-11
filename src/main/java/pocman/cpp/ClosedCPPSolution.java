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

import pocman.graph.UndirectedGraph;
import pocman.graph.WeightedEdge;
import pocman.matching.MatchingAlgorithm;

import com.google.common.base.Objects;

public final class ClosedCPPSolution<T> {

    private final UndirectedGraph<T> graph;
    private final Map<WeightedEdge<T>, Integer> traversalByEdge;
    private final Double lowerBoundCost;
    private final Double upperBoundCost;
    private final MatchingAlgorithm matchingAlgorithm;

    public double getLowerBoundCost() {
        return this.lowerBoundCost;
    }

    public double getUpperBoundCost() {
        return this.upperBoundCost;
    }

    public UndirectedGraph<T> getGraph() {
        return this.graph;
    }

    public Map<WeightedEdge<T>, Integer> getTraversalByEdge() {
        return this.traversalByEdge;
    }

    public MatchingAlgorithm getMatchingAlgorithm() {
        return this.matchingAlgorithm;
    }

    @Override
    public int hashCode() { // TODO définir à la construction + à revoir
        int hashcode = 17;
        hashcode += this.getTraversalByEdge().hashCode(); // TODO ? sortedMap
        hashcode *= 31;
        hashcode += this.getUpperBoundCost();
        hashcode *= 31;
        hashcode += this.getLowerBoundCost();
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

    public ClosedCPPSolution(
            final MatchingAlgorithm matchingAlgorithm,
            final UndirectedGraph<T> graph,
            final Map<WeightedEdge<T>, Integer> traversalByEdge,
            final Double lowerBoundCost,
            final Double upperBoundCost) {
        this.matchingAlgorithm = matchingAlgorithm;
        this.graph = graph;
        this.traversalByEdge = traversalByEdge;
        // TODO objet Bound
        this.lowerBoundCost = lowerBoundCost;
        this.upperBoundCost = upperBoundCost;
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(this)
                .add("Lower-Bound Cost", this.getLowerBoundCost())
                .add("Upper-Bound Cost", this.getUpperBoundCost())
                .add("Extra Cost", this.upperBoundCost - this.lowerBoundCost)
                //.add("Traversal By Edge", this.traversalByEdge)
                .toString();
    }

}