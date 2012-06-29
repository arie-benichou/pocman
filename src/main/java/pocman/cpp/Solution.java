
package pocman.cpp;

import java.util.Map;

import pocman.graph.UndirectedGraph;
import pocman.graph.WeightedEdge;

import com.google.common.base.Objects;
import com.google.common.collect.ImmutableMap;


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
    public final UndirectedGraph<T> graph;

    public UndirectedGraph<T> getGraph() {
        return this.graph;
    }

    private final T MazeNode;

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

    public Solution(
            final T MazeNode,
            final UndirectedGraph<T> graph,
            final Map<WeightedEdge<T>, Integer> traversalByEdge,
            final Double lowerBoundCost,
            final Double upperBoundCost) {
        //Preconditions.checkArgument(traversalByEdge != null);
        //Preconditions.checkArgument(lowerBoundCost != null);
        //Preconditions.checkArgument(upperBoundCost != null);
        this.graph = graph;
        this.MazeNode = MazeNode;
        if (traversalByEdge != null) this.traversalByEdge = ImmutableMap.copyOf(traversalByEdge);
        else this.traversalByEdge = null;
        this.lowerBoundCost = lowerBoundCost;
        this.upperBoundCost = upperBoundCost;
        //Preconditions.checkState(this.upperBoundCost >= this.lowerBoundCost, "Cost upper-bound must be greater than lower-bound.");
    }

    /*
    public OpenSolution(Node<T> node, UndirectedGraph<Node<T>> virtualGraph, Map<WeightedEdge<Node<T>>, Integer> traversalByEdge2, Double lowerBoundCost2,
            Double upperBoundCost2) {
        // TODO Auto-generated constructor stub
    }
    */

    @Override
    public String toString() {
        return Objects.toStringHelper(this)
                .add("MazeNode", this.getMazeNode())
                .add("Lower-Bound Cost", this.lowerBoundCost)
                .add("Upper-Bound Cost", this.upperBoundCost)
                .add("Extra Cost", this.upperBoundCost - this.lowerBoundCost)
                .add("Traversal By Edge", this.traversalByEdge)
                .toString();
    }

    public T getMazeNode() {
        return this.MazeNode;
    }

}