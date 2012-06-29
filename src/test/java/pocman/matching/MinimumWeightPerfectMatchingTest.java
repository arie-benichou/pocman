
package pocman.matching;

import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

import pocman.graph.UndirectedGraph;
import pocman.graph.WeightedEdge;
import pocman.graph.UndirectedGraph.Builder;
import pocman.matching.MinimumWeightPerfectMatching;

import com.google.common.collect.Maps;


public class MinimumWeightPerfectMatchingTest { // TODO à compléter

    @Test(expected = IllegalStateException.class)
    public void testComputeOptimalEulerizationOnEulerianGraph() {
        final Builder<String> builder = new UndirectedGraph.Builder<String>(3);
        builder.addEdge("A", "B", 1.0);
        builder.addEdge("B", "C", 1.0);
        builder.addEdge("A", "C", 1.0);
        final UndirectedGraph<String> graph = builder.build();
        MinimumWeightPerfectMatching.computeOptimalEulerization(graph);
    }

    @Test
    public void testComputeOptimalEulerization1() {
        final Builder<String> builder = new UndirectedGraph.Builder<String>(2);
        builder.addEdge("A", "B", 1.0);
        final UndirectedGraph<String> graph = builder.build();

        // TODO à revoir
        final Map<WeightedEdge<String>, Integer> expectedTraversalByEdge = Maps.newHashMap();
        expectedTraversalByEdge.put(graph.getEdge("A", "B"), 2);
        final Map<WeightedEdge<String>, Integer> traversalByEdge = MinimumWeightPerfectMatching.computeOptimalEulerization(graph);
        //System.out.println(traversalByEdge);
        Assert.assertTrue(expectedTraversalByEdge.equals(traversalByEdge));
    }

    @Test
    public void testComputeOptimalEulerization2() {

        final Builder<String> builder = new UndirectedGraph.Builder<String>(3);
        builder.addEdge("A", "B", 1.0);
        builder.addEdge("B", "C", 1.0);
        final UndirectedGraph<String> graph = builder.build();

        // TODO à revoir
        final Map<WeightedEdge<String>, Integer> expectedTraversalByEdge = Maps.newHashMap();
        expectedTraversalByEdge.put(graph.getEdge("A", "B"), 2);
        expectedTraversalByEdge.put(graph.getEdge("B", "C"), 2);
        final Map<WeightedEdge<String>, Integer> traversalByEdge = MinimumWeightPerfectMatching.computeOptimalEulerization(graph);
        //System.out.println(traversalByEdge);
        Assert.assertTrue(expectedTraversalByEdge.equals(traversalByEdge));

    }

}