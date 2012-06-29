
package pocman.cpp;

import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Test;

import pocman.cpp.ClosedCPP;
import pocman.cpp.EulerianTrail;
import pocman.cpp.ClosedSolution;
import pocman.graph.UndirectedGraph;

import com.google.common.collect.Lists;


public class EulerianTrailTest {

    @Test
    public void testFrom1() {
        final UndirectedGraph<String> input = new UndirectedGraph.Builder<String>(2)
                .addEdge("A", "B", 1.0)
                .build();
        final List<String> expectedTrail = Lists.newArrayList("A", "B", "A");
        final ClosedSolution<String> solution = ClosedCPP.from(input).solve();
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
        final ClosedSolution<String> solution = ClosedCPP.from(input).solve();
        final List<String> actualTrail = EulerianTrail.from(input, solution.getTraversalByEdge(), "A");
        assertTrue(actualTrail.equals(expectedTrail2) || actualTrail.equals(expectedTrail1));
    }

}