
package cpp;

public class OpenCPPSolver {

    private final Graph graph;

    public OpenCPPSolver(final Graph graph) {
        this.graph = graph;
    }

    public CPPSolution solveFrom(final int nodeIndex) {
        Graph graph = this.graph;
        double cost;
        ClosedCPPSolver bestSolverInstance = null;
        double bestCost = 0; //Integer.MAX_VALUE; // 0 ?
        int i = 0;
        final int n = this.graph.getNumberOfNodes();
        do {
            final Graph tmpGraph = new Graph.Builder(n + 1).add(this.graph.getArcObjects()).build();
            //final ClosedCPPSolver build = new ClosedCPPSolverBuilder(tmpGraph, false).build();
            //final double extraCost = build.computeExtraCost();
            //cost = tmpGraph.geTotalCostLowerBound() + extraCost;
            cost = tmpGraph.geTotalCostLowerBound();
            final Arc virtualFrom = Arc.from("'virtual start'", n, nodeIndex, cost);
            final Arc virtualTo = Arc.from("'virtual end'", tmpGraph.isEulerian() ? nodeIndex : tmpGraph.getNegatives().get(i), n, cost);
            graph = new Graph.Builder(n + 1).add(tmpGraph.getArcObjects()).add(virtualFrom, virtualTo).build();
            final ClosedCPPSolver solver = new ClosedCPPSolverBuilder(graph, false).build(); // TODO true ? inutile ?
            final double currentCost = graph.geTotalCostLowerBound() + solver.computeExtraCost();
            System.out.println(currentCost);
            if (bestSolverInstance == null || currentCost < bestCost) {
                bestSolverInstance = solver;
                bestCost = currentCost;
            }
        }
        while (++i < graph.getNegatives().size());
        final CPPSolution solution = bestSolverInstance.solveFrom(n);
        return new CPPSolution(solution, cost + bestSolverInstance.computeExtraCost());
    }
}