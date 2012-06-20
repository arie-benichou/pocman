
package fr.designpattern.pocman.model;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class MoveTest {

    @Test
    public void testFrom() {
        assertTrue(Move.from(Direction.NOWHERE).equals(Move.GO_NOWHERE));
        assertTrue(Move.from(Direction.UP).equals(Move.GO_UP));
        assertTrue(Move.from(Direction.RIGHT).equals(Move.GO_RIGHT));
        assertTrue(Move.from(Direction.DOWN).equals(Move.GO_DOWN));
        assertTrue(Move.from(Direction.LEFT).equals(Move.GO_LEFT));
    }

    @Test
    public void testGetOpposite() {
        assertTrue(Move.GO_NOWHERE.getOpposite().equals(Move.GO_NOWHERE));
        assertTrue(Move.GO_UP.getOpposite().equals(Move.GO_DOWN));
        assertTrue(Move.GO_RIGHT.getOpposite().equals(Move.GO_LEFT));
        assertTrue(Move.GO_DOWN.getOpposite().equals(Move.GO_UP));
        assertTrue(Move.GO_LEFT.getOpposite().equals(Move.GO_RIGHT));
    }

    @Test
    public void testGetDelta() {// TODO devrait appartenir à (Board)Direction
        assertTrue(Move.from(Direction.NOWHERE).getDelta() == 0);
        assertTrue(Move.from(Direction.UP).getDelta() == -27);
        assertTrue(Move.from(Direction.RIGHT).getDelta() == 1);
        assertTrue(Move.from(Direction.DOWN).getDelta() == 27);
        assertTrue(Move.from(Direction.LEFT).getDelta() == -1);
    }

}