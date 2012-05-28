
package cpp;

import java.util.List;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.common.primitives.Ints;

public final class Graph {

    public final static class Builder {

        private final int numberOfNodes;

        public List<Arc> getArcs() {
            return this.arcs;
        }

        private final List<Arc> arcs = Lists.newArrayList();

        public int getNumberOfNodes() {
            return this.numberOfNodes;
        }

        public Builder(final int numberOfNodes) {
            Preconditions.checkArgument(numberOfNodes > 0);
            this.numberOfNodes = numberOfNodes;
        }

        public Builder add(final Arc arc) {
            this.arcs.add(arc);
            return this;
        }

        public Builder add(final Arc... arcs) {
            for (final Arc arc : arcs)
                this.add(arc);
            return this;
        }

        public Builder add(final List<Arc> arcs) {
            for (final Arc arc : arcs)
                this.add(arc);
            return this;
        }

        public Graph build() {
            return new Graph(this);
        }

    }

    // deltas of vertices
    final int delta[];

    /**
     * Number of nodes in this graph.
     */
    private final int numberOfNodes;

    public int getNumberOfNodes() {
        return this.numberOfNodes;
    }

    /**
     * Balancing of this graph's arcs.
     */
    private final Balancing balancing;

    public List<Integer> getNegatives() {
        return this.balancing.getNegatives();
    }

    public List<Integer> getPositives() {
        return this.balancing.getPositives();
    }

    /**
     * Arcs of this graph.
     */
    final int[][] arcs; // adjacency matrix, counts arcs between vertices

    public int getArc(final int u, final int v) {
        return this.arcs[u][v];
    }

    /**
     * Labels of this graph's arcs (for each vertex pair).
     */
    final List<String>[][] labels;

    public List<String> getLabel(final int u, final int v) {
        return this.labels[u][v];
    }

    /**
     * Costs of cheapest arcs or paths in this graph.
     */
    final double[][] costs;

    public double getCost(final int u, final int v) {
        return this.costs[u][v];
    }

    /**
     * Labels of cheapest arcs in this graph.
     */
    private final String[][] cheapestLabels;

    public String getCheapestLabel(final int u, final int v) {
        return this.cheapestLabels[u][v];
    }

    /**
     * Whether path cost is defined between 2 vertices of this graph.
     */
    private final boolean[][] defined;

    private boolean isDefined(final int u, final int v) {
        return this.defined[u][v];
    }

    /**
     * Spanning tree of this graph.
     */
    final int[][] path;

    int getPath(final int u, final int v) {
        return this.path[u][v];
    }

    /**
     * Total cost of traversing each arc once in this graph.
     */
    private double totalCostLowerBound;

    private final ImmutableList<Arc> arcObjects;

    public ImmutableList<Arc> getArcObjects() {
        return this.arcObjects;
    }

    public double geTotalCostLowerBound() {
        return this.totalCostLowerBound;
    }

    private Graph add(final Arc arc) {

        final int u = arc.getU();
        final int v = arc.getV();
        final String label = arc.getLabel();
        final double cost = arc.getCost();

        if (!this.isDefined(u, v)) this.labels[u][v] = Lists.newArrayList();

        this.labels[u][v].add(label);
        this.totalCostLowerBound += cost;

        if (!this.isDefined(u, v) || this.getCost(u, v) > cost) {
            this.costs[u][v] = cost;
            this.cheapestLabels[u][v] = label;
            this.defined[u][v] = true;
            this.path[u][v] = v;
        }

        this.arcs[u][v]++;
        this.delta[u]++;
        this.delta[v]--;

        return this;

    }

    private Graph add(final List<Arc> arcs) {
        for (final Arc arc : arcs)
            this.add(arc);
        return this;
    }

    /**
     * Floyd-Warshall algorithm Assumes no negative self-cycles. Finds least
     * cost paths or terminates on finding any non-trivial negative cycle.
     */
    private void computeLeastCostPaths() { // TODO extract
        final int n = this.getNumberOfNodes();
        for (int k = 0; k < n; ++k)
            for (int i = 0; i < n; ++i)
                if (this.isDefined(i, k))
                    for (int j = 0; j < n; ++j)
                        if (this.isDefined(k, j) && (!this.isDefined(i, j) || this.getCost(i, j) > this.getCost(i, k) + this.getCost(k, j))) {
                            this.path[i][j] = this.getPath(i, k);
                            this.costs[i][j] = this.getCost(i, k) + this.getCost(k, j);
                            this.defined[i][j] = true;
                            if (i == j && this.getCost(i, j) < 0) return; // stop on negative cycle
                        }
    }

    private Balancing computeBalancing() {
        return new Balancing(Ints.asList(this.delta));
    }

    @SuppressWarnings("unchecked")
    public Graph(final Builder builder) {

        final int n = builder.getNumberOfNodes();

        this.numberOfNodes = n;

        this.delta = new int[n];
        this.defined = new boolean[n][n];
        this.labels = new List[n][n];
        this.costs = new double[n][n];
        this.cheapestLabels = new String[n][n];
        this.path = new int[n][n];
        this.totalCostLowerBound = 0;

        this.arcs = new int[n][n];
        this.add(builder.arcs);
        this.arcObjects = ImmutableList.copyOf(builder.arcs);

        this.computeLeastCostPaths();
        this.balancing = this.computeBalancing();

    }

    public boolean isStronglyConnected() {
        final int n = this.getNumberOfNodes();
        for (int i = 0; i < n; ++i)
            for (int j = 0; j < n; ++j)
                if (!this.isDefined(i, j)) return false;
        return true;
    }

    public boolean hasNegativeCycle() {
        for (int i = 0; i < this.getNumberOfNodes(); ++i)
            if (this.getCost(i, i) < 0) return true;
        return false;
    }

    public boolean isEulerian() {
        return this.getNegatives().isEmpty();
    }

}