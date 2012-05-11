
package fr.ut7.dojo.pacman.graph;

import java.util.List;
import java.util.Map;

import com.google.common.collect.Lists;

public final class PathManager {

    private final Map<Integer, List<GraphEdge>> edgesById;

    public static PathManager from(final Map<Integer, List<GraphEdge>> edgesById) {
        return new PathManager(edgesById);
    }

    private PathManager(final Map<Integer, List<GraphEdge>> edgesById) {
        this.edgesById = edgesById;
    }

    public Map<Integer, List<GraphEdge>> getEdgesById() {
        return this.edgesById;
    }

    private GraphEdge findEdge(final PathNode parent, final PathNode child) {
        final List<GraphEdge> edges = this.getEdgesById().get(parent.getId());
        for (final GraphEdge edge : edges)
            if (edge.getLastNode().getId() == child.getId()) return edge;
        return null;
    }

    public List<GraphEdge> computeEdgeSequence(final List<PathNode> interestingNodesSequence) {
        final List<GraphEdge> edges = Lists.newArrayList();
        PathNode previous = interestingNodesSequence.get(0);
        for (int i = 1; i < interestingNodesSequence.size(); ++i) {
            final PathNode current = interestingNodesSequence.get(i);
            edges.add(this.findEdge(previous, current));
            previous = current;
        }
        return edges;
    }

}