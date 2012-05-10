
package fr.ut7.dojo.pacman.graph;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import fr.ut7.dojo.pacman.model.Board;
import fr.ut7.dojo.pacman.model.Constants;
import fr.ut7.dojo.pacman.view.BoardView;

public class TreeOfWalk {

    private static Board showWalk(final Board board, final GraphEdge edge) {
        final char[] array = board.toCharArray();
        final char c = edge.getMove().toString().charAt(0);
        array[edge.getFirstNode().getId()] = c;
        for (final GraphNode node : edge.getBetweenNodes())
            array[node.getId()] = c;
        array[edge.getLastNode().getId()] = Constants.PACMAN;
        final Board newBoard = Board.from(array);
        System.out.println(new BoardView().render(newBoard));
        try {
            Thread.sleep(350);
        }
        catch (final InterruptedException e) {}
        return newBoard;
    }

    public static void showWalk(final Board board, final List<GraphEdge> path) {
        Board boardCopy = board;
        for (final GraphEdge edge : path)
            boardCopy = showWalk(boardCopy, edge);
    }

    private final Map<Integer, PathNode> map;
    private final int numberOfInterestingNodes;
    private final Map<Integer, List<GraphEdge>> edgesById;
    private final int startingNode;

    // TODO vérifier au niveau du builder que map.size() == Graph.numberOfInterestingNodes 
    public TreeOfWalk(final int startingNode, final Map<Integer, PathNode> map, final Map<Integer, List<GraphEdge>> edgesById) {
        this.map = map;
        this.numberOfInterestingNodes = map.size();
        this.edgesById = edgesById;
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

    public List<PathNode> computeInterestingNodesSequence() {
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

    private GraphEdge findEdge(final PathNode parent, final PathNode child) {
        final List<GraphEdge> edges = this.edgesById.get(parent.getId()); // TODO
        for (final GraphEdge edge : edges)
            if (edge.getLastNode().getId() == child.getId()) return edge;
        return null;
    }

    public List<GraphEdge> computePath(final List<PathNode> interestingNodesSequence) {
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