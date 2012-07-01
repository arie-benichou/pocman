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

package pocman.view;

import pocman.maze.Maze;
import pocman.maze.MazeNode;

public class MazeView {

    private final MazeAsBoardView mazeAsBoardView;
    private final MazeAsGraphView mazeAsGraphView;

    public MazeView() {
        this.mazeAsBoardView = new MazeAsBoardView();
        this.mazeAsGraphView = new MazeAsGraphView();
    }

    public String render(final Maze maze) {
        final StringBuilder sb = new StringBuilder();
        sb.append(this.mazeAsBoardView.render(maze));
        sb.append("\n");
        sb.append(this.mazeAsGraphView.render(maze));
        return sb.toString();
    }

    public String renderAsBoard(final Maze maze, final int nodeId) {
        final StringBuilder sb = new StringBuilder();
        sb.append(this.mazeAsBoardView.render(maze, nodeId));
        return sb.toString();
    }

    public String renderAsGraph(final Maze maze, final MazeNode MazeNode) {
        final StringBuilder sb = new StringBuilder();
        sb.append(this.mazeAsGraphView.render(maze, MazeNode));
        return sb.toString();
    }

}