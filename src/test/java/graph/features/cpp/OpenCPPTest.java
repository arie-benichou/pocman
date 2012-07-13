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

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import graph.UndirectedGraph;
import graph.features.cpp.ClosedCPP;
import graph.features.cpp.ClosedCPPSolution;

import org.junit.Test;

import cpp.OpenCPP;


public class OpenCPPTest {

    @Test
    public void testFromClosedCPP() {
        final UndirectedGraph<String> input = new UndirectedGraph.Builder<String>(2).addEdge("A", "B", 1.0).build();
        final ClosedCPPSolution<String> closedCPPSolution = ClosedCPP.from(input).solve();
        assertNotNull(OpenCPP.from(closedCPPSolution));
    }

    @Test
    public void testGetClosedCPP() {
        final UndirectedGraph<String> input = new UndirectedGraph.Builder<String>(2).addEdge("A", "B", 1.0).build();
        final ClosedCPPSolution<String> closedCPPSolution = ClosedCPP.from(input).solve();
        final OpenCPP<String> openCPP = OpenCPP.from(closedCPPSolution);
        assertTrue(openCPP.getClosedCPPSolution().equals(closedCPPSolution)); // TODO ?
    }
}