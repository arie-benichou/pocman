
package fr.designpattern.pocman.cpp;

import java.util.Map;

import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableMap;

import fr.designpattern.pocman.graph.UndirectedGraph;
import fr.designpattern.pocman.graph.WeightedEdge;

public final class Solution<T> {

    private final UndirectedGraph<T> graph;

    public UndirectedGraph<T> getGraph() {
        return this.graph;
    }

    private final T startingVertex;

    public T getStartingVertex() {
        return this.startingVertex;
    }

    private final Double lowerBoundCost;

    public Double getLowerBoundCost() {
        return this.lowerBoundCost;
    }

    private final Double upperBoundCost;

    private final Map<WeightedEdge<T>, Integer> traversalByEdge;

    public Map<WeightedEdge<T>, Integer> getTraversalByEdge() {
        return this.traversalByEdge;
    }

    public Double getUpperBoundCost() {
        return this.upperBoundCost;
    }

    @Override
    public int hashCode() { // TODO à la construction
        int hashcode = 17;
        hashcode += this.getStartingVertex().hashCode();
        hashcode *= 31;
        hashcode += this.getTraversalByEdge().hashCode(); // TODO
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
        //if (!that.getGraph().equals(this.getGraph())) return false; // TODO equals & hascode
        if (!that.getLowerBoundCost().equals(this.getLowerBoundCost())) return false;
        if (!that.getUpperBoundCost().equals(this.getUpperBoundCost())) return false;
        if (!that.getStartingVertex().equals(this.getStartingVertex())) return false;
        return that.getTraversalByEdge().equals(this.getTraversalByEdge());
    }

    public Solution(final UndirectedGraph<T> graph, final T startingVertex, final Map<WeightedEdge<T>, Integer> edgeInstances, final Double lowerBoundCost,
            final Double upperBoundCost) {
        this.graph = graph;
        this.startingVertex = startingVertex;
        this.traversalByEdge = ImmutableMap.copyOf(edgeInstances);
        this.lowerBoundCost = lowerBoundCost;
        this.upperBoundCost = upperBoundCost;
        Preconditions.checkState(this.upperBoundCost >= this.lowerBoundCost, "Cost upper-bound must be greater than lower-bound.");
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(this)
                .add("Lower-Bound Cost", this.lowerBoundCost)
                .add("Upper-Bound Cost", this.upperBoundCost)
                .add("Extra Cost", this.upperBoundCost - this.lowerBoundCost)
                .add("Starting Vertex", this.startingVertex)
                .add("Traversal By Edge", this.traversalByEdge)
                //.add("Graph", this.graph.toString()) // TODO
                .toString();
    }

}