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

import graph.WeightedEdge;

import org.junit.Test;

public class WeightedEdgeTestExceptions {

    /*
    @Test(expected = IllegalArgumentException.class)
    public void testNewEdgeFromOneEndpointToSameEndpoint() {
        WeightedEdge.from("A", "A", 1.0);
    }
    */

    @Test(expected = IllegalArgumentException.class)
    public void testNewEdgeWithEndpoint1BeingNull() {
        WeightedEdge.from(null, "B", 1.0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNewEdgeWithEndpoint2BeingNull() {
        WeightedEdge.from("A", null, 1.0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNewEdgeWithNegativeWeight() {
        WeightedEdge.from("A", "B", -1.0);
    }

}