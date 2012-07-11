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

package pocman.matching.edmonds1;

import static org.junit.Assert.assertTrue;
import junit.framework.Assert;

import matching.edmonds1.EdmondsAlgorithm;
import matching.edmonds1.MutableUndirectedGraph;

import org.junit.Test;


public class EdmondsMatchingTest { // TODO à compléter

    @Test
    public void testMaximumMatching1() {
        final MutableUndirectedGraph<String> graph = new MutableUndirectedGraph<String>();
        graph.addEndPoint("A");
        graph.addEndPoint("B");
        graph.addEdge("A", "B");
        final MutableUndirectedGraph<String> maximumMatching = EdmondsAlgorithm.maximumMatching(graph);
        Assert.assertTrue(maximumMatching.equals(graph));
    }

    @Test
    public void testMaximumMatching2() {
        final MutableUndirectedGraph<String> graph = new MutableUndirectedGraph<String>();
        graph.addEndPoint("A");
        graph.addEndPoint("B");
        graph.addEndPoint("C");
        graph.addEndPoint("D");
        graph.addEdge("A", "B");
        graph.addEdge("C", "D");
        final MutableUndirectedGraph<String> maximumMatching = EdmondsAlgorithm.maximumMatching(graph);
        assertTrue(maximumMatching.equals(graph));
    }

    @Test
    public void testMaximumMatching3() {
        final MutableUndirectedGraph<String> graph = new MutableUndirectedGraph<String>();
        graph.addEndPoint("A");
        graph.addEndPoint("B");
        graph.addEndPoint("C");
        graph.addEndPoint("D");
        graph.addEdge("A", "B");
        graph.addEdge("B", "C");
        graph.addEdge("C", "D");
        final MutableUndirectedGraph<String> maximumMatching = EdmondsAlgorithm.maximumMatching(graph);
        graph.removeEdge("B", "C");
        assertTrue(maximumMatching.equals(graph));
    }

    @Test
    public void testMaximumMatching4() {
        final MutableUndirectedGraph<String> graph = new MutableUndirectedGraph<String>();
        graph.addEndPoint("A");
        graph.addEndPoint("B");
        graph.addEndPoint("C");
        graph.addEndPoint("D");
        graph.addEdge("A", "B");
        graph.addEdge("B", "C");
        graph.addEdge("C", "D");
        graph.addEdge("A", "D");
        final MutableUndirectedGraph<String> maximumMatching = EdmondsAlgorithm.maximumMatching(graph);
        graph.removeEdge("A", "B");
        graph.removeEdge("C", "D");
        assertTrue(maximumMatching.equals(graph));
    }

}