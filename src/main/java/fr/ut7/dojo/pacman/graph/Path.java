
package fr.ut7.dojo.pacman.graph;

import java.util.List;

import com.google.common.base.Objects;
import com.google.common.collect.ImmutableList;

public final class Path {

    private final List<GraphEdge> edgesSequence;
    private final int sum;

    public static Path from(final List<GraphEdge> edgesSequence) {
        return new Path(edgesSequence);
    }

    private Path(final List<GraphEdge> edgesSequence) {
        this.edgesSequence = ImmutableList.copyOf(edgesSequence);
        int sum = 0;
        for (final GraphEdge edge : edgesSequence)
            sum += edge.getBetweenNodes().size();// + 1;
        this.sum = sum;
    }

    public List<GraphEdge> getEdgesSequence() {
        return this.edgesSequence;
    }

    public int getSum() {
        return this.sum;
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(this)
                .add("edges", this.getEdgesSequence())
                .add("sum", this.getSum())
                .toString();
    }

}