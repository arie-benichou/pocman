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

import java.util.NoSuchElementException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import pocman.graph.UndirectedGraph.Builder;

public class UndirectedGraphExceptionsTest {

    private UndirectedGraph<String> graph;

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

    @Test(expected = NoSuchElementException.class)
    public void testIllegalContainsEdge1() {
        this.graph.contains("A", "C");
    }

    @Test(expected = NoSuchElementException.class)
    public void testIllegalContainsEdge2() {
        this.graph.contains("C", "A");
    }

    @Test(expected = NoSuchElementException.class)
    public void testIllegalGetConnectedVerticeSet() {
        this.graph.getEndPoints("C");
    }

    @Test(expected = NoSuchElementException.class)
    public void testIllegalGetEdge1() {
        this.graph.getEdge("C", "A");
    }

    @Test(expected = NoSuchElementException.class)
    public void testIllegalGetEdge2() {
        this.graph.getEdge("A", "C");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIllegalContainsT() {
        this.graph.contains(null);
    }

    @Test(expected = NoSuchElementException.class)
    public void testIllegalGetShortestPathBetween1() {
        this.graph.getShortestPathBetween("A", "C");
    }

    @Test(expected = NoSuchElementException.class)
    public void testIllegalGetShortestPathBetween2() {
        this.graph.getShortestPathBetween("C", "A");
    }

}