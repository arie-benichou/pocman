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

package pocman.matching.edmonds2;

import java.util.List;
import java.util.Map;

import pocman.graph.UndirectedGraph;
import pocman.matching.MutableUndirectedGraph;
import todo.SmallerWeightedMatch;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.primitives.Ints;

/**
 * Minimum Weight Perfect Matching Algorithm
 */
public final class Matching {

    public static <T> pocman.matching.Match<T> from(final UndirectedGraph<T> originalGraph, final MutableUndirectedGraph<T> residualGraph) {

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
                matrix[i][j] = originalGraph.getShortestPathBetween(vertexByIndex.get(i), vertexByIndex.get(j)).getWeight();

        final WeightedMatch weightedMatch = new WeightedMatch(matrix);
        final int[] mate = weightedMatch.weightedMatch(SmallerWeightedMatch.MINIMIZE);

        final int[] matched = weightedMatch.getMatched(mate);
        final List<List<Integer>> partition = Lists.partition(Ints.asList(matched), 2);

        double cost = 0.0;
        final Map<T, T> matches = Maps.newHashMap();
        for (final List<Integer> position : partition) {
            final Integer i = position.get(0);
            final Integer j = position.get(1);
            matches.put(vertexByIndex.get(i), vertexByIndex.get(j));
            cost += matrix[i][j];
        }

        return new pocman.matching.Match<T>(matches, cost);
    }

    private Matching() {}

}