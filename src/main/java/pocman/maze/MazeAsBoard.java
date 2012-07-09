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

package pocman.maze;

import java.util.List;
import java.util.Map;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

public final class MazeAsBoard
{

    public final static int WIDTH = 27;
    public final static int HEIGHT = 19;
    public final static int SIZE = WIDTH * HEIGHT;

    private final String data;
    private final Tile[][] tileBoard = new Tile[HEIGHT][WIDTH];

    public static MazeAsBoard from(final String data) {
        return new MazeAsBoard(data);
    }

    public static MazeAsBoard from(final char[] array) {
        return from(new String(array));
    }

    private MazeAsBoard(final String data) {
        this.data = data;
        for (int i = 0; i < HEIGHT; ++i)
            for (int j = 0; j < WIDTH; ++j)
                this.tileBoard[i][j] = Tiles.fromCharacter(data.charAt(WIDTH * i + j));
    }

    /*
    public int contains(final Tile tile) {
        return this.data.contains(tile.toCharacter());
    }
    */

    public int find(final Tile tile, final int index) {
        return this.data.indexOf(tile.toCharacter(), index);
    }

    public int find(final Tile tile) {
        return this.find(tile, 0);
    }

    public Tile getCell(final int y, final int x) {
        return this.tileBoard[y][x];
    }

    public Tile getCell(final int index) {
        return this.getCell(index / WIDTH, index % WIDTH);
    }

    public char[] toCharArray() {
        final char[] array = new char[HEIGHT * WIDTH];
        for (int i = 0; i < HEIGHT; ++i)
            for (int j = 0; j < WIDTH; ++j)
                array[WIDTH * i + j] = this.getCell(i, j).toCharacter();
        return array;
    }

    public Map<Direction, Tile> getNeighbours(final int y, final int x) {
        final Map<Direction, Tile> neighbours = Maps.newHashMap();
        if (y > 0) neighbours.put(Direction.UP, this.getCell(y - 1, x));
        if (x + 1 < WIDTH) neighbours.put(Direction.RIGHT, this.getCell(y, x + 1));
        if (y + 1 < HEIGHT) neighbours.put(Direction.DOWN, this.getCell(y + 1, x));
        if (x > 0) neighbours.put(Direction.LEFT, this.getCell(y, x - 1));
        return neighbours;
    }

    public Map<Direction, Tile> getNeighbours(final int index) {
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
        //return Arrays.hashCode(this.tileBoard);
        return this.data.hashCode();
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
                if (this.tileBoard[i][j] != that.tileBoard[i][j]) return false;
        return true;
    }

}