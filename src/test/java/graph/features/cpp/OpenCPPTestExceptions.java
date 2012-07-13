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

package graph.features.cpp;

import graph.UndirectedGraph;
import graph.features.cpp.ClosedCPP;
import graph.features.cpp.ClosedCPPSolution;

import org.junit.Test;

import cpp.OpenCPP;


public class OpenCPPTestExceptions {

    @Test(expected = IllegalArgumentException.class)
    public void testFromNullReference1() {
        final UndirectedGraph<?> graph = null;
        OpenCPP.from(graph);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFromNullReference2() {
        final ClosedCPPSolution<?> ClosedCPPSolution = null;
        OpenCPP.from(ClosedCPPSolution);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSolveFromNullReference() {
        final UndirectedGraph<String> input = new UndirectedGraph.Builder<String>(2).addEdge("A", "B", 1.0).build();
        final ClosedCPPSolution<String> ClosedCPPSolution = ClosedCPP.from(input).solve();
        OpenCPP.from(ClosedCPPSolution).solveFrom(null);
    }

    @Test(expected = IllegalStateException.class)
    public void testSolveFromUnknownMazeNode() {
        final UndirectedGraph<String> input = new UndirectedGraph.Builder<String>(2).addEdge("A", "B", 1.0).build();
        final ClosedCPPSolution<String> ClosedCPPSolution = ClosedCPP.from(input).solve();
        OpenCPP.from(ClosedCPPSolution).solveFrom("C");
    }

}