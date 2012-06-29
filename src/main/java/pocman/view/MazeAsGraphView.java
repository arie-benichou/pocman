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
import pocman.maze.MazeNode.Type;

import com.google.common.base.Preconditions;


public class MazeAsGraphView {

    public static final char YOUR_ARE_HERE = '⬤';

    private static final MazeAsBoardView MAZE_AS_BOARD_VIEW = new MazeAsBoardView();

    private char[] map(final Maze maze) {
        final char[] data = new char[maze.size()];
        for (int nodeId = 0; nodeId < maze.size(); ++nodeId) {
            final MazeNode node = maze.getNode(nodeId);
            final int n = node == null ? 0 : node.getNumberOfOptions();
            char c;
            switch (n) {
                case 0:
                    c = ' ';
                    break;
                case 2:
                    c = node.is(Type.STREET) ? '∙' : '2';
                    break;
                default:
                    c = String.valueOf(n).charAt(0);
            }
            data[nodeId] = c;
        }
        return data;
    }

    public String render(final Maze maze) {
        Preconditions.checkArgument(maze != null);
        return MAZE_AS_BOARD_VIEW.render(this.map(maze));
    }

    public Object render(final Maze maze, final MazeNode MazeNode) {
        final int nodeId = MazeNode.getId();
        final char[] charArray = this.map(maze);
        charArray[nodeId] = MazeAsGraphView.YOUR_ARE_HERE;
        return MAZE_AS_BOARD_VIEW.render(charArray);
    }
}