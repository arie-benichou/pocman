
package pocman.cpp;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import pocman.cpp.ClosedSolution;
import pocman.graph.WeightedEdge;

import com.google.common.collect.ImmutableMap;


public class SolutionTest {

    private final static Map<WeightedEdge<String>, Integer> TRAVERSAL_BY_EDGE = ImmutableMap.copyOf(new HashMap<WeightedEdge<String>, Integer>());
    private final static ClosedSolution<String> SOLUTION = new ClosedSolution<String>(TRAVERSAL_BY_EDGE, 1.0, 2.0);

    @Test(expected = IllegalArgumentException.class)
    public void testSolution1() {
        new ClosedSolution<String>(null, 1.0, 0.0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSolution2() {
        new ClosedSolution<String>(TRAVERSAL_BY_EDGE, null, 0.0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSolution3() {
        new ClosedSolution<String>(TRAVERSAL_BY_EDGE, 0.0, null);
    }

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
        assertTrue(SOLUTION.equals(null) == false);
        assertTrue(SOLUTION.equals(SOLUTION));
        assertTrue(SOLUTION.equals(new Object()) == false);
        ClosedSolution<String> differentSolution;
        differentSolution = new ClosedSolution<String>(TRAVERSAL_BY_EDGE, 0.0, 2.0);
        assertTrue(SOLUTION.equals(differentSolution) == false);
        differentSolution = new ClosedSolution<String>(TRAVERSAL_BY_EDGE, 1.0, 3.0);
        assertTrue(SOLUTION.equals(differentSolution) == false);
        final ClosedSolution<String> sameSolution = new ClosedSolution<String>(TRAVERSAL_BY_EDGE, 1.0, 2.0);
        assertTrue(SOLUTION.equals(sameSolution));
    }

    @Test
    public void testHashCode() {
        fail("Not yet implemented"); // TODO
    }

}