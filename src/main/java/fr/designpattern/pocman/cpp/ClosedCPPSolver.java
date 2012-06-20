
package fr.designpattern.pocman.cpp;

import java.util.List;
import java.util.Map;

import com.google.common.base.Supplier;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import fr.designpattern.pocman.cpp.graph.UndirectedGraph;
import fr.designpattern.pocman.cpp.graph.Vertex;
import fr.designpattern.pocman.cpp.graph.WeightedEdge;
import fr.designpattern.pocman.cpp.graph.algo.EulerianTrail;
import fr.designpattern.pocman.cpp.graph.algo.MinimumWeightPerfectMatching;

// TODO générique
public class ClosedCPPSolver {

    private final UndirectedGraph<Vertex> graph;
    private final double lowerBoundCost;
    private Map<WeightedEdge<Vertex>, Integer> edgeInstances;

    public double getLowerBoundCost() {
        return this.lowerBoundCost;
    }

    public static class Factory {

        public ClosedCPPSolver newClosedCPPSolver(final UndirectedGraph<Vertex> graph){
            return new ClosedCPPSolver(graph);
        }

        public ClosedCPPSolver newClosedCPPSolver(final Supplier<UndirectedGraph<Vertex>> graphSupplier){
            return this.newClosedCPPSolver(graphSupplier.get());
        }

    }

    public ClosedCPPSolver(final UndirectedGraph<Vertex> graph){
        this.graph = graph;
        this.lowerBoundCost = 0; // TODO
    }

    private void computeOptimalEulerization(){
        this.edgeInstances = MinimumWeightPerfectMatching.computeOptimalEulerization(this.graph);
    }

    public List<Vertex> solveFrom(final Vertex vertex){

        if (this.edgeInstances == null) this.computeOptimalEulerization();

        final List<Vertex> trail = Lists.newArrayList();
        EulerianTrail.apply(vertex, this.graph, Maps.newHashMap(this.edgeInstances), trail);

        return trail;

    }

}