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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertTrue;

import java.util.HashSet;
import java.util.Map;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import pocman.game.MazeNode;
import pocman.game.Move;
import pocman.game.MazeNode.Type;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Sets;

public class MazeNodeTest {

    private static Map<String, MazeNode> vertices;

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        vertices = new ImmutableMap.Builder<String, MazeNode>()
                .put(Type.ISLAND.name(), MazeNode.from(0, new HashSet<Move>()))
                .put(Type.DEAD_END.name(), MazeNode.from(1, Sets.newHashSet(Move.GO_UP)))
                .put(Type.STREET.name(), MazeNode.from(2, Sets.newHashSet(Move.GO_UP, Move.GO_DOWN)))
                .put(Type.CORNER.name(), MazeNode.from(3, Sets.newHashSet(Move.GO_UP, Move.GO_RIGHT)))
                .put(Type.CROSSROADS.name(), MazeNode.from(4, Sets.newHashSet(Move.GO_UP, Move.GO_DOWN, Move.GO_RIGHT)))
                .put(Type.ROUNDABOUT.name(), MazeNode.from(5, Sets.newHashSet(Move.GO_UP, Move.GO_DOWN, Move.GO_RIGHT, Move.GO_LEFT)))
                .build();
    }

    @AfterClass
    public static void tearDownAfterClass() throws Exception {
        vertices = null;
    }

    @Test
    public void testGetId() {
        assertEquals(0, vertices.get(Type.ISLAND.name()).getId());
        assertEquals(1, vertices.get(Type.DEAD_END.name()).getId());
        assertEquals(2, vertices.get(Type.STREET.name()).getId());
        assertEquals(3, vertices.get(Type.CORNER.name()).getId());
        assertEquals(4, vertices.get(Type.CROSSROADS.name()).getId());
        assertEquals(5, vertices.get(Type.ROUNDABOUT.name()).getId());
    }

    @Test
    public void testGetType() {
        assertTrue(vertices.get(Type.ISLAND.name()).getType().equals(Type.ISLAND));
        assertTrue(vertices.get(Type.DEAD_END.name()).getType().equals(Type.DEAD_END));
        assertTrue(vertices.get(Type.STREET.name()).getType().equals(Type.STREET));
        assertTrue(vertices.get(Type.CORNER.name()).getType().equals(Type.CORNER));
        assertTrue(vertices.get(Type.CROSSROADS.name()).getType().equals(Type.CROSSROADS));
        assertTrue(vertices.get(Type.ROUNDABOUT.name()).getType().equals(Type.ROUNDABOUT));
    }

    @Test
    public void testIs() {
        assertTrue(vertices.get(Type.ISLAND.name()).is(Type.ISLAND));
        assertTrue(vertices.get(Type.DEAD_END.name()).is(Type.DEAD_END));
        assertTrue(vertices.get(Type.STREET.name()).is(Type.STREET));
        assertTrue(vertices.get(Type.CORNER.name()).is(Type.CORNER));
        assertTrue(vertices.get(Type.CROSSROADS.name()).is(Type.CROSSROADS));
        assertTrue(vertices.get(Type.ROUNDABOUT.name()).is(Type.ROUNDABOUT));
    }

    @Test
    public void testGetNumberOfOptions() {
        assertEquals(0, vertices.get(Type.ISLAND.name()).getNumberOfOptions());
        assertEquals(1, vertices.get(Type.DEAD_END.name()).getNumberOfOptions());
        assertEquals(2, vertices.get(Type.STREET.name()).getNumberOfOptions());
        assertEquals(2, vertices.get(Type.CORNER.name()).getNumberOfOptions());
        assertEquals(3, vertices.get(Type.CROSSROADS.name()).getNumberOfOptions());
        assertEquals(4, vertices.get(Type.ROUNDABOUT.name()).getNumberOfOptions());
    }

    @Test
    public void testGetOptions() {
        assertTrue(vertices.get(Type.ISLAND.name()).getOptions().equals(new HashSet<Move>()));
        assertTrue(vertices.get(Type.DEAD_END.name()).getOptions().equals(Sets.newHashSet(Move.GO_UP)));
        assertTrue(vertices.get(Type.STREET.name()).getOptions().equals(Sets.newHashSet(Move.GO_UP, Move.GO_DOWN)));
        assertTrue(vertices.get(Type.CORNER.name()).getOptions().equals(Sets.newHashSet(Move.GO_UP, Move.GO_RIGHT)));
        assertTrue(vertices.get(Type.CROSSROADS.name()).getOptions().equals(Sets.newHashSet(Move.GO_UP, Move.GO_DOWN, Move.GO_RIGHT)));
        assertTrue(vertices.get(Type.ROUNDABOUT.name()).getOptions().equals(Sets.newHashSet(Move.GO_UP, Move.GO_DOWN, Move.GO_RIGHT, Move.GO_LEFT)));
    }

    @Test
    public void testEqualsObject() {
        assertFalse(vertices.get(Type.ISLAND.name()).equals(null));
        assertTrue(vertices.get(Type.ISLAND.name()).equals(vertices.get(Type.ISLAND.name())));
        assertFalse(vertices.get(Type.ISLAND.name()).equals(new Object()));
        assertFalse(vertices.get(Type.ISLAND.name()).equals(vertices.get(Type.DEAD_END.name())));
        assertTrue(vertices.get(Type.ISLAND.name()).equals(MazeNode.from(0, new HashSet<Move>())));
        assertFalse(vertices.get(Type.ISLAND.name()).equals(MazeNode.from(1, new HashSet<Move>())));
        //assertFalse(vertices.get(Type.ISLAND.name()).equals(MazeNode.from(0, Sets.newHashSet(Move.GO_NOWHERE)))); // TODO
    }

    //@Test
    public void testHashCode() { // TODO à compléter...

        assertNotSame(vertices.get(Type.ISLAND.name()).hashCode(), vertices.get(Type.DEAD_END.name()).hashCode());
        assertNotSame(vertices.get(Type.DEAD_END.name()).hashCode(), vertices.get(Type.STREET.name()).hashCode());
        assertNotSame(vertices.get(Type.STREET.name()).hashCode(), vertices.get(Type.CORNER.name()).hashCode());
        assertNotSame(vertices.get(Type.CORNER.name()).hashCode(), vertices.get(Type.CROSSROADS.name()).hashCode());
        assertNotSame(vertices.get(Type.CROSSROADS.name()).hashCode(), vertices.get(Type.ROUNDABOUT.name()).hashCode());

        assertEquals(vertices.get(Type.ISLAND.name()).hashCode(), MazeNode.from(0, new HashSet<Move>()).hashCode());
        assertNotSame(vertices.get(Type.ISLAND.name()).hashCode(), MazeNode.from(1, new HashSet<Move>()).hashCode());
        assertNotSame(vertices.get(Type.ISLAND.name()).hashCode(), MazeNode.from(0, Sets.newHashSet(Move.GO_NOWHERE)).hashCode());

        assertEquals(vertices.get(Type.DEAD_END.name()).hashCode(), MazeNode.from(1, Sets.newHashSet(Move.GO_UP)).hashCode());

        assertEquals(vertices.get(Type.STREET.name()).hashCode(), MazeNode.from(2, Sets.newHashSet(Move.GO_UP, Move.GO_DOWN)).hashCode());
        assertEquals(vertices.get(Type.STREET.name()).hashCode(), MazeNode.from(2, Sets.newHashSet(Move.GO_DOWN, Move.GO_UP)).hashCode());

        assertEquals(vertices.get(Type.CORNER.name()).hashCode(), MazeNode.from(3, Sets.newHashSet(Move.GO_UP, Move.GO_RIGHT)).hashCode());
        assertEquals(vertices.get(Type.CORNER.name()).hashCode(), MazeNode.from(3, Sets.newHashSet(Move.GO_RIGHT, Move.GO_UP)).hashCode());

        assertEquals(vertices.get(Type.CROSSROADS.name()).hashCode(), MazeNode.from(4, Sets.newHashSet(Move.GO_UP, Move.GO_DOWN, Move.GO_RIGHT)).hashCode());
        assertEquals(vertices.get(Type.CROSSROADS.name()).hashCode(), MazeNode.from(4, Sets.newHashSet(Move.GO_RIGHT, Move.GO_UP, Move.GO_DOWN)).hashCode());

        assertEquals(vertices.get(Type.ROUNDABOUT.name()).hashCode(), MazeNode.from(5, Sets.newHashSet(Move.GO_UP, Move.GO_DOWN, Move.GO_RIGHT, Move.GO_LEFT))
                .hashCode());
        assertEquals(vertices.get(Type.ROUNDABOUT.name()).hashCode(), MazeNode.from(5, Sets.newHashSet(Move.GO_RIGHT, Move.GO_LEFT, Move.GO_UP, Move.GO_DOWN))
                .hashCode());
    }

}