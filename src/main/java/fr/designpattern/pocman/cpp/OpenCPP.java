
package fr.designpattern.pocman.cpp;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.TimeUnit;

import com.google.common.base.Preconditions;
import com.google.common.base.Stopwatch;
import com.google.common.base.Supplier;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import fr.designpattern.pocman.graph.Node;
import fr.designpattern.pocman.graph.UndirectedGraph;
import fr.designpattern.pocman.graph.UndirectedGraph.Builder;
import fr.designpattern.pocman.graph.WeightedEdge;

public final class OpenCPP<T> {

    private final UndirectedGraph<T> graph;

    public UndirectedGraph<T> getGraph() {
        return this.graph;
    }

    private ClosedCPP<T> closedCPP = null;

    public ClosedCPP<T> getClosedCPP() {
        if (this.closedCPP == null) this.closedCPP = ClosedCPP.from(this.getGraph());
        return this.closedCPP;
    }

    private final List<T> oddVertices; // TODO lazy

    private UndirectedGraph<Node<T>> boxedGraph;

    public static <T> OpenCPP<T> from(final UndirectedGraph<T> graph) {
        Preconditions.checkArgument(graph != null);
        return new OpenCPP<T>(graph);
    }

    public static <T> OpenCPP<T> from(final Supplier<UndirectedGraph<T>> graphSupplier) {
        Preconditions.checkArgument(graphSupplier != null);
        return OpenCPP.from(graphSupplier.get());
    }

    public static <T> OpenCPP<T> from(final ClosedCPP<T> closedCPP) {
        Preconditions.checkArgument(closedCPP != null);
        return new OpenCPP<T>(closedCPP);
    }

    private UndirectedGraph<Node<T>> getBoxedGraph() {
        if (this.boxedGraph == null) {
            final Builder<Node<T>> builder = new UndirectedGraph.Builder<Node<T>>(this.getGraph().getOrder());
            for (final T vertex : this.getGraph()) {
                final List<WeightedEdge<T>> edges = this.getGraph().getEdges(vertex);
                for (final WeightedEdge<T> weightedEdge : edges) {
                    final WeightedEdge<Node<T>> edge = WeightedEdge.from(
                            new Node<T>(weightedEdge.getEndPoint1()),
                            new Node<T>(weightedEdge.getEndPoint2()),
                            weightedEdge.getWeight());
                    if (!builder.contains(edge)) builder.addEdge(edge);
                }
            }
            this.boxedGraph = builder.build();
        }
        return this.boxedGraph;
    }

    private OpenCPP(final UndirectedGraph<T> graph) {
        this.graph = graph;
        this.oddVertices = Lists.newArrayList(this.getGraph().getOddVertices());
    }

    private OpenCPP(final ClosedCPP<T> closedCPP) {
        this(closedCPP.getGraph());
        this.closedCPP = closedCPP;
    }

    public Double getLowerBoundCost() {
        return this.getClosedCPP().getLowerBoundCost();
    }

    // TODO optimisation possible
    private UndirectedGraph<Node<T>> buildVirtualGraph(final UndirectedGraph<Node<T>> boxedGraph, final T startingVertex, final T oddVertice) {
        final Builder<Node<T>> builder = new UndirectedGraph.Builder<Node<T>>(boxedGraph.getOrder() + 1);
        for (final Node<T> vertex : boxedGraph) {
            final List<WeightedEdge<Node<T>>> edges = boxedGraph.getEdges(vertex);
            for (final WeightedEdge<Node<T>> edge : edges)
                if (!builder.contains(edge)) builder.addEdge(edge);
        }
        final WeightedEdge<Node<T>> virtualEdge1 = WeightedEdge.from(new Node<T>(null), new Node<T>(startingVertex), this.getLowerBoundCost());
        builder.addEdge(virtualEdge1);
        if (!boxedGraph.isEulerian()) {
            final WeightedEdge<Node<T>> virtualEdge2 = WeightedEdge.from(new Node<T>(oddVertice), new Node<T>(null), this.getLowerBoundCost());
            if (!builder.contains(virtualEdge2)) builder.addEdge(virtualEdge2);
        }
        return builder.build();
    }

    // TODO OpenSolution et ClosedSolution
    public OpenSolution<T> solveFrom(final T startingVertex) {

        Preconditions.checkArgument(startingVertex != null);
        Preconditions.checkState(this.getGraph().hasVertex(startingVertex));

        final OpenSolution<Node<T>> bestSolution = this.bestSolution(this.getBoxedGraph(), startingVertex);

        final Map<WeightedEdge<T>, Integer> traversalByEdge = Maps.newHashMap();
        final Map<WeightedEdge<Node<T>>, Integer> boxedTraversalByEdge = bestSolution.getTraversalByEdge();

        System.out.println(bestSolution);

        for (final Entry<WeightedEdge<Node<T>>, Integer> entry : boxedTraversalByEdge.entrySet()) {
            final WeightedEdge<Node<T>> edge = entry.getKey();
            final T data1 = edge.getEndPoint1().getData();
            final T data2 = edge.getEndPoint2().getData();
            if (data1 == null || data2 == null) continue;
            traversalByEdge.put(this.getGraph().getEdge(data1, data2), entry.getValue());
        }
        final OpenSolution<T> unboxedSolution = new OpenSolution<T>(
                bestSolution.getVertex().getData(),
                this.getGraph(),
                traversalByEdge,
                this.getLowerBoundCost(),
                this.getLowerBoundCost() + (bestSolution.getUpperBoundCost() - bestSolution.getLowerBoundCost()));

        return unboxedSolution;
    }

    private OpenSolution<Node<T>> bestSolution(final UndirectedGraph<Node<T>> boxedGraph, final T startingVertex) {

        OpenSolution<Node<T>> bestSolution = new OpenSolution<Node<T>>(null, null, null, null, 2 * this.getLowerBoundCost() * 2);

        final Stopwatch stopwatch = new Stopwatch();

        int i = 0;
        for (final T oddVertice : this.oddVertices) {
            stopwatch.start();
            final UndirectedGraph<Node<T>> virtualGraph = this.buildVirtualGraph(boxedGraph, startingVertex, oddVertice);
            final ClosedCPP<Node<T>> cppSolver = ClosedCPP.from(virtualGraph);

            final Solution<Node<T>> solution = cppSolver.solve();

            if (cppSolver.getUpperBoundCost() < bestSolution.getUpperBoundCost()) {
                bestSolution = new OpenSolution<Node<T>>(new Node<T>(oddVertice), virtualGraph, cppSolver.getTraversalByEdge(),
                        cppSolver.getLowerBoundCost(), cppSolver.getUpperBoundCost());
            }

            System.out.println();
            System.out.println(++i + "/" + this.oddVertices.size() + " : " + stopwatch.elapsedTime(TimeUnit.MILLISECONDS) + " " + TimeUnit.MILLISECONDS);
            System.out.println(oddVertice + " -> " + solution.getUpperBoundCost() + "$");
            System.out.println();

            stopwatch.reset();
        }

        return bestSolution;

    }

}