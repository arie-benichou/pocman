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

package graph;

import static org.junit.Assert.assertTrue;
import graph.UndirectedGraph;
import graph.UndirectedGraph.Builder;

import org.junit.Test;


public class UndirectedGraphBuilderTestExceptions {

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

}