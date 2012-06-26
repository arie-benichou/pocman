
package fr.designpattern.pocman.cpp;

import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Test;

import com.google.common.collect.Lists;

import fr.designpattern.pocman.cpp.ClosedCPP;
import fr.designpattern.pocman.cpp.EulerianTrail;
import fr.designpattern.pocman.graph.UndirectedGraph;

public class EulerianTrailTest {

    @Test
    public void testFrom1() {
        final UndirectedGraph<String> input = new UndirectedGraph.Builder<String>(2)
                .addEdge("A", "B", 1.0)
                .build();
        final List<String> expectedTrail = Lists.newArrayList("A", "B", "A");
        final List<String> actualTrail = EulerianTrail.from(ClosedCPP.newSolver(input).solveFrom("A"));
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
        final List<String> eulerianTrail = EulerianTrail.from(ClosedCPP.newSolver(input).solveFrom("A"));
        assertTrue(eulerianTrail.equals(expectedTrail2) || eulerianTrail.equals(expectedTrail1));
    }

}