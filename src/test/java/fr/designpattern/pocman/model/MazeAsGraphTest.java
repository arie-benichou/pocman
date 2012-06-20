
package fr.designpattern.pocman.model;

import static org.junit.Assert.assertTrue;

import java.util.HashMap;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

import fr.designpattern.pocman.cpp.graph.UndirectedGraph;
import fr.designpattern.pocman.cpp.graph.Vertex;

public class MazeAsGraphTest {

    private MazeAsBoard board;
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

        this.board = MazeAsBoard.from(data);
        this.mazeAsGraph = new MazeAsGraph.Factory().newMazeAsGraph(this.board);
    }

    @After
    public void tearDown() {
        this.board = null;
        this.mazeAsGraph = null;
    }

    @Test
    public void testGetBoard() {
        assertTrue(this.mazeAsGraph.getBoard().equals(this.board));
    }

    @Test
    public void testGetNumberOfVertices() {
        final int expected = 2;
        assertTrue(expected == this.mazeAsGraph.getNumberOfVertices());
    }

    @Test
    public void testGetNodeById() {
        final Vertex node1 = Vertex.from(1, Sets.newHashSet(Move.from(Direction.RIGHT)));
        final Vertex node2 = Vertex.from(2, Sets.newHashSet(Move.from(Direction.LEFT)));
        assertTrue(this.mazeAsGraph.getNodeById(1).equals(node1));
        assertTrue(this.mazeAsGraph.getNodeById(2).equals(node2));
    }

    @Test
    public void testGetWalkableGameTiles() {
        final HashMap<Integer, Vertex> expected = Maps.newHashMap();
        expected.put(1, this.mazeAsGraph.getNodeById(1));
        expected.put(2, this.mazeAsGraph.getNodeById(2));
        assertTrue(expected.equals(this.mazeAsGraph.getWalkableGameTiles()));
    }

    @Test
    public void testGetUndirectedGraphOfVertexSupplierInterface() {
        assertTrue(this.mazeAsGraph.get() instanceof UndirectedGraph); // TODO egalité de 2 graphes
    }

    @Test
    public void testIsConnected() {
        assertTrue(this.mazeAsGraph.isConnected());
    }

    @Test
    public void testToString() { // TODO tester plutôt la vue
        final String expected = "\n 11                        \n";
        System.out.println(expected.equals(this.mazeAsGraph.toString()));
    }

}