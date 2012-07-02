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
import pocman.cpp.EulerianTrail;
import pocman.cpp.Solution;
import pocman.matching.MatchingAlgorithm;
import pocman.maze.Maze;
import pocman.maze.MazeNode;
import pocman.maze.Tile;
import pocman.view.MazeView;

import com.google.common.base.Preconditions;
import com.google.common.base.Stopwatch;

public class ClosedChinesePostmanProblem {

    //public final static String MAZE = Mazes.DEBUG11333;
    //public final static String MAZE = Mazes.DEBUG14;
    //public final static String MAZE = Mazes.DEBUG15;
    public final static String MAZE = Mazes.LEVEL155;

    public static List<MazeNode> computeOptimalPath(final Maze maze, final int pocManPosition, final MatchingAlgorithm matchingAlgorithm) {
        final ClosedCPP<MazeNode> closedCPP = ClosedCPP.from(maze, matchingAlgorithm);
        final Solution<MazeNode> solution = closedCPP.solve();
        System.out.println(solution);
        return EulerianTrail.from(maze, solution.getTraversalByEdge(), maze.getNode(pocManPosition));
    }

    private static void debug(final Maze maze, final List<MazeNode> trail) throws InterruptedException {
        final MazeView view = new MazeView();
        for (final MazeNode MazeNode : trail) {
            System.out.println(view.renderAsBoard(maze, MazeNode.getId()));
            Thread.sleep(170);
        }
    }

    public static void main(final String[] args) throws InterruptedException {
        final int pocManPosition = MAZE.indexOf(Tile.POCMAN.toCharacter());
        Preconditions.checkState(pocManPosition > -1, "POCMAN POSITION NOT FOUND !");
        final Maze maze = new Maze(MAZE);
        {
            final String time = time(maze, pocManPosition, new pocman.matching.edmonds1.Matching());
            System.out.println(time);
        }
        /*
        {
            final String time = time(maze, pocManPosition, new pocman.matching.edmonds2.Matching());
            System.out.println(time);
        }
        {
            final String time = time(maze, pocManPosition, new pocman.matching.naive.Matching());
            System.out.println(time);
        }
        */
    }

    private static String time(final Maze maze, final int pocManPosition, final MatchingAlgorithm matchingAlgorithm) throws InterruptedException {
        final Stopwatch stopwatch = new Stopwatch().start();
        final List<MazeNode> trail = computeOptimalPath(maze, pocManPosition, matchingAlgorithm);
        stopwatch.stop();
        debug(maze, trail);
        return stopwatch.elapsedTime(TimeUnit.MILLISECONDS) + " " + TimeUnit.MILLISECONDS.toString();
    }

}