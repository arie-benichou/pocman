
package tdd;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import com.google.common.base.Preconditions;
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

public class GameGraphBuilder {

    private final Board board;
    private Map<Integer, List<Edge>> edgeByVertexId;

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

    private Map<Integer, List<Edge>> buildEdges(final Map<Integer, Vertex> gameTiles) {
        final Builder<Integer, List<Edge>> builder = new ImmutableSortedMap.Builder<Integer, List<Edge>>(Ordering.natural());
        for (final Vertex gameTile : gameTiles.values()) {
            if (!gameTile.is(Type.STREET)) {
                final List<Edge> edges = Lists.newArrayList();
                for (final Move move : gameTile.getOptions()) {
                    final List<Integer> betweenTiles = Lists.newArrayList();
                    Vertex currentTile = gameTile;
                    while ((currentTile = gameTiles.get(currentTile.getId() + move.getDelta())).is(Type.STREET))
                        betweenTiles.add(currentTile.getId());
                    edges.add(new Edge(gameTile.getId(), betweenTiles, currentTile.getId()));
                }
                Collections.sort(edges);
                builder.put(gameTile.getId(), ImmutableList.copyOf(edges));
            }
        }
        return builder.build();
    }

    public GameGraph build() { // TODO vérifier que le graphe de départ est connecté
        final Map<Integer, Vertex> walkableGameTiles = this.buildVertices(this.board);
        this.edgeByVertexId = this.buildEdges(walkableGameTiles);
        return new GameGraph(this.board, walkableGameTiles, this.edgeByVertexId);
    }

    public static void main(final String[] args) throws InterruptedException {

        final Stopwatch stopwatch = new Stopwatch().start();

        final String data = Levels.DEBUG122;
        //final String data = Levels.DEBUG122;
        //final String data = Levels.DEBUG11333;
        //final String data = Levels.LEVEL15;
        //final String data = Levels.LEVEL01C;
        //final String data = Levels.LEVEL13;
        //final String data = Levels.LEVEL155;
        //final String data = Levels.DEBUG1145;

        final int pacManPosition = data.indexOf(Constants.PACMAN);
        final char[] array = Board.from(data).toCharArray();
        if (pacManPosition > 0) array[pacManPosition] = Constants.SPACE;
        final Board board = Board.from(array);

        final GameGraphBuilder builder = new GameGraphBuilder(board);
        final GameGraph gameGraph = builder.build();

        //System.out.println(gameGraph.getNumberOfWalkableGameTiles());

        final ClosedCPPSolver closedCPPSolver = new ClosedCPPSolver(gameGraph);

        System.out.println(closedCPPSolver.getLowerBoundCost());
        System.out.println(closedCPPSolver.getExtraCost());
        System.out.println(closedCPPSolver.getTotalCost());

        System.out.println(closedCPPSolver.getOddVerticeIndexes());

        Preconditions.checkState(pacManPosition > -1, "POCMAN POSITION NOT FOUND !");

        final Integer startingVertexIndex = gameGraph.getVertexIndexByVertexId(pacManPosition);
        //final Integer startingVertexIndex = gameGraph.getVertexIndexByVertexId(417);
        //final Integer startingVertexIndex = gameGraph.getVertexIndexByVertexId(252);
        //final Integer startingVertexIndex = gameGraph.getVertexIndexByVertexId(469);
        //final Integer startingVertexIndex = gameGraph.getVertexIndexByVertexId(408);

        //final Integer startingVertexIndex = 0;

        final List<Integer> closedTrail = closedCPPSolver.solveFrom(startingVertexIndex);

        stopwatch.stop();
        System.out.println(stopwatch.elapsedTime(TimeUnit.SECONDS) + " " + TimeUnit.SECONDS.toString());

        /*
        Thread.sleep(1000);
        System.out.println(new BoardView().render(board));
        closedCPPSolver.debugTrail(closedTrail);
        */

        //final Path path = closedCPPSolver.solveFrom2(startingVertexIndex);
        //System.out.println(path);

        //System.exit(0);

        final OpenCPPSolver openCPPSolver = new OpenCPPSolver(closedCPPSolver);

        //final ClosedCPPSolver bestClosedCPPSolver = openCPPSolver.solveFrom(startingVertexIndex);
        openCPPSolver.solveFrom(startingVertexIndex);

        /*
        final List<Integer> openTrail = bestClosedCPPSolver.solveFrom(startingVertexIndex);

        for (final Integer integer : openTrail) {
            System.out.println(integer);
        }

        //openTrail.remove(0);
        //openTrail.remove(openTrail.size() - 1);

        bestClosedCPPSolver.debugTrail(openTrail);
        */

    }
}