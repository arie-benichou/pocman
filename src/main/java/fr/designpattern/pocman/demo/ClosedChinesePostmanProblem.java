
package fr.designpattern.pocman.demo;

import java.util.List;
import java.util.concurrent.TimeUnit;

import com.google.common.base.Preconditions;
import com.google.common.base.Stopwatch;

import fr.designpattern.pocman.cpp.ClosedCPPSolver;
import fr.designpattern.pocman.cpp.graph.Vertex;
import fr.designpattern.pocman.data.Mazes;
import fr.designpattern.pocman.model.Constants;
import fr.designpattern.pocman.model.MazeAsBoard;
import fr.designpattern.pocman.model.MazeAsGraph;
import fr.designpattern.pocman.view.NodeInMazeView;

public class ClosedChinesePostmanProblem {

    private static MazeAsGraph.Factory mazeAsGraphFactory = new MazeAsGraph.Factory();
    private static ClosedCPPSolver.Factory closedCPPSolverFactory = new ClosedCPPSolver.Factory();

    public static void main(final String[] args) {

        final Stopwatch stopwatch = new Stopwatch().start();

        final String data = Mazes.DEBUG112;
        final int pacManPosition = data.indexOf(Constants.POCMAN);

        Preconditions.checkState(pacManPosition > -1, "POCMAN POSITION NOT FOUND !");

        final char[] array = MazeAsBoard.from(data).toCharArray();
        array[pacManPosition] = Constants.SPACE;
        final MazeAsBoard board = MazeAsBoard.from(array);
        System.out.println(board);

        // TODO ? avoir un seul objet Maze

        final MazeAsGraph mazeAsGraph = mazeAsGraphFactory.newMazeAsGraph(board);
        System.out.println(mazeAsGraph);
        System.out.println(mazeAsGraph.isConnected());

        final ClosedCPPSolver closedCPPSolver = closedCPPSolverFactory.newClosedCPPSolver(mazeAsGraph);
        final List<Vertex> closedTrail = closedCPPSolver.solveFrom(mazeAsGraph.getNodeById(pacManPosition)); // TODO objet Solution

        stopwatch.stop();

        final NodeInMazeView nodeInMazeView = new NodeInMazeView(board);
        for (final Vertex vertex : closedTrail) {
            System.out.println(nodeInMazeView.render(vertex));
            try {
                Thread.sleep(175);
            }
            catch (final InterruptedException e) {}
        }

        System.out.println(stopwatch.elapsedTime(TimeUnit.MILLISECONDS) + " " + TimeUnit.MILLISECONDS.toString());

    }

}