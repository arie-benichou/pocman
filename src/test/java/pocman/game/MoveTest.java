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

package pocman.game;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import pocman.game.Move;
import pocman.maze.Direction;


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