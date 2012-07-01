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

package pocman.matching;

import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

import pocman.graph.UndirectedGraph;
import pocman.graph.UndirectedGraph.Builder;
import pocman.graph.WeightedEdge;

import com.google.common.collect.Maps;

public class MinimumWeightPerfectMatchingTest { // TODO à compléter

    @Test(expected = IllegalStateException.class)
    public void testComputeOptimalEulerizationOnEulerianGraph() {
        final Builder<String> builder = new UndirectedGraph.Builder<String>(3);
        builder.addEdge("A", "B", 1.0);
        builder.addEdge("B", "C", 1.0);
        builder.addEdge("A", "C", 1.0);
        final UndirectedGraph<String> graph = builder.build();
        MinimumWeightPerfectMatching.computeOptimalEulerization(graph);
    }

    @Test
    public void testComputeOptimalEulerization1() {
        final Builder<String> builder = new UndirectedGraph.Builder<String>(2);
        builder.addEdge("A", "B", 1.0);
        final UndirectedGraph<String> graph = builder.build();

        // TODO à revoir
        final Map<WeightedEdge<String>, Integer> expectedTraversalByEdge = Maps.newHashMap();
        expectedTraversalByEdge.put(graph.getEdge("A", "B"), 2);
        final Map<WeightedEdge<String>, Integer> traversalByEdge = MinimumWeightPerfectMatching.computeOptimalEulerization(graph);
        //System.out.println(traversalByEdge);
        Assert.assertTrue(expectedTraversalByEdge.equals(traversalByEdge));
    }

    @Test
    public void testComputeOptimalEulerization2() {

        final Builder<String> builder = new UndirectedGraph.Builder<String>(3);
        builder.addEdge("A", "B", 1.0);
        builder.addEdge("B", "C", 1.0);
        final UndirectedGraph<String> graph = builder.build();

        // TODO à revoir
        final Map<WeightedEdge<String>, Integer> expectedTraversalByEdge = Maps.newHashMap();
        expectedTraversalByEdge.put(graph.getEdge("A", "B"), 2);
        expectedTraversalByEdge.put(graph.getEdge("B", "C"), 2);
        final Map<WeightedEdge<String>, Integer> traversalByEdge = MinimumWeightPerfectMatching.computeOptimalEulerization(graph);
        //System.out.println(traversalByEdge);
        Assert.assertTrue(expectedTraversalByEdge.equals(traversalByEdge));

    }

}