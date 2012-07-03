
package pocman.matching;

import pocman.graph.UndirectedGraph;

public interface MatchingAlgorithm {

    // TODO pouvoir passer uniquement le graphe résiduel 
    <T> Match<T> from(final UndirectedGraph<T> originalGraph, final MutableUndirectedGraph<T> residualGraph);

}