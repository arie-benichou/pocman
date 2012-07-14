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

package graph.features.eulerianTrail;

import static org.junit.Assert.assertTrue;
import graph.UndirectedGraph;
import graph.features.cpp.ClosedCPPFeature;
import graph.features.cpp.ClosedCPPInterface;
import graph.features.cpp.ClosedCPPSolution;

import java.util.List;

import org.junit.Test;

import com.google.common.collect.Lists;

public class EulerianTrailTest {

    @Test
    public void testFrom1() {

        final UndirectedGraph<String> graph = new UndirectedGraph.Builder<String>(2)
                .addEdge("A", "B", 1.0)
                .build();
        final List<String> expectedTrail = Lists.newArrayList("A", "B", "A");

        // TODO ! soit:
        // - écrire une classe de graphe non orienté écrite ?
        // - eulerianTrail résoud avant le CPP open/close ?
        final ClosedCPPInterface<String> closedCPPInterface = graph.fetch(ClosedCPPFeature.class).up();
        final ClosedCPPSolution<String> solution = closedCPPInterface.solve();

        final EulerianTrailInterface<String> eulerianTrailInterface = graph.fetch(EulerianTrailFeature.class).up();
        final List<String> actualTrail = eulerianTrailInterface.getEulerianTrail("A", solution.getTraversalByEdge());

        assertTrue(actualTrail.equals(expectedTrail));
    }

    @Test
    public void testFrom2() {
        final UndirectedGraph<String> graph = new UndirectedGraph.Builder<String>(3)
                .addEdge("A", "B", 1.0)
                .addEdge("B", "C", 1.0)
                .addEdge("C", "A", 1.0)
                .build();
        final List<String> expectedTrail = Lists.newArrayList("A", "B", "C", "A");

        // TODO ! soit:
        // - écrire une classe de graphe non orienté écrite ?
        // - eulerianTrail résoud avant le CPP open/close ?
        final ClosedCPPInterface<String> closedCPPInterface = graph.fetch(ClosedCPPFeature.class).up();
        final ClosedCPPSolution<String> solution = closedCPPInterface.solve();

        final EulerianTrailInterface<String> eulerianTrailInterface = graph.fetch(EulerianTrailFeature.class).up();
        final List<String> actualTrail = eulerianTrailInterface.getEulerianTrail("A", solution.getTraversalByEdge());

        assertTrue(actualTrail.equals(expectedTrail));
    }

}