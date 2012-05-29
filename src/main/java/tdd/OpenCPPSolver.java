
package tdd;

import java.util.List;

import com.google.common.collect.Lists;

import cpp.Arc;

public class OpenCPPSolver {

    private final GameGraph gameGraph;

    public GameGraph getGameGraph() {
        return this.gameGraph;
    }

    private final ClosedCPPSolver closedCPPSolver;

    public ClosedCPPSolver getClosedCPPSolver() {
        return this.closedCPPSolver;
    }

    private final double lowerBoundCost;

    public double getLowerBoundCost() {
        return this.lowerBoundCost;
    }

    public OpenCPPSolver(final ClosedCPPSolver closedCPPSolver) {
        this.closedCPPSolver = closedCPPSolver;
        this.gameGraph = closedCPPSolver.getGameGraph();
        this.lowerBoundCost = closedCPPSolver.getLowerBoundCost();
    }

    public void solveFrom(final int startingVertexIndex) {

        final int n = this.getGameGraph().getNumberOfVertices();
        final int numberOfOddVertices = this.getClosedCPPSolver().getOddVerticeIndexes().size();

        ClosedCPPSolver bestSolverInstance = null;
        double bestExtraCost = 0; // TODO ? Double.POSITIVE_INFINITY;

        double cost = 0;
        ClosedCPPSolver cppSolver;
        int i = 0;

        do {

            cost = this.getLowerBoundCost();

            final Arc virtualFrom = Arc.from(
                    "Virtual Start",
                    n,
                    startingVertexIndex,
                    cost
                    );

            final Arc virtualTo = Arc.from(
                    "Virtual End",
                    this.getClosedCPPSolver().hasEulerianTrail() ? startingVertexIndex : this.getClosedCPPSolver().getOddVerticeIndexes().get(i),
                    //this.getClosedCPPSolver().hasEulerianTrail() ? startingVertexIndex : i,
                    n,
                    cost
                    );

            cppSolver = new ClosedCPPSolver(this.getClosedCPPSolver(), virtualFrom, virtualTo);

            //final double currentExtraCost = cppSolver.getTotalCost();
            final double currentExtraCost = cppSolver.getExtraCost();

            /*
            final List<Integer> openTrail = cppSolver.solveFrom(n);
            final List<Integer> reverse = Lists.reverse(openTrail);
            try {
                cppSolver.debugTrail(reverse);
                System.out.println(virtualFrom);
                System.out.println(virtualTo);
                System.out.println(currentExtraCost);
                System.out.println();
                Thread.sleep(1500);
            }
            catch (final InterruptedException e) {}
            */

            if (bestSolverInstance == null || currentExtraCost < bestExtraCost) {
                bestSolverInstance = cppSolver;
                bestExtraCost = currentExtraCost;
            }
            System.out.println(i);

        }
        //while (++i < cppSolver.getOddVerticeIndexes().size());
        while (++i < numberOfOddVertices);
        //while (++i < n);

        //return new CPPSolution(bestSolverInstance.solveFrom(n), cost + bestSolverInstance.computeExtraCost());        
        final List<Integer> openTrail = bestSolverInstance.solveFrom(n);

        /*
        try {
            bestSolverInstance.debugTrail(openTrail);
        }
        catch (final InterruptedException e) {}
        */
        final List<Integer> reverse = Lists.reverse(openTrail);

        /*
        //System.out.println(reverse);
        try {
            Thread.sleep(1500);
        }
        catch (final InterruptedException e1) {}
        */

        try {
            bestSolverInstance.debugTrail(reverse);
        }
        catch (final InterruptedException e) {}

        System.out.println(cost + bestSolverInstance.getExtraCost());
        //System.out.println(bestSolverInstance.getTotalCost());

    }
}