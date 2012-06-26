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

import fr.designpattern.pocman.game.MazeAsBoard;
import fr.designpattern.pocman.game.MazeAsGraph;
import fr.designpattern.pocman.graph.Vertex;

public class MazeAsGraphView {

    public String render(final MazeAsGraph mazeAsGraph) {
        final char[] array = new char[MazeAsBoard.SIZE];
        for (int nodeId = 0; nodeId < MazeAsBoard.SIZE; ++nodeId) {
            final Vertex node = mazeAsGraph.getNodeById(nodeId);
            final int n = node == null ? 0 : node.getNumberOfOptions();
            char c;
            switch (n) {
                case 0:
                    c = ' ';
                    break;
                case 2:
                    c = node.is(Vertex.Type.STREET) ? '⬤' : '2';
                    break;
                default:
                    c = String.valueOf(n).charAt(0);
            }
            array[nodeId] = c;
        }
        final StringBuilder sb = new StringBuilder("\n");
        sb.append(new MazeAsBoardView().render(MazeAsBoard.from(array)).replaceAll(" {27}\\n", ""));//.append("\n");
        return sb.toString();
    }

}