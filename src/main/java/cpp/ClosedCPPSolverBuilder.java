
package cpp;

import java.util.List;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;

public final class ClosedCPPSolverBuilder
{

    private final Graph graph;

    public Graph getGraph() {
        return this.graph;
    }

    private final int[][] repeatedArcs;

    private int getArcRepeat(final int u, final int v) {
        return this.repeatedArcs[u][v];
    }

    private int[][] computeFeasible() {
        final int n = this.graph.getNumberOfNodes();
        final int delta[] = new int[n];
        System.arraycopy(this.graph.delta, 0, delta, 0, n); // TODO !!
        final int[][] repeatedArcs = new int[n][n];
        for (final int i : this.graph.getNegatives()) { // TODO !!
            for (final int j : this.graph.getPositives()) { // TODO !!
                final int value = -delta[i] < delta[j] ? -delta[i] : delta[j];
                repeatedArcs[i][j] = value;
                delta[i] += value;
                delta[j] -= value;
            }
        }
        return repeatedArcs;
    }

    public ClosedCPPSolverBuilder(final Graph graph, final boolean checkState) { // TODO !!
        if (checkState) {
            Preconditions.checkState(!graph.hasNegativeCycle(), "Graph has a negative cycle");
            Preconditions.checkState(graph.isStronglyConnected(), "Graph is not strongly connected");
        }
        this.graph = graph;
        this.repeatedArcs = this.computeFeasible();
    }

    public ClosedCPPSolverBuilder(final Graph graph) {
        this(graph, true);
    }

    private List<Arc> createResidualArcs() {
        final List<Arc> arcs = Lists.newArrayList();
        for (final Integer i : this.graph.getNegatives()) {
            for (final Integer j : this.graph.getPositives()) {
                arcs.add(Arc.from(null, i, j, this.graph.getCost(i, j)));
                if (this.getArcRepeat(i, j) != 0) arcs.add(Arc.from(null, j, i, -this.graph.getCost(i, j)));
            }
        }
        return arcs;
    }

    private Graph createResidualGraph() {
        return new Graph.Builder(this.graph.getNumberOfNodes()).add(this.createResidualArcs()).build();
    }

    private void processNegativeCycle(final Graph graph, final int i) {
        int k = 0;
        boolean kunset = true;
        int u, v;
        u = i;
        do { // find k to cancel
            v = graph.getPath(u, i);
            if (graph.getCost(u, v) < 0 && (kunset || k > this.getArcRepeat(v, u))) {
                k = this.getArcRepeat(v, u);
                kunset = false;
            }
            u = v;
        }
        while (u != i);
        u = i;
        do { // cancel k along the cycle
            v = graph.getPath(u, i);
            if (graph.getCost(u, v) < 0) this.repeatedArcs[v][u] -= k;
            else this.repeatedArcs[u][v] += k;
            u = v;
        }
        while (u != i);

    }

    private boolean isOptimal() {
        final Graph residualGraph = this.createResidualGraph();
        boolean hasNoNegativeCycle = true;
        for (int i = 0; i < this.graph.getNumberOfNodes() && hasNoNegativeCycle; ++i) {
            if (residualGraph.getCost(i, i) < 0) { // cancel the cycle (if any)
                this.processNegativeCycle(residualGraph, i);
                hasNoNegativeCycle = false;
            }
        }
        return hasNoNegativeCycle; // no improvements found
    }

    public ClosedCPPSolver build() {
        while (!this.isOptimal()) {}
        return new ClosedCPPSolver(this.graph, this.repeatedArcs);
    }
}