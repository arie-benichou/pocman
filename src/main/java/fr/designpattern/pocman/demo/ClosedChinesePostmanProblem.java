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

package fr.designpattern.pocman.demo;

import java.util.concurrent.TimeUnit;

import com.google.common.base.Preconditions;
import com.google.common.base.Stopwatch;

import fr.designpattern.pocman.cpp.ClosedCPP;
import fr.designpattern.pocman.cpp.EulerianTrail;
import fr.designpattern.pocman.cpp.Solution;
import fr.designpattern.pocman.game.Constants;
import fr.designpattern.pocman.game.MazeAsBoard;
import fr.designpattern.pocman.game.MazeAsGraph;
import fr.designpattern.pocman.graph.Vertex;
import fr.designpattern.pocman.view.NodeInMazeView;

public class ClosedChinesePostmanProblem {

    public final static String MAZE = "" +

            "┃⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛┃" +
            "┃∙              ∙        ∙┃" +
            "┃ ⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛ ⬛⬛⬛⬛⬛⬛⬛⬛ ┃" +
            "┃∙        ∙┃∙   ∙   ∙┃∙  ∙┃" +
            "┃ ⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛ ⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛┃" +
            "┃∙   ∙    ∙┃ ┃∙          ∙┃" +
            "┃⬛⬛⬛⬛ ⬛⬛⬛⬛⬛⬛ ┃ ⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛ ┃" +
            "┃∙   ∙  ∙┃∙ ∙┃∙ ∙        ∙┃" +
            "┃⬛⬛⬛⬛ ⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛ ⬛⬛⬛⬛⬛⬛⬛⬛ ┃" +
            "┃∙   ∙    ∙┃∙   ∙        ∙┃" +
            "┃ ⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛ ⬛⬛⬛⬛⬛⬛⬛⬛⬛┃" +
            "┃∙        ∙     ∙        ∙┃" +
            "┃⬛⬛⬛⬛⬛⬛⬛⬛⬛ ⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛┃" +
            "┃∙        ∙              ∙┃" +
            "┃ ⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛┃" +
            "┃∙  ∙┃∙   ∙┃⬤            ∙┃" +
            "┃ ⬛⬛⬛⬛⬛⬛⬛⬛ ⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛ ┃" +
            "┃∙        ∙              ∙┃" +
            "┃⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛┃";

    private static MazeAsGraph.Factory mazeAsGraphFactory = new MazeAsGraph.Factory();

    public static void main(final String[] args) {

        final Stopwatch stopwatch = new Stopwatch().start();

        final int pacManPosition = MAZE.indexOf(Constants.POCMAN);

        Preconditions.checkState(pacManPosition > -1, "POCMAN POSITION NOT FOUND !");

        final char[] array = MazeAsBoard.from(MAZE).toCharArray();
        array[pacManPosition] = Constants.SPACE;
        final MazeAsBoard board = MazeAsBoard.from(array);
        System.out.println(board);

        // TODO ? avoir un seul objet Maze

        final MazeAsGraph mazeAsGraph = mazeAsGraphFactory.newMazeAsGraph(board);
        System.out.println(mazeAsGraph);
        System.out.println(mazeAsGraph.isConnected());

        final ClosedCPP<Vertex> closedCPPSolver = ClosedCPP.newSolver(mazeAsGraph);
        final Solution<Vertex> solution = closedCPPSolver.solveFrom(mazeAsGraph.getNodeById(pacManPosition));

        stopwatch.stop();

        final NodeInMazeView nodeInMazeView = new NodeInMazeView(board);
        for (final Vertex vertex : EulerianTrail.from(solution)) {
            System.out.println(nodeInMazeView.render(vertex));
            try {
                Thread.sleep(160);
            }
            catch (final InterruptedException e) {}
        }

        System.out.println(solution);

        System.out.println(stopwatch.elapsedTime(TimeUnit.MILLISECONDS) + " " + TimeUnit.MILLISECONDS.toString());

    }
}