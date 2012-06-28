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

package fr.designpattern.pocman.cpp;

import static org.junit.Assert.assertTrue;

import java.util.Map;

import org.junit.Test;

import com.google.common.base.Supplier;
import com.google.common.collect.Maps;

import fr.designpattern.pocman.graph.UndirectedGraph;
import fr.designpattern.pocman.graph.WeightedEdge;

public class ClosedCPPTest {

    @Test(expected = IllegalArgumentException.class)
    public void testFromUndirectedGraphOfTWithNullReference() {
        final UndirectedGraph<?> input = null;
        ClosedCPP.from(input);
    }

    @Test(expected = IllegalStateException.class)
    public void testFromUndirectedGraphOfTWithNotConnectedGraph() {
        final UndirectedGraph<String> input = new UndirectedGraph.Builder<String>(4).addEdge("A", "B", 1.0).addEdge("C", "D", 1.0).build();
        ClosedCPP.from(input);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFromSupplierOfUndirectedGraphOfTWithNullReference() {
        final Supplier<UndirectedGraph<Object>> input = null;
        ClosedCPP.from(input);
    }

    @Test
    public void testFromUndirectedGraphOfT() {
        final UndirectedGraph<String> input = new UndirectedGraph.Builder<String>(2).addEdge("A", "B", 1.0).build();
        final ClosedCPP<String> solver = ClosedCPP.from(input);
        assertTrue(solver != null);
        assertTrue(solver instanceof ClosedCPP);
    }

    @Test
    public void testFromSupplierOfUndirectedGraphOfT() {
        final Supplier<UndirectedGraph<String>> input = new Supplier<UndirectedGraph<String>>() {

            @Override
            public UndirectedGraph<String> get() {
                return new UndirectedGraph.Builder<String>(2).addEdge("A", "B", 1.0).build();
            }
        };
        final ClosedCPP<String> solver = ClosedCPP.from(input);
        assertTrue(solver != null);
        assertTrue(solver instanceof ClosedCPP);
    }

    @Test
    public void testGetLowerBoundCost1() {
        final UndirectedGraph<String> input = new UndirectedGraph.Builder<String>(2).addEdge("A", "B", 1.0).build();
        final ClosedCPP<String> solver = ClosedCPP.from(input);
        assertTrue(solver.getLowerBoundCost() == 1.0);
    }

    @Test
    public void testGetLowerBoundCost2() {
        final UndirectedGraph<String> input = new UndirectedGraph.Builder<String>(3)
                .addEdge("A", "B", 1.0)
                .addEdge("B", "C", 2.0)
                .addEdge("C", "A", 1.0)
                .build();
        final ClosedCPP<String> solver = ClosedCPP.from(input);
        assertTrue(solver.getLowerBoundCost() == 4.0);
    }

    @Test
    public void testGetUpperBoundCost1() {
        final UndirectedGraph<String> input = new UndirectedGraph.Builder<String>(2).addEdge("A", "B", 1.0).build();
        final ClosedCPP<String> solver = ClosedCPP.from(input);
        assertTrue(solver.getUpperBoundCost() == 2.0);
    }

    @Test
    public void testSolveNonEulerianGraph() {
        final UndirectedGraph<String> input = new UndirectedGraph.Builder<String>(2).addEdge("A", "B", 1.0).build();
        final Map<WeightedEdge<String>, Integer> expectedTraversalByEdge = Maps.newHashMap();
        expectedTraversalByEdge.put(input.getEdge("A", "B"), 2);
        final Solution<String> expectedSolution = new Solution<String>(expectedTraversalByEdge, 1.0, 2.0);
        final Solution<String> solution = ClosedCPP.from(input).solve();
        assertTrue(solution.equals(expectedSolution));
    }

    @Test
    public void testSolveFromWithEulerianGraph() {
        final UndirectedGraph<String> input = new UndirectedGraph.Builder<String>(3)
                .addEdge("A", "B", 1.0)
                .addEdge("B", "C", 1.0)
                .addEdge("C", "A", 1.0)
                .build();

        final Map<WeightedEdge<String>, Integer> expectedTraversalByEdge = Maps.newHashMap();
        expectedTraversalByEdge.put(input.getEdge("A", "B"), 1);
        expectedTraversalByEdge.put(input.getEdge("B", "C"), 1);
        expectedTraversalByEdge.put(input.getEdge("C", "A"), 1);
        final Solution<String> expectedSolution = new Solution<String>(expectedTraversalByEdge, 3.0, 3.0);
        final Solution<String> solution = ClosedCPP.from(input).solve();
        assertTrue(solution.equals(expectedSolution));
    }

}