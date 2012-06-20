
package fr.designpattern.pocman.model;

import java.util.Map;

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