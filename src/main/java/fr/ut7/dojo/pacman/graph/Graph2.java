
package fr.ut7.dojo.pacman.graph;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import algorithm.RoyWarshallFloyd;

import com.google.common.base.Predicate;
import com.google.common.base.Strings;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSortedMap;
import com.google.common.collect.ImmutableSortedMap.Builder;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Ordering;
import com.google.common.collect.Sets;

import cpp.Arc;
import cpp.CPPSolution;
import cpp.Graph;
import cpp.OpenCPPSolver;
import fr.ut7.dojo.pacman.graph.Vertex.Type;
import fr.ut7.dojo.pacman.model.Board;
import fr.ut7.dojo.pacman.model.Constants;
import fr.ut7.dojo.pacman.model.Direction;
import fr.ut7.dojo.pacman.model.Game;
import fr.ut7.dojo.pacman.model.GameState;
import fr.ut7.dojo.pacman.model.GhostReferee;
import fr.ut7.dojo.pacman.model.Levels;
import fr.ut7.dojo.pacman.model.Move;
import fr.ut7.dojo.pacman.model.NullMoveEmitter;
import fr.ut7.dojo.pacman.model.PacmanReferee;
import fr.ut7.dojo.pacman.model.characters.Ghost;
import fr.ut7.dojo.pacman.model.characters.Pacman;
import fr.ut7.dojo.pacman.view.BoardView;
import fr.ut7.dojo.pacman.view.GameView;

// TODO ! pathPruning
// TODO ? visitor pattern
// TODO ? prendre en compte la distance
// TODO ? chaque node pourrait contenir ses edges
public final class Graph2 {

    private static Map<Integer, Vertex> createNodes(final Board board) {
        final Builder<Integer, Vertex> builder = new ImmutableSortedMap.Builder<Integer, Vertex>(Ordering.natural());
        for (int i = 0; i < Board.SIZE; ++i) {
            /*
            System.out.print(i);
            System.out.print(": ");
            System.out.print(board.getCell(i));
            System.out.println();
            */
            final HashSet<Move> options = Sets.newHashSet();
            if (board.getCell(i) == Constants.PILL || board.getCell(i) == Constants.SPACE) {
                final List<Direction> directions = board.getDirections(i);
                for (final Direction direction : directions) {
                    final Move move = Move.from(direction);
                    final int neighbour = i + move.getDelta();
                    if (board.getCell(neighbour) == Constants.PILL || board.getCell(neighbour) == Constants.SPACE) options.add(move);
                }
            }
            if (!options.isEmpty()) builder.put(i, Vertex.from(i, options));
        }
        return builder.build();
    }

    private static GraphEdge createEdge(final Map<Integer, Vertex> NODES, final Vertex firstNode, final Move move) {
        final List<Vertex> nodes = Lists.newArrayList();
        Vertex currentNode = firstNode;
        while ((currentNode = NODES.get(currentNode.getId() + move.getDelta())).is(Type.STREET))
            nodes.add(currentNode);
        return new GraphEdge(move, firstNode, NODES.get(currentNode.getId()), nodes);
    }

    private static List<GraphEdge> createEdges(final Map<Integer, Vertex> NODES, final Vertex node) {
        final List<GraphEdge> edges = Lists.newArrayList();
        for (final Move move : node.getOptions())
            edges.add(createEdge(NODES, node, move));
        Collections.sort(edges);
        return ImmutableList.copyOf(edges);
    }

    private final static Predicate<Vertex> HAS_ODD_DEGREE = new Predicate<Vertex>() {

        public boolean apply(final Vertex node) {
            return node.getNumberOfOptions() % 2 == 1; //&& node.getNumberOfOptions() > 1;
        }

    };

    private final static Predicate<Vertex> IS_INTERESTING_NODE = new Predicate<Vertex>() {

        public boolean apply(final Vertex node) {
            return !node.is(Type.STREET);
        }

    };

    private static Map<Integer, Vertex> computeInterestingNodes(final Map<Integer, Vertex> nodes) {
        return ImmutableSortedMap.copyOf(Maps.filterValues(nodes, IS_INTERESTING_NODE));
    }

    private static Map<Integer, List<GraphEdge>> computesEdges(final Map<Integer, Vertex> nodes, final Map<Integer, Vertex> interestingNodes) {
        final Builder<Integer, List<GraphEdge>> builder = new ImmutableSortedMap.Builder<Integer, List<GraphEdge>>(Ordering.natural());
        for (final Vertex node : interestingNodes.values())
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
    private final Map<Integer, GraphEdge> edgesByHashCode;

    public Map<Integer, List<GraphEdge>> getEdgesById() {
        return this.edgesById;
    }

    private final Map<Integer, Vertex> nodeById;

    private final int numberOfNodes;

    public int getNumberOfNodes() {
        return this.numberOfNodes;
    }

    private final int numberOfInterestingNodes;

    private final Board board;

    private final Map<Integer, Vertex> interestingNodes;

    private final Map<Integer, Integer> nodeByOrdinal;

    private final Map<Integer, Integer> ordinalByNode;

    public int getNumberOfInterestingNodes() {
        return this.numberOfInterestingNodes;
    }

    public Graph2(final Board board) {
        this.board = board;
        this.nodeById = createNodes(board);
        this.numberOfNodes = this.nodeById.size();
        this.interestingNodes = computeInterestingNodes(this.nodeById);

        /////////////////////////////////////////////
        final Builder<Integer, Integer> interestingNodeByOrdinalBuilder = new ImmutableSortedMap.Builder<Integer, Integer>(Ordering.natural());
        final Builder<Integer, Integer> ordinalByInterestingNodeBuilder = new ImmutableSortedMap.Builder<Integer, Integer>(Ordering.natural());
        int ordinal = 0;
        for (final Entry<Integer, Vertex> entry : this.interestingNodes.entrySet()) {
            interestingNodeByOrdinalBuilder.put(ordinal, entry.getKey());
            ordinalByInterestingNodeBuilder.put(entry.getKey(), ordinal);
            ++ordinal;
        }
        this.nodeByOrdinal = interestingNodeByOrdinalBuilder.build();
        this.ordinalByNode = ordinalByInterestingNodeBuilder.build();
        System.out.println(this.nodeByOrdinal);
        System.out.println(this.ordinalByNode);
        /////////////////////////////////////////////

        this.numberOfInterestingNodes = this.interestingNodes.size();
        this.edgesById = computesEdges(this.nodeById, this.interestingNodes);

        this.edgesByHashCode = Maps.newHashMap();
        for (final List<GraphEdge> edges : this.edgesById.values()) {
            for (final GraphEdge edge : edges) {
                this.edgesByHashCode.put(edge.getFirstNode().getId() * edge.getLastNode().getId(), edge);
            }
        }

        //System.out.println(this.edgesByHashCode.size());

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
        final Vertex rootNode = this.nodeById.get(initialInterestingNode);
        final TransientNode parent = new TransientNode(rootNode);
        final Set<Integer> ids = Sets.newHashSet(parent.getId());
        this.exploreTreeNodes(parent, 0, ids);
        return parent;
    }

    public TreeOfWalk getTreeOfWalk(final int initialInterestingNode) {
        final TransientNode mutableTree = this.computeTree(initialInterestingNode);
        //debugNode(mutableTree);

        final PathBuilder pathBuilder = new PathBuilder();
        //final PathBuilder2 pathBuilder = new PathBuilder2(this.edgesById, this.board);

        final Map<Integer, PathNode> map = pathBuilder.build(mutableTree);
        final TreeOfWalk treeOfWalk = new TreeOfWalk(mutableTree.getId(), map, this.edgesById);

        return treeOfWalk;
    }

    public int[][] computeRoyWarshallFloydMatrix() {
        final int[][] matrix = new int[this.getNumberOfInterestingNodes()][this.getNumberOfInterestingNodes()];
        final List<Integer> keys = Lists.newArrayList(this.interestingNodes.keySet());
        System.out.println(keys);
        for (int i = 0; i < keys.size(); ++i) {
            for (int j = 0; j < keys.size(); ++j) {
                if (keys.get(i).equals(keys.get(j))) matrix[i][j] = 0;
                else {
                    final GraphEdge edge = this.edgesByHashCode.get(keys.get(i) * keys.get(j));
                    matrix[i][j] = edge == null ? Integer.MAX_VALUE : edge.getValue();
                }
            }
        }
        return matrix;
    }

    @Override
    public String toString() {
        final char[] array = new char[Board.SIZE];
        for (int i = 0; i < Board.SIZE; ++i) {
            final Vertex node = this.nodeById.get(i);
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
        final StringBuilder sb = new StringBuilder("\n");
        sb.append("Graph: ").append("\n");
        sb.append(new BoardView().render(Board.from(array)).replaceAll(" {27}\\n", "")).append("\n");
        sb.append("Walkable space length: " + this.getNumberOfNodes()).append("\n");
        sb.append("      Number of nodes: " + Strings.padStart(String.valueOf(this.getNumberOfInterestingNodes()), 2, '0')).append("\n");
        sb.append("\n");
        /*
        sb.append("Edges: ").append("\n");
        int n = 0;
        for (final Entry<Integer, List<GraphEdge>> entry : this.edgesById.entrySet()) {
            sb.append("\n").append(entry.getKey()).append("\n");
            for (final GraphEdge edge : entry.getValue()) {
                sb.append(" " + edge).append("\n");
                ++n;
            }
        }
        sb.append("\n");
        sb.append("Number of Edges: " + n / 2).append("\n");
        */
        return sb.toString();
    }

    /*
    public static void main(final String[] args) {
        final Game game = Game.from(
                new PacmanReferee(), new Pacman(new NullMoveEmitter()),
                new GhostReferee(), new Ghost(new NullMoveEmitter()),
                GameState.from(Levels.DEBUG114)
                );

        final Board board = game.getBoard();
        System.out.println(new GameView().render(game));
        final Graph graph = new Graph(board);
        System.out.println(graph);

        //final Map<Integer, List<GraphEdge>> edgesById2 = graph.getEdgesById();
        //for (final String string : args) {}

        final Map<Integer, GraphNode> oddNodes = Maps.filterValues(graph.nodeById, HAS_ODD_DEGREE);
        System.out.println(oddNodes.size());
        //System.out.println(oddNodes);

        for (final Entry<Integer, GraphNode> entry : oddNodes.entrySet()) {
            System.out.println(this.entry);
        }

        final ArrayList<Integer> keys = Lists.newArrayList(this.oddNodes.keySet());
        System.out.println(keys);

        final int n = this.oddNodes.size();
        //final int[][] matrix = new int[n][n];
        for (final Integer node1 : keys) {
            for (final Integer node2 : this.keys) {
                if (this.node1 == node2) continue;

                System.out.print(this.node1 + "-" + node2 + ": ");
                final List<GraphEdge> e1 = graph.getEdgesById().get(this.node1);

                for (final GraphEdge edge : e1) {
                    if (edge.getLastNode().getId() == node2) {
                        System.out.print(": ok!");
                        System.out.println();
                        System.out.println(edge);
                    }
                }

                System.out.println();
            }
        }

    }
    */

    public static void main(final String[] args) {

        final Game game = Game.from(
                new PacmanReferee(), new Pacman(new NullMoveEmitter()),
                new GhostReferee(), new Ghost(new NullMoveEmitter()),
                GameState.from(Levels.DEBUG12)
                );

        final Board board = game.getBoard();
        System.out.println(new GameView().render(game));

        final Graph2 graph = new Graph2(board);
        System.out.println(graph);

        final int[][] matrix = graph.computeRoyWarshallFloydMatrix();
        RoyWarshallFloyd.debug(matrix);

        final int[][] paths = RoyWarshallFloyd.from(matrix);
        RoyWarshallFloyd.debug(paths);

        //System.exit(0);

        /*
        for (final int[] row : paths) {
            final List<List<Integer>> partition = Lists.partition(Ints.asList(row), 2);
            System.out.println(partition);
        }
        */

        /*
        System.out.println();
        final Map<Integer, GraphNode> oddNodes = Maps.filterValues(graph.nodeById, HAS_ODD_DEGREE);
        final List<Integer> list = Lists.newArrayList(oddNodes.keySet());
        System.out.println(oddNodes.size());
        System.out.println(list);
        final List<Integer> indexes = Lists.transform(list, new Function<Integer, Integer>() {

            public Integer apply(final Integer input) {
                return graph.ordinalByNode.get(input);
            }
        });
        System.out.println(indexes);
        */

        /*
        final int[][] oddMatrix = new int[oddNodes.size()][oddNodes.size()];
        int r = 0;
        for (final Entry<Integer, GraphNode> entry : oddNodes.entrySet()) {
            System.out.println(entry);
            final Integer i = graph.ordinalByNode.get(entry.getKey());
            final int[] row = paths[i];

            //            System.out.println(Ints.asList(row));
            //            for (final int j : indexes) {
            //                System.out.println(row[j]);
            //            }

            for (int c = 0; c < indexes.size(); c++) {
                //System.out.println(row[indexes.get(c)]);
                oddMatrix[r][c] = row[indexes.get(c)];
            }

            ++r;
            //break;
        }
        */

        /*
        System.out.println();
        RoyWarshallFloyd.debug(oddMatrix);
        System.out.println();

        System.out.println("[0][2] = 26.0");
        System.out.println("[1][0] = 24.0");
        System.out.println("[2][1] = 2.0");
        System.out.println("[3][4] = 31.0");
        System.out.println("[4][5] = 27.0");
        System.out.println("[5][3] = 4.0");

        System.out.println();
        System.out.print(graph.nodeByOrdinal.get(indexes.get(0)));
        System.out.print(" - ");
        System.out.print(graph.nodeByOrdinal.get(indexes.get(2)));
        System.out.println();
        System.out.print(graph.nodeByOrdinal.get(indexes.get(1)));
        System.out.print(" - ");
        System.out.print(graph.nodeByOrdinal.get(indexes.get(0)));
        System.out.println();
        System.out.print(graph.nodeByOrdinal.get(indexes.get(2)));
        System.out.print(" - ");
        System.out.print(graph.nodeByOrdinal.get(indexes.get(1)));
        System.out.println();
        System.out.print(graph.nodeByOrdinal.get(indexes.get(3)));
        System.out.print(" - ");
        System.out.print(graph.nodeByOrdinal.get(indexes.get(4)));
        System.out.println();
        System.out.print(graph.nodeByOrdinal.get(indexes.get(4)));
        System.out.print(" - ");
        System.out.print(graph.nodeByOrdinal.get(indexes.get(5)));
        System.out.println();
        System.out.print(graph.nodeByOrdinal.get(indexes.get(5)));
        System.out.print(" - ");
        System.out.print(graph.nodeByOrdinal.get(indexes.get(3)));
        System.out.println();
        */

        /*
        final List<Integer> keys = Lists.newArrayList(oddNodes.keySet());
        System.out.println(keys);
        final int n = oddNodes.size();
        final int[][] matrix2 = new int[n][n];
        for (int i = 0; i < keys.size(); ++i) {
            for (int j = 0; j < keys.size(); ++j) {
                if (i == j) matrix2[i][j] = 0;
                else {
                    final GraphEdge edge = graph.edgesByHashCode.get(keys.get(i) * keys.get(j));
                    matrix2[i][j] = edge == null ? Integer.MAX_VALUE : edge.getValue();
                }
            }
        }

        RoyWarshallFloyd.debug(matrix2);
        final int[][] paths2 = RoyWarshallFloyd.from(matrix2);
        RoyWarshallFloyd.debug(paths2);
        */

        System.out.println();
        graph.test();

    }

    private void test() {

        System.out.println("#######################################");
        System.out.println(this.getNumberOfInterestingNodes());
        final List<Arc> arcs = Lists.newArrayList();

        final Set<GraphEdge> addedEdges = Sets.newHashSet();
        for (final Entry<Integer, Integer> entry : this.nodeByOrdinal.entrySet()) {
            final Integer key = entry.getKey();
            final Integer node = entry.getValue();
            System.out.println(key + " : " + node);
            final List<GraphEdge> edges = this.edgesById.get(node);
            for (final GraphEdge graphEdge : edges) {
                final GraphEdge edge = this.edgesByHashCode.get(graphEdge.getFirstNode().getId() * graphEdge.getLastNode().getId());
                if (!addedEdges.contains(edge)) {
                    addedEdges.add(edge);
                    final Integer u = this.ordinalByNode.get(edge.getFirstNode().getId());
                    final Integer v = this.ordinalByNode.get(edge.getLastNode().getId());
                    /*
                    Integer v;
                    Integer u;
                    if (this.ordinalByNode.get(edge.getFirstNode().getId()).equals(key)) {
                        u = key;
                        v = this.ordinalByNode.get(edge.getLastNode().getId());
                    }
                    else {
                        u = this.ordinalByNode.get(edge.getFirstNode().getId());
                        v = key;
                    }
                    */
                    final Arc arc =
                                    Arc.from(
                                            "(" + this.ordinalByNode.get(edge.getFirstNode().getId()) + ", "
                                                    + this.ordinalByNode.get(edge.getLastNode().getId()) + ")", u, v, edge.getValue());
                    arcs.add(arc);
                    System.out.println();
                    System.out.println(arc);
                    System.out.println(edge);
                    System.out.println();

                }
            }
            System.out.println();
        }

        /*
        for (final Entry<Integer, Integer> entry : this.nodeByOrdinal.entrySet()) {
            final Integer key = entry.getKey();
            final Integer node = entry.getValue();
            System.out.println(key + " : " + node);
            final List<GraphEdge> edges = this.edgesById.get(node);
            for (final GraphEdge edge : edges) {
                System.out.println(edge);
                
                final Integer u = this.ordinalByNode.get(edge.getFirstNode().getId());
                final Integer v = this.ordinalByNode.get(edge.getLastNode().getId());
                arcs.add(Arc.from(null, u, v, edge.getValue()));

                Integer v;
                Integer u;
                if (this.ordinalByNode.get(edge.getFirstNode().getId()).equals(key)) {
                    u = key;
                    v = this.ordinalByNode.get(edge.getLastNode().getId());
                }
                else {
                    u = this.ordinalByNode.get(edge.getFirstNode().getId());
                    v = key;
                }
                final Arc arc = Arc.from(edge.toString(), u, v, edge.getValue());
                arcs.add(arc);

            }
            System.out.println();
        }
        */

        for (final Arc arc : arcs) {
            System.out.println(arc);
        }

        final cpp.Graph.Builder builder = new cpp.Graph.Builder(this.getNumberOfInterestingNodes());
        builder.add(arcs);
        final cpp.Graph graph = builder.build();
        System.out.println(graph.geTotalCostLowerBound());
        final OpenCPPSolver openCPPSolver = new OpenCPPSolver(graph);

        final CPPSolution cppSolution = openCPPSolver.solveFrom(this.ordinalByNode.get(417));
        System.out.println(cppSolution);
    }

    private void test2(final int[][] matrix) {
        final List<Arc> arcs = Lists.newArrayList();
        final int n = matrix.length;
        for (int i = 0, k = 1; i < n; ++i, ++k)
            for (int j = k; j < n; ++j)
                arcs.add(Arc.from("[" + i + "][" + j + "]", i, j, matrix[i][j]));
        System.out.println(arcs.size());
        for (final Arc arc : arcs)
            System.out.println(arc);

        final Graph graph = new cpp.Graph.Builder(n).add(arcs).build();
        System.out.println(graph.geTotalCostLowerBound());
        final OpenCPPSolver openCPPSolver = new OpenCPPSolver(graph);
        final CPPSolution cppSolution = openCPPSolver.solveFrom(this.ordinalByNode.get(417));
        System.out.println(cppSolution);
    }
}