
package fr.ut7.dojo.pacman.graph;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

public class TreeOfWalk {

    private final Map<Integer, PathNode> map;
    private final int numberOfInterestingNodes;
    private final int startingNode;
    private final Map<Integer, List<GraphEdge>> edgesById;

    // TODO vérifier au niveau du builder que map.size() == Graph.numberOfInterestingNodes 
    public TreeOfWalk(final int startingNode, final Map<Integer, PathNode> map, final Map<Integer, List<GraphEdge>> edgesById) {
        this.map = map;
        this.numberOfInterestingNodes = map.size();
        this.startingNode = startingNode;
        this.edgesById = edgesById;
    }

    public Map<Integer, PathNode> getMap() {
        return this.map;
    }

    public Map<Integer, List<GraphEdge>> getEdgesById() {
        return this.edgesById;
    }

    public int getStartingNode() {
        return this.startingNode;
    }

    private PathNode findNextNodeToVisit(final PathNode node, final Set<Integer> visitedNodes, final Set<GraphEdge> visitedEdges) {
        List<Integer> candidates = Lists.newArrayList();

        if (node.getId() == 410) {
            System.out.println("410");
        }

        for (final Integer child : node.getChildren())
            if (!visitedNodes.contains(child)) candidates.add(child);

        boolean flag = false;
        if (candidates.isEmpty()) {
            flag = true;
            final List<GraphEdge> edges = this.edgesById.get(node.getId());
            for (final GraphEdge graphEdge : edges) {
                //System.out.println(graphEdge);
                //System.out.println(visitedEdges.contains(graphEdge));
                if (!visitedEdges.contains(graphEdge)) {
                    candidates.add(graphEdge.getLastNode().getId());
                }
            }

        }

        if (candidates.isEmpty()) return this.map.get(node.getParent());
        final List<Integer> candidatesCopy = Lists.newArrayList();
        if (candidates.size() > 1 && flag) {
            System.out.println(candidates);
            for (final Integer c : candidates) {
                //System.out.println(c);
                final GraphEdge edge = this.findEdge(this.map.get(c), node);
                //System.out.println(edge);
                //System.out.println(visitedEdges.contains(edge));
                if (!visitedEdges.contains(edge)) candidatesCopy.add(c);
            }
            candidates = candidatesCopy;
            System.out.println(candidates);
        }

        return this.map.get(candidates.get(0));
    }

    private GraphEdge findEdge(final PathNode parent, final PathNode child) {
        final List<GraphEdge> edges = this.getEdgesById().get(parent.getId());
        for (final GraphEdge edge : edges)
            if (edge.getLastNode().getId() == child.getId()) return edge;
        return null;
    }

    public List<PathNode> computeNodeSequence() {
        PathNode childNode = this.map.get(this.startingNode);
        final List<PathNode> path = Lists.newArrayList(childNode);
        final Set<Integer> visitedNodes = Sets.newHashSet(childNode.getId());
        final Set<GraphEdge> visitedEdges = Sets.newHashSet();
        while (visitedNodes.size() != this.numberOfInterestingNodes) {
            final PathNode parentNode = childNode;
            childNode = this.findNextNodeToVisit(parentNode, visitedNodes, visitedEdges);
            visitedNodes.add(childNode.getId());
            visitedEdges.add(this.findEdge(parentNode, childNode));
            path.add(childNode);
        }
        return path;
    }

    public List<PathNode> computeArcSequence() {
        /*
        PathNode node = this.map.get(this.startingNode);
        final List<PathNode> path = Lists.newArrayList(node);
        final Set<Integer> visitedNodes = Sets.newHashSet(node.getId());
        while (visitedNodes.size() != this.numberOfInterestingNodes) {
            node = this.findNextNodeToVisit(node, visitedNodes);
            visitedNodes.add(node.getId());
            path.add(node);
        }
        return path;
        */

        for (final Entry<Integer, List<GraphEdge>> entry : this.edgesById.entrySet()) {
            System.out.println(entry);
        }

        return null;

    }

}