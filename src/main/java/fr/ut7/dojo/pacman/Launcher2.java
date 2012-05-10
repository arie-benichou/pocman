
package fr.ut7.dojo.pacman;

import java.util.List;

import fr.ut7.dojo.pacman.graph.Graph;
import fr.ut7.dojo.pacman.graph.GraphEdge;
import fr.ut7.dojo.pacman.graph.PathNode;
import fr.ut7.dojo.pacman.graph.TreeOfWalk;
import fr.ut7.dojo.pacman.model.BestGhostMoveSearch;
import fr.ut7.dojo.pacman.model.Board;
import fr.ut7.dojo.pacman.model.Game;
import fr.ut7.dojo.pacman.model.GameState;
import fr.ut7.dojo.pacman.model.GhostReferee;
import fr.ut7.dojo.pacman.model.HalfRandomPacmanMoveEmitter;
import fr.ut7.dojo.pacman.model.Levels;
import fr.ut7.dojo.pacman.model.PacmanReferee;
import fr.ut7.dojo.pacman.model.characters.Ghost;
import fr.ut7.dojo.pacman.model.characters.Pacman;

public final class Launcher2 {

    private Launcher2() {}

    public static void main(final String[] args) {
        final Game game = Game.from(
                new PacmanReferee(), new Pacman(new HalfRandomPacmanMoveEmitter()),
                new GhostReferee(), new Ghost(new BestGhostMoveSearch()),
                GameState.from(
                        Levels.LEVEL155
                        ));

        final Board board = game.getBoard();
        final Graph graph = new Graph(board);
        //System.out.println(graph);
        final TreeOfWalk treeOfWalk = graph.getTreeOfWalk(game.getPacmanPosition());
        final List<PathNode> sequence = treeOfWalk.computeInterestingNodesSequence();
        final List<GraphEdge> path = treeOfWalk.computePath(sequence);
        TreeOfWalk.showWalk(board, path);
    }

}