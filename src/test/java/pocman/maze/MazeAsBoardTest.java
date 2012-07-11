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

import java.util.Map;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import pocman.game.Direction;
import pocman.game.MazeAsBoard;
import pocman.game.Tile;

import com.google.common.collect.Maps;

/**
 * Unit test for simple App.
 */
public class MazeAsBoardTest
{

    private MazeAsBoard board;
    private String data;

    @Before
    public void setup() {

        this.data = "" +
                "┃⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛┃" +
                "┃∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙┃" +
                "┃⬛⬛⬛⬛⬛∙⬛⬛⬛⬛⬛∙┃∙⬛⬛⬛⬛⬛∙⬛⬛⬛⬛⬛┃" +
                "┃∙∙∙∙∙∙∙∙∙∙∙∙┃∙∙∙∙∙∙∙∙∙∙∙∙┃" +
                "┃⬛⬛⬛⬛⬛∙⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛∙⬛⬛⬛⬛⬛┃" +
                "┃∙∙∙∙∙∙∙∙∙∙∙∙┃∙∙∙∙∙∙∙∙∙∙∙∙┃" +
                "┃⬛⬛⬛⬛⬛∙⬛⬛⬛⬛⬛∙┃∙⬛⬛⬛⬛⬛∙⬛⬛⬛⬛⬛┃" +
                "┃∙∙∙∙∙∙∙∙∙∙∙∙┃∙∙∙∙∙∙∙∙∙∙∙∙┃" +
                "┃⬛⬛⬛⬛⬛∙⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛∙⬛⬛⬛⬛⬛┃" +
                "┃∙∙∙∙∙∙∙∙∙∙∙∙┃∙∙∙∙∙∙∙∙∙∙∙∙┃" +
                "┃⬛⬛⬛⬛⬛∙⬛⬛⬛⬛⬛∙┃∙⬛⬛⬛⬛⬛∙⬛⬛⬛⬛⬛┃" +
                "┃∙∙∙∙∙∙∙∙∙∙∙∙┃∙∙∙∙∙∙∙∙∙∙∙∙┃" +
                "┃⬛⬛⬛⬛⬛∙⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛∙⬛⬛⬛⬛⬛┃" +
                "┃∙∙∙∙∙∙∙∙∙∙∙∙┃∙∙∙∙∙∙∙∙∙∙∙∙┃" +
                "┃⬛⬛⬛⬛⬛∙⬛⬛⬛⬛⬛∙┃∙⬛⬛⬛⬛⬛∙⬛⬛⬛⬛⬛┃" +
                "┃∙∙∙∙∙∙∙∙∙∙∙∙┃∙∙∙∙∙∙∙∙∙∙∙∙┃" +
                "┃⬛⬛⬛⬛⬛∙⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛∙⬛⬛⬛⬛⬛┃" +
                "┃∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙⬤┃" +
                "┃⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛┃";

        this.board = MazeAsBoard.from(this.data);

    }

    @After
    public void tearDown() {
        this.data = null;
        this.board = null;
    }

    @Test
    public void testToCharArray()
    {
        final char[] expectedArray = this.data.toCharArray();
        final char[] actualArray = this.board.toCharArray();
        Assert.assertEquals(expectedArray.length, actualArray.length);
        for (int i = 0; i < expectedArray.length; ++i)
            Assert.assertEquals(expectedArray[i], actualArray[i]);
    }

    @Test
    public void testGetCellFromYAndX()
    {
        final int y = MazeAsBoard.HEIGHT - 1 - 1;
        final int x = MazeAsBoard.WIDTH - 1 - 1;
        Assert.assertTrue(this.board.getCell(y, x).is(Tile.POCMAN));
    }

    @Test
    public void testGetCellFromIndex()
    {
        final int y = MazeAsBoard.HEIGHT - 1 - 1;
        final int x = MazeAsBoard.WIDTH - 1 - 1;
        Assert.assertTrue(this.board.getCell(y * MazeAsBoard.WIDTH + x).is(Tile.POCMAN));
    }

    @Test
    public void testGetNeighboursFromYAndX()
    {
        final Map<Direction, Tile> expected = Maps.newHashMap();
        expected.put(Direction.UP, Tile.HORIZONTAL_WALL);
        expected.put(Direction.RIGHT, Tile.VERTICAL_WALL);
        expected.put(Direction.DOWN, Tile.HORIZONTAL_WALL);
        expected.put(Direction.LEFT, Tile.COIN);
        final int y = MazeAsBoard.HEIGHT - 1 - 1;
        final int x = MazeAsBoard.WIDTH - 1 - 1;
        Assert.assertTrue(this.board.getNeighbours(y, x).equals(expected));
    }

    @Test
    public void testGetNeighboursFromIndex()
    {
        final int y = MazeAsBoard.HEIGHT - 1 - 1;
        final int x = MazeAsBoard.WIDTH - 1 - 1;
        Assert.assertTrue(this.board.getNeighbours((MazeAsBoard.HEIGHT - 1) * MazeAsBoard.WIDTH - 1 - 1).equals(this.board.getNeighbours(y, x)));
    }

}