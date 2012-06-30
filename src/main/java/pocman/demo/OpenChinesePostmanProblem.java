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

import old.Mazes;
import pocman.cpp.EulerianTrail;
import pocman.cpp.OpenCPP;
import pocman.cpp.Solution;
import pocman.maze.Maze;
import pocman.maze.MazeNode;
import pocman.maze.Tile;
import pocman.view.MazeView;

import com.google.common.base.Preconditions;
import com.google.common.base.Stopwatch;

public class OpenChinesePostmanProblem {

    //public final static String MAZE = Mazes.DEBUG11333;
    public final static String MAZE = Mazes.LEVEL155;

    public static void main(final String[] args) {

        final Stopwatch stopwatch = new Stopwatch().start();

        final int pocManPosition = MAZE.indexOf(Tile.POCMAN.toCharacter());
        Preconditions.checkState(pocManPosition > -1, "POCMAN POSITION NOT FOUND !");

        final Maze maze = new Maze(MAZE);
        final OpenCPP<MazeNode> openCPP = OpenCPP.from(maze);
        final Solution<MazeNode> solution = openCPP.solveFrom(maze.getNode(pocManPosition));

        //Preconditions.checkState(solution.getLowerBoundCost().equals(190.0));
        //Preconditions.checkState(solution.getUpperBoundCost().equals(290.0));
        //Preconditions.checkState(solution.getEndPoint().equals(maze.getNode(196)));

        final List<MazeNode> trail = EulerianTrail.from(maze, solution.getTraversalByEdge(), solution.getEndPoint());

        stopwatch.stop();

        debug(maze, trail);
        System.out.println(stopwatch.elapsedTime(TimeUnit.SECONDS) + " " + TimeUnit.SECONDS.toString());

    }

    private static void debug(final Maze maze, final List<MazeNode> trail) {
        final MazeView view = new MazeView();
        for (final MazeNode MazeNode : trail) {
            System.out.println(view.renderAsBoard(maze, MazeNode.getId()));
            try {
                Thread.sleep(200);
            }
            catch (final InterruptedException e) {}
        }
    }

}