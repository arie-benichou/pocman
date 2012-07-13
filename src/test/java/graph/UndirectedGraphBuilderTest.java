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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import graph.UndirectedGraph;
import graph.WeightedEdge;
import graph.UndirectedGraph.Builder;

import org.junit.Test;


public class UndirectedGraphBuilderTest {

    @Test
    public void testlegalBuilder() {
        new UndirectedGraph.Builder<String>(2);
    }

    @Test
    public void testGetOrder() {
        final Builder<String> builder = new UndirectedGraph.Builder<String>(2);
        assertEquals(2, builder.getOrder());
    }

    @Test
    public void testAddEdgeTTDouble4() {
        final Builder<String> builder = new UndirectedGraph.Builder<String>(3);
        assertEquals(3, builder.getOrder());
        assertEquals(builder, builder.addEdge("A", "B", 1.0));
        assertEquals(builder, builder.addEdge("A", "C", 1.0));
    }

    @Test
    public void testContains() {
        final Builder<String> builder = new UndirectedGraph.Builder<String>(2);
        final WeightedEdge<String> edge = WeightedEdge.from("A", "B", 1.0);

        assertFalse(builder.contains(edge));
        assertEquals(builder, builder.addEdge("A", "B", 1.0));
        assertTrue(builder.contains(edge));

        assertTrue(builder.contains(edge.reverse()));

        assertTrue(builder.contains(WeightedEdge.from("B", "A", 1.0)));
        assertFalse(builder.contains(WeightedEdge.from("A", "C", 1.0)));
    }

    @Test
    public void testLegalBuild() {
        final Builder<String> builder = new UndirectedGraph.Builder<String>(2);
        assertEquals(builder, builder.addEdge("A", "B", 1.0));
        final UndirectedGraph<String> build = builder.build();
        assertNotNull(build);
        assertTrue(build instanceof UndirectedGraph);
    }

}