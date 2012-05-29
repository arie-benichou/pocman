
package fr.ut7.dojo.pacman.graph;

import java.util.List;

import com.google.common.base.Strings;
import com.google.common.collect.ImmutableList;

import fr.ut7.dojo.pacman.model.Move;

public class GraphEdge implements Comparable<GraphEdge> {

    private final Move move;
    private final Vertex firstNode;
    private final List<Vertex> betweenNodes;
    private final Vertex lastNode;
    private final int value;
    private final int hashCode;

    public GraphEdge(final Move move, final Vertex firstNode, final Vertex lastNode, final List<Vertex> betweenNodes) {
        this.move = move;
        this.firstNode = firstNode;
        this.betweenNodes = ImmutableList.copyOf(betweenNodes);
        this.lastNode = lastNode;
        this.value = this.betweenNodes.size() + 1;
        this.hashCode = 27 * firstNode.getId() * move.getDelta() * this.value;
    }

    public Move getMove() {
        return this.move;
    }

    public Vertex getFirstNode() {
        return this.firstNode;
    }

    public List<Vertex> getBetweenNodes() {
        return this.betweenNodes;
    }

    public Vertex getLastNode() {
        return this.lastNode;
    }

    public int getValue() {
        return this.value;
    }

    @Override
    public int hashCode() {
        return this.hashCode;
    }

    @Override
    public boolean equals(final Object object) {
        if (object == null) return false;
        if (object == this) return true;
        if (!(object instanceof GraphEdge)) return false;
        final GraphEdge that = (GraphEdge) object;
        return this.hashCode() == that.hashCode();
    }

    @Override
    public int compareTo(final GraphEdge that) {
        return this.getValue() - that.getValue();
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("    ");
        sb.append(Strings.padStart(String.valueOf(this.getValue()), 2, ' ') + '$');
        sb.append(" [");
        sb.append(Strings.padStart(String.valueOf(this.getFirstNode().getId()), 3, '0'));
        sb.append(", ");
        sb.append(Strings.padStart(String.valueOf(this.getLastNode().getId()), 3, '0'));
        sb.append("] ");
        sb.append(" ");
        sb.append(this.getMove());
        return sb.toString();
    }

}