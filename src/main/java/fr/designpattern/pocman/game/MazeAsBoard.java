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

package fr.designpattern.pocman.game;

import java.util.List;
import java.util.Map;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import fr.designpattern.pocman.view.MazeAsBoardView;

public final class MazeAsBoard
{

    public final static int WIDTH = 27;
    public final static int HEIGHT = 19;
    public final static int SIZE = WIDTH * HEIGHT;

    private final char[][] board = new char[HEIGHT][];

    public static MazeAsBoard from(final String data) {
        return new MazeAsBoard(data);
    }

    public static MazeAsBoard from(final char[] array) {
        return from(new String(array));
    }

    private MazeAsBoard(final String data) {
        for (int i = 0; i < HEIGHT; ++i) {
            this.board[i] = new char[WIDTH];
            for (int j = 0; j < WIDTH; ++j)
                this.board[i][j] = data.charAt(WIDTH * i + j);
        }
    }

    public char[] toCharArray() {
        final char[] array = new char[HEIGHT * WIDTH];
        for (int i = 0; i < HEIGHT; ++i)
            for (int j = 0; j < WIDTH; ++j)
                array[WIDTH * i + j] = this.board[i][j];
        return array;
    }

    @Override
    public String toString() {
        /*
        final StringBuilder sb = new StringBuilder();
        for (int i = 0; i < HEIGHT; ++i)
            for (int j = 0; j < WIDTH; ++j)
                sb.append(this.board[i][j]);
        return sb.toString();
        */
        return new MazeAsBoardView().render(this); // TODO
    }

    public char getCell(final int y, final int x) {
        return this.board[y][x];
    }

    public char getCell(final int index) {
        return this.getCell(index / WIDTH, index % WIDTH);
    }

    public Map<Direction, Character> getNeighbours(final int y, final int x) {
        final Map<Direction, Character> neighbours = Maps.newHashMap();
        if (y > 0) neighbours.put(Direction.UP, this.board[y - 1][x]);
        if (x + 1 < WIDTH) neighbours.put(Direction.RIGHT, this.board[y][x + 1]);
        if (y + 1 < HEIGHT) neighbours.put(Direction.DOWN, this.board[y + 1][x]);
        if (x > 0) neighbours.put(Direction.LEFT, this.board[y][x - 1]);
        return neighbours;
    }

    public Map<Direction, Character> getNeighbours(final int index) {
        return this.getNeighbours(index / WIDTH, index % WIDTH);
    }

    public List<Direction> getDirections(final int y, final int x) {
        final List<Direction> directions = Lists.newArrayList();
        if (y > 0) directions.add(Direction.UP);
        if (x + 1 < WIDTH) directions.add(Direction.RIGHT);
        if (y + 1 < HEIGHT) directions.add(Direction.DOWN);
        if (x > 0) directions.add(Direction.LEFT);
        return directions;
    }

    public List<Direction> getDirections(final int index) {
        return this.getDirections(index / WIDTH, index % WIDTH);
    }

    @Override
    public int hashCode() {
        return this.board.hashCode();
    }

    @Override
    public boolean equals(final Object object) {
        if (object == null) return false;
        if (object == this) return true;
        if (!(object instanceof MazeAsBoard)) return false;
        final MazeAsBoard that = (MazeAsBoard) object;
        if (that.hashCode() != this.hashCode()) return false;
        for (int i = 0; i < HEIGHT; ++i)
            for (int j = 0; j < WIDTH; ++j)
                if (this.board[i][j] != that.board[i][j]) return false;
        return true;
    }

}