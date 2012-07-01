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

import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Test;

import pocman.graph.UndirectedGraph;

import com.google.common.collect.Lists;

public class EulerianTrailTest {

    @Test
    public void testFrom1() {
        final UndirectedGraph<String> input = new UndirectedGraph.Builder<String>(2)
                .addEdge("A", "B", 1.0)
                .build();
        final List<String> expectedTrail = Lists.newArrayList("A", "B", "A");
        final Solution<String> solution = ClosedCPP.from(input).solve();
        final List<String> actualTrail = EulerianTrail.from(input, solution.getTraversalByEdge(), "A");
        assertTrue(actualTrail.equals(expectedTrail));
    }

    @Test
    public void testFrom2() {
        final UndirectedGraph<String> input = new UndirectedGraph.Builder<String>(3)
                .addEdge("A", "B", 1.0)
                .addEdge("B", "C", 1.0)
                .addEdge("C", "A", 1.0)
                .build();
        final List<String> expectedTrail1 = Lists.newArrayList("A", "B", "C", "A");
        final List<String> expectedTrail2 = Lists.newArrayList("A", "C", "B", "A");
        final Solution<String> solution = ClosedCPP.from(input).solve();
        final List<String> actualTrail = EulerianTrail.from(input, solution.getTraversalByEdge(), "A");
        assertTrue(actualTrail.equals(expectedTrail2) || actualTrail.equals(expectedTrail1));
    }

}