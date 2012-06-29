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

import static org.junit.Assert.assertTrue;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import pocman.graph.Path;
import pocman.graph.UndirectedGraph;
import pocman.graph.WeightedEdge;
import pocman.graph.Path.Factory;
import pocman.graph.UndirectedGraph.Builder;

import com.google.common.collect.Lists;
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

    /*
    @Test(expected = UnsupportedOperationException.class)
    public void testAddMazeNode() {
        this.graph.addMazeNode("C");
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testAddEdge() {
        this.graph.addEdge("A", "C");
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testRemoveEdge() {
        this.graph.addEdge("A", "B");
    }
    */

    @Test
    public void testIsEmpty() {
        assertTrue(this.graph.isEmpty() == false);
    }

    @Test
    public void testGetOrder() {
        assertTrue(this.graph.getOrder() == 2);
    }

    @Test(expected = NoSuchElementException.class)
    public void testIllegalEdgeExists() {
        this.graph.hasEdge("A", "C");
    }

    @Test
    public void testLegalEdgeExists() {
        final Builder<String> builder = new UndirectedGraph.Builder<String>(3);
        builder.addEdge("A", "B", 1.0);
        builder.addEdge("A", "C", 1.0);

        final UndirectedGraph<String> graph = builder.build();

        assertTrue(graph.hasEdge("C", "B") == false);
        assertTrue(graph.hasEdge("A", "B"));

        assertTrue(graph.hasEdge("B", "C") == false);
        assertTrue(graph.hasEdge("B", "A"));
    }

    @Test(expected = NoSuchElementException.class)
    public void testIllegalGetConnectedVerticeSet() {
        this.graph.getConnectedVerticeSet("C");
    }

    @Test
    public void testLegalGetConnectedVerticeSet() {
        assertTrue(this.graph.getConnectedVerticeSet("A").equals(Sets.newHashSet("C")) == false);
        assertTrue(this.graph.getConnectedVerticeSet("A").equals(Sets.newHashSet("B")));
        assertTrue(this.graph.getConnectedVerticeSet("B").equals(Sets.newHashSet("C")) == false);
        assertTrue(this.graph.getConnectedVerticeSet("B").equals(Sets.newHashSet("A")));
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
        assertTrue(i == expectedSet.size());
        assertTrue(actualSet.equals(expectedSet));
    }

    @Test(expected = NoSuchElementException.class)
    public void testIllegalGetEdge1() {
        this.graph.getEdge("C", "A");
    }

    @Test(expected = NoSuchElementException.class)
    public void testIllegalGetEdge2() {
        this.graph.getEdge("A", "C");
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

    @Test(expected = NoSuchElementException.class)
    public void testIllegalGetEdges() {
        this.graph.getEdges("C");
    }

    @Test
    public void testLegalGetEdges1() {
        final List<WeightedEdge<String>> expectedEdges = Lists.newArrayList();
        expectedEdges.add(WeightedEdge.from("A", "B", 1.0));
        final List<WeightedEdge<String>> actualEdges = this.graph.getEdges("A");
        assertTrue(actualEdges.equals(expectedEdges));
    }

    @Test
    public void testLegalGetEdges2() {
        final List<WeightedEdge<String>> expectedEdges = Lists.newArrayList();
        expectedEdges.add(WeightedEdge.from("B", "A", 1.0));
        final List<WeightedEdge<String>> actualEdges = this.graph.getEdges("A");
        assertTrue(actualEdges.equals(expectedEdges));
    }

    @Test
    public void testIsConnected() {
        final Builder<String> builder = new UndirectedGraph.Builder<String>(4);
        builder.addEdge("A", "B", 1.0);
        builder.addEdge("C", "D", 1.0);
        final UndirectedGraph<String> graph = builder.build();
        assertTrue(graph.isConnected() == false);
        assertTrue(this.graph.isConnected());
    }

    @Test
    public void testIsEulerian() {
        assertTrue(this.graph.isEulerian() == false);
        final Builder<String> builder = new UndirectedGraph.Builder<String>(3);
        builder.addEdge("A", "B", 1.0);
        builder.addEdge("B", "C", 1.0);
        builder.addEdge("C", "A", 1.0);
        final UndirectedGraph<String> graph = builder.build();
        assertTrue(graph.isEulerian());
    }

    @Test(expected = NoSuchElementException.class)
    public void testIllegalGetShortestPathBetween1() {
        this.graph.getShortestPathBetween("A", "C");
    }

    @Test(expected = NoSuchElementException.class)
    public void testIllegalGetShortestPathBetween2() {
        this.graph.getShortestPathBetween("C", "A");
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

    //@Test
    public void testGetShortestPathBetween4() {
        final Path<String> expectedPath = new Path.Factory<String>().newPath("A", "B", 999.999);
        assertTrue(this.graph.isConnected());
        final Path<String> actualPath = this.graph.getShortestPathBetween("A", "B");
        assertTrue(actualPath.equals(expectedPath) == false);// TODO SHOULD be false : revoir Equals de Path
    }

    @Test
    public void test() {
        final Builder<String> builder = new UndirectedGraph.Builder<String>(2);
        builder.addEdge(null, "A", 1.0);
        final UndirectedGraph<String> graph = builder.build();
    }
}