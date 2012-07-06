
package pocman.matching;

import pocman.graph.UndirectedGraph;

public interface MatchingAlgorithm {

    <T> Matches<T> from(final UndirectedGraph<T> graph);

    //TODO !! à faire
    //<T> Matches<T> from(final UndirectedGraph<T> graph, map edgeInstance);

    <T> void setOriginalGraph(final UndirectedGraph<T> graph);

}