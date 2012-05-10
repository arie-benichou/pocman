
package fr.ut7.dojo.pacman.graph;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.google.common.base.Predicate;
import com.google.common.base.Stopwatch;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSortedMap;
import com.google.common.collect.ImmutableSortedMap.Builder;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Ordering;
import com.google.common.collect.Sets;

import fr.ut7.dojo.pacman.graph.GraphNode.Type;
import fr.ut7.dojo.pacman.model.BestGhostMoveSearch;
import fr.ut7.dojo.pacman.model.Board;
import fr.ut7.dojo.pacman.model.Constants;
import fr.ut7.dojo.pacman.model.Direction;
import fr.ut7.dojo.pacman.model.Game;
import fr.ut7.dojo.pacman.model.GameState;
import fr.ut7.dojo.pacman.model.GhostReferee;
import fr.ut7.dojo.pacman.model.HalfRandomPacmanMoveEmitter;
import fr.ut7.dojo.pacman.model.Levels;
import fr.ut7.dojo.pacman.model.Move;
import fr.ut7.dojo.pacman.model.PacmanReferee;
import fr.ut7.dojo.pacman.model.characters.Ghost;
import fr.ut7.dojo.pacman.model.characters.Pacman;
import fr.ut7.dojo.pacman.view.BoardView;

// TODO ! pathPruning
// TODO ? visitor pattern
// TODO ? prendre en compte la distance
// TODO ? chaque node pourrait contenir ses edges
public final class Graph {

    private static Map<Integer, GraphNode> createNodes(final Board board) {
        final Builder<Integer, GraphNode> builder = new ImmutableSortedMap.Builder<Integer, GraphNode>(Ordering.natural());
        for (int i = 0; i < Board.SIZE; ++i) {
            final HashSet<Move> options = Sets.newHashSet();
            if (board.getCell(i) == Constants.PILL || board.getCell(i) == Constants.SPACE) {
                final List<Direction> directions = board.getDirections(i);
                for (final Direction direction : directions) {
                    final Move move = Move.from(direction);
                    final int neighbour = i + move.getDelta();
                    if (board.getCell(neighbour) == Constants.PILL || board.getCell(neighbour) == Constants.SPACE) options.add(move);
                }
            }
            if (!options.isEmpty()) builder.put(i, GraphNode.from(i, options));
        }
        return builder.build();
    }

    private static GraphEdge createEdge(final Map<Integer, GraphNode> NODES, final GraphNode firstNode, final Move move) {
        final List<GraphNode> nodes = Lists.newArrayList();
        GraphNode currentNode = firstNode;
        while ((currentNode = NODES.get(currentNode.getId() + move.getDelta())).is(Type.STREET))
            nodes.add(currentNode);
        return new GraphEdge(move, firstNode, NODES.get(currentNode.getId()), nodes);
    }

    private static List<GraphEdge> createEdges(final Map<Integer, GraphNode> NODES, final GraphNode node) {
        final List<GraphEdge> edges = Lists.newArrayList();
        for (final Move move : node.getOptions())
            edges.add(createEdge(NODES, node, move));
        Collections.sort(edges);
        return ImmutableList.copyOf(edges);
    }

    private final static Predicate<GraphNode> IS_INTERESTING_NODE = new Predicate<GraphNode>() {

        public boolean apply(final GraphNode node) {
            return !node.is(Type.STREET);
        }

    };

    private static Map<Integer, GraphNode> computeInterestingNodes(final Map<Integer, GraphNode> nodes) {
        return Maps.filterValues(nodes, IS_INTERESTING_NODE);
    }

    private static Map<Integer, List<GraphEdge>> computesEdges(final Map<Integer, GraphNode> nodes, final Map<Integer, GraphNode> interestingNodes) {
        final Builder<Integer, List<GraphEdge>> builder = new ImmutableSortedMap.Builder<Integer, List<GraphEdge>>(Ordering.natural());
        for (final GraphNode node : interestingNodes.values())
            builder.put(node.getId(), Lists.newArrayList(createEdges(nodes, node)));
        return builder.build();
    }

    /*
    private static void debug(final Object object, final int i) {
        System.out.println(Strings.repeat("    ", i) + (object == null ? "NULL" : object.toString()));
    }

    private static void debugNode(final TransientNode node) {
        debugNode(node, 0);
    }

    private static void debugNode(final TransientNode node, final int i) {
        debug(node, i);
        if (node.hasChild()) {
            final List<TransientNode> children = node.getChildren();
            for (final TransientNode nextNode : children) {
                debugNode(nextNode, i + 1);
            }
        }
    }
    */

    private final Map<Integer, List<GraphEdge>> edgesById;
    private final Map<Integer, GraphNode> nodeById;

    private final int numberOfNodes;

    public int getNumberOfNodes() {
        return this.numberOfNodes;
    }

    private final int numberOfInterestingNodes;

    public int getNumberOfInterestingNodes() {
        return this.numberOfInterestingNodes;
    }

    public Graph(final Board board) {
        this.nodeById = createNodes(board);
        this.numberOfNodes = this.nodeById.size();
        final Map<Integer, GraphNode> interestingNodes = computeInterestingNodes(this.nodeById);
        this.numberOfInterestingNodes = interestingNodes.size();
        this.edgesById = computesEdges(this.nodeById, interestingNodes);
    }

    private int exploreTreeNodes(final TransientNode parent, int descendants, final Set<Integer> ids) {
        final List<GraphEdge> edges = this.edgesById.get(parent.getId());
        int descendantsOfParentNode = descendants;
        for (final GraphEdge edge : edges) {
            if (ids.contains(edge.getLastNode().getId())) continue;
            final TransientNode child = new TransientNode(edge.getLastNode());
            ++descendants;
            parent.addChild(child);
            ids.add(edge.getLastNode().getId());
            ++descendantsOfParentNode;
            final int nextDescendants = this.exploreTreeNodes(child, 0, ids);
            descendantsOfParentNode += nextDescendants;
        }
        parent.setNumberOfDescendants(descendantsOfParentNode - descendants);
        return descendantsOfParentNode;
    }

    private TransientNode computeTree(final int initialInterestingNode) {
        final GraphNode rootNode = this.nodeById.get(initialInterestingNode);
        final TransientNode parent = new TransientNode(rootNode);
        final Set<Integer> ids = Sets.newHashSet(parent.getId());
        this.exploreTreeNodes(parent, 0, ids);
        return parent;
    }

    public TreeOfWalk getTreeOfWalk(final int initialInterestingNode) {
        final TransientNode mutableTree = this.computeTree(initialInterestingNode);
        //debugNode(mutableTree);
        final PathBuilder pathBuilder = new PathBuilder();
        final Map<Integer, PathNode> map = pathBuilder.build(mutableTree);
        final TreeOfWalk treeOfWalk = new TreeOfWalk(mutableTree.getId(), map, this.edgesById);
        return treeOfWalk;
    }

    @Override
    public String toString() {
        final char[] array = new char[Board.SIZE];
        for (int i = 0; i < Board.SIZE; ++i) {
            final GraphNode node = this.nodeById.get(i);
            final int n = node == null ? 0 : node.getNumberOfOptions();
            char c;
            switch (n) {
                case 0:
                    c = ' ';
                    break;
                case 2:
                    c = node.is(Type.STREET) ? '⬤' : '2';
                    break;
                default:
                    c = String.valueOf(n).charAt(0);
            }
            array[i] = c;
        }
        final StringBuilder sb = new StringBuilder(new BoardView().render(Board.from(array)));
        sb.append("number of nodes: " + this.getNumberOfNodes()).append("\n");
        sb.append("number of interesting nodes: " + this.getNumberOfInterestingNodes()).append("\n");
        sb.append("\n");
        sb.append("edges: ").append("\n");
        for (final Entry<Integer, List<GraphEdge>> entry : this.edgesById.entrySet()) {
            sb.append("\n").append(entry.getKey()).append("\n");
            for (final GraphEdge edge : entry.getValue())
                sb.append(" " + edge).append("\n");
        }
        return sb.toString();
    }

    public static void main(final String[] args) {

        final Game game = Game.from(
                new PacmanReferee(), new Pacman(new HalfRandomPacmanMoveEmitter()),
                new GhostReferee(), new Ghost(new BestGhostMoveSearch()),
                GameState.from(
                        //Levels.DEBUG113
                        Levels.LEVEL155
                        ));

        final Board board = game.getBoard();
        final Graph graph = new Graph(board);

        //System.out.println(graph);

        final Stopwatch stopwatch = new Stopwatch().start();

        final TreeOfWalk treeOfWalk = graph.getTreeOfWalk(game.getPacmanPosition());
        final List<PathNode> sequence = treeOfWalk.computeInterestingNodesSequence();
        final List<GraphEdge> path = treeOfWalk.computePath(sequence);

        //System.out.println(stopwatch.elapsedTime(TimeUnit.MICROSECONDS));

        TreeOfWalk.showWalk(board, path);

        int c = 0;
        for (final GraphEdge edge : path) {
            c += edge.getBetweenNodes().size();
        }

        System.out.println(c);

    }
}