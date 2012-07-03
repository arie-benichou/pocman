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

import java.util.HashSet;
import java.util.Map;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import pocman.game.Move;
import pocman.maze.MazeNode.Type;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

public class MazeNodeTestExceptions {

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

    @Test(expected = IllegalArgumentException.class)
    public void testFromUnsupportedMazeNode() {
        MazeNode.from(-1, Sets.newHashSet(Move.GO_NOWHERE, Move.GO_UP, Move.GO_DOWN, Move.GO_RIGHT, Move.GO_LEFT));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetOptionsAsImmutableSet0() {
        vertices.get(Type.ISLAND.name()).getOptions().clear();
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetOptionsAsImmutableSet1() {
        vertices.get(Type.ISLAND.name()).getOptions().add(Move.GO_NOWHERE);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetOptionsAsImmutableSet2() {
        vertices.get(Type.ISLAND.name()).getOptions().addAll(Lists.newArrayList(Move.GO_NOWHERE, Move.GO_NOWHERE));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetOptionsAsImmutableSet3() {
        vertices.get(Type.ISLAND.name()).getOptions().remove(Move.GO_NOWHERE);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetOptionsAsImmutableSet4() {
        vertices.get(Type.ISLAND.name()).getOptions().removeAll(Lists.newArrayList(Move.GO_NOWHERE, Move.GO_NOWHERE));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetOptionsAsImmutableSet5() {
        vertices.get(Type.ISLAND.name()).getOptions().retainAll(Lists.newArrayList(Move.GO_NOWHERE, Move.GO_NOWHERE));
    }

}