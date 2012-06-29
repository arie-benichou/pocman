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

import java.util.List;
import java.util.concurrent.TimeUnit;

import old.Mazes;

import com.google.common.base.Preconditions;
import com.google.common.base.Stopwatch;

import fr.designpattern.pocman.cpp.EulerianTrail;
import fr.designpattern.pocman.cpp.OpenCPP;
import fr.designpattern.pocman.cpp.OpenSolution;
import fr.designpattern.pocman.game.Maze;
import fr.designpattern.pocman.game.Tile;
import fr.designpattern.pocman.graph.Vertex;
import fr.designpattern.pocman.view.MazeView;

public class OpenChinesePostmanProblem {

    public final static String _MAZE = "" +

            "┃⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛┃" +
            "┃∙              ∙        ∙┃" +
            "┃ ⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛ ⬛⬛⬛⬛⬛⬛ ⬛ ┃" +
            "┃∙  ┃                   ┃ ┃" +
            "┃ ⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛ ⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛┃ ┃" +
            "┃∙   ∙    ∙┃ ┃∙         ┃∙┃" +
            "┃⬛⬛⬛⬛ ⬛⬛⬛⬛⬛⬛ ┃ ⬛⬛⬛⬛⬛⬛⬛⬛ ┃⬛┃" +
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

    //public final static String MAZE = Mazes.LEVEL155;

    //public final static String MAZE = Mazes.DEBUG11455;
    public final static String MAZE = Mazes.DEBUG11333;

    //public final static String MAZE = Mazes.DEBUG1556;
    //public final static String MAZE = Mazes.DEBUG1150;
    //public final static String MAZE = Mazes.DEBUG12;

    public static void main(final String[] args) {

        final Stopwatch stopwatch = new Stopwatch();
        stopwatch.start();

        final int pocManPosition = MAZE.indexOf(Tile.POCMAN.toCharacter());
        Preconditions.checkState(pocManPosition > -1, "POCMAN POSITION NOT FOUND !");

        final Maze maze = new Maze(MAZE);
        final OpenCPP<Vertex> openCPP = OpenCPP.from(maze);
        final OpenSolution<Vertex> solution = openCPP.solveFrom(maze.getNode(pocManPosition));

        final List<Vertex> trail = EulerianTrail.from(maze, solution.getTraversalByEdge(), solution.getVertex());

        stopwatch.stop();

        debug(maze, trail);

        System.out.println(stopwatch.elapsedTime(TimeUnit.SECONDS) + " " + TimeUnit.SECONDS.toString());

    }

    private static void debug(final Maze maze, final List<Vertex> trail) {
        final MazeView view = new MazeView();
        for (final Vertex vertex : trail) {
            System.out.println(view.renderAsBoard(maze, vertex));
            try {
                Thread.sleep(200);
            }
            catch (final InterruptedException e) {}
        }
    }
}