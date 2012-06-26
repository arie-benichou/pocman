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

public class MazeAsBoardView {

    public String render(final MazeAsBoard board) {
        final StringBuilder sb = new StringBuilder();
        final char[] data = board.toCharArray();
        for (int i = 0; i < MazeAsBoard.HEIGHT; ++i) {
            for (int j = 0; j < MazeAsBoard.WIDTH; ++j)
                sb.append(data[MazeAsBoard.WIDTH * i + j]);
            sb.append("\n");
        }
        return sb.toString();
    }

}