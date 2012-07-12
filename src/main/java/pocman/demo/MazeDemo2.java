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

import pocman.game.Maze;
import pocman.game.MazeNode;
import pocman.game.Tile;
import pocman.view.MazeAsBoardView;

public class MazeDemo2 {

    public static void main(final String[] args) throws InterruptedException {

        final Maze maze = Maze.from("" +
                "┃⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛┃" +
                "┃∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙┃" +
                "┃∙⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛∙⬛⬛⬛⬛⬛⬛⬛⬛∙┃" +
                "┃∙∙∙∙∙∙∙∙∙∙┃∙∙∙∙∙∙∙∙∙∙∙∙┃∙┃" +
                "┃∙⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛∙⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛┃" +
                "┃∙∙∙∙∙∙∙∙∙∙┃∙┃∙∙∙∙∙∙∙∙∙∙∙∙┃" +
                "┃⬛⬛⬛⬛∙⬛⬛⬛⬛⬛⬛∙┃⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛∙┃" +
                "┃∙∙∙∙∙┃∙∙∙∙∙∙┃∙∙∙∙∙∙∙∙∙∙∙∙┃" +
                "┃⬛⬛⬛⬛∙⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛∙⬛⬛⬛⬛⬛⬛⬛⬛⬛┃" +
                "┃∙∙∙∙∙∙∙∙∙∙┃∙∙∙∙∙∙∙∙∙∙∙∙∙∙┃" +
                "┃∙⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛∙⬛⬛⬛⬛⬛⬛⬛⬛⬛┃" +
                "┃∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙┃" +
                "┃⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛┃" +
                "┃∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙┃" +
                "┃∙⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛┃" +
                "┃∙∙∙∙┃∙∙∙∙∙┃⬤∙∙∙∙∙∙∙∙∙∙∙∙∙┃" +
                "┃∙⬛⬛⬛⬛⬛⬛⬛⬛∙⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛∙┃" +
                "┃∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙┃" +
                "┃⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛┃");

        final MazeNode pocmanPosition = maze.find(Tile.POCMAN);

        //final Map<MazeNode, Entry<Move, Integer>> graphNodeRange = maze.getGraphNodeRange(pocmanPosition);
        //System.out.println(graphNodeRange);

        final MazeNode pacManNode = maze.getNearestGraphNode(pocmanPosition);

        final char[] boardAsCharArray = maze.getBoard().toCharArray();
        for (final MazeNode mazeNode : maze.get())
            if (maze.hasPath(pacManNode, mazeNode)) boardAsCharArray[mazeNode.getId()] = 'O';
            else {
                boardAsCharArray[mazeNode.getId()] = 'X';
                /* TODO
                final List<MazeEdge> edges = mazeNode.getEdges();
                for (final MazeEdge mazeEdge : edges)
                    for (final MazeNode node : mazeEdge)
                        boardAsCharArray[node.getId()] = 'X';
                */
            }
        System.out.println(new MazeAsBoardView().render(boardAsCharArray));
    }

}