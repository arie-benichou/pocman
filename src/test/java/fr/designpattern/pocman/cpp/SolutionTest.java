
package fr.designpattern.pocman.cpp;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.HashMap;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import fr.designpattern.pocman.graph.WeightedEdge;

public class SolutionTest { // TODO à compléter...

    private Solution<String> solution;

    @Before
    public void setUp() throws Exception {
        this.solution = new Solution<String>(null, "A", new HashMap<WeightedEdge<String>, Integer>(), 1.0, 2.0);
    }

    @After
    public void tearDown() throws Exception {
        this.solution = null;
    }

    @Test(expected = IllegalStateException.class)
    public void testSolution() {
        new Solution<String>(null, "A", new HashMap<WeightedEdge<String>, Integer>(), 1.0, 0.0);
    }

    @Test
    public void testGetGraph() {
        assertTrue(this.solution.getGraph() == null);
    }

    @Test
    public void testGetStartingVertex() {
        assertTrue(this.solution.getStartingVertex().equals("A"));
    }

    @Test
    public void testGetLowerBoundCost() {
        assertTrue(this.solution.getLowerBoundCost().equals(1.0));
    }

    @Test
    public void testGetUpperBoundCost() {
        assertTrue(this.solution.getUpperBoundCost().equals(2.0));
    }

    @Test
    public void testGetTraversalByEdge() {
        assertTrue(this.solution.getTraversalByEdge().equals(new HashMap<WeightedEdge<String>, Integer>()));
    }

    @Test
    public void testEqualsObject() {

        assertTrue(this.solution.equals(null) == false);
        assertTrue(this.solution.equals(this.solution));
        assertTrue(this.solution.equals(new Object()) == false);

        Solution<String> differentSolution;
        differentSolution = new Solution<String>(null, "A", new HashMap<WeightedEdge<String>, Integer>(), 0.0, 2.0);
        assertTrue(this.solution.equals(differentSolution) == false);

        differentSolution = new Solution<String>(null, "A", new HashMap<WeightedEdge<String>, Integer>(), 1.0, 3.0);
        assertTrue(this.solution.equals(differentSolution) == false);

        differentSolution = new Solution<String>(null, "B", new HashMap<WeightedEdge<String>, Integer>(), 1.0, 2.0);
        assertTrue(this.solution.equals(differentSolution) == false);

        final HashMap<WeightedEdge<String>, Integer> traversalByEdge = new HashMap<WeightedEdge<String>, Integer>();
        traversalByEdge.put(WeightedEdge.from("A", "B", 1.0), 1);
        differentSolution = new Solution<String>(null, "A", traversalByEdge, 1.0, 2.0);
        assertTrue(this.solution.equals(differentSolution) == false);

        final Solution<String> sameSolution = new Solution<String>(null, "A", new HashMap<WeightedEdge<String>, Integer>(), 1.0, 2.0);
        assertTrue(this.solution.equals(sameSolution));
    }

    //@Test
    public void testHashCode() {
        fail("Not yet implemented");
    }

}