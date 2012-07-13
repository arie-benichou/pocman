
package pocman.demo;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import pocman.game.Maze;
import pocman.game.MazeNode;
import cpp.Bound;
import cpp.ClosedCPPSolution;

public class ClosedCPPDemoTest {

    private static void check(final Bound expectedBound, final String level) {
        final Maze maze = Maze.from(level);
        final ClosedCPPDemo cppDemo = new ClosedCPPDemo(pocman.demo.ClosedCPPDemo.MATCHING_ALGORITHM_1);
        final ClosedCPPSolution<MazeNode> closedCPPSolution = cppDemo.solve(maze);
        //System.out.println(closedCPPSolution);
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
        ClosedCPPDemoTest.check(new Bound(234, 438), level);
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
        ClosedCPPDemoTest.check(new Bound(234, 388), level);
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
        ClosedCPPDemoTest.check(new Bound(238, 328), level);
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
        ClosedCPPDemoTest.check(new Bound(234, 468), level);
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
        ClosedCPPDemoTest.check(new Bound(232, 464), level);
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
        ClosedCPPDemoTest.check(new Bound(250, 368), level);
    }

    @Test
    public void testSolveClosedCPP7() {
        final String level = "" +
                "┃⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛┃" +
                "┃∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙┃" +
                "┃⬛⬛⬛⬛⬛∙⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛┃" +
                "┃∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙┃" +
                "┃⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛∙⬛⬛⬛⬛⬛┃" +
                "┃∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙┃" +
                "┃⬛⬛⬛⬛⬛∙⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛┃" +
                "┃∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙┃" +
                "┃⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛∙⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛┃" +
                "┃∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙┃" +
                "┃⬛⬛⬛⬛⬛∙⬛⬛⬛⬛⬛∙┃∙⬛⬛⬛⬛⬛∙⬛⬛⬛⬛⬛┃" +
                "┃∙∙∙∙∙∙∙∙∙∙∙∙┃∙∙∙∙∙∙∙∙∙∙∙∙┃" +
                "┃⬛⬛⬛⬛⬛∙⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛∙⬛⬛⬛⬛⬛┃" +
                "┃∙∙∙∙∙∙∙∙∙∙∙∙┃∙∙∙∙∙∙∙∙∙∙∙∙┃" +
                "┃⬛⬛⬛⬛⬛∙⬛⬛⬛⬛┃∙┃∙┃⬛⬛⬛⬛∙⬛⬛⬛⬛⬛┃" +
                "┃∙∙∙∙∙∙∙∙∙∙┃∙┃∙┃∙∙∙∙∙∙∙∙∙∙┃" +
                "┃⬛⬛⬛⬛⬛∙⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛┃" +
                "┃∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙┃" +
                "┃⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛┃";
        ClosedCPPDemoTest.check(new Bound(236, 440), level);
    }

    @Test
    public void testSolveClosedCPP8() {
        final String level = "" +
                "┃⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛┃" +
                "┃∙∙∙∙∙∙┃∙∙∙∙∙∙∙∙∙∙∙∙∙∙┃∙∙∙┃" +
                "┃⬛⬛⬛⬛⬛∙⬛∙⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛∙⬛⬛∙⬛┃" +
                "┃∙∙∙∙∙∙∙∙┃∙∙∙∙∙∙∙∙∙┃∙∙∙∙∙∙┃" +
                "┃⬛∙⬛⬛⬛⬛∙⬛⬛⬛⬛⬛∙⬛⬛⬛⬛⬛⬛∙⬛⬛⬛⬛⬛┃" +
                "┃∙∙∙∙┃∙∙∙∙∙∙∙∙∙∙┃∙∙∙∙∙∙∙∙∙┃" +
                "┃⬛⬛⬛⬛⬛∙⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛∙⬛⬛⬛⬛⬛⬛⬛⬛┃" +
                "┃∙∙∙∙∙∙∙∙∙∙┃∙∙∙∙∙∙∙∙∙∙∙∙∙∙┃" +
                "┃⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛∙⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛┃" +
                "┃∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙┃" +
                "┃⬛∙⬛⬛⬛∙⬛⬛⬛∙⬛⬛⬛⬛⬛∙⬛⬛⬛∙⬛⬛⬛∙⬛┃" +
                "┃∙∙∙∙┃∙┃∙∙∙∙∙┃∙∙∙∙∙┃∙┃∙∙∙∙┃" +
                "┃⬛⬛⬛⬛⬛∙⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛∙⬛⬛⬛⬛⬛┃" +
                "┃∙∙∙∙∙∙∙∙∙∙∙∙┃∙∙∙∙∙∙∙∙∙∙∙∙┃" +
                "┃⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛┃∙┃∙┃⬛⬛⬛⬛∙⬛⬛⬛⬛⬛┃" +
                "┃∙∙∙∙∙∙∙∙∙∙┃∙┃∙┃∙∙∙∙∙∙∙∙∙∙┃" +
                "┃⬛⬛⬛⬛⬛∙⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛∙⬛⬛⬛⬛⬛⬛┃" +
                "┃∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙┃" +
                "┃⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛┃";
        ClosedCPPDemoTest.check(new Bound(232, 464), level);
    }

    @Test
    public void testSolveClosedCPP9() {
        final String level = "" +
                "┃⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛┃" +
                "┃∙∙∙∙∙∙┃∙∙∙∙∙∙∙∙∙∙∙∙∙∙┃∙∙∙┃" +
                "┃⬛⬛⬛⬛⬛∙⬛∙⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛∙⬛⬛∙⬛┃" +
                "┃∙∙∙∙∙∙∙∙┃∙∙∙∙∙∙∙∙∙┃∙∙∙∙∙∙┃" +
                "┃⬛∙⬛⬛⬛⬛∙⬛⬛⬛⬛⬛∙⬛⬛⬛⬛⬛⬛∙⬛⬛⬛⬛⬛┃" +
                "┃∙∙∙∙┃∙∙∙∙∙∙∙∙∙∙┃∙∙∙∙∙∙∙∙∙┃" +
                "┃⬛⬛⬛⬛⬛∙⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛∙⬛⬛⬛⬛⬛⬛⬛⬛┃" +
                "┃∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙┃∙∙∙∙∙∙∙∙∙┃" +
                "┃⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛∙⬛⬛⬛⬛⬛⬛∙⬛⬛⬛⬛⬛┃" +
                "┃∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙┃∙∙∙∙∙∙┃" +
                "┃⬛∙⬛⬛⬛∙⬛⬛⬛∙⬛⬛⬛⬛⬛∙⬛⬛⬛∙⬛⬛⬛∙⬛┃" +
                "┃∙∙∙∙┃∙┃∙∙∙∙∙┃∙∙∙∙∙┃∙┃∙∙∙∙┃" +
                "┃⬛⬛⬛⬛⬛∙⬛⬛⬛⬛∙⬛⬛⬛⬛⬛⬛⬛⬛∙⬛⬛⬛⬛⬛┃" +
                "┃∙∙∙∙∙∙∙∙∙┃∙∙∙∙∙∙∙∙┃∙∙∙∙∙∙┃" +
                "┃⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛∙⬛⬛⬛⬛⬛┃" +
                "┃∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙┃∙∙∙∙∙∙∙┃" +
                "┃⬛⬛⬛⬛⬛∙⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛∙⬛⬛⬛⬛⬛⬛┃" +
                "┃∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙┃" +
                "┃⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛┃";
        ClosedCPPDemoTest.check(new Bound(232, 464), level);
    }

    @Test
    public void testSolveClosedCPP10() {
        final String level = "" +
                "┃⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛┃" +
                "┃∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙┃" +
                "┃⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛∙┃" +
                "┃∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙┃∙┃" +
                "┃∙⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛∙⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛∙┃" +
                "┃∙∙∙∙∙∙∙∙∙∙┃∙┃∙∙∙∙∙∙∙∙∙∙┃∙┃" +
                "┃⬛⬛⬛⬛∙⬛⬛⬛⬛⬛⬛∙┃∙⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛∙┃" +
                "┃∙∙∙∙∙∙∙∙┃∙∙∙┃∙∙∙∙∙∙∙∙∙∙┃∙┃" +
                "┃⬛⬛⬛⬛∙⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛∙⬛⬛⬛⬛⬛⬛⬛⬛∙┃" +
                "┃∙∙∙∙∙∙∙∙∙∙┃∙∙∙∙∙∙∙∙∙∙∙∙┃∙┃" +
                "┃∙⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛∙⬛⬛⬛⬛⬛⬛⬛⬛∙┃" +
                "┃∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙┃∙┃" +
                "┃∙⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛∙┃" +
                "┃∙∙∙┃∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙┃∙┃" +
                "┃∙⬛⬛┃⬛∙⬛⬛⬛∙⬛⬛⬛∙⬛⬛⬛∙⬛⬛⬛∙⬛┃∙┃" +
                "┃∙∙∙┃∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙┃∙┃" +
                "┃∙⬛⬛⬛⬛⬛⬛⬛⬛∙⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛∙┃" +
                "┃∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙┃" +
                "┃⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛┃";
        ClosedCPPDemoTest.check(new Bound(240, 444), level);
    }

    @Test
    public void testSolveClosedCPP11() {
        final String level = "" +
                "┃⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛┃" +
                "┃∙∙∙∙∙∙∙∙∙∙∙∙┃∙∙∙∙∙∙∙∙∙∙∙∙┃" +
                "┃⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛∙⬛⬛⬛∙⬛⬛⬛⬛⬛⬛⬛┃∙┃" +
                "┃∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙┃∙┃" +
                "┃∙⬛⬛⬛⬛∙⬛⬛⬛⬛⬛⬛⬛⬛⬛∙⬛⬛⬛⬛⬛⬛⬛⬛∙┃" +
                "┃∙∙∙∙∙∙┃∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙┃" +
                "┃⬛⬛⬛∙⬛⬛⬛⬛⬛⬛⬛∙∙⬛⬛⬛⬛⬛⬛∙∙⬛⬛⬛⬛┃" +
                "┃∙∙∙∙∙∙∙∙∙∙∙∙∙∙┃∙∙∙∙∙∙∙∙∙∙┃" +
                "┃⬛⬛⬛⬛⬛⬛∙⬛⬛⬛⬛⬛∙⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛┃" +
                "┃∙∙∙∙∙∙∙∙∙∙┃∙∙∙∙∙∙∙∙┃∙∙∙∙∙┃" +
                "┃∙⬛⬛⬛⬛⬛∙⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛∙⬛⬛⬛⬛⬛∙┃" +
                "┃∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙┃" +
                "┃⬛⬛∙⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛∙⬛⬛⬛⬛⬛⬛⬛⬛┃" +
                "┃∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙┃" +
                "┃⬛⬛⬛⬛⬛⬛∙⬛⬛⬛⬛⬛∙⬛⬛⬛⬛⬛⬛∙⬛⬛⬛⬛⬛┃" +
                "┃∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙┃" +
                "┃∙⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛∙⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛∙⬛┃" +
                "┃∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙┃" +
                "┃⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛┃";
        ClosedCPPDemoTest.check(new Bound(258, 360), level);
    }

    @Test
    public void testSolveClosedCPP12() {
        final String level = "" +
                "┃⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛┃" +
                "┃∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙┃" +
                "┃∙⬛⬛⬛⬛⬛⬛∙⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛∙┃" +
                "┃∙∙∙∙∙∙┃∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙┃" +
                "┃⬛⬛⬛⬛⬛∙⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛∙┃" +
                "┃∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙┃∙∙∙∙∙∙∙∙∙┃" +
                "┃∙⬛⬛∙⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛∙┃∙⬛⬛⬛⬛⬛⬛⬛⬛┃" +
                "┃∙∙∙∙∙∙┃∙∙∙∙∙∙∙∙┃∙∙∙∙∙∙∙∙∙┃" +
                "┃∙⬛⬛⬛⬛⬛⬛∙⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛∙⬛⬛⬛⬛⬛┃" +
                "┃∙∙∙∙∙∙┃∙∙∙∙∙∙∙∙┃∙∙∙∙∙∙∙∙∙┃" +
                "┃∙⬛⬛∙⬛⬛⬛⬛⬛⬛∙⬛⬛⬛⬛⬛⬛⬛⬛∙⬛⬛⬛⬛⬛┃" +
                "┃∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙┃∙∙∙∙∙∙∙∙∙┃" +
                "┃∙⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛∙⬛⬛⬛⬛∙⬛⬛⬛⬛∙┃" +
                "┃∙∙∙∙∙∙┃∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙┃" +
                "┃∙⬛⬛∙⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛∙⬛⬛⬛⬛⬛┃" +
                "┃∙∙∙∙∙∙∙∙∙∙∙┃∙∙∙∙∙∙∙∙∙∙∙∙∙┃" +
                "┃∙⬛⬛⬛⬛⬛⬛⬛⬛⬛∙⬛⬛⬛⬛⬛⬛⬛⬛∙⬛⬛⬛⬛⬛┃" +
                "┃∙∙∙∙∙∙┃∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙┃" +
                "┃⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛┃";
        ClosedCPPDemoTest.check(new Bound(248, 352), level);
    }

    /*

    @Test
    public void testSolveClosedCPP13() { //TODO FIXME
        final String level = "" +
                "┃⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛┃" +
                "┃∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙┃" +
                "┃∙⬛⬛⬛⬛⬛⬛∙⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛∙┃" +
                "┃∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙┃" +
                "┃⬛⬛⬛⬛⬛∙⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛∙┃" +
                "┃∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙┃" +
                "┃∙⬛⬛∙⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛∙⬛⬛⬛⬛⬛⬛⬛⬛⬛┃" +
                "┃∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙┃" +
                "┃∙⬛⬛⬛⬛⬛⬛∙⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛∙⬛⬛⬛⬛⬛┃" +
                "┃∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙┃" +
                "┃∙⬛⬛∙⬛⬛⬛⬛⬛⬛∙⬛⬛⬛⬛⬛⬛⬛⬛∙⬛⬛⬛⬛⬛┃" +
                "┃∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙┃" +
                "┃∙⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛∙⬛⬛⬛⬛∙⬛⬛⬛⬛∙┃" +
                "┃∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙┃" +
                "┃∙⬛⬛∙⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛∙⬛⬛⬛⬛⬛┃" +
                "┃∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙┃" +
                "┃∙⬛⬛⬛⬛⬛⬛⬛⬛⬛∙⬛⬛⬛⬛⬛⬛⬛⬛∙⬛⬛⬛⬛⬛┃" +
                "┃∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙┃" +
                "┃⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛┃";
        ClosedCPPDemoTest.check(new Bound(206, 257), level);
    }

    @Test
    public void testSolveClosedCPP13_5_1() {// TODO FIXME
        final String level = "" +
                "┃⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛┃" +
                "┃⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛┃" +
                "┃⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛┃" +
                "┃⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛┃" +
                "┃⬛⬛⬛⬛⬛∙⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛∙┃" +
                "┃∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙┃" +
                "┃∙⬛⬛∙⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛∙⬛⬛⬛⬛⬛⬛⬛⬛⬛┃" +
                "┃∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙┃" +
                "┃∙⬛⬛⬛⬛⬛⬛∙⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛∙⬛⬛⬛⬛⬛┃" +
                "┃∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙┃" +
                "┃∙⬛⬛∙⬛⬛⬛⬛⬛⬛∙⬛⬛⬛⬛⬛⬛⬛⬛∙⬛⬛⬛⬛⬛┃" +
                "┃∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙┃" +
                "┃∙⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛∙⬛⬛⬛⬛∙⬛⬛⬛⬛∙┃" +
                "┃∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙┃" +
                "┃∙⬛⬛∙⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛∙⬛⬛⬛⬛⬛┃" +
                "┃∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙┃" +
                "┃∙⬛⬛⬛⬛⬛⬛⬛⬛⬛∙⬛⬛⬛⬛⬛⬛⬛⬛∙⬛⬛⬛⬛⬛┃" +
                "┃∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙┃" +
                "┃⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛┃";
        ClosedCPPDemoTest.check(new Bound(158, 208), level); 
    }

    @Test
    public void testSolveClosedCPP13_5_2() {// TODO FIXME
        final String level = "" +
                "┃⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛┃" +
                "┃⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛┃" +
                "┃⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛┃" +
                "┃⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛┃" +
                "┃⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛┃" +
                "┃∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙┃" +
                "┃∙⬛⬛∙⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛∙⬛⬛⬛⬛⬛⬛⬛⬛⬛┃" +
                "┃∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙┃" +
                "┃∙⬛⬛⬛⬛⬛⬛∙⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛∙⬛⬛⬛⬛⬛┃" +
                "┃∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙┃" +
                "┃∙⬛⬛∙⬛⬛⬛⬛⬛⬛∙⬛⬛⬛⬛⬛⬛⬛⬛∙⬛⬛⬛⬛⬛┃" +
                "┃∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙┃" +
                "┃∙⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛∙⬛⬛⬛⬛∙⬛⬛⬛⬛∙┃" +
                "┃∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙┃" +
                "┃∙⬛⬛∙⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛∙⬛⬛⬛⬛⬛┃" +
                "┃∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙┃" +
                "┃∙⬛⬛⬛⬛⬛⬛⬛⬛⬛∙⬛⬛⬛⬛⬛⬛⬛⬛∙⬛⬛⬛⬛⬛┃" +
                "┃∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙┃" +
                "┃⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛┃";
        ClosedCPPDemoTest.check(new Bound(159, 209), level);
    }
    */

    @Test
    public void testSolveClosedCPP13_6() { // TODO FIXME
        final String level = "" +
                "┃⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛┃" +
                "┃⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛┃" +
                "┃⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛┃" +
                "┃⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛┃" +
                "┃⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛┃" +
                "┃⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛┃" +
                "┃⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛┃" +
                "┃⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛┃" +
                "┃⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛┃" +
                "┃⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛┃" +
                "┃⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛┃" +
                "┃∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙┃" +
                "┃∙⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛∙⬛⬛⬛⬛∙⬛⬛⬛⬛∙┃" +
                "┃∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙┃" +
                "┃∙⬛⬛∙⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛∙⬛⬛⬛⬛⬛┃" +
                "┃∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙┃" +
                "┃∙⬛⬛⬛⬛⬛⬛⬛⬛⬛∙⬛⬛⬛⬛⬛⬛⬛⬛∙⬛⬛⬛⬛⬛┃" +
                "┃∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙┃" +
                "┃⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛┃";
        //ClosedCPPDemoTest.check(new Bound(116, 144), level);
        ClosedCPPDemoTest.check(new Bound(116, 138), level);
    }

    @Test
    public void testSolveClosedCPP14() {
        final String level = "" +
                "┃∙∙∙∙∙∙┃∙∙∙∙∙∙∙∙∙∙∙┃∙∙∙∙∙∙┃" +
                "┃∙⬛⬛⬛⬛∙┃∙⬛⬛⬛⬛⬛⬛⬛⬛⬛∙┃∙⬛⬛⬛⬛∙┃" +
                "┃∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙┃" +
                "┃⬛⬛⬛∙┃∙⬛⬛⬛⬛⬛∙┃∙⬛⬛⬛⬛⬛∙┃∙⬛⬛⬛┃" +
                "┃∙∙∙∙┃∙∙∙∙∙∙∙┃∙∙∙∙∙∙∙┃∙∙∙∙┃" +
                "┃⬛⬛┃∙┃⬛⬛⬛∙⬛⬛⬛⬛⬛⬛⬛∙⬛⬛⬛┃∙┃⬛⬛┃" +
                "┃⬛⬛┃∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙┃⬛⬛┃" +
                "┃⬛⬛┃∙⬛⬛⬛⬛∙⬛⬛⬛⬛⬛⬛⬛∙⬛⬛⬛┃∙┃⬛⬛┃" +
                "┃∙∙∙∙┃∙∙∙∙⬛⬛⬛⬛⬛⬛⬛∙∙∙∙┃∙∙∙∙┃" +
                "┃⬛⬛┃∙∙∙∙┃∙∙∙∙∙∙∙∙∙┃∙∙∙∙┃⬛⬛┃" +
                "┃⬛⬛┃∙⬛⬛⬛⬛⬛⬛⬛∙┃∙⬛⬛⬛⬛⬛⬛⬛∙┃⬛⬛┃" +
                "┃⬛⬛┃∙∙∙∙∙∙∙∙∙┃∙∙∙∙∙∙∙∙∙┃⬛⬛┃" +
                "┃⬛⬛┃∙⬛⬛⬛⬛∙⬛⬛⬛⬛⬛⬛⬛∙⬛⬛⬛⬛∙┃⬛⬛┃" +
                "┃∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙┃" +
                "┃∙┃⬛⬛┃∙⬛⬛⬛⬛⬛∙┃∙⬛⬛⬛⬛⬛∙┃⬛⬛┃∙┃" +
                "┃∙┃⬛⬛┃∙┃∙∙∙∙∙┃∙∙∙∙∙┃∙┃⬛⬛┃∙┃" +
                "┃∙┃⬛⬛┃∙┃∙⬛⬛⬛⬛⬛⬛⬛⬛⬛∙┃∙┃⬛⬛┃∙┃" +
                "┃∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙┃" +
                "┃⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛┃";
        //ClosedCPPDemoTest.check(new Bound(266, 336), level); // TODO FIXME
        //ClosedCPPDemoTest.check(new Bound(266, 340), level); // TODO FIXME
        ClosedCPPDemoTest.check(new Bound(266, 334), level); // TODO FIXME
    }

    /*"" +


        
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