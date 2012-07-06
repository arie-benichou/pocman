
package pocman.demo;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import pocman.cpp.Bound;
import pocman.cpp.ClosedCPPSolution;
import pocman.maze.Maze;
import pocman.maze.MazeNode;

public class CPPDemoTest {

    private static void check(final Bound expectedBound, final String level) {
        final Maze maze = Maze.from(level);
        final CPPDemo cppDemo = new CPPDemo(pocman.demo.CPPDemo.MATCHING_ALGORITHM_1);
        final ClosedCPPSolution<MazeNode> closedCPPSolution = cppDemo.solve(maze);
        System.out.println(closedCPPSolution);
        assertEquals(expectedBound.getLowerBound(), closedCPPSolution.getLowerBoundCost(), 0.1);
        assertEquals(expectedBound.getUpperBound(), closedCPPSolution.getUpperBoundCost(), 0.1);
    }

    @Test
    public void testSolveClosedCPP1() {
        final String level = "" +
                "┃⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛┃" +
                "┃∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙┃" +
                "┃∙⬛⬛⬛⬛⬛⬛⬛∙⬛⬛⬛⬛⬛⬛∙⬛⬛⬛⬛⬛⬛⬛⬛∙┃" +
                "┃∙∙∙∙∙┃∙∙∙∙∙∙┃∙∙∙∙∙∙┃∙∙∙∙∙┃" +
                "┃∙⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛∙⬛⬛⬛⬛⬛⬛⬛⬛∙┃" +
                "┃∙∙∙∙∙┃∙∙∙∙∙∙∙∙∙∙∙∙∙┃∙∙∙∙∙┃" +
                "┃∙⬛⬛⬛⬛⬛⬛⬛∙⬛⬛⬛⬛⬛⬛∙⬛⬛⬛⬛⬛⬛⬛⬛∙┃" +
                "┃∙∙∙∙∙┃∙∙∙∙∙∙┃∙∙∙∙∙∙∙∙∙∙∙∙┃" +
                "┃∙⬛⬛⬛⬛⬛⬛⬛∙⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛┃" +
                "┃∙∙∙∙∙┃∙∙∙∙∙∙┃∙∙∙∙∙∙∙∙∙∙∙∙┃" +
                "┃∙⬛⬛⬛⬛⬛⬛⬛∙⬛⬛⬛⬛⬛⬛∙⬛⬛⬛⬛⬛⬛∙⬛⬛┃" +
                "┃∙∙∙∙∙┃∙∙∙∙∙∙∙∙∙∙∙∙∙┃∙∙∙∙∙┃" +
                "┃∙⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛∙⬛⬛┃" +
                "┃∙∙∙∙∙∙∙∙∙∙∙∙┃∙∙∙∙∙∙∙∙∙∙∙∙┃" +
                "┃⬛⬛⬛⬛⬛⬛⬛⬛⬛∙⬛⬛⬛⬛⬛∙⬛⬛⬛⬛⬛⬛⬛⬛⬛┃" +
                "┃∙∙∙∙∙∙∙∙∙∙∙∙┃∙∙∙∙∙∙∙∙∙∙∙∙┃" +
                "┃∙⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛∙⬛⬛┃" +
                "┃∙∙∙∙∙∙∙∙∙∙∙∙┃∙∙∙∙∙∙∙∙∙∙∙∙┃" +
                "┃⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛┃";
        CPPDemoTest.check(new Bound(167, 312), level);
    }

    @Test
    public void testSolveClosedCPP2() {
        final String level = "" +
                "┃⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛┃" +
                "┃∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙┃" +
                "┃∙⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛∙┃" +
                "┃∙┃∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙┃∙┃" +
                "┃∙⬛⬛∙⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛∙⬛⬛⬛⬛⬛∙⬛⬛∙┃" +
                "┃∙┃∙∙∙┃∙∙∙∙∙∙∙∙∙∙∙∙∙┃∙∙∙┃∙┃" +
                "┃∙⬛⬛∙⬛⬛⬛⬛∙⬛⬛⬛⬛⬛⬛∙⬛⬛⬛⬛⬛⬛⬛⬛∙┃" +
                "┃∙┃∙∙∙┃∙∙∙∙∙∙┃∙∙∙∙∙∙∙∙∙∙┃∙┃" +
                "┃∙⬛⬛∙⬛⬛⬛⬛∙⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛∙┃" +
                "┃∙┃∙∙∙┃∙∙∙∙∙∙┃∙∙∙∙∙∙∙∙∙∙┃∙┃" +
                "┃∙⬛⬛∙⬛⬛⬛⬛∙⬛⬛⬛⬛⬛⬛∙⬛⬛⬛⬛⬛⬛∙⬛∙┃" +
                "┃∙┃∙∙∙┃∙∙∙∙∙∙∙∙∙∙∙∙∙┃∙∙∙┃∙┃" +
                "┃∙⬛∙⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛∙⬛∙┃" +
                "┃∙┃∙∙∙∙∙∙∙∙∙∙┃∙∙∙∙∙∙∙∙∙∙┃∙┃" +
                "┃∙⬛⬛⬛⬛⬛⬛⬛⬛∙⬛⬛⬛⬛⬛∙⬛⬛⬛⬛⬛⬛⬛⬛∙┃" +
                "┃∙┃∙∙∙∙∙∙∙∙∙∙┃∙∙∙∙∙∙∙∙∙∙∙∙┃" +
                "┃∙⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛∙┃" +
                "┃∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙┃" +
                "┃⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛┃";
        CPPDemoTest.check(new Bound(176, 277), level);
    }

    @Test
    public void testSolveClosedCPP3() {
        final String level = "" +
                "┃⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛┃" +
                "┃∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙┃" +
                "┃∙⬛⬛⬛⬛⬛⬛⬛∙⬛⬛⬛⬛⬛⬛∙⬛⬛⬛⬛⬛⬛⬛⬛∙┃" +
                "┃∙∙∙∙∙┃∙∙∙∙∙∙┃∙∙∙∙∙∙┃∙∙∙∙∙┃" +
                "┃∙⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛∙⬛⬛⬛⬛⬛⬛⬛⬛∙┃" +
                "┃∙∙∙∙∙┃∙∙∙∙∙∙∙∙∙∙∙∙∙┃∙∙∙∙∙┃" +
                "┃∙⬛⬛⬛⬛⬛⬛⬛∙⬛⬛⬛⬛⬛⬛∙⬛⬛⬛⬛⬛⬛⬛⬛∙┃" +
                "┃∙∙∙∙∙┃∙∙∙∙∙∙┃∙∙∙∙∙∙∙∙∙∙∙∙┃" +
                "┃∙⬛⬛⬛⬛⬛⬛⬛∙⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛┃" +
                "┃∙∙∙∙∙┃∙∙∙∙∙∙┃∙∙∙∙∙∙∙∙∙∙∙∙┃" +
                "┃∙⬛⬛⬛⬛⬛⬛⬛∙⬛⬛⬛⬛⬛⬛∙⬛⬛⬛⬛⬛⬛∙⬛⬛┃" +
                "┃∙∙∙∙∙┃∙∙∙∙∙∙∙∙∙∙∙∙∙┃∙∙∙∙∙┃" +
                "┃∙⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛∙⬛⬛┃" +
                "┃∙∙∙∙∙∙∙∙∙∙∙∙┃∙∙∙∙∙∙∙∙∙∙∙∙┃" +
                "┃⬛⬛⬛⬛⬛⬛⬛⬛⬛∙⬛⬛⬛⬛⬛∙⬛⬛⬛⬛⬛⬛⬛⬛⬛┃" +
                "┃∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙┃" +
                "┃∙⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛∙⬛⬛┃" +
                "┃∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙┃" +
                "┃⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛┃";
        CPPDemoTest.check(new Bound(173, 230), level);
    }

    @Test
    public void testSolveClosedCPP4() {
        final String level = "" +
                "┃⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛┃" +
                "┃∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙┃∙∙∙∙∙∙┃" +
                "┃∙⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛∙⬛⬛⬛⬛⬛⬛⬛⬛∙⬛⬛⬛⬛┃" +
                "┃∙∙∙∙∙∙∙∙∙┃∙∙∙∙∙∙┃∙∙∙∙∙∙∙∙┃" +
                "┃⬛⬛⬛⬛⬛⬛⬛∙⬛⬛⬛⬛⬛⬛∙⬛⬛⬛∙⬛⬛⬛⬛⬛∙┃" +
                "┃∙∙∙∙∙∙∙∙∙∙∙∙∙┃∙∙∙∙∙∙∙┃∙∙∙┃" +
                "┃⬛⬛⬛⬛⬛⬛⬛⬛∙⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛∙┃" +
                "┃∙∙∙∙∙∙∙┃∙∙∙∙∙∙∙∙∙∙┃∙∙∙∙∙∙┃" +
                "┃⬛⬛⬛∙⬛⬛⬛⬛∙⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛∙⬛⬛⬛⬛⬛┃" +
                "┃∙∙∙∙∙∙∙∙∙∙∙∙┃∙∙∙∙∙∙┃∙∙∙∙∙┃" +
                "┃⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛∙⬛⬛⬛⬛∙⬛⬛⬛⬛∙⬛⬛⬛⬛┃" +
                "┃∙∙∙∙∙┃∙∙∙∙∙∙∙∙∙∙∙∙∙┃∙∙∙∙∙┃" +
                "┃∙⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛∙⬛⬛∙⬛⬛⬛⬛∙┃" +
                "┃∙∙┃∙∙∙∙∙∙∙∙∙∙∙┃∙∙∙∙∙∙∙∙∙∙┃" +
                "┃∙⬛⬛⬛⬛∙⬛⬛⬛⬛⬛⬛⬛⬛⬛∙⬛⬛⬛⬛⬛⬛⬛∙⬛┃" +
                "┃∙∙∙∙∙∙∙∙∙∙┃∙∙∙∙∙∙∙∙∙∙┃∙∙∙┃" +
                "┃⬛⬛∙⬛⬛⬛⬛⬛⬛⬛⬛⬛∙⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛┃" +
                "┃∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙┃" +
                "┃⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛┃";
        CPPDemoTest.check(new Bound(156, 312), level);
    }

    @Test
    public void testSolveClosedCPP5() {
        final String level = "" +
                "┃⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛┃" +
                "┃∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙┃" +
                "┃∙⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛∙⬛⬛⬛⬛⬛⬛⬛⬛∙┃" +
                "┃∙∙∙∙∙∙∙∙∙∙┃∙∙∙∙∙∙∙∙∙∙∙∙┃∙┃" +
                "┃∙⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛∙⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛┃" +
                "┃∙∙∙∙∙∙∙∙∙∙┃∙┃∙∙∙∙∙∙∙∙∙∙∙∙┃" +
                "┃⬛⬛⬛⬛∙⬛⬛⬛⬛⬛⬛∙┃⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛∙┃" +
                "┃∙∙∙∙∙┃∙∙∙∙∙∙┃∙∙∙∙∙∙∙∙∙∙∙∙┃" +
                "┃⬛⬛⬛⬛∙⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛∙⬛⬛⬛⬛⬛⬛⬛⬛⬛┃" +
                "┃∙∙∙∙∙∙∙∙∙∙┃∙∙∙∙∙∙∙∙∙∙∙∙∙∙┃" +
                "┃∙⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛∙⬛⬛⬛⬛⬛⬛⬛⬛⬛┃" +
                "┃∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙┃" +
                "┃⬛⬛⬛⬛⬛⬛⬛⬛⬛∙⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛┃" +
                "┃∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙┃" +
                "┃∙⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛┃" +
                "┃∙∙∙∙┃∙∙∙∙∙┃∙∙∙∙∙∙∙∙∙∙∙∙∙∙┃" +
                "┃∙⬛⬛⬛⬛⬛⬛⬛⬛∙⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛∙┃" +
                "┃∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙┃" +
                "┃⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛┃";
        CPPDemoTest.check(new Bound(190, 380), level);
    }

    @Test
    public void testSolveClosedCPP6() {
        final String level = "" +
                "┃⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛┃" +
                "┃∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙┃" +
                "┃⬛⬛⬛⬛⬛∙⬛⬛⬛⬛⬛∙┃∙⬛⬛⬛⬛⬛∙⬛⬛⬛⬛⬛┃" +
                "┃∙∙∙∙∙∙∙∙∙∙∙∙┃∙∙∙∙∙∙∙∙∙∙∙∙┃" +
                "┃⬛⬛⬛⬛⬛∙⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛∙⬛⬛⬛⬛⬛┃" +
                "┃∙∙∙∙∙∙∙∙∙∙∙∙┃∙∙∙∙∙∙∙∙∙∙∙∙┃" +
                "┃⬛⬛⬛⬛⬛∙⬛⬛⬛⬛⬛∙┃∙⬛⬛⬛⬛⬛∙⬛⬛⬛⬛⬛┃" +
                "┃∙∙∙∙∙∙∙∙∙∙∙∙┃∙∙∙∙∙∙∙∙∙∙∙∙┃" +
                "┃⬛⬛⬛⬛⬛∙⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛∙⬛⬛⬛⬛⬛┃" +
                "┃∙∙∙∙∙∙∙∙∙∙∙∙┃∙∙∙∙∙∙∙∙∙∙∙∙┃" +
                "┃⬛⬛⬛⬛⬛∙⬛⬛⬛⬛⬛∙┃∙⬛⬛⬛⬛⬛∙⬛⬛⬛⬛⬛┃" +
                "┃∙∙∙∙∙∙∙∙∙∙∙∙┃∙∙∙∙∙∙∙∙∙∙∙∙┃" +
                "┃⬛⬛⬛⬛⬛∙⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛∙⬛⬛⬛⬛⬛┃" +
                "┃∙∙∙∙∙∙∙∙∙∙∙∙┃∙∙∙∙∙∙∙∙∙∙∙∙┃" +
                "┃⬛⬛⬛⬛⬛∙⬛⬛⬛⬛⬛∙┃∙⬛⬛⬛⬛⬛∙⬛⬛⬛⬛⬛┃" +
                "┃∙∙∙∙∙∙∙∙∙∙∙∙┃∙∙∙∙∙∙∙∙∙∙∙∙┃" +
                "┃⬛⬛⬛⬛⬛∙⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛∙⬛⬛⬛⬛⬛┃" +
                "┃∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙┃" +
                "┃⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛┃";
        CPPDemoTest.check(new Bound(190, 280), level);

    }

    // TODO faire un test par niveau
    /*
    @Test
    public void testSolveMazeInt() {
        final int[] expectedEndPoints = {
                471,
                408,
                154,
                236,
                196,
                406
        };
        final Bound[] expectedBounds = {
                new Bound(167, 228),
                new Bound(176, 244),
                new Bound(173, 226),
                new Bound(156, 253),
                new Bound(190, 290),
                new Bound(190, 266),
        };

        final CPPDemo that = new CPPDemo(pocman.demo.CPPDemo.MATCHING_ALGORITHM_1);
        int i = 0;
        for (final String level : Mazes.LEVELS) {
            final int pocManPosition = level.indexOf(Tile.POCMAN.toCharacter());
            Preconditions.checkState(pocManPosition > -1, "POCMAN POSITION NOT FOUND !");
            final char[] data = level.toCharArray();
            data[pocManPosition] = Tile.COIN.toCharacter();
            final Maze maze = Maze.from(data);
            final OpenCPPSolution<MazeNode> closedCPPSolution = that.solve(maze, pocManPosition);
            System.out.println(closedCPPSolution);
            assertEquals(expectedEndPoints[i], closedCPPSolution.getEndPoint().getId());
            assertEquals(expectedBounds[i].getLowerBound(), closedCPPSolution.getLowerBoundCost(), 0.1);
            assertEquals(expectedBounds[i].getUpperBound(), closedCPPSolution.getUpperBoundCost(), 0.1);
            ++i;
        }
    }
    */

}