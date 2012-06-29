
package pocman.maze;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import pocman.maze.Direction;

public class DirectionTest {

    private final static String[] NAMES = { "NOWHERE", "UP", "RIGHT", "DOWN", "LEFT" };

    @Test
    public void testDirection() {
        final Direction[] values = Direction.values();
        assertTrue(values.length == NAMES.length);
        int i = -1;
        for (final String string : NAMES)
            assertTrue(Direction.valueOf(string).equals(values[++i]));
    }

}