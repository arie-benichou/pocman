
package pocman.cpp;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import pocman.graph.UndirectedGraph;

public class OpenCPPTest {

    @Test(expected = IllegalArgumentException.class)
    public void testFromNullReference1() {
        final UndirectedGraph<?> graph = null;
        OpenCPP.from(graph);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFromNullReference2() {
        final ClosedCPP<?> closedCPP = null;
        OpenCPP.from(closedCPP);
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