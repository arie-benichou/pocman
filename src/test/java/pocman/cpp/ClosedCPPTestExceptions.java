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

package pocman.cpp;

import graph.UndirectedGraph;

import org.junit.Test;


import com.google.common.base.Supplier;

import cpp.ClosedCPP;

public class ClosedCPPTestExceptions {

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

}