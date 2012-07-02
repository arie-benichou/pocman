
package pocman.matching;

import pocman.graph.UndirectedGraph;

public interface MatchingAlgorithm {

    <T> Match<T> from(final UndirectedGraph<T> originalGraph, final MutableUndirectedGraph<T> residualGraph);

}