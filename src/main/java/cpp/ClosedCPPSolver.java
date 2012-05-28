
package cpp;

import java.util.List;

import com.google.common.collect.Lists;

public class ClosedCPPSolver {

    private final static int NONE = -1;

    private final Graph graph;

    private final int[][] repeatedArcs;

    private int getArcRepeat(final int u, final int v) {
        return this.repeatedArcs[u][v];
    }

    public ClosedCPPSolver(final Graph graph, final int[][] repeatedArcs) {
        this.graph = graph;
        this.repeatedArcs = repeatedArcs;
    }

    private int findPath(final int from, final int[][] f) {
        for (int i = 0; i < this.graph.getNumberOfNodes(); ++i)
            if (f[from][i] > 0) return i;
        return NONE;
    }

    public double computeExtraCost() {
        double phi = 0;
        final int n = this.graph.getNumberOfNodes();
        for (int i = 0; i < n; ++i)
            for (int j = 0; j < n; ++j)
                phi += this.getArcRepeat(i, j) * this.graph.getCost(i, j);
        return phi;
    }

    public CPPSolution solveFrom(final int startVertex) {

        final List<Path> path = Lists.newArrayList();

        int v = startVertex;

        final int n = this.graph.getNumberOfNodes();
        final int arcsCopy[][] = new int[n][n];
        final int repeatedArcsCopy[][] = new int[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                arcsCopy[i][j] = this.graph.getArc(i, j);
                repeatedArcsCopy[i][j] = this.getArcRepeat(i, j);
            }
        }

        while (true) {
            int u = v;
            if ((v = this.findPath(u, repeatedArcsCopy)) != NONE) {
                repeatedArcsCopy[u][v]--; // remove path
                for (int p; u != v; u = p) { // break down path into its arcs
                    p = this.graph.getPath(u, v);
                    //path.add("Take arc " + this.graph.getCheapestLabel(u, p) + " from " + u + " to " + p);
                    System.out.println("Take arc " + this.graph.getCheapestLabel(u, p) + " from " + u + " to " + p);
                    path.add(new Path(u, p));
                }
            }
            else {
                final int bridgeVertex = this.graph.getPath(u, startVertex);
                if (arcsCopy[u][bridgeVertex] == 0) break; // finished if bridge already used
                v = bridgeVertex;
                for (int i = 0; i < n; ++i) {
                    if (i != bridgeVertex && arcsCopy[u][i] > 0) { // find an unused arc, using bridge last
                        v = i;
                        break;
                    }
                }
                arcsCopy[u][v]--; // decrement count of parallel arcs
                //path.add("Take arc " + this.graph.getLabel(u, v).get(arcsCopy[u][v]) + " from " + u + " to " + v); // use each arc label in turn
                System.out.println("Take arc " + this.graph.getLabel(u, v).get(arcsCopy[u][v]) + " from " + u + " to " + v);
                path.add(new Path(u, v));
            }
        }

        return new CPPSolution(path, this.graph.geTotalCostLowerBound() + this.computeExtraCost());
    }

}