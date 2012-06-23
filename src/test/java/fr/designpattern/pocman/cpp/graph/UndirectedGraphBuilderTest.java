
package fr.designpattern.pocman.cpp.graph;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import fr.designpattern.pocman.cpp.graph.UndirectedGraph.Builder;

public class UndirectedGraphBuilderTest {

    @Test(expected = IllegalArgumentException.class)
    public void testIllegalBuilder1() {
        new UndirectedGraph.Builder<String>(-1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIllegalBuilder2() {
        new UndirectedGraph.Builder<String>(0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIllegalBuilder3() {
        new UndirectedGraph.Builder<String>(1);
    }

    @Test
    public void testlegalBuilder() {
        new UndirectedGraph.Builder<String>(2);
    }

    @Test
    public void testGetNumberOfVertices() {
        final Builder<String> builder = new UndirectedGraph.Builder<String>(2);
        assertTrue(builder.getNumberOfVertices() == 2);
    }

    @Test(expected = IllegalStateException.class)
    public void testIllegalAddEdgeTTDouble1() {
        final Builder<String> builder = new UndirectedGraph.Builder<String>(2);
        builder.addEdge("1", "2", 1.0);
        builder.addEdge("3", "2", 1.0);
    }

    @Test(expected = IllegalStateException.class)
    public void testIllegalAddEdgeTTDouble2() {
        final Builder<String> builder = new UndirectedGraph.Builder<String>(2);
        builder.addEdge("1", "2", 1.0);
        builder.addEdge("1", "3", 1.0);
    }

    @Test(expected = IllegalStateException.class)
    public void testIllegalAddEdgeTTDouble3() {
        final Builder<String> builder = new UndirectedGraph.Builder<String>(2);
        builder.addEdge("A", "B", 1.0);
        builder.addEdge("A", "B", 1.0);
    }

    @Test(expected = IllegalStateException.class)
    public void testIllegalAddEdgeTTDouble4() {
        final Builder<String> builder = new UndirectedGraph.Builder<String>(2);
        builder.addEdge("A", "B", 1.0);
        builder.addEdge("B", "A", 1.0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddEdgeWithNullReferenceOfWeightedEdgeOfT() {
        final Builder<String> builder = new UndirectedGraph.Builder<String>(2);
        builder.addEdge(null);
    }

    @Test
    public void testAddEdgeTTDouble4() {
        final Builder<String> builder = new UndirectedGraph.Builder<String>(3);
        assertTrue(builder.getNumberOfVertices() == 3);
        assertTrue(builder.addEdge("A", "B", 1.0) == builder);
        assertTrue(builder.addEdge("A", "C", 1.0) == builder);
    }

    @Test
    public void testContains() {
        final Builder<String> builder = new UndirectedGraph.Builder<String>(2);
        final WeightedEdge<String> edge = new WeightedEdge.Factory<String>().newEdge("A", "B", 1.0);
        assertTrue(!builder.contains(edge));
        assertTrue(builder.addEdge("A", "B", 1.0) == builder);
        assertTrue(builder.contains(edge));
        assertTrue(builder.contains(edge.reverse()));
        assertTrue(builder.contains(new WeightedEdge.Factory<String>().newEdge("B", "A", 1.0)));
        assertTrue(!builder.contains(new WeightedEdge.Factory<String>().newEdge("A", "C", 1.0)));
    }

    @Test(expected = IllegalStateException.class)
    public void testIllegalBuild1() {
        final Builder<String> builder = new UndirectedGraph.Builder<String>(2);
        builder.build();
    }

    @Test(expected = IllegalStateException.class)
    public void testIllegalBuild2() {
        final Builder<String> builder = new UndirectedGraph.Builder<String>(3);
        assertTrue(builder.addEdge("A", "B", 1.0) == builder);
        builder.build();
    }

    @Test
    public void testLegalBuild() {
        final Builder<String> builder = new UndirectedGraph.Builder<String>(2);
        assertTrue(builder.addEdge("A", "B", 1.0) == builder);
        final UndirectedGraph<String> build = builder.build();
        assertTrue(build != null);
        assertTrue(build instanceof UndirectedGraph);
    }

}