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
import pocman.maze.MazeAsBoard;

import com.google.common.base.Preconditions;

public class MazeAsBoardView {

    public static final char YOUR_ARE_HERE = '⬤';

    public String render(final char[] charArray) {
        final StringBuilder sb = new StringBuilder();
        for (int i = 0; i < MazeAsBoard.HEIGHT; ++i) {
            for (int j = 0; j < MazeAsBoard.WIDTH; ++j)
                sb.append(charArray[MazeAsBoard.WIDTH * i + j]);
            sb.append("\n");
        }
        return sb.toString();
    }

    public String render(final Maze maze) {
        Preconditions.checkArgument(maze != null);
        return this.render(maze.toCharArray());
    }

    /*
    public String render(final Maze maze, final MazeNode mazeNode) {
        Preconditions.checkArgument(maze != null);
        Preconditions.checkArgument(mazeNode != null);
        Preconditions.checkArgument(mazeNode.getId() >= 0 && mazeNode.getId() < maze.size());
        final char[] charArray = maze.toCharArray();
        charArray[mazeNode.getId()] = YOUR_ARE_HERE;
        return mazeNode + "\n" + this.render(charArray);
    }
    */

    public String render(final MazeAsBoard mazeAsBoard) {
        Preconditions.checkArgument(mazeAsBoard != null);
        return this.render(mazeAsBoard.toCharArray());
    }

}