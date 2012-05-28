
package tdd;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import com.google.common.base.Stopwatch;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSortedMap;
import com.google.common.collect.ImmutableSortedMap.Builder;
import com.google.common.collect.Lists;
import com.google.common.collect.Ordering;
import com.google.common.collect.Sets;

import fr.ut7.dojo.pacman.graph.Vertex;
import fr.ut7.dojo.pacman.graph.Vertex.Type;
import fr.ut7.dojo.pacman.model.Board;
import fr.ut7.dojo.pacman.model.Constants;
import fr.ut7.dojo.pacman.model.Direction;
import fr.ut7.dojo.pacman.model.Levels;
import fr.ut7.dojo.pacman.model.Move;
import fr.ut7.dojo.pacman.view.BoardView;

public class GameGraphBuilder {

    private final Board board;
    private Map<Integer, List<Edge>> edgeByNodeId;

    public GameGraphBuilder(final Board board) {
        this.board = board;
    }

    private Map<Integer, Vertex> buildVertices(final Board board) {
        final Builder<Integer, Vertex> builder = new ImmutableSortedMap.Builder<Integer, Vertex>(Ordering.natural());
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
            if (!options.isEmpty()) builder.put(i, Vertex.from(i, options));
        }
        return builder.build();
    }

    private Map<Integer, List<Edge>> buildEdges(final Map<Integer, Vertex> nodes) {
        final Builder<Integer, List<Edge>> builder = new ImmutableSortedMap.Builder<Integer, List<Edge>>(Ordering.natural());
        for (final Vertex node : nodes.values()) {
            if (!node.is(Type.STREET)) {
                final List<Edge> edges = Lists.newArrayList();
                for (final Move move : node.getOptions()) {
                    final List<Integer> betweenNodes = Lists.newArrayList();
                    Vertex currentNode = node;
                    while ((currentNode = nodes.get(currentNode.getId() + move.getDelta())).is(Type.STREET))
                        betweenNodes.add(currentNode.getId());
                    edges.add(new Edge(node.getId(), betweenNodes, currentNode.getId()));
                }
                Collections.sort(edges);
                builder.put(node.getId(), ImmutableList.copyOf(edges));
            }
        }
        return builder.build();
    }

    public GameGraph build() { // TODO vérifier que le graphe de départ est connecté
        final Map<Integer, Vertex> vertices = this.buildVertices(this.board);
        this.edgeByNodeId = this.buildEdges(vertices);
        return new GameGraph(this.board, vertices, this.edgeByNodeId);
    }

    public static void main(final String[] args) throws InterruptedException {

        final Stopwatch stopwatch = new Stopwatch().start();

        final String data = Levels.DEBUG15;
        final int pacManPosition = data.indexOf(Constants.PACMAN);
        final char[] array = Board.from(data).toCharArray();
        if (pacManPosition > 0) array[pacManPosition] = Constants.SPACE;
        final Board board = Board.from(array);

        final GameGraphBuilder builder = new GameGraphBuilder(board);
        final GameGraph gameGraph = builder.build();

        final ClosedCPPSolver closedCPPSolver = new ClosedCPPSolver(gameGraph);

        System.out.println(closedCPPSolver.getLowerBoundCost());
        System.out.println(closedCPPSolver.getExtraCost());
        System.out.println(closedCPPSolver.getTotalCost());

        final List<Integer> trail = closedCPPSolver.solveFrom(8);

        stopwatch.stop();
        System.out.println(stopwatch.elapsedTime(TimeUnit.MILLISECONDS) + " " + TimeUnit.MILLISECONDS.toString());

        System.out.println(new BoardView().render(board));
        closedCPPSolver.debugTrail(trail);

    }
}