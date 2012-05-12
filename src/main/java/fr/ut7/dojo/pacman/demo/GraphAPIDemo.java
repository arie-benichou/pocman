
package fr.ut7.dojo.pacman.demo;

import java.util.List;
import java.util.concurrent.TimeUnit;

import com.google.common.base.Stopwatch;

import fr.ut7.dojo.pacman.graph.Graph;
import fr.ut7.dojo.pacman.graph.GraphEdge;
import fr.ut7.dojo.pacman.graph.Path;
import fr.ut7.dojo.pacman.graph.PathManager;
import fr.ut7.dojo.pacman.graph.PathNode;
import fr.ut7.dojo.pacman.graph.TreeOfWalk;
import fr.ut7.dojo.pacman.model.Board;
import fr.ut7.dojo.pacman.model.Game;
import fr.ut7.dojo.pacman.model.GameState;
import fr.ut7.dojo.pacman.model.GhostReferee;
import fr.ut7.dojo.pacman.model.Levels;
import fr.ut7.dojo.pacman.model.NullMoveEmitter;
import fr.ut7.dojo.pacman.model.PacmanReferee;
import fr.ut7.dojo.pacman.model.characters.Ghost;
import fr.ut7.dojo.pacman.model.characters.Pacman;
import fr.ut7.dojo.pacman.view.GameView;
import fr.ut7.dojo.pacman.view.PathView;

public final class GraphAPIDemo {

    private GraphAPIDemo() {}

    public static void main(final String[] args) {

        /*--------------------8<--------------------*/

        final Stopwatch stopwatch = new Stopwatch();

        /*--------------------8<--------------------*/

        final Game game = Game.from(
                new PacmanReferee(), new Pacman(new NullMoveEmitter()),
                new GhostReferee(), new Ghost(new NullMoveEmitter()),
                GameState.from(Levels.DEBUG113)
                );

        final Board board = game.getBoard();
        System.out.println(new GameView().render(game));

        /*--------------------8<--------------------*/

        stopwatch.start();

        final Graph graph = new Graph(board);

        final long elapsedTime1 = stopwatch.elapsedTime(TimeUnit.MICROSECONDS);

        System.out.println(graph);
        System.out.println("Graph computed within " + elapsedTime1 + " μs !");

        stopwatch.reset();

        /*--------------------8<--------------------*/

        stopwatch.start();

        final TreeOfWalk treeOfWalk = graph.getTreeOfWalk(game.getPacmanPosition());
        final List<PathNode> nodeSequence = treeOfWalk.computeNodeSequence(); // TODO instancier TreeOfWalk en dehors de graph et lui injecter graph
        final PathManager pathManager = PathManager.from(graph.getEdgesById());
        final List<GraphEdge> edgeSequence = pathManager.computeEdgeSequence(nodeSequence);
        final Path path = Path.from(edgeSequence);

        final long elapsedTime2 = stopwatch.elapsedTime(TimeUnit.MICROSECONDS);

        System.out.println(new PathView(board).render(path));
        System.out.println("Path computed within " + elapsedTime2 + " μs !");

        stopwatch.reset();

        /*--------------------8<--------------------*/
    }

}