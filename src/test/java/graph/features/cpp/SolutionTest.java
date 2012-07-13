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

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import graph.WeightedEdge;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;


import com.google.common.collect.ImmutableMap;

import cpp.OpenCPPSolution;

public class SolutionTest {

    private final static Map<WeightedEdge<String>, Integer> TRAVERSAL_BY_EDGE = ImmutableMap.copyOf(new HashMap<WeightedEdge<String>, Integer>());
    private final static OpenCPPSolution<String> SOLUTION = new OpenCPPSolution<String>(TRAVERSAL_BY_EDGE, 1.0, 2.0);

    /*
    //@Test(expected = IllegalArgumentException.class)
    public void testSolution1() {
        new Solution<String>(null, 1.0, 0.0);
    }

    //@Test(expected = IllegalArgumentException.class)
    public void testSolution2() {
        new Solution<String>(TRAVERSAL_BY_EDGE, null, 0.0);
    }

    //@Test(expected = IllegalArgumentException.class)
    public void testSolution3() {
        new Solution<String>(TRAVERSAL_BY_EDGE, 0.0, null);
    }
    */

    @Test
    public void testGetLowerBoundCost() {
        assertTrue(SOLUTION.getLowerBoundCost().equals(1.0));
    }

    @Test
    public void testGetUpperBoundCost() {
        assertTrue(SOLUTION.getUpperBoundCost().equals(2.0));
    }

    @Test
    public void testGetTraversalByEdge() {
        assertTrue(SOLUTION.getTraversalByEdge().equals(TRAVERSAL_BY_EDGE));
    }

    @Test
    public void testEqualsObject() {
        assertFalse(SOLUTION.equals(null));
        assertTrue(SOLUTION.equals(SOLUTION));
        assertFalse(SOLUTION.equals(new Object()));
        OpenCPPSolution<String> differentSolution;
        differentSolution = new OpenCPPSolution<String>(TRAVERSAL_BY_EDGE, 0.0, 2.0);
        assertFalse(SOLUTION.equals(differentSolution));
        differentSolution = new OpenCPPSolution<String>(TRAVERSAL_BY_EDGE, 1.0, 3.0);
        assertFalse(SOLUTION.equals(differentSolution));
        final OpenCPPSolution<String> sameSolution = new OpenCPPSolution<String>(TRAVERSAL_BY_EDGE, 1.0, 2.0);
        assertTrue(SOLUTION.equals(sameSolution));
    }

    /*
    @Test
    public void testHashCode() {
        fail("Not yet implemented"); // TODO
    }
    */

}