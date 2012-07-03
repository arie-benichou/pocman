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

package pocman.graph;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Set;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import pocman.graph.Path.Factory;
import pocman.graph.UndirectedGraph.Builder;

import com.google.common.collect.Sets;

public class UndirectedGraphTest {

    private UndirectedGraph<String> graph;

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {}

    @AfterClass
    public static void tearDownAfterClass() throws Exception {}

    @Before
    public void setUp() throws Exception {
        final Builder<String> builder = new UndirectedGraph.Builder<String>(2);
        builder.addEdge("A", "B", 1.0);
        this.graph = builder.build();
    }

    @After
    public void tearDown() throws Exception {
        this.graph = null;
    }

    @Test
    public void testGetOrder() {
        final int order = this.graph.getOrder();
        assertEquals(2, order);
    }

    @Test
    public void testLegalContainsEdge() {
        final Builder<String> builder = new UndirectedGraph.Builder<String>(3);
        builder.addEdge("A", "B", 1.0);
        builder.addEdge("A", "C", 1.0);

        final UndirectedGraph<String> graph = builder.build();

        assertFalse(graph.contains("C", "B"));
        assertTrue(graph.contains("A", "B"));

        assertFalse(graph.contains("B", "C"));
        assertTrue(graph.contains("B", "A"));
    }

    @Test
    public void testLegalGetConnectedVerticeSet() {
        assertFalse(this.graph.getEndPoints("A").equals(Sets.newHashSet("C")));
        assertTrue(this.graph.getEndPoints("A").equals(Sets.newHashSet("B")));
        assertFalse(this.graph.getEndPoints("B").equals(Sets.newHashSet("C")));
        assertTrue(this.graph.getEndPoints("B").equals(Sets.newHashSet("A")));
    }

    @Test
    public void testIterator() {
        final Set<String> expectedSet = Sets.newHashSet("A", "B");
        final Set<String> actualSet = Sets.newHashSet();
        int i = 0;
        for (final String MazeNode : this.graph) {
            ++i;
            actualSet.add(MazeNode);
        }
        assertEquals(expectedSet.size(), i);
        assertTrue(actualSet.equals(expectedSet));
    }

    @Test
    public void testLegalGetEdge1() {
        final WeightedEdge<String> expectedEdge = WeightedEdge.from("A", "B", 1.0);
        final WeightedEdge<String> actualEdge = this.graph.getEdge("A", "B");
        assertTrue(actualEdge.equals(expectedEdge));
    }

    @Test
    public void testLegalGetEdge2() {
        final WeightedEdge<String> expectedEdge = WeightedEdge.from("B", "A", 1.0);
        final WeightedEdge<String> actualEdge = this.graph.getEdge("A", "B");
        assertTrue(actualEdge.equals(expectedEdge));
    }

    @Test
    public void testLegalGetEdges1() {
        final Set<WeightedEdge<String>> expectedEdges = Sets.newHashSet();
        expectedEdges.add(WeightedEdge.from("A", "B", 1.0));
        final Set<WeightedEdge<String>> actualEdges = this.graph.getEdges("A");
        assertTrue(actualEdges.equals(expectedEdges));
    }

    @Test
    public void testLegalGetEdges2() {
        final Set<WeightedEdge<String>> expectedEdges = Sets.newHashSet();
        expectedEdges.add(WeightedEdge.from("B", "A", 1.0));
        final Set<WeightedEdge<String>> actualEdges = this.graph.getEdges("A");
        assertTrue(actualEdges.equals(expectedEdges));
    }

    @Test
    public void testContainsT() {
        assertFalse(this.graph.contains("C"));
        assertTrue(this.graph.contains("A"));
        assertTrue(this.graph.contains("B"));
        assertFalse(this.graph.contains(""));
    }

    @Test
    public void testIsConnected1() {
        final Builder<String> builder = new UndirectedGraph.Builder<String>(4);
        builder.addEdge("A", "B", 1.0);
        builder.addEdge("C", "D", 1.0);
        final UndirectedGraph<String> graph = builder.build();
        assertFalse(graph.isConnected());
        assertFalse(graph.isConnected());
    }

    @Test
    public void testIsConnected2() {
        final Builder<String> builder = new UndirectedGraph.Builder<String>(2);
        builder.addEdge("A", "B", 1.0);
        final UndirectedGraph<String> graph = builder.build();
        assertTrue(graph.isConnected());
        assertTrue(graph.isConnected());
    }

    @Test
    public void testIsEulerian1() {
        final Builder<String> builder = new UndirectedGraph.Builder<String>(3);
        builder.addEdge("A", "B", 1.0);
        builder.addEdge("B", "C", 1.0);
        builder.addEdge("C", "A", 1.0);
        final UndirectedGraph<String> graph = builder.build();
        assertTrue(graph.isEulerian());
        assertTrue(graph.isEulerian());
    }

    @Test
    public void testIsNotEulerian2() {
        final Builder<String> builder = new UndirectedGraph.Builder<String>(3);
        builder.addEdge("A", "B", 1.0);
        builder.addEdge("B", "C", 1.0);
        final UndirectedGraph<String> graph = builder.build();
        assertFalse(graph.isEulerian());
        assertFalse(graph.isEulerian());
    }

    @Test
    public void testGetShortestPathBetween1() {
        final Path<String> expectedPath = new Path.Factory<String>().newPath(this.graph.getEdge("A", "B"));
        assertTrue(this.graph.isConnected());
        final Path<String> actualPath = this.graph.getShortestPathBetween("A", "B");
        assertTrue(actualPath.equals(expectedPath));
    }

    @Test
    public void testGetShortestPathBetween2() {
        final Builder<String> builder = new UndirectedGraph.Builder<String>(3);
        builder.addEdge("A", "B", 1.0);
        builder.addEdge("B", "C", 1.0);
        builder.addEdge("A", "C", 1.0);
        final UndirectedGraph<String> graph = builder.build();
        assertTrue(graph.isConnected());
        final Path<String> expectedPath = new Path.Factory<String>().newPath(graph.getEdge("A", "C"));
        final Path<String> actualPath = graph.getShortestPathBetween("A", "C");
        assertTrue(actualPath.equals(expectedPath));
    }

    @Test
    public void testGetShortestPathBetween3() {
        final Builder<String> builder = new UndirectedGraph.Builder<String>(3);
        builder.addEdge("A", "B", 1.0);
        builder.addEdge("B", "C", 1.0);
        builder.addEdge("A", "C", 3.0);
        final UndirectedGraph<String> graph = builder.build();
        assertTrue(graph.isConnected());
        final Factory<String> factory = new Path.Factory<String>();
        final Path<String> expectedPath = factory.newPath(graph.getEdge("A", "B")).add(factory.newPath(graph.getEdge("B", "C")));
        final Path<String> actualPath = graph.getShortestPathBetween("A", "C");
        assertTrue(actualPath.equals(expectedPath));
    }

    /*
    @Test
    public void testGetShortestPathBetween4() {
        final Path<String> expectedPath = new Path.Factory<String>().newPath("A", "B", 999.999);
        assertTrue(this.graph.isConnected());
        final Path<String> actualPath = this.graph.getShortestPathBetween("A", "B");
        assertTrue(actualPath.equals(expectedPath) == false);// TODO SHOULD be false : revoir Equals de Path
    }
    */

}