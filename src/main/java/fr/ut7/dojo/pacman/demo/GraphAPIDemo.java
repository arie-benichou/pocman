
package fr.ut7.dojo.pacman.demo;

import java.util.HashMap;
import java.util.List;
import java.util.Set;

import com.google.common.base.Splitter;

import fr.ut7.dojo.pacman.graph.Graph;
import fr.ut7.dojo.pacman.graph.GraphEdge;
import fr.ut7.dojo.pacman.graph.Path;
import fr.ut7.dojo.pacman.graph.PathManager;
import fr.ut7.dojo.pacman.graph.PathNode;
import fr.ut7.dojo.pacman.graph.TreeOfEdges;
import fr.ut7.dojo.pacman.graph.TreeOfWalk;
import fr.ut7.dojo.pacman.model.Board;
import fr.ut7.dojo.pacman.model.Constants;
import fr.ut7.dojo.pacman.model.Game;
import fr.ut7.dojo.pacman.model.GameState;
import fr.ut7.dojo.pacman.model.GhostReferee;
import fr.ut7.dojo.pacman.model.Levels;
import fr.ut7.dojo.pacman.model.NullMoveEmitter;
import fr.ut7.dojo.pacman.model.PacmanReferee;
import fr.ut7.dojo.pacman.model.characters.Ghost;
import fr.ut7.dojo.pacman.model.characters.Pacman;
import fr.ut7.dojo.pacman.view.BoardView;
import fr.ut7.dojo.pacman.view.GameView;
import fr.ut7.dojo.pacman.view.PathView;

public final class GraphAPIDemo {

    private static int depth = 0;

    private GraphAPIDemo() {}

    private static void ShowPath(final Board board, final Path path) {
        final PathView pathView = new PathView(board);
        for (final String data : Splitter.on("\n\n").split(pathView.render(path))) {
            System.out.println(data);
            try {
                Thread.sleep(250);
            }
            catch (final InterruptedException e) {}
        }
    }

    public static void main(final String[] args) {

        final Game game = Game.from(
                new PacmanReferee(), new Pacman(new NullMoveEmitter()),
                new GhostReferee(), new Ghost(new NullMoveEmitter()),
                //GameState.from(Levels.LEVEL13)
                //GameState.from(Levels.LEVEL155)
                //GameState.from(Levels.DEBUG11455)
                //GameState.from(Levels.DEBUG122)
                //GameState.from(Levels.DEBUG1132)
                GameState.from(Levels.DEBUG122)

                //GameState.from(Levels.DEBUG1150)
                //GameState.from(Levels.LEVEL1555)
                //GameState.from(Levels.LEVEL01C)
                //GameState.from(Levels.DEBUG19)
                );

        final Board board = game.getBoard();
        System.out.println(new GameView().render(game));
        //final Path path = solve1(game.getPacmanPosition(), board);
        final Path path = solve2(game.getPacmanPosition(), board);

        ShowPath(board, path);

    }

    private static Path solve1(final int initialPosition, final Board board) {

        System.out.println(depth);
        System.out.println(new BoardView().render(board));
        try {
            Thread.sleep(1000);
        }
        catch (final InterruptedException e) {}

        final Graph graph = new Graph(board);
        System.out.println(graph);
        final TreeOfWalk treeOfWalk = graph.getTreeOfWalk(initialPosition);
        final TreeOfEdges treeOfEdges = new TreeOfEdges(treeOfWalk.getMap(), treeOfWalk.getEdgesById(), board);
        final HashMap<Integer, PathNode> postorderMap = treeOfEdges.postorder(treeOfWalk.getStartingNode());
        final Set<Integer> keySet1 = treeOfWalk.getMap().keySet();
        final Set<Integer> keySet2 = postorderMap.keySet();
        keySet1.removeAll(keySet2);

        if (keySet1.isEmpty() /*|| depth > 0*/) {
            final TreeOfWalk treeOfWalk2 = new TreeOfWalk(treeOfWalk.getStartingNode(), postorderMap, treeOfWalk.getEdgesById());
            final List<PathNode> nodeSequence = treeOfWalk2.computeNodeSequence();
            //final List<PathNode> nodeSequence = treeOfWalk.computeNodeSequence();

            final PathManager pathManager = PathManager.from(graph.getEdgesById());
            final List<GraphEdge> edgeSequence = pathManager.computeEdgeSequence(nodeSequence);
            final Path path = Path.from(edgeSequence);
            return path;
        }
        else {
            final char[] array = board.toCharArray();
            for (final Integer integer : keySet1)
                array[integer] = 'x';
            array[initialPosition] = Constants.PACMAN;
            final GameState state = GameState.from(new String(array));
            ++depth;
            return solve1(initialPosition, state.getBoard());
        }

    }

    private static Path solve2(final int initialPosition, final Board board) {

        System.out.println(depth);
        System.out.println(new BoardView().render(board));
        try {
            Thread.sleep(1000);
        }
        catch (final InterruptedException e) {}

        final Graph graph = new Graph(board);
        System.out.println(graph);
        final TreeOfWalk treeOfWalk = graph.getTreeOfWalk(initialPosition);
        final List<PathNode> nodeSequence = treeOfWalk.computeNodeSequence();

        //treeOfWalk.computeArcSequence();
        //System.exit(0);

        final PathManager pathManager = PathManager.from(graph.getEdgesById());
        final List<GraphEdge> edgeSequence = pathManager.computeEdgeSequence(nodeSequence);
        final Path path = Path.from(edgeSequence);
        return path;

    }

}