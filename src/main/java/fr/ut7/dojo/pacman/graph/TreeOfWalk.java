
package fr.ut7.dojo.pacman.graph;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

public class TreeOfWalk {

    private final Map<Integer, PathNode> map;
    private final int numberOfInterestingNodes;
    private final int startingNode;

    // TODO vérifier au niveau du builder que map.size() == Graph.numberOfInterestingNodes 
    public TreeOfWalk(final int startingNode, final Map<Integer, PathNode> map, final Map<Integer, List<GraphEdge>> edgesById) {
        this.map = map;
        this.numberOfInterestingNodes = map.size();
        this.startingNode = startingNode;
    }

    private PathNode findNextNodeToVisit(final PathNode node, final Set<Integer> visitedNodes) {
        final List<Integer> candidates = Lists.newArrayList();
        for (final Integer child : node.getChildren())
            if (!visitedNodes.contains(child)) candidates.add(child);
        if (candidates.isEmpty()) return this.map.get(node.getParent());
        //if (candidates.size() > 1) { /*TODO ?*/}
        return this.map.get(candidates.get(0));
    }

    public List<PathNode> computeNodeSequence() {
        PathNode node = this.map.get(this.startingNode);
        final List<PathNode> path = Lists.newArrayList(node);
        final Set<Integer> visitedNodes = Sets.newHashSet(node.getId());
        while (visitedNodes.size() != this.numberOfInterestingNodes) {
            node = this.findNextNodeToVisit(node, visitedNodes);
            visitedNodes.add(node.getId());
            path.add(node);
        }
        return path;
    }

}