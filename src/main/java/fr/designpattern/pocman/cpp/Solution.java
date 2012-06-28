
package fr.designpattern.pocman.cpp;

import java.util.Map;

import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableMap;

import fr.designpattern.pocman.graph.UndirectedGraph;
import fr.designpattern.pocman.graph.WeightedEdge;

public final class Solution<T> {

    private final Double lowerBoundCost;

    public Double getLowerBoundCost() {
        return this.lowerBoundCost;
    }

    private final Double upperBoundCost;

    public Double getUpperBoundCost() {
        return this.upperBoundCost;
    }

    private final Map<WeightedEdge<T>, Integer> traversalByEdge;
    public final UndirectedGraph<T> graph; // TODO

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
        if (!(object instanceof Solution)) return false;
        final Solution<?> that = (Solution<?>) object;
        if (!that.getLowerBoundCost().equals(this.getLowerBoundCost())) return false;
        if (!that.getUpperBoundCost().equals(this.getUpperBoundCost())) return false;
        return that.getTraversalByEdge().equals(this.getTraversalByEdge()); // TODO ? comparer uniquement la taille des maps
    }

    public Solution(final UndirectedGraph<T> graph, final Map<WeightedEdge<T>, Integer> traversalByEdge, final Double lowerBoundCost,
            final Double upperBoundCost) {
        Preconditions.checkArgument(traversalByEdge != null);
        Preconditions.checkArgument(lowerBoundCost != null);
        Preconditions.checkArgument(upperBoundCost != null);
        this.traversalByEdge = ImmutableMap.copyOf(traversalByEdge);
        this.lowerBoundCost = lowerBoundCost;
        this.upperBoundCost = upperBoundCost;
        Preconditions.checkState(this.upperBoundCost >= this.lowerBoundCost, "Cost upper-bound must be greater than lower-bound.");
        this.graph = graph;
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(this)
                .add("Lower-Bound Cost", this.lowerBoundCost)
                .add("Upper-Bound Cost", this.upperBoundCost)
                .add("Extra Cost", this.upperBoundCost - this.lowerBoundCost)
                .add("Traversal By Edge", this.traversalByEdge)
                .toString();
    }

}