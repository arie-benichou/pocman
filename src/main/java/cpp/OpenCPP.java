/*
 * Copyright 2012 Arie Benichou
 * 
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * this program. If not, see <http://www.gnu.org/licenses/>.
 */

package cpp;

import graph.Feature;
import graph.UndirectedGraph;
import graph.WeightedEdge;
import graph.UndirectedGraph.Builder;
import graph.features.Degree;

import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import matching.MatchingAlgorithm;


import com.google.common.base.Preconditions;
import com.google.common.base.Stopwatch;
import com.google.common.base.Supplier;
import com.google.common.collect.Maps;

// TODO vérifier que le graphe est connecté
public final class OpenCPP<T> {

    public final static MatchingAlgorithm DEFAULT_MATCHING_ALGORITHM = ClosedCPP.DEFAULT_MATCHING_ALGORITHM;

    private static class Box<T> {

        private final T data;

        public T getData() {
            return this.data;
        }

        private final int hashcode;

        @Override
        public int hashCode() {
            return this.hashcode;
        }

        public Box(final T data) {
            this.data = data;
            this.hashcode = data == null ? -1 : this.data.hashCode();
        }

        @Override
        public boolean equals(final Object object) {
            if (object == null) return false;
            if (object == this) return true;
            if (!(object instanceof Box)) return false;
            final Box<?> that = (Box<?>) object;
            if (this.getData() != null) return this.getData().equals(that.getData());
            else return that.getData() == null;
        }

        @Override
        public String toString() {
            return this.data == null ? "NULL" : this.data.toString();
        }

    }

    private final UndirectedGraph<T> graph;

    public UndirectedGraph<T> getGraph() {
        return this.graph;
    }

    public ClosedCPPSolution<T> getClosedCPPSolution() {
        if (this.closedCPPSolution == null) this.closedCPPSolution = ClosedCPP.from(this.getGraph(), this.matchingAlgorithm).solve();
        return this.closedCPPSolution;
    }

    private UndirectedGraph<Box<T>> boxedGraph;
    private final MatchingAlgorithm matchingAlgorithm;
    private ClosedCPPSolution<T> closedCPPSolution;

    public static <T> OpenCPP<T> from(final UndirectedGraph<T> graph, final MatchingAlgorithm matchingAlgorithm) {
        Preconditions.checkArgument(graph != null);
        Preconditions.checkArgument(matchingAlgorithm != null);
        return new OpenCPP<T>(graph, matchingAlgorithm);
    }

    public static <T> OpenCPP<T> from(final Supplier<UndirectedGraph<T>> graphSupplier, final MatchingAlgorithm matchingAlgorithm) {
        Preconditions.checkArgument(graphSupplier != null);
        Preconditions.checkArgument(matchingAlgorithm != null);
        return OpenCPP.from(graphSupplier.get(), matchingAlgorithm);
    }

    public static <T> OpenCPP<T> from(final UndirectedGraph<T> graph) {
        return OpenCPP.from(graph, ClosedCPP.DEFAULT_MATCHING_ALGORITHM);
    }

    public static <T> OpenCPP<T> from(final Supplier<UndirectedGraph<T>> graphSupplier) {
        return OpenCPP.from(graphSupplier, ClosedCPP.DEFAULT_MATCHING_ALGORITHM);
    }

    public static <T> OpenCPP<T> from(final ClosedCPPSolution<T> closedCPPSolution) {
        Preconditions.checkArgument(closedCPPSolution != null);
        return new OpenCPP<T>(closedCPPSolution);
    }

    private UndirectedGraph<Box<T>> getBoxedGraph() {
        if (this.boxedGraph == null) {
            final Builder<Box<T>> builder = new UndirectedGraph.Builder<Box<T>>(this.getGraph().getOrder());
            for (final T MazeNode : this.getGraph()) {
                final Set<WeightedEdge<T>> edges = this.getGraph().getEdgesFrom(MazeNode);
                for (final WeightedEdge<T> weightedEdge : edges) {
                    final WeightedEdge<Box<T>> edge = WeightedEdge.from(
                            new Box<T>(weightedEdge.getEndPoint1()),
                            new Box<T>(weightedEdge.getEndPoint2()),
                            weightedEdge.getWeight());
                    if (!builder.contains(edge)) builder.addEdge(edge);
                }
            }
            this.boxedGraph = builder.build();
        }
        return this.boxedGraph;
    }

    private OpenCPP(final UndirectedGraph<T> graph, final MatchingAlgorithm matchingAlgorithm) {
        this.graph = graph;
        this.matchingAlgorithm = matchingAlgorithm;
    }

    public OpenCPP(final ClosedCPPSolution<T> closedCPPSolution) {
        this(closedCPPSolution.getGraph(), closedCPPSolution.getMatchingAlgorithm());
        this.closedCPPSolution = closedCPPSolution;
    }

    public Double getLowerBoundCost() {
        return this.getClosedCPPSolution().getLowerBoundCost();
    }

    // TODO optimisation possible
    private UndirectedGraph<Box<T>> buildVirtualGraph(final UndirectedGraph<Box<T>> boxedGraph, final T startingMazeNode, final T oddVertice) {
        final Builder<Box<T>> builder = new UndirectedGraph.Builder<Box<T>>(boxedGraph.getOrder() + 1);
        for (final Box<T> MazeNode : boxedGraph) {
            final Set<WeightedEdge<Box<T>>> edges = boxedGraph.getEdgesFrom(MazeNode);
            for (final WeightedEdge<Box<T>> edge : edges)
                if (!builder.contains(edge)) builder.addEdge(edge);
        }
        final WeightedEdge<Box<T>> virtualEdge1 = WeightedEdge.from(new Box<T>(null), new Box<T>(startingMazeNode), this.getLowerBoundCost());
        builder.addEdge(virtualEdge1);
        final Degree<T> degreeFeature = boxedGraph.getFeature(Feature.DEGREE); // TODO ? utiliser le graph original
        if (!degreeFeature.isEulerian()) {
            final WeightedEdge<Box<T>> virtualEdge2 = WeightedEdge.from(new Box<T>(oddVertice), new Box<T>(null), this.getLowerBoundCost());
            if (!builder.contains(virtualEdge2)) builder.addEdge(virtualEdge2);
        }
        return builder.build();
    }

    public OpenCPPSolution<T> solveFrom(final T startingMazeNode) {

        Preconditions.checkArgument(startingMazeNode != null);
        Preconditions.checkState(this.getGraph().hasEndPoint(startingMazeNode));

        final OpenCPPSolution<Box<T>> bestSolution = this.bestSolution(this.getBoxedGraph(), startingMazeNode);

        final Map<WeightedEdge<T>, Integer> traversalByEdge = Maps.newHashMap();
        final Map<WeightedEdge<Box<T>>, Integer> boxedTraversalByEdge = bestSolution.getTraversalByEdge();

        //System.out.println(bestSolution);

        for (final Entry<WeightedEdge<Box<T>>, Integer> entry : boxedTraversalByEdge.entrySet()) {
            final WeightedEdge<Box<T>> edge = entry.getKey();
            final T data1 = edge.getEndPoint1().getData();
            final T data2 = edge.getEndPoint2().getData();
            if (data1 == null || data2 == null) continue;
            traversalByEdge.put(this.getGraph().getEdge(data1, data2), entry.getValue());
        }
        final OpenCPPSolution<T> unboxedSolution = new OpenCPPSolution<T>(
                bestSolution.getEndPoint().getData(),
                this.getGraph(),
                traversalByEdge,
                this.getLowerBoundCost(),
                this.getLowerBoundCost() + (bestSolution.getUpperBoundCost() - bestSolution.getLowerBoundCost()));

        return unboxedSolution;
    }

    // TODO vérifier qu'il suffit d'itérer sur les noeuds de degré impair
    private OpenCPPSolution<Box<T>> bestSolution(final UndirectedGraph<Box<T>> boxedGraph, final T startingMazeNode) {

        OpenCPPSolution<Box<T>> bestSolution = new OpenCPPSolution<Box<T>>(null, null, null, null, 2 * this.getLowerBoundCost() * 2);

        final Stopwatch stopwatch = new Stopwatch();

        final Degree<T> degreeFeature = this.getGraph().getFeature(Feature.DEGREE);

        //final int i = 0;
        for (final T oddVertice : degreeFeature.getNodesWithOddDegree().keySet()) {
            stopwatch.start();
            final UndirectedGraph<Box<T>> virtualGraph = this.buildVirtualGraph(boxedGraph, startingMazeNode, oddVertice);
            final ClosedCPP<Box<T>> cppSolver = ClosedCPP.from(virtualGraph);
            final ClosedCPPSolution<Box<T>> cppSolution = cppSolver.solve();
            if (cppSolution.getUpperBoundCost() < bestSolution.getUpperBoundCost()) {
                bestSolution = new OpenCPPSolution<Box<T>>(new Box<T>(oddVertice), virtualGraph, cppSolution.getTraversalByEdge(),
                        cppSolution.getLowerBoundCost(), cppSolution.getUpperBoundCost());
            }
            /*
            System.out.println();
            System.out.println(++i + "/" + this.oddVertices.size() + " : " + stopwatch.elapsedTime(TimeUnit.MILLISECONDS) + " " + TimeUnit.MILLISECONDS);
            System.out.println(oddVertice + " -> " + cppSolver.getUpperBoundCost() + "$");
            System.out.println();
            */
            stopwatch.reset();
        }

        return bestSolution;

    }

}