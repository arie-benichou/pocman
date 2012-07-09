
package pocman.matching.naive;

import java.util.List;
import java.util.Map;

import pocman.graph.UndirectedGraph;
import pocman.matching.MatchingAlgorithm;
import pocman.matching.naive.BranchAndBoundMatching.Match;
import pocman.matching.naive.BranchAndBoundMatching.Position;

import com.google.common.base.Function;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;

public class Matching implements MatchingAlgorithm {

    @Override
    //public <T> pocman.matching.Match<T> from(final UndirectedGraph<T> originalGraph, final MutableUndirectedGraph<T> residualGraph) {
    public <T> pocman.matching.Matches<T> from(final UndirectedGraph<T> residualGraph) {

        final int order = residualGraph.getOrder();
        final Map<T, Integer> indexByVertex = Maps.newHashMap();
        final Map<Integer, T> vertexByIndex = Maps.newHashMap();

        int n = 0;
        for (final T vertex : residualGraph) {
            indexByVertex.put(vertex, n);
            vertexByIndex.put(n, vertex);
            ++n;
        }

        final double[][] matrix = new double[order][order];
        for (int i = 0; i < order; ++i)
            for (int j = 0; j < order; ++j)
                matrix[i][j] = residualGraph.getShortestPathBetween(vertexByIndex.get(i), vertexByIndex.get(j)).getWeight();

        final Match match = new BranchAndBoundMatching(matrix).match();

        final Function<Position, Map<T, T>> mapping = new Function<Position, Map<T, T>>() {

            @Override
            public Map<T, T> apply(final Position position) {
                final ImmutableMap.Builder<T, T> builder = new ImmutableMap.Builder<T, T>();
                final T t1 = vertexByIndex.get(position.getRowIndex());
                final T t2 = vertexByIndex.get(position.getColumnIndex());
                builder.put(t1, t2);
                return builder.build();
            }
        };

        final List<Map<T, T>> map = match.apply(mapping);

        final Map<T, T> result = Maps.newHashMap(); // TODO builder Map immutable
        for (final Map<T, T> entry : map)
            result.putAll(entry);

        return new pocman.matching.Matches<T>(result, match.getCost());

    }

    @Override
    public <T> void setOriginalGraph(final UndirectedGraph<T> graph) {
        // TODO Auto-generated method stub

    }

}