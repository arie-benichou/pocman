
package fr.designpattern.pocman.cpp;

import junit.framework.Assert;

import org.junit.Test;

public class EdmondsMatchingTest { // TODO à compléter

    @Test
    public void testMaximumMatching1() {
        final MutableUndirectedGraph<String> graph = new MutableUndirectedGraph<String>();
        graph.addVertex("A");
        graph.addVertex("B");
        graph.addEdge("A", "B");
        final MutableUndirectedGraph<String> maximumMatching = EdmondsMatching.maximumMatching(graph);
        Assert.assertTrue(maximumMatching.equals(graph));
    }

    @Test
    public void testMaximumMatching2() {
        final MutableUndirectedGraph<String> graph = new MutableUndirectedGraph<String>();
        graph.addVertex("A");
        graph.addVertex("B");
        graph.addVertex("C");
        graph.addVertex("D");
        graph.addEdge("A", "B");
        graph.addEdge("C", "D");
        final MutableUndirectedGraph<String> maximumMatching = EdmondsMatching.maximumMatching(graph);
        Assert.assertTrue(maximumMatching.equals(graph));
    }

    @Test
    public void testMaximumMatching3() {
        final MutableUndirectedGraph<String> graph = new MutableUndirectedGraph<String>();
        graph.addVertex("A");
        graph.addVertex("B");
        graph.addVertex("C");
        graph.addVertex("D");
        graph.addEdge("A", "B");
        graph.addEdge("B", "C");
        graph.addEdge("C", "D");
        final MutableUndirectedGraph<String> maximumMatching = EdmondsMatching.maximumMatching(graph);
        graph.removeEdge("B", "C");
        Assert.assertTrue(maximumMatching.equals(graph));
    }

    @Test
    public void testMaximumMatching4() {
        final MutableUndirectedGraph<String> graph = new MutableUndirectedGraph<String>();
        graph.addVertex("A");
        graph.addVertex("B");
        graph.addVertex("C");
        graph.addVertex("D");
        graph.addEdge("A", "B");
        graph.addEdge("B", "C");
        graph.addEdge("C", "D");
        graph.addEdge("A", "D");
        final MutableUndirectedGraph<String> maximumMatching = EdmondsMatching.maximumMatching(graph);
        /*
        for (final String string : maximumMatching) {
            System.out.print(string + ": ");
            System.out.println(maximumMatching.getConnectedVerticeSet(string));
        }
        */
        graph.removeEdge("A", "B");
        graph.removeEdge("C", "D");
        Assert.assertTrue(maximumMatching.equals(graph));
    }

}