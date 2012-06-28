
package fr.designpattern.pocman.cpp;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.google.common.collect.ImmutableMap;

import fr.designpattern.pocman.graph.WeightedEdge;

public class SolutionTest {

    private final static Map<WeightedEdge<String>, Integer> TRAVERSAL_BY_EDGE = ImmutableMap.copyOf(new HashMap<WeightedEdge<String>, Integer>());
    private final static Solution<String> SOLUTION = new Solution<String>(TRAVERSAL_BY_EDGE, 1.0, 2.0);

    @Test(expected = IllegalArgumentException.class)
    public void testSolution1() {
        new Solution<String>(null, 1.0, 0.0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSolution2() {
        new Solution<String>(TRAVERSAL_BY_EDGE, null, 0.0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSolution3() {
        new Solution<String>(TRAVERSAL_BY_EDGE, 0.0, null);
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
        Solution<String> differentSolution;
        differentSolution = new Solution<String>(TRAVERSAL_BY_EDGE, 0.0, 2.0);
        assertTrue(SOLUTION.equals(differentSolution) == false);
        differentSolution = new Solution<String>(TRAVERSAL_BY_EDGE, 1.0, 3.0);
        assertTrue(SOLUTION.equals(differentSolution) == false);
        final Solution<String> sameSolution = new Solution<String>(TRAVERSAL_BY_EDGE, 1.0, 2.0);
        assertTrue(SOLUTION.equals(sameSolution));
    }

    @Test
    public void testHashCode() {
        fail("Not yet implemented"); // TODO
    }

}