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
import static org.junit.Assert.assertTrue;

import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import pocman.game.Move;
import pocman.graph.UndirectedGraph;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

public class MazeAsGraphTest {

    private MazeAsBoard mazeAsBoard;
    private MazeAsGraph mazeAsGraph;

    @Before
    public void setup() {

        final String data = "" +
                "┃  ⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛┃" +
                "┃⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛┃" +
                "┃⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛┃" +
                "┃⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛┃" +
                "┃⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛┃" +
                "┃⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛┃" +
                "┃⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛┃" +
                "┃⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛┃" +
                "┃⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛┃" +
                "┃⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛┃" +
                "┃⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛┃" +
                "┃⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛┃" +
                "┃⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛┃" +
                "┃⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛┃" +
                "┃⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛┃" +
                "┃⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛┃" +
                "┃⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛┃" +
                "┃⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛┃" +
                "┃⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛┃";

        this.mazeAsBoard = MazeAsBoard.from(data);
        this.mazeAsGraph = MazeAsGraph.from(this.mazeAsBoard);
    }

    @After
    public void tearDown() {
        this.mazeAsBoard = null;
        this.mazeAsGraph = null;
    }

    @Test
    public void testGetBoard() {
        assertTrue(this.mazeAsGraph.getBoard().equals(this.mazeAsBoard));
    }

    @Test
    public void testGetNumberOfVertices() {
        final int expected = 2;
        assertEquals(expected, this.mazeAsGraph.getNumberOfVertices());
    }

    @Test
    public void testGetNodeById() {
        final MazeNode node1 = MazeNode.from(1, Sets.newHashSet(Move.from(Direction.RIGHT)));
        final MazeNode node2 = MazeNode.from(2, Sets.newHashSet(Move.from(Direction.LEFT)));
        assertTrue(this.mazeAsGraph.getNode(1).equals(node1));
        assertTrue(this.mazeAsGraph.getNode(2).equals(node2));
    }

    @Test
    public void testGetWalkableNodes() {
        final Map<Integer, MazeNode> expected = Maps.newHashMap();
        expected.put(1, this.mazeAsGraph.getNode(1));
        expected.put(2, this.mazeAsGraph.getNode(2));
        assertTrue(expected.equals(this.mazeAsGraph.getWalkableNodes()));
    }

    @Test
    public void testGetUndirectedGraphOfMazeNodeSupplierInterface() {
        assertTrue(this.mazeAsGraph.get() instanceof UndirectedGraph); // TODO egalité de 2 graphes
    }

    @Test
    public void testHasIsland() {
        assertFalse(this.mazeAsGraph.hasIsland());
        final MazeAsGraph mazeAsGraph = MazeAsGraph.from("" +
                "┃⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛┃" +
                "┃⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛     ⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛┃" +
                "┃⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛     ⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛┃" +
                "┃⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛┃" +
                "┃⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛┃" +
                "┃⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛┃" +
                "┃⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛┃" +
                "┃⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛┃" +
                "┃⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛┃" +
                "┃⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛┃" +
                "┃⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛     ⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛┃" +
                "┃⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛     ⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛┃" +
                "┃⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛┃" +
                "┃⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛┃" +
                "┃⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛┃" +
                "┃⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛┃" +
                "┃⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛┃" +
                "┃⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛┃" +
                "┃⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛┃");

        assertTrue(mazeAsGraph.hasIsland());
    }
}