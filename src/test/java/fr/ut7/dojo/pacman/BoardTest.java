
package fr.ut7.dojo.pacman;

import java.util.Map;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.Maps;

import fr.designpattern.pocman.model.MazeAsBoard;
import fr.designpattern.pocman.model.Direction;

/**
 * Unit test for simple App.
 */
public class BoardTest
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

    @Test
    public void testToString()
    {
        Assert.assertEquals(this.data.toString(), this.board.toString());
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
        Assert.assertTrue(this.board.getCell(y, x) == '⬤');
    }

    @Test
    public void testGetCellFromIndex()
    {
        final int y = MazeAsBoard.HEIGHT - 1 - 1;
        final int x = MazeAsBoard.WIDTH - 1 - 1;
        Assert.assertTrue(this.board.getCell(y * MazeAsBoard.WIDTH + x) == '⬤');
    }

    @Test
    public void testGetNeighboursFromYAndX()
    {
        final Map<Direction, Character> expected = Maps.newHashMap();
        expected.put(Direction.UP, '⬛');
        expected.put(Direction.RIGHT, '┃');
        expected.put(Direction.DOWN, '⬛');
        expected.put(Direction.LEFT, '∙');
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