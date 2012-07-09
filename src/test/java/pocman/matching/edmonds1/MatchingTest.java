
package pocman.matching.edmonds1;

import pocman.graph.UndirectedGraph;
import pocman.graph.UndirectedGraph.Builder;

public class MatchingTest {

    //@Test
    public void testFrom1() {

        final Builder<String> builder = new UndirectedGraph.Builder<String>(4);
        builder.addEdge("A", "B", 1);
        builder.addEdge("A", "C", 1);
        builder.addEdge("A", "D", 1);
        builder.addEdge("B", "C", 1);
        builder.addEdge("B", "D", 1);
        builder.addEdge("C", "D", 1);
        final UndirectedGraph<String> graph = builder.build();

        final Matching matching = new Matching();
        //final Matches<String> from = matching.from(graph);

        matching.enumeration(graph, 0);

    }

}