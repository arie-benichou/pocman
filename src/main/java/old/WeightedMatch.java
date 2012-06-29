/* ************************************************************************* *
 * *
 * Copyright (c) 2004 Peter Cappello <cappello@cs.ucsb.edu> * * Permission is
 * hereby granted, free of charge, to any person obtaining * a copy of this
 * software and associated documentation files (the * "Software"), to deal in
 * the Software without restriction, including * without limitation the rights
 * to use, copy, modify, merge, publish, * distribute, sublicense, and/or sell
 * copies of the Software, and to * permit persons to whom the Software is
 * furnished to do so, subject to * the following conditions: * * The above
 * copyright notice and this permission notice shall be * included in all copies
 * or substantial portions of the Software. * * THE SOFTWARE IS PROVIDED
 * "AS IS", WITHOUT WARRANTY OF ANY KIND, * EXPRESS OR IMPLIED, INCLUDING BUT
 * NOT LIMITED TO THE WARRANTIES OF * MERCHANTABILITY, FITNESS FOR A PARTICULAR
 * PURPOSE AND NONINFRINGEMENT. * IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
 * HOLDERS BE LIABLE FOR ANY * CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN
 * ACTION OF CONTRACT, * TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
 * CONNECTION WITH THE * SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 * * * *************************************************************************
 */

/**
 * This class implements a [minimum | maximum] cost maximum matching based on an
 * O(n^3) implementation of Edmonds' algorithm, as presented by Harold N. Gabow
 * in his Ph.D. dissertation, Computer Science, Stanford University, 1973.
 * 
 * Created on July 8, 2003, 11:00 AM
 * 
 * @author Peter Cappello
 */
/*
 * Gabow's implementation of Edmonds' algorithm is described in chapter 6 of
 * Nonbipartite Matching, of Combinatorial Optimization, Networks and Matroids,
 * authored by Eugene Lawler, published by Holt, Rinehart, and Winston, 1976.
 * <p> Lawler's description is referred to in the Notes and References section
 * of chapter 11, Weighted Matching, of Combinatorial Optimation, Algorithms and
 * Complexity, authored by Christos Papadimitriou and Kenneth Steiglitz,
 * published by Prentice-Hall, 1982. <p> The implementation here mimics Gabow's
 * description and Rothberg's C coding of Gabow's description, making it easy
 * for others to see the correspondence between this code, Rothberg's C code,
 * and Gabow's English description of the algorithm, given in Appendix D of his
 * dissertation. <p> Since the code mimics Gabow's description (Rothberg's C
 * code does so even more closely), the code below is not object-oriented, much
 * less good Java. It also violates many Java naming conventions. <p> Currently,
 * the graph is assumed to be complete & symmetric. <p> It is unclear to me why
 * cost values are doubled in setUp() and intialize(). I think it may have to do
 * with the implementation being entirely integer. When I remove the doubling,
 * the minimum weight maximum match fails on the test graph.
 */

package old;

public final class WeightedMatch
{

    // constants        
    /** The value that indicates that a minimum cost maximum match is sought. */
    public final static boolean MINIMIZE = true;
    /** The value that indicates that a maximum cost maximum match is sought. */
    public final static boolean MAXIMIZE = false;

    private final static boolean DEBUG = false;
    private final static int UNMATCHED = 0;

    private final int[][] costs;

    private int V; // the number of vertices in the graph
    private int E; // the number of edges    in the graph
    private int dummyMazeNode; // artifical MazeNode for boundary conditions
    private int dummyEdge; // artifical edge   for boundary conditions

    private int[] a; // adjacency list
    private int[] end;
    private int[] mate;
    private int[] weight;

    private int[] base;
    private final int[] lastEdge = new int[3]; // Used by methods that undo blossoms.
    private int[] lastMazeNode;
    private int[] link;
    private int[] nextDelta;
    private int[] nextEdge;
    private int[] nextPair;
    private int[] nextMazeNode;
    private int[] y;

    private int delta, lastDelta;
    private int newBase, nextBase, oldBase, stopScan, pairPoint;
    private int neighbor, newLast, nextPoint;
    private int firstMate, newMate, oldFirst, oldMate, secondMate;
    private int f, nxtEdge, nextE, nextU;

    private int e, v, i; // edge, MazeNode, index used by several methods.

    /** Construct a WeightedMatch object. */
    public WeightedMatch(final int[][] costs)
    {
        this.costs = costs;
    }

    /**
     * The int cost matrix is assumed to be square and symmetric (undirected).
     * <p>
     * if ( minimizeWeight ) <br>
     * &nbsp;&nbsp;&nbsp;&nbsp; performs a minimum cost maximum matching;<br>
     * else<br>
     * performs a maximum cost maximum matching.
     * 
     * @param minimizeWeight
     *            if ( minimizeWeight ) performs a minimum cost maximum
     *            matching; else performs a maximum cost maximum matching.
     * @return an array of the form MazeNode[i] = j, where MazeNode i is matched to
     *         MazeNode j. The numbering of vertices is 1, ..., n, where the graph
     *         has n vertices. Thus, the 0th element of the returned int[] is
     *         undefined.
     *         <p>
     *         I don't particularly like this, I am just propagating custom. I
     *         may change this, at some point, so that vertices are numbered 0,
     *         ..., n-1.
     */
    public int[] weightedMatch(final boolean minimizeWeight)
    {
        if (DEBUG)
        {
            System.out.println("weightedMatch: input costs matrix:");
            for (int i = 0; i < this.costs.length; i++)
            {
                System.out.print(" Row " + i + ":");
                for (int j = i + 1; j < this.costs.length; j++)
                {
                    System.out.print(" " + this.costs[i][j]);
                }
                System.out.println("");
            }
        }

        int loop = 1;

        // W0. Input.
        this.input(this.costs);

        // W1. Initialize.
        this.initialize(this.costs, minimizeWeight);

        while (true)
        {
            if (DEBUG)
            {
                System.out.println("\n *** A U G M E N T " + loop++);
            }
            // W2. Start a new search.
            this.delta = 0;
            for (this.v = 1; this.v <= this.V; this.v++)
            {
                if (this.mate[this.v] == this.dummyEdge)
                {
                    // Link all exposed vertices.
                    this.pointer(this.dummyMazeNode, this.v, this.dummyEdge);
                }
            }

            if (DEBUG)
            {
                for (int q = 1; q <= this.V + 1; q++)
                {
                    System.out.println("W2: i: " + q +
                            ", mate: " + this.mate[q] +
                            ", nextEdge: " + this.nextEdge[q] +
                            ", nextMazeNode: " + this.nextMazeNode[q] +
                            ", link: " + this.link[q] +
                            ", base: " + this.base[q] +
                            ", lastMazeNode: " + this.lastMazeNode[q] +
                            ", y: " + this.y[q] +
                            ", nextDelta: " + this.nextDelta[q] +
                            ", lastDelta: " + this.lastDelta
                            );
                }
            }

            // W3. Get next edge.
            while (true)
            {
                this.i = 1;
                for (int j = 2; j <= this.V; j++)
                {
                    /* !!! Dissertation, p. 213, it is nextDelta[i] < nextDelta[j]
                     * When I make it <, the routine seems to do nothing.
                     */
                    if (this.nextDelta[this.i] > this.nextDelta[j])
                    {
                        this.i = j;
                    }
                }

                // delta is the minimum slack in the next edge.
                this.delta = this.nextDelta[this.i];

                if (DEBUG)
                {
                    System.out.println("\nW3: i: " + this.i + " delta: " + this.delta);
                }

                if (this.delta == this.lastDelta)
                {
                    if (DEBUG)
                    {
                        System.out.println("\nW8: delta: " + this.delta + " lastDelta: " + this.lastDelta);
                    }
                    // W8. Undo blossoms.
                    this.setBounds();
                    this.unpairAll();
                    for (this.i = 1; this.i <= this.V; this.i++)
                    {
                        this.mate[this.i] = this.end[this.mate[this.i]];
                        if (this.mate[this.i] == this.dummyMazeNode)
                        {
                            this.mate[this.i] = UNMATCHED;
                        }
                    }

                    // W9.
                    return this.mate;
                }

                // W4. Assign pair links.
                this.v = this.base[this.i];

                if (DEBUG)
                {
                    System.out.println("W4. delta: " + this.delta + " v: " + this.v + " link[v]: " + this.link[this.v]);
                }

                if (this.link[this.v] >= 0)
                {
                    if (this.pair())
                    {
                        break;
                    }
                }
                else
                {
                    // W5. Assign pointer link.
                    if (DEBUG)
                    {
                        System.out.println("W5. delta: " + this.delta + " v: " + this.v);
                    }
                    final int w = this.bmate(this.v); // blossom w is matched with blossom v.
                    if (this.link[w] < 0)
                    {
                        if (DEBUG)
                        {
                            System.out.println("WeightedMatch: delta: " + this.delta + " v: " + this.v + " w: " + w + " link[w]: " + this.link[w]);
                        }
                        // w is unlinked.
                        this.pointer(this.v, w, this.oppEdge(this.nextEdge[this.i]));
                    }
                    else
                    {
                        // W6. Undo a pair link.
                        if (DEBUG)
                        {
                            System.out.println("W6. v: " + this.v + " w: " + w);
                        }
                        this.unpair(this.v, w);
                    }
                }
            }

            // W7. Enlarge the matching.
            this.lastDelta -= this.delta;
            this.setBounds();
            final int g = this.oppEdge(this.e);
            this.rematch(this.bend(this.e), g);
            this.rematch(this.bend(g), this.e);
        }
    }

    // Begin 5 simple functions
    //
    private int bend(final int e) {
        return this.base[this.end[e]];
    }

    private int blink(final int v) {
        return this.base[this.end[this.link[v]]];
    }

    private int bmate(final int v) {
        return this.base[this.end[this.mate[v]]];
    }

    private int oppEdge(final int e)
    {
        return (e - this.V) % 2 == 0 ? e - 1 : e + 1;
    }

    private int slack(final int e)
    {
        return this.y[this.end[e]] + this.y[this.end[this.oppEdge(e)]] - this.weight[e];
    }

    //
    // End 5 simple functions

    private void initialize(final int[][] costs, final boolean minimizeWeight)
    {
        // initialize basic data structures
        this.setUp(costs);

        if (DEBUG)
        {
            for (int q = 0; q < this.V + 2 * this.E + 2; q++)
            {
                System.out.println("initialize: i: " + q + ", a: " + this.a[q] + " end: " + this.end[q] + " weight: " + this.weight[q]);
            }
        }

        this.dummyMazeNode = this.V + 1;
        this.dummyEdge = this.V + 2 * this.E + 1;
        this.end[this.dummyEdge] = this.dummyMazeNode;

        if (DEBUG)
        {
            System.out.println("initialize: dummyMazeNode: " + this.dummyMazeNode +
                    " dummyEdge: " + this.dummyEdge + " oppEdge(dummyEdge): " + this.oppEdge(this.dummyEdge));
        }

        int maxWeight = Integer.MIN_VALUE, minWeight = Integer.MAX_VALUE;
        for (int i = 0; i < this.V; i++)
            for (int j = i + 1; j < this.V; j++)
            {
                final int cost = 2 * costs[i][j];
                if (cost > maxWeight)
                {
                    maxWeight = cost;
                }
                if (cost < minWeight)
                {
                    minWeight = cost;
                }
            }

        if (DEBUG)
        {
            System.out.println("initialize: minWeight: " + minWeight + ", maxWeight: "
                    + maxWeight);
        }

        // If minimize costs, invert weights
        if (minimizeWeight)
        {
            if (this.V % 2 != 0) { throw new IllegalArgumentException("|V| must be even for a " +
                    "minimum cost maximum matching."); }
            maxWeight += 2; // Don't want all 0 weight
            for (int i = this.V + 1; i <= this.V + 2 * this.E; i++)
            {
                this.weight[i] = maxWeight - this.weight[i];
                //System.out.println("initialize: inverted weight[" + i + "]: " +
                //weight[i]);
            }
            maxWeight = maxWeight - minWeight;
        }

        this.lastDelta = maxWeight / 2;
        if (DEBUG)
        {
            System.out.println("initialize: minWeight: " + minWeight +
                    " maxWeight: " + maxWeight + " lastDelta: " + this.lastDelta);
        }

        int allocationSize = this.V + 2;
        this.mate = new int[allocationSize];
        this.link = new int[allocationSize];
        this.base = new int[allocationSize];
        this.nextMazeNode = new int[allocationSize];
        this.lastMazeNode = new int[allocationSize];
        this.y = new int[allocationSize];
        this.nextDelta = new int[allocationSize];
        this.nextEdge = new int[allocationSize];

        allocationSize = this.V + 2 * this.E + 2;
        this.nextPair = new int[allocationSize];

        for (this.i = 1; this.i <= this.V + 1; this.i++)
        {
            this.mate[this.i] = this.nextEdge[this.i] = this.dummyEdge;
            this.nextMazeNode[this.i] = 0;
            this.link[this.i] = -this.dummyEdge;
            this.base[this.i] = this.lastMazeNode[this.i] = this.i;
            this.y[this.i] = this.nextDelta[this.i] = this.lastDelta;

            if (DEBUG)
            {
                System.out.println("initialize: v: " + this.v + ", i: " + this.i +
                        ", mate: " + this.mate[this.i] +
                        ", nextEdge: " + this.nextEdge[this.i] +
                        ", nextMazeNode: " + this.nextMazeNode[this.i] +
                        ", link: " + this.link[this.i] +
                        ", base: " + this.base[this.i] +
                        ", lastMazeNode: " + this.lastMazeNode[this.i] +
                        ", y: " + this.y[this.i] +
                        ", nextDelta: " + this.nextDelta[this.i] +
                        ", lastDelta: " + this.lastDelta
                        );
            }
        }
        //System.out.println("initialize: complete.");
    }

    private void input(final int[][] costs)
    {
        this.V = costs.length;
        this.E = this.V * (this.V - 1) / 2;

        final int allocationSize = this.V + 2 * this.E + 2;
        this.a = new int[allocationSize];
        this.end = new int[allocationSize];
        this.weight = new int[allocationSize];
        for (int i = 0; i < allocationSize; i++)
        {
            this.a[i] = this.end[i] = this.weight[i] = 0;
            //System.out.println("input: i: " + i + ", a: " + a[i] + " " + end[i] + " " + weight[i] );
        }

        if (DEBUG)
        {
            System.out.println("input: V: " + this.V + ", E: " + this.E +
                    ", allocationSize: " + allocationSize);
        }
    }

    /**
     * Updates a blossom's pair list, possibly inserting a new edge. It is
     * invoked by scan and mergePairs. It is invoked with global int e set to
     * the edge to be inserted, neighbor set to the end MazeNode of e, and
     * pairPoint pointing to the next pair to be examined in the pair list.
     */
    private void insertPair()
    {
        if (DEBUG)
        {
            System.out.println("Insert Pair e: " + this.e + " " + this.end[this.oppEdge(this.e)] + "- " + this.end[this.e]);
        }
        int deltaE; // !! check declaration.

        // IP1. Prepare to insert.
        deltaE = this.slack(this.e) / 2;

        if (DEBUG)
        {
            System.out.println("IP1: deltaE: " + deltaE);
        }

        this.nextPoint = this.nextPair[this.pairPoint];

        // IP2. Fint insertion point.
        for (; this.end[this.nextPoint] < this.neighbor; this.nextPoint = this.nextPair[this.nextPoint])
        {
            this.pairPoint = this.nextPoint;
        }

        if (DEBUG)
        {
            System.out.println("IP2: nextPoint: " + this.nextPoint);
        }

        if (this.end[this.nextPoint] == this.neighbor)
        {
            // IP3. Choose the edge.
            if (deltaE >= this.slack(this.nextPoint) / 2) // !!! p. 220. reversed in diss.
            { return; }
            this.nextPoint = this.nextPair[this.nextPoint];
        }

        // IP4. 
        this.nextPair[this.pairPoint] = this.e;
        this.pairPoint = this.e;
        this.nextPair[this.e] = this.nextPoint;

        // IP5. Update best linking edge.
        if (DEBUG)
        {
            System.out.println("IP5: newBase: " + this.newBase + " nextDelta[newBase]: "
                    + this.nextDelta[this.newBase] + " deltaE: " + deltaE);
        }
        if (this.nextDelta[this.newBase] > deltaE)
        {
            this.nextDelta[this.newBase] = deltaE;
        }
    }

    /**
     * Links the unlined vertices inthe path P( end[e], newBase ). Edge e
     * completes a linking path. Invoked by pair. Pre-condition: newBase ==
     * MazeNode of the new blossom. newLast == MazeNode that is currently last on
     * the list of vertices for newBase's blossom.
     */
    private void linkPath(int e)
    {
        int u; // !! declaration?

        if (DEBUG)
        {
            System.out.println("Link Path e = " + this.end[this.oppEdge(e)] + " END[e]: " + this.end[e]);
        }

        // L1. Done?
        for ( /* L1. */this.v = this.bend(e); this.v != this.newBase; this.v = this.bend(e))
        {
            // L2. Link next MazeNode.
            u = this.bmate(this.v);
            this.link[u] = this.oppEdge(e);

            if (DEBUG)
            {
                System.out.println(" L2: LINK[" + u + "]: " + this.link[u]);
            }

            // L3. Add vertices to blossom list.
            this.nextMazeNode[this.newLast] = this.v;
            this.nextMazeNode[this.lastMazeNode[this.v]] = u;
            this.newLast = this.lastMazeNode[u];
            this.i = this.v;

            // L4. Update base.
            do
            {
                this.base[this.i] = this.newBase;
                this.i = this.nextMazeNode[this.i];
            }
            while (this.i != this.dummyMazeNode);

            // L5. Get next edge.
            e = this.link[this.v];
        }
    }

    /**
     * Merges a subblossom's pair list into a new blossom's pair list. Invoked
     * by pair. Pre-condition: v is the base of a previously linked subblossom.
     */
    private void mergePairs(final int v)
    {
        if (DEBUG)
        {
            System.out.println("Merge Pairs v = " + v + " lastDelta: " + this.lastDelta);
        }
        // MP1. Prepare to merge.        
        this.nextDelta[v] = this.lastDelta;

        if (DEBUG)
        {
            System.out.println(" mergePairs: v: " + v + " nextDelta[v]: "
                    + this.nextDelta[v] + " lastDelta: " + this.lastDelta);
        }

        this.pairPoint = this.dummyEdge;
        for (this.f = this.nextEdge[v]; this.f != this.dummyEdge;)
        {
            // MP2. Prepare to insert.
            this.e = this.f;
            this.neighbor = this.end[this.e];
            this.f = this.nextPair[this.f];

            // MP3. Insert edge.
            if (this.base[this.neighbor] != this.newBase)
            {
                this.insertPair();
            }
        }
    }

    /**
     * Processes an edge joining 2 linked vertices. Invoked from W4 of
     * weightedMatch. Pre-condition: v is the base of 1 end of the linking edge.
     * Pair checks whether the edge completes an augmenting path or a pair link
     * path. returns true iff an augmenting path is found.
     */
    private boolean pair()
    {
        if (DEBUG)
        {
            System.out.println("pair: v: " + this.v);
        }
        int u, w, temp;

        // PA1. Prepare to find edge.
        this.e = this.nextEdge[this.v];

        // PA2. Find edge.
        while (this.slack(this.e) != 2 * this.delta)
        {
            this.e = this.nextPair[this.e];
        }

        // PA3. Begin flagging vertices.
        w = this.bend(this.e);
        this.link[this.bmate(w)] = -this.e; // Flag bmate(w)

        if (DEBUG)
        {
            System.out.println(" PA3 LINK[" + this.bmate(w) + "]: " + this.link[this.bmate(w)] +
                    " w: " + w +
                    " bmate: " + this.bmate(w) +
                    " e: " + this.e);
        }

        u = this.bmate(this.v);

        // PA4. Flag vertices.
        while (this.link[u] != -this.e) // u is NOT FLAGGED
        {
            this.link[u] = -this.e;

            if (DEBUG)
            {
                System.out.println(" PA4 LINK[" + u + "]: " + this.link[u] +
                        " e: " + this.e);
            }

            if (this.mate[w] != this.dummyEdge)
            {
                temp = this.v;
                this.v = w;
                w = temp;
            }
            this.v = this.blink(this.v);
            u = this.bmate(this.v);
        }

        // PA5. Augmenting path?
        if (u == this.dummyMazeNode && this.v != w) { return true; // augmenting path found
        }

        // PA6. Prepare to link vertices.
        this.newLast = this.newBase = this.v;
        this.oldFirst = this.nextMazeNode[this.v];

        // PA7. Link vertices.
        this.linkPath(this.e);
        this.linkPath(this.oppEdge(this.e));

        // PA8. Finish linking.
        this.nextMazeNode[this.newLast] = this.oldFirst;
        if (this.lastMazeNode[this.newBase] == this.newBase)
        {
            this.lastMazeNode[this.newBase] = this.newLast;
        }

        // PA9. Start new pair list.
        this.nextPair[this.dummyEdge] = this.dummyEdge;
        this.mergePairs(this.newBase);
        this.i = this.nextMazeNode[this.newBase];
        do
        {
            // PA10. Merge subblossom's pair list
            this.mergePairs(this.i);
            this.i = this.nextMazeNode[this.lastMazeNode[this.i]];

            // PA11. Scan subblossom.
            this.scan(this.i, 2 * this.delta - this.slack(this.mate[this.i]));
            this.i = this.nextMazeNode[this.lastMazeNode[this.i]];
        }
        // PA12. More blossoms?
        while (this.i != this.oldFirst);

        // PA14.
        return false;
    }

    /**
     * pointer assigns a pointer link to a MazeNode. Vertices u & v are the bases
     * of blossoms matched with each other. Edge e joins a MazeNode in blossom u
     * to a linked MazeNode.
     * 
     * pointer is invoked by weightedMatch to link exposed vertices (step W2)
     * and to link unlinked vertices (step W5), and from unpair (steps UP5, UP7)
     * to relink vertices.
     */
    private void pointer(final int u, final int v, final int e)
    {
        if (DEBUG)
        {
            System.out.println("\nPointer on entry: delta: " + this.delta + " u: " + u + " v: " + v + " e: " + e +
                    " oppEdge(e) = " + this.oppEdge(e));
            //System.out.println("Pointer: end[oppEdge(e)]" + end[oppEdge(e)]);
            //System.out.println("Pointer: u, v, e = " + u + " " + v + " " + end[oppEdge(e)] + " " + end[e]);
        }
        int i, del; // !! Is this declaration correct. Check both i & del.

        if (DEBUG)
        {
            System.out.println("\nPointer: delta: " + this.delta + " u: " + u + " v: " + v + " e: " + e +
                    " oppEdge(e) = " + this.oppEdge(e));
            //System.out.println("Pointer: end[oppEdge(e)]" + end[oppEdge(e)]);
            //System.out.println("Pointer: u, v, e = " + u + " " + v + " " + end[oppEdge(e)] + " " + end[e]);
        }

        // PT1. Reinitialize values that may have changed.
        this.link[u] = -this.dummyEdge;

        if (DEBUG)
        {
            System.out.println("PT1. LINK[" + u + "]: " + this.link[u] +
                    " dummyEdge: " + this.dummyEdge);
        }
        this.nextMazeNode[this.lastMazeNode[u]] = this.nextMazeNode[this.lastMazeNode[v]] = this.dummyMazeNode;

        //System.out.println("Pointer: PT2. " + (lastMazeNode[u] != u ));
        // PT2. Find unpairing value.
        if (this.lastMazeNode[u] != u)
        {
            // u's blossom contains other vertices
            i = this.mate[this.nextMazeNode[u]];
            //System.out.println("Pointer: true: i: " + i);
            del = -this.slack(i) / 2;
        }
        else
        {
            //System.out.println("Pointer: false: lastDelta: " + lastDelta);
            del = this.lastDelta;
        }
        i = u;

        if (DEBUG)
        {
            System.out.println(" PT3. del: " + del);
        }

        // PT3.
        for (; i != this.dummyMazeNode; i = this.nextMazeNode[i])
        {
            this.y[i] += del;
            this.nextDelta[i] += del;
            if (DEBUG)
            {
                System.out.println(" PT3: i: " + i + " nextDelta[i]: "
                        + this.nextDelta[i] + " del: " + del);
            }
        }

        // PT4. Link v & scan.

        if (DEBUG)
        {
            System.out.println("POINTER: ?? LINK < 0 ?? LINK: " + this.link[v] + " v: " + v);
        }

        if (this.link[v] < 0)
        {
            // v is unlinked.
            this.link[v] = e;

            if (DEBUG)
            {
                System.out.println("PT4. LINK[" + v + "]: " + this.link[v] + " e: " + e);
            }

            this.nextPair[this.dummyEdge] = this.dummyEdge;
            this.scan(v, this.delta);
        }
        else
        {
            /* Yes, it looks like this statement can be factored out, and put
             * after if condition, eliminating the else. 
             * However, link is a global variable used in scan: 
             *
             * I'm not fooling with it!
             */
            this.link[v] = e;

            if (DEBUG)
            {
                System.out.println("PT4.1. LINK[" + v + "]: " + +this.link[v] + " e: " + e);
            }
        }
    }

    /**
     * Changes the matching along an alternating path. Invoked by weightedMatch
     * (W7) to augment the matching, and from unpair (UP2) and unpairAll (UA6)
     * to rematch a blossom.
     * 
     * Pre-conditions: firstMate is the first base MazeNode on the alternating
     * path. Edge e is the new matched edge for firstMate.
     */
    private void rematch(int firstMate, int e)
    {
        if (DEBUG)
        {
            System.out.println("rematch: firstMate: " + firstMate +
                    ", end[ oppEdge( e ) ]: " + this.end[this.oppEdge(e)] + ", end[e]: " + this.end[e]);
        }
        // R1. Start rematching.
        this.mate[firstMate] = e;
        this.nextE = -this.link[firstMate];

        // R2. Done?
        while (this.nextE != this.dummyEdge)
        {
            // R3. Get next edge.
            e = this.nextE;
            this.f = this.oppEdge(e);
            firstMate = this.bend(e);
            this.secondMate = this.bend(this.f);
            this.nextE = -this.link[firstMate];

            // R4. Relink and rematch.
            this.link[firstMate] = -this.mate[this.secondMate];
            this.link[this.secondMate] = -this.mate[firstMate];

            if (DEBUG)
            {
                System.out.println("R4: LINK[" + firstMate + "]: " + this.link[firstMate] +
                        " link[" + this.secondMate + "]: " + this.link[this.secondMate] + " firstMate: " + firstMate +
                        " secondMate: " + this.secondMate + " mate[secondMate]: " + this.mate[this.secondMate] +
                        " mate[fisrtMate]: " + this.mate[firstMate]);
            }

            this.mate[firstMate] = this.f;
            this.mate[this.secondMate] = e;
        }
    }

    /**
     * scan scans a linked blossom. MazeNode x is the base of a blossom that has
     * just been linked by either pointer or pair. del is used to update y. scan
     * is invoked with the list head nextPair[dummyEdge] pointing to the 1st
     * edge on the pair list of base[x].
     */
    private void scan(int x, final int del)
    {
        int u, delE; // !! is this declaration correct? Check both u & delE.

        if (DEBUG)
        {
            System.out.println("Scan del= " + del + " x= " + x);
        }

        // SC1. Initialize.
        this.newBase = this.base[x];
        this.stopScan = this.nextMazeNode[this.lastMazeNode[x]];
        for (; x != this.stopScan; x = this.nextMazeNode[x] /* SC7. */)
        {
            // SC2. Set bounds & initialize for x.
            this.y[x] += del;
            this.nextDelta[x] = this.lastDelta;

            if (DEBUG)
            {
                System.out.println(" SC2: x: " + x + " lastDelta: " + this.lastDelta
                        + " nextDelta: " + this.nextDelta[x]);
            }

            this.pairPoint = this.dummyEdge;
            this.e = this.a[x]; // !!! in dissertation: if there are no edges, go to SC7.
            for (; this.e != 0; this.e = this.a[this.e] /* SC6. */)
            {
                // SC3. Find a neighbor.
                this.neighbor = this.end[this.e];
                u = this.base[this.neighbor];

                // SC4. Pair link edge.
                if (DEBUG)
                {
                    System.out.println("Scan: SC4: link[" + u + "]: " + this.link[u]);
                }
                if (this.link[u] < 0)
                {

                    if (this.link[this.bmate(u)] < 0 || this.lastMazeNode[u] != u)
                    {
                        delE = this.slack(this.e);
                        if (this.nextDelta[this.neighbor] > delE)
                        {
                            this.nextDelta[this.neighbor] = delE;
                            this.nextEdge[this.neighbor] = this.e;

                            if (DEBUG)
                            {
                                System.out.println(" SC4.1: neighbor: " + this.neighbor +
                                        " nextDelta[neighbor]: " + this.nextDelta[this.neighbor] +
                                        " delE: " + delE);
                            }
                        }
                    }
                }
                else
                {
                    // SC5.
                    if (u != this.newBase)
                    {
                        if (DEBUG)
                        {
                            System.out.println("Scan: SC5: u: " + u + " newBase: " + this.newBase);
                        }
                        this.insertPair();
                    }
                }
            }
        }

        // SC8. 
        this.nextEdge[this.newBase] = this.nextPair[this.dummyEdge];
    }

    /**
     * Updates numerical bounds for linking paths. Invoked by weigtedMatch
     * 
     * Pre-condition: lastDelta set to bound on delta for the next search.
     */
    private void setBounds()
    {
        if (DEBUG)
        {
            System.out.println("setBounds: entered: delta: " + this.delta);
        }
        int del;

        // SB1. Examine each MazeNode.
        for (this.v = 1; this.v <= this.V; this.v++)
        {
            // SB2. Is MazeNode a linked base?
            if (this.link[this.v] < 0 || this.base[this.v] != this.v)
            {
                // SB8. Update nextDelta.
                this.nextDelta[this.v] = this.lastDelta;

                if (DEBUG)
                {
                    System.out.println(" setBounds: v: " + this.v + " nextDelta[v]: "
                            + this.nextDelta[this.v] + " lastDelta: " + this.lastDelta);
                }
                continue;
            }

            // SB3. Begin processing linked blossom.
            this.link[this.v] = -this.link[this.v];

            if (DEBUG)
            {
                System.out.println(" SB3: LINK[" + this.v + "]: " + this.link[this.v]);
            }

            this.i = this.v;

            // SB4. Update y in linked blossom.
            // !! discrepancy: dissertation (do-while); Rothberg (while)
            while (this.i != this.dummyMazeNode)
            {
                this.y[this.i] -= this.delta;
                this.i = this.nextMazeNode[this.i];
            }

            // SB5. Is linked blossom matched?
            this.f = this.mate[this.v];
            if (this.f != this.dummyEdge)
            {
                // SB6. Begin processing unlinked blossom.
                this.i = this.bend(this.f);
                del = this.slack(this.f);

                // SB7. Update y in unlinked blossom.
                // !! discrepancy: dissertation (do-while); Rothberg (while)
                while (this.i != this.dummyMazeNode)
                {
                    this.y[this.i] -= del;
                    this.i = this.nextMazeNode[this.i];
                }
            }
            this.nextDelta[this.v] = this.lastDelta;

            if (DEBUG)
            {
                System.out.println(" setBounds: v: " + this.v + " nextDelta[v]: "
                        + this.nextDelta[this.v] + " lastDelta: " + this.lastDelta);
            }
        }
    }

    private void setUp(final int[][] costs)
    {
        int currentEdge = this.V + 2;
        //System.out.println("setUp: initial currentEdge: " + currentEdge);
        for (int i = this.V; i >= 1; i--)
            for (int j = i - 1; j >= 1; j--)
            {
                /* !! in Rothberg, I only understand the SetMatrix function in the
                 * file "term.c". 
                 * He seems to treat each matrix entry as a directed arc weight in
                 * a symmetric graph. Thus, he multiplies its value by 2, 
                 * representing the undirected symmetric equivalent.
                 *
                 * If I deviate from this, I must also change initialize, which also
                 * refers to the costs matrix.
                 */
                if (DEBUG)
                {
                    System.out.println("setUp: i-1: " + i + " j-1: " + j + " cost: " + costs[i - 1][j - 1]);
                }
                final int cost = 2 * costs[i - 1][j - 1];
                this.weight[currentEdge - 1] = this.weight[currentEdge] = cost;
                this.end[currentEdge - 1] = i;
                this.end[currentEdge] = j;
                this.a[currentEdge] = this.a[i];
                this.a[i] = currentEdge;
                this.a[currentEdge - 1] = this.a[j];
                this.a[j] = currentEdge - 1;
                /*
                if ( DEBUG )
                {
                    System.out.println("setUp: i: " + i + ", j: " + j + 
                    ", costs[i-1,j-1]: " + costs[i-1][j-1] + ", currentEdge: " + currentEdge + 
                    "\n\t weight: " + weight[currentEdge-1] + " " + weight[currentEdge-1] +
                    "\n\t end: " + end[currentEdge-1] +" " + end[currentEdge-1] +
                    "\n\t a: " + a[currentEdge-1] +" " + a[currentEdge-1] + 
                    "\n\t a[i], a[j]: " + a[i] +" " + a[j]  
                 );
                }
                 */
                currentEdge += 2;
            }
    }

    /**
     * Unlinks subblossoms in a blossom. Invoked by unpair and unpairAll
     * Pre-conditions: oldbase is the base of the blossom to be unlinked. unlink
     * preserves the values of the links it undoes, for use by rematch and
     * unpair.
     * 
     * unlink sets the array lastEdge, for use by unpair and unpairAll.
     */
    private void unlink(final int oldBase)
    {
        if (DEBUG)
        {
            System.out.println("unlink: oldBase: " + oldBase);
        }

        // UL1. Prepare to unlink paths.
        this.i = this.newBase = this.nextMazeNode[oldBase];
        this.nextBase = this.nextMazeNode[this.lastMazeNode[this.newBase]];
        this.e = this.link[this.nextBase];

        // Loop is executed twice, for the 2 paths containing the subblossom.       
        for (int j = 1; j <= 2; j++)
        {
            do
            {
                // UL2. Get next path edge.
                if (DEBUG)
                {
                    System.out.println("UL2. j: " + j);
                }
                this.nxtEdge = this.oppEdge(this.link[this.newBase]);

                for (int k = 1; k <= 2; k++)
                {
                    // UL3. Unlink blossom base.
                    this.link[this.newBase] = -this.link[this.newBase];

                    if (DEBUG)
                    {
                        System.out.println("UL3. LINK[" + this.newBase + "]: " + this.link[this.newBase]);
                    }

                    // UL4. Update base array.
                    do
                    {
                        this.base[this.i] = this.newBase;
                        this.i = this.nextMazeNode[this.i];
                    }
                    while (this.i != this.nextBase);

                    // UL5. Get next MazeNode.
                    this.newBase = this.nextBase;
                    this.nextBase = this.nextMazeNode[this.lastMazeNode[this.newBase]];
                }
            }
            // UL6. More vertices?
            while (this.link[this.nextBase] == this.nxtEdge);

            // UL7. End of path.
            if (j == 1)
            {
                this.lastEdge[1] = this.nxtEdge;
                this.nxtEdge = this.oppEdge(this.e);
                if (this.link[this.nextBase] == this.nxtEdge)
                {
                    if (DEBUG)
                    {
                        System.out.println("UL7*. Going to UL2.");
                    }
                    continue; // check the control flow logic.
                }
            }
            break;
        }
        this.lastEdge[2] = this.nxtEdge;

        // UL8. Update blossom list.
        if (this.base[this.lastMazeNode[oldBase]] == oldBase)
        {
            this.nextMazeNode[oldBase] = this.newBase;
        }
        else
        {
            this.nextMazeNode[oldBase] = this.dummyMazeNode;
            this.lastMazeNode[oldBase] = oldBase;
        }
    }

    /**
     * Undoes a blossom by unlinking, rematching, and relinking subblossoms.
     * Invoked by weightedMatch Pre-conditions: oldBase == an unlinked MazeNode,
     * the base of the blossom to be undone. oldMate == a linked MazeNode, the
     * base of the blossom matched to oldBase
     * 
     * It uses a local variable newbase.
     */
    private void unpair(final int oldBase, final int oldMate)
    {
        int e, newbase, u; // !! Are these global (i.e., )?

        if (DEBUG)
        {
            System.out.println("Unpair oldBase: " + oldBase + ", oldMate: " + oldMate);
        }

        // UP1. Unlink vertices.
        this.unlink(oldBase);

        // UP2. Rematch a path.
        newbase = this.bmate(oldMate);
        if (newbase != oldBase)
        {
            this.link[oldBase] = -this.dummyEdge;
            this.rematch(newbase, this.mate[oldBase]);
            this.link[this.secondMate] = this.f == this.lastEdge[1] ? -this.lastEdge[2] : -this.lastEdge[1];
        }

        // UP3. Examine the linking edge.
        e = this.link[oldMate];
        u = this.bend(this.oppEdge(e));
        if (u == newbase)
        {
            // UP7. Relink oldmate.
            this.pointer(newbase, oldMate, e);
            return;
        }
        this.link[this.bmate(u)] = -e;
        // UP4. missing from dissertation.
        do
        {
            // UP5. Relink a MazeNode
            e = -this.link[u];
            this.v = this.bmate(u);
            this.pointer(u, this.v, -this.link[this.v]);

            // UP6. Get next blossom.
            u = this.bend(e);
        }
        while (u != newbase);
        e = this.oppEdge(e);

        // UP7. Relink oldmate
        this.pointer(newbase, oldMate, e);
    }

    /**
     * Undoes all the blossoms, rematching them to get the final matching.
     * Invoked by weightedMatch.
     */
    private void unpairAll()
    {
        int u;

        // UA1. Unpair each blossom.
        for (this.v = 1; this.v <= this.V; this.v++)
        {
            if (this.base[this.v] != this.v || this.lastMazeNode[this.v] == this.v)
            {
                continue;
            }

            // UA2. Prepare to unpair.
            this.nextU = this.v;
            this.nextMazeNode[this.lastMazeNode[this.nextU]] = this.dummyMazeNode;

            while (true)
            {
                // UA3. Get next blossom to unpair.
                u = this.nextU;
                this.nextU = this.nextMazeNode[this.nextU];

                // UA4. Unlink a blossom.
                this.unlink(u);
                if (this.lastMazeNode[u] != u)
                {
                    // UA5. List subblossoms to unpair.
                    this.f = this.lastEdge[2] == this.oppEdge(this.e) ? this.lastEdge[1] : this.lastEdge[2];
                    this.nextMazeNode[this.lastMazeNode[this.bend(this.f)]] = u;
                }

                // UA6. Rematch blossom.
                this.newBase = this.bmate(this.bmate(u));
                if (this.newBase != this.dummyMazeNode && this.newBase != u)
                {
                    this.link[u] = -this.dummyEdge;
                    this.rematch(this.newBase, this.mate[u]);
                }

                // UA7. Find next blossom to unpair.
                while (this.lastMazeNode[this.nextU] == this.nextU && this.nextU != this.dummyMazeNode)
                {
                    this.nextU = this.nextMazeNode[this.nextU];
                }
                if (this.lastMazeNode[this.nextU] == this.nextU && this.nextU == this.dummyMazeNode)
                {
                    break;
                }
            }
        }
    }

    public int[] getMatched(final int[] mates)
    {
        /* WeightedMatch.weightedMatch returns mates, indexed and valued
         * 1, ..., V. Shift the index to 0, ... , V-1 and put the values in
         * this range too (i.e., decrement them). 
         */
        final int[] matches = new int[this.costs.length];
        System.arraycopy(mates, 1, matches, 0, matches.length);
        for (int i = 0; i < matches.length; i++)
        {
            matches[i]--;
        }

        return matches;
    }

    /**
     * Used as a convenient repository of a simple unit test.
     * 
     * @param args
     *            The number of vertices and the seed for a random Euclidean
     *            graph, to use as a unit test.
     */
    public static void main(final String[] args)
    {
        /*
        int[][] costs =
        {
            {    0,    6,    8, 1000, 1000, 1000 },
            {    6,    0, 1000,    6,    3, 1000 },
            {    8, 1000,    0,    3,    3, 1000 },
            { 1000,    6,    3,    0, 1000,    8 },
            { 1000,    3,    3, 1000,    0,    6 },
            { 1000, 1000, 1000,    8,    6,    0 },
        };
         */
        /*
        final int size = Integer.parseInt(args[0]);
        final GraphEuclidean graph = new GraphEuclidean(size, size, 100);
        final int[][] costs = graph.getCosts();

        final long startTime = System.currentTimeMillis();
        final WeightedMatch weightedMatch = new WeightedMatch(costs);
        final int[] mate = weightedMatch.weightedMatch(WeightedMatch.MINIMIZE);
        final long stopTime = System.currentTimeMillis();
        System.out.println("Elapsed time (in milliseconds) for " + size +
                " node graph is " + (stopTime - startTime));

        for (int i = 1; i <= costs.length; i++)
        {
            System.out.println(" " + i + " matches " + mate[i]);
        }
        System.out.println("main: Done.");
        */
    }
}
