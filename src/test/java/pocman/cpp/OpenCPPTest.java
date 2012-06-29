
package pocman.cpp;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import pocman.cpp.ClosedCPP;
import pocman.cpp.OpenCPP;
import pocman.graph.UndirectedGraph;


public class OpenCPPTest {

    @Test(expected = IllegalArgumentException.class)
    public void testFromNullReference() {
        OpenCPP.from(null);
    }

    @Test
    public void testFromClosedCPP() {
        final UndirectedGraph<String> input = new UndirectedGraph.Builder<String>(2).addEdge("A", "B", 1.0).build();
        final ClosedCPP<String> closedCPP = ClosedCPP.from(input);
        assertTrue(OpenCPP.from(closedCPP) != null);
    }

    @Test
    public void testGetClosedCPP() {
        final UndirectedGraph<String> input = new UndirectedGraph.Builder<String>(2).addEdge("A", "B", 1.0).build();
        final ClosedCPP<String> closedCPP = ClosedCPP.from(input);
        final OpenCPP<String> openCPP = OpenCPP.from(closedCPP);
        assertTrue(openCPP.getClosedCPP().equals(closedCPP)); // TODO ?
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSolveFromNullReference() {
        final UndirectedGraph<String> input = new UndirectedGraph.Builder<String>(2).addEdge("A", "B", 1.0).build();
        final ClosedCPP<String> closedCPP = ClosedCPP.from(input);
        OpenCPP.from(closedCPP).solveFrom(null);
    }

    @Test(expected = IllegalStateException.class)
    public void testSolveFromUnknownMazeNode() {
        final UndirectedGraph<String> input = new UndirectedGraph.Builder<String>(2).addEdge("A", "B", 1.0).build();
        final ClosedCPP<String> closedCPP = ClosedCPP.from(input);
        OpenCPP.from(closedCPP).solveFrom("C");
    }

}