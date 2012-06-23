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

package fr.designpattern.pocman.model;

import java.util.Map;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

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
        final char[] array1 = this.data.toCharArray();
        final char[] array2 = this.board.toCharArray();
        Assert.assertEquals(array1.length, array2.length);
        for (int i = 0; i < array1.length; ++i)
            Assert.assertTrue(array1[i] == array2[i]);
    }

    @Test
    public void testGetCellFromYAndX()
    {
        final int y = MazeAsBoard.HEIGHT - 1 - 1;
        final int x = MazeAsBoard.WIDTH - 1 - 1;
        Assert.assertTrue(this.board.getCell(y, x) == Constants.POCMAN);
    }

    @Test
    public void testGetCellFromIndex()
    {
        final int y = MazeAsBoard.HEIGHT - 1 - 1;
        final int x = MazeAsBoard.WIDTH - 1 - 1;
        Assert.assertTrue(this.board.getCell(y * MazeAsBoard.WIDTH + x) == Constants.POCMAN);
    }

    @Test
    public void testGetNeighboursFromYAndX()
    {
        final Map<Direction, Character> expected = Maps.newHashMap();
        expected.put(Direction.UP, Constants.HORIZONTAL_WALL);
        expected.put(Direction.RIGHT, Constants.VERTICAL_WALL);
        expected.put(Direction.DOWN, Constants.HORIZONTAL_WALL);
        expected.put(Direction.LEFT, Constants.PILL);
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

    //@Test
    public void testToString() // TODO tester plutôt la vue
    {
        Assert.assertEquals(this.data.toString(), this.board.toString());
    }

}