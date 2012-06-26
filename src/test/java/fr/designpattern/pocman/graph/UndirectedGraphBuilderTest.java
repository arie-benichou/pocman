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

package fr.designpattern.pocman.graph;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import fr.designpattern.pocman.graph.UndirectedGraph;
import fr.designpattern.pocman.graph.WeightedEdge;
import fr.designpattern.pocman.graph.UndirectedGraph.Builder;

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