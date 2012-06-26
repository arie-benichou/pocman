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

package fr.designpattern.pocman.view;

import fr.designpattern.pocman.game.Constants;
import fr.designpattern.pocman.game.MazeAsBoard;
import fr.designpattern.pocman.graph.Vertex;

public class NodeInMazeView {

    private final MazeAsBoard board;

    public NodeInMazeView(final MazeAsBoard board) {
        this.board = board;
    }

    public String render(final int nodeId) {
        final char[] array = this.board.toCharArray();
        array[nodeId] = Constants.POCMAN;
        return nodeId + "\n" + MazeAsBoard.from(array);
    }

    public String render(final Vertex vertex) {
        return this.render(vertex.getId());
    }

}