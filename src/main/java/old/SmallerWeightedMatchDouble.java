
package old;

public final class SmallerWeightedMatchDouble {

    public final static boolean MINIMIZE = true;
    public final static boolean MAXIMIZE = false;

    private final static int UNMATCHED = 0;

    private final double[][] costs;

    private int V;
    private int E;
    private int dummyVertex;
    private int dummyEdge;

    private int[] adjacency;
    private int[] end;
    private int[] mate;

    private double[] weight;
    private double delta, lastDelta;
    private double[] nextDelta;
    private double[] y;

    private int[] base;
    private final int[] lastEdge = new int[3];
    private int[] lastVertex;
    private int[] link;

    private int[] nextEdge;
    private int[] nextPair;
    private int[] nextVertex;

    private int newBase, nextBase;
    private int stopScan, pairPoint;
    private int neighbor, newLast, nextPoint;
    private int oldFirst, secondMate;
    private int f, nxtEdge, nextE, nextU;
    private int e, v, i; // edge, vertex, index used by several methods.

    public SmallerWeightedMatchDouble(final double[][] costs) {
        this.costs = costs;
    }

    public int[] weightedMatch(final boolean minimizeWeight) {
        this.input(this.costs);
        this.initialize(this.costs, minimizeWeight);
        while (true) {
            this.delta = 0;
            for (this.v = 1; this.v <= this.V; this.v++)
                if (this.mate[this.v] == this.dummyEdge) this.pointer(this.dummyVertex, this.v, this.dummyEdge);
            while (true) {
                this.i = 1;
                for (int j = 2; j <= this.V; j++)
                    if (this.nextDelta[this.i] > this.nextDelta[j]) this.i = j;
                this.delta = this.nextDelta[this.i];
                if (this.delta == this.lastDelta) {
                    this.setBounds();
                    this.unpairAll();
                    for (this.i = 1; this.i <= this.V; this.i++) {
                        this.mate[this.i] = this.end[this.mate[this.i]];
                        if (this.mate[this.i] == this.dummyVertex) this.mate[this.i] = UNMATCHED;
                    }
                    return this.mate;
                }
                this.v = this.base[this.i];
                if (this.link[this.v] >= 0) {
                    if (this.pair()) break;
                }
                else
                {
                    final int w = this.bmate(this.v); // blossom w is matched with blossom v.
                    if (this.link[w] < 0) this.pointer(this.v, w, this.oppEdge(this.nextEdge[this.i]));
                    else this.unpair(this.v, w);
                }
            }
            this.lastDelta -= this.delta;
            this.setBounds();
            final int g = this.oppEdge(this.e);
            this.rematch(this.bend(this.e), g);
            this.rematch(this.bend(g), this.e);
        }
    }

    private int bend(final int e) {
        return this.base[this.end[e]];
    }

    private int blink(final int v) {
        return this.base[this.end[this.link[v]]];
    }

    private int bmate(final int v) {
        return this.base[this.end[this.mate[v]]];
    }

    private int oppEdge(final int e) {
        return (e - this.V) % 2 == 0 ? e - 1 : e + 1;
    }

    private double slack(final int e) {
        return this.y[this.end[e]] + this.y[this.end[this.oppEdge(e)]] - this.weight[e];
    }

    private void initialize(final double[][] costs, final boolean minimizeWeight) {
        this.setUp(costs);
        this.dummyVertex = this.V + 1;
        this.dummyEdge = this.V + 2 * this.E + 1;
        this.end[this.dummyEdge] = this.dummyVertex;
        double maxWeight = Integer.MIN_VALUE;
        double minWeight = Integer.MAX_VALUE;
        for (int i = 0; i < this.V; i++)
            for (int j = i + 1; j < this.V; j++) {
                final double cost = 2 * costs[i][j];
                if (cost > maxWeight) maxWeight = cost;
                if (cost < minWeight) minWeight = cost;
            }
        if (minimizeWeight) {
            if (this.V % 2 != 0) throw new IllegalArgumentException("|V| must be even for a " + "minimum cost maximum matching.");
            maxWeight += 2; // Don't want all 0 weight
            for (int i = this.V + 1; i <= this.V + 2 * this.E; i++)
                this.weight[i] = maxWeight - this.weight[i];
            maxWeight = maxWeight - minWeight;
        }
        this.lastDelta = maxWeight / 2;
        int allocationSize = this.V + 2;
        this.mate = new int[allocationSize];
        this.link = new int[allocationSize];
        this.base = new int[allocationSize];
        this.nextVertex = new int[allocationSize];
        this.lastVertex = new int[allocationSize];
        this.y = new double[allocationSize];
        this.nextDelta = new double[allocationSize];
        this.nextEdge = new int[allocationSize];
        allocationSize = this.V + 2 * this.E + 2;
        this.nextPair = new int[allocationSize];
        for (this.i = 1; this.i <= this.V + 1; this.i++) {
            this.mate[this.i] = this.nextEdge[this.i] = this.dummyEdge;
            this.nextVertex[this.i] = 0;
            this.link[this.i] = -this.dummyEdge;
            this.base[this.i] = this.lastVertex[this.i] = this.i;
            this.y[this.i] = this.nextDelta[this.i] = this.lastDelta;

        }
    }

    private void input(final double[][] costs) {
        this.V = costs.length;
        this.E = this.V * (this.V - 1) / 2;
        final int allocationSize = this.V + 2 * this.E + 2;
        this.adjacency = new int[allocationSize];
        this.end = new int[allocationSize];
        this.weight = new double[allocationSize];
        for (int i = 0; i < allocationSize; i++) {
            this.adjacency[i] = this.end[i] = 0;
            this.weight[i] = 0;
        }
    }

    private void insertPair() {
        double deltaE;
        deltaE = this.slack(this.e) / 2;
        this.nextPoint = this.nextPair[this.pairPoint];
        for (; this.end[this.nextPoint] < this.neighbor; this.nextPoint = this.nextPair[this.nextPoint])
            this.pairPoint = this.nextPoint;
        if (this.end[this.nextPoint] == this.neighbor) {
            if (deltaE >= this.slack(this.nextPoint) / 2) return;
            this.nextPoint = this.nextPair[this.nextPoint];
        }
        this.nextPair[this.pairPoint] = this.e;
        this.pairPoint = this.e;
        this.nextPair[this.e] = this.nextPoint;
        if (this.nextDelta[this.newBase] > deltaE) this.nextDelta[this.newBase] = deltaE;
    }

    private void linkPath(int e) {
        int u;
        for (this.v = this.bend(e); this.v != this.newBase; this.v = this.bend(e)) {
            u = this.bmate(this.v);
            this.link[u] = this.oppEdge(e);
            this.nextVertex[this.newLast] = this.v;
            this.nextVertex[this.lastVertex[this.v]] = u;
            this.newLast = this.lastVertex[u];
            this.i = this.v;
            do {
                this.base[this.i] = this.newBase;
                this.i = this.nextVertex[this.i];
            }
            while (this.i != this.dummyVertex);
            e = this.link[this.v];
        }
    }

    private void mergePairs(final int v) {
        this.nextDelta[v] = this.lastDelta;
        this.pairPoint = this.dummyEdge;
        for (this.f = this.nextEdge[v]; this.f != this.dummyEdge;) {
            this.e = this.f;
            this.neighbor = this.end[this.e];
            this.f = this.nextPair[this.f];
            if (this.base[this.neighbor] != this.newBase) this.insertPair();
        }
    }

    private boolean pair() {
        int u, w, temp;
        this.e = this.nextEdge[this.v];
        while (this.slack(this.e) != 2 * this.delta)
            this.e = this.nextPair[this.e];
        w = this.bend(this.e);
        this.link[this.bmate(w)] = -this.e;
        u = this.bmate(this.v);
        while (this.link[u] != -this.e) {
            this.link[u] = -this.e;
            if (this.mate[w] != this.dummyEdge) {
                temp = this.v;
                this.v = w;
                w = temp;
            }
            this.v = this.blink(this.v);
            u = this.bmate(this.v);
        }
        if (u == this.dummyVertex && this.v != w) return true;
        this.newLast = this.newBase = this.v;
        this.oldFirst = this.nextVertex[this.v];
        this.linkPath(this.e);
        this.linkPath(this.oppEdge(this.e));
        this.nextVertex[this.newLast] = this.oldFirst;
        if (this.lastVertex[this.newBase] == this.newBase) this.lastVertex[this.newBase] = this.newLast;
        this.nextPair[this.dummyEdge] = this.dummyEdge;
        this.mergePairs(this.newBase);
        this.i = this.nextVertex[this.newBase];
        do {
            this.mergePairs(this.i);
            this.i = this.nextVertex[this.lastVertex[this.i]];
            this.scan(this.i, 2 * this.delta - this.slack(this.mate[this.i]));
            this.i = this.nextVertex[this.lastVertex[this.i]];
        }
        while (this.i != this.oldFirst);
        return false;
    }

    private SmallerWeightedMatchDouble pointer(final int u, final int v, final int e) {
        int i;
        double del;
        this.link[u] = -this.dummyEdge;
        this.nextVertex[this.lastVertex[u]] = this.nextVertex[this.lastVertex[v]] = this.dummyVertex;
        if (this.lastVertex[u] != u) {
            i = this.mate[this.nextVertex[u]];
            del = -this.slack(i) / 2;
        }
        else del = this.lastDelta;
        i = u;
        for (; i != this.dummyVertex; i = this.nextVertex[i]) {
            this.y[i] += del;
            this.nextDelta[i] += del;
        }
        if (this.link[v] < 0) {
            this.link[v] = e;
            this.nextPair[this.dummyEdge] = this.dummyEdge;
            this.scan(v, this.delta);
        }
        else this.link[v] = e;
        return this;
    }

    private void rematch(int firstMate, int e) {
        this.mate[firstMate] = e;
        this.nextE = -this.link[firstMate];
        while (this.nextE != this.dummyEdge) {
            e = this.nextE;
            this.f = this.oppEdge(e);
            firstMate = this.bend(e);
            this.secondMate = this.bend(this.f);
            this.nextE = -this.link[firstMate];
            this.link[firstMate] = -this.mate[this.secondMate];
            this.link[this.secondMate] = -this.mate[firstMate];
            this.mate[firstMate] = this.f;
            this.mate[this.secondMate] = e;
        }
    }

    private void scan(int x, final double delta) {
        int u;
        double delE;
        this.newBase = this.base[x];
        this.stopScan = this.nextVertex[this.lastVertex[x]];
        for (; x != this.stopScan; x = this.nextVertex[x]) {
            this.y[x] += delta;
            this.nextDelta[x] = this.lastDelta;
            this.pairPoint = this.dummyEdge;
            this.e = this.adjacency[x];
            for (; this.e != 0; this.e = this.adjacency[this.e]) {
                this.neighbor = this.end[this.e];
                u = this.base[this.neighbor];
                if (this.link[u] < 0) {
                    if (this.link[this.bmate(u)] < 0 || this.lastVertex[u] != u) {
                        delE = this.slack(this.e);
                        if (this.nextDelta[this.neighbor] > delE) {
                            this.nextDelta[this.neighbor] = delE;
                            this.nextEdge[this.neighbor] = this.e;
                        }
                    }
                }
                else if (u != this.newBase) this.insertPair();
            }
        }
        this.nextEdge[this.newBase] = this.nextPair[this.dummyEdge];
    }

    private void setBounds() {
        double del;
        for (this.v = 1; this.v <= this.V; this.v++) {
            if (this.link[this.v] < 0 || this.base[this.v] != this.v) {
                this.nextDelta[this.v] = this.lastDelta;
                continue;
            }
            this.link[this.v] = -this.link[this.v];
            this.i = this.v;
            while (this.i != this.dummyVertex) {
                this.y[this.i] -= this.delta;
                this.i = this.nextVertex[this.i];
            }
            this.f = this.mate[this.v];
            if (this.f != this.dummyEdge) {
                this.i = this.bend(this.f);
                del = this.slack(this.f);
                while (this.i != this.dummyVertex) {
                    this.y[this.i] -= del;
                    this.i = this.nextVertex[this.i];
                }
            }
            this.nextDelta[this.v] = this.lastDelta;
        }
    }

    private void setUp(final double[][] costs) {
        int currentEdge = this.V + 2;
        for (int i = this.V; i >= 1; i--)
            for (int j = i - 1; j >= 1; j--) {
                final double cost = 2 * costs[i - 1][j - 1];
                this.weight[currentEdge - 1] = this.weight[currentEdge] = cost;
                this.end[currentEdge - 1] = i;
                this.end[currentEdge] = j;
                this.adjacency[currentEdge] = this.adjacency[i];
                this.adjacency[i] = currentEdge;
                this.adjacency[currentEdge - 1] = this.adjacency[j];
                this.adjacency[j] = currentEdge - 1;
                currentEdge += 2;
            }
    }

    private void unlink(final int oldBase) {
        this.i = this.newBase = this.nextVertex[oldBase];
        this.nextBase = this.nextVertex[this.lastVertex[this.newBase]];
        this.e = this.link[this.nextBase];
        for (int j = 1; j <= 2; j++) {
            do {
                this.nxtEdge = this.oppEdge(this.link[this.newBase]);
                for (int k = 1; k <= 2; k++) {
                    this.link[this.newBase] = -this.link[this.newBase];
                    do {
                        this.base[this.i] = this.newBase;
                        this.i = this.nextVertex[this.i];
                    }
                    while (this.i != this.nextBase);
                    this.newBase = this.nextBase;
                    this.nextBase = this.nextVertex[this.lastVertex[this.newBase]];
                }
            }
            while (this.link[this.nextBase] == this.nxtEdge);
            if (j == 1) {
                this.lastEdge[1] = this.nxtEdge;
                this.nxtEdge = this.oppEdge(this.e);
                if (this.link[this.nextBase] == this.nxtEdge) continue;
            }
            break;
        }
        this.lastEdge[2] = this.nxtEdge;
        if (this.base[this.lastVertex[oldBase]] == oldBase) this.nextVertex[oldBase] = this.newBase;
        else {
            this.nextVertex[oldBase] = this.dummyVertex;
            this.lastVertex[oldBase] = oldBase;
        }
    }

    private SmallerWeightedMatchDouble unpair(final int oldBase, final int oldMate) {
        int e, newbase, u;
        this.unlink(oldBase);
        newbase = this.bmate(oldMate);
        if (newbase != oldBase) {
            this.link[oldBase] = -this.dummyEdge;
            this.rematch(newbase, this.mate[oldBase]);
            this.link[this.secondMate] = this.f == this.lastEdge[1] ? -this.lastEdge[2] : -this.lastEdge[1];
        }
        e = this.link[oldMate];
        u = this.bend(this.oppEdge(e));
        if (u == newbase) { return this.pointer(newbase, oldMate, e); }
        this.link[this.bmate(u)] = -e;
        do {
            e = -this.link[u];
            this.v = this.bmate(u);
            this.pointer(u, this.v, -this.link[this.v]);
            u = this.bend(e);
        }
        while (u != newbase);
        e = this.oppEdge(e);
        return this.pointer(newbase, oldMate, e);
    }

    private void unpairAll() {
        int u;
        for (this.v = 1; this.v <= this.V; this.v++) {
            if (this.base[this.v] != this.v || this.lastVertex[this.v] == this.v) continue;
            this.nextU = this.v;
            this.nextVertex[this.lastVertex[this.nextU]] = this.dummyVertex;
            while (true) {
                u = this.nextU;
                this.nextU = this.nextVertex[this.nextU];
                this.unlink(u);
                if (this.lastVertex[u] != u) {
                    this.f = this.lastEdge[2] == this.oppEdge(this.e) ? this.lastEdge[1] : this.lastEdge[2];
                    this.nextVertex[this.lastVertex[this.bend(this.f)]] = u;
                }
                this.newBase = this.bmate(this.bmate(u));
                if (this.newBase != this.dummyVertex && this.newBase != u) {
                    this.link[u] = -this.dummyEdge;
                    this.rematch(this.newBase, this.mate[u]);
                }
                while (this.lastVertex[this.nextU] == this.nextU && this.nextU != this.dummyVertex)
                    this.nextU = this.nextVertex[this.nextU];
                if (this.lastVertex[this.nextU] == this.nextU && this.nextU == this.dummyVertex) break;
            }
        }
    }

    public int[] getMatched(final int[] mates) {
        final int[] matches = new int[this.costs.length];
        System.arraycopy(mates, 1, matches, 0, matches.length);
        for (int i = 0; i < matches.length; i++)
            matches[i]--;
        return matches;
    }

}