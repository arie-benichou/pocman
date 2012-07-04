/*
 * Copyright 2012 Arie Benichou
 * 
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * this program. If not, see <http://www.gnu.org/licenses/>.
 */

package pocman.demo;

import java.util.List;
import java.util.concurrent.TimeUnit;

import pocman.cpp.ClosedCPP;
import pocman.cpp.ClosedCPPSolution;
import pocman.cpp.EulerianTrail;
import pocman.cpp.OpenCPP;
import pocman.cpp.OpenCPPSolution;
import pocman.matching.MatchingAlgorithm;
import pocman.maze.Maze;
import pocman.maze.MazeNode;
import pocman.maze.Tile;
import pocman.view.MazeView;

import com.google.common.base.Preconditions;
import com.google.common.base.Stopwatch;

public class CPPDemo {

    public final static MatchingAlgorithm MATCHING_ALGORITHM_1 = new pocman.matching.edmonds1.Matching();
    public final static MatchingAlgorithm MATCHING_ALGORITHM_2 = new pocman.matching.edmonds2.Matching();
    public final static MatchingAlgorithm MATCHING_ALGORITHM_3 = new pocman.matching.naive.Matching();

    private final MatchingAlgorithm matchingAlgorithm;

    public CPPDemo(final MatchingAlgorithm matchingAlgorithm) {
        this.matchingAlgorithm = matchingAlgorithm;
    }

    private OpenCPPSolution<MazeNode> solve(final ClosedCPPSolution<MazeNode> closedCPPSolution, final MazeNode node) {
        return OpenCPP.from(closedCPPSolution).solveFrom(node);
    }

    public OpenCPPSolution<MazeNode> solve(final Maze maze, final int pocManPosition) {
        return OpenCPP.from(maze, this.matchingAlgorithm).solveFrom(maze.getNode(pocManPosition));
    }

    public ClosedCPPSolution<MazeNode> solve(final Maze maze) {
        return ClosedCPP.from(maze, this.matchingAlgorithm).solve();
    }

    // TODO pouvoir afficher les traces
    private void debug(final Maze maze, final List<MazeNode> trail, final long laps) throws InterruptedException {
        final MazeView view = new MazeView();
        for (final MazeNode MazeNode : trail) {
            System.out.println(view.renderAsBoard(maze, MazeNode.getId()));
            Thread.sleep(laps);
        }
    }

    public static void main(final String[] args) throws InterruptedException {

        final Stopwatch stopwatch = new Stopwatch();

        final CPPDemo that = new CPPDemo(MATCHING_ALGORITHM_1);

        for (final String level : Mazes.LEVELS) {

            final int pocManPosition = level.indexOf(Tile.POCMAN.toCharacter());
            Preconditions.checkState(pocManPosition > -1, "POCMAN POSITION NOT FOUND !");
            final char[] data = level.toCharArray();
            data[pocManPosition] = Tile.COIN.toCharacter();

            final Maze maze = Maze.from(data);

            stopwatch.start();
            final ClosedCPPSolution<MazeNode> closedCPPSolution = that.solve(maze);
            stopwatch.stop();

            final List<MazeNode> closedTrail = EulerianTrail.from(
                    closedCPPSolution.getGraph(),
                    closedCPPSolution.getTraversalByEdge(),
                    maze.getNode(pocManPosition));

            that.debug(maze, closedTrail, 160);
            System.out.println(closedCPPSolution);
            Thread.sleep(1000);

            stopwatch.start();
            final OpenCPPSolution<MazeNode> openCPPSolution = that.solve(closedCPPSolution, maze.getNode(pocManPosition));
            stopwatch.stop();

            final List<MazeNode> trail = EulerianTrail.from(
                    openCPPSolution.getGraph(),
                    openCPPSolution.getTraversalByEdge(),
                    openCPPSolution.getEndPoint());

            that.debug(maze, trail, 320);
            System.out.println(openCPPSolution);
            Thread.sleep(1000);

        }

        System.out.println(stopwatch.elapsedTime(TimeUnit.MILLISECONDS) + " " + TimeUnit.MILLISECONDS.toString());
    }
}