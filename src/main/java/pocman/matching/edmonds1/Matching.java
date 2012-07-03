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

package pocman.matching.edmonds1;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import pocman.graph.UndirectedGraph;
import pocman.matching.Match;
import pocman.matching.MatchingAlgorithm;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMap.Builder;
import com.google.common.collect.Sets;

/**
 * Minimum Weight Perfect Matching Algorithm
 */
public final class Matching implements MatchingAlgorithm {

    private static <T> Map<T, T> buildMatchingMap(final MutableUndirectedGraph<T> maximumMatching) {
        final Builder<T, T> builder = new ImmutableMap.Builder<T, T>();
        final Set<T> set = Sets.newHashSet();
        for (final T endPoint1 : maximumMatching) {
            final T endPoint2 = maximumMatching.getEndPoints(endPoint1).iterator().next();
            if (!set.contains(endPoint2)) {
                set.add(endPoint1);
                set.add(endPoint2);
                builder.put(endPoint1, endPoint2);
            }
        }
        return builder.build();
    }

    private static <T> double computeCost(final UndirectedGraph<T> originalGraph, final Map<T, T> matching) {
        if (matching.isEmpty()) return Double.POSITIVE_INFINITY;
        double cost = 0;
        for (final Entry<T, T> entry : matching.entrySet())
            cost += originalGraph.getShortestPathBetween(entry.getKey(), entry.getValue()).getWeight();
        return cost;
    }

    private static <T> boolean isPerfect(final MutableUndirectedGraph<T> maximumMatching) {
        for (final T MazeNode : maximumMatching)
            if (maximumMatching.getEndPoints(MazeNode).size() != 1) return false;
        return true;
    }

    @Override
    //public <T> Match<T> from(final UndirectedGraph<T> originalGraph, final MutableUndirectedGraph<T> residualGraph) {
    public <T> Match<T> from(final UndirectedGraph<T> residualGraph) {
        final MutableUndirectedGraph<T> mutableResidualGraph = new MutableUndirectedGraph<T>();
        for (final T endPoint : residualGraph)
            mutableResidualGraph.addEndPoint(endPoint);
        for (final T endPoint1 : residualGraph)
            for (final T endPoint2 : residualGraph.getEndPoints(endPoint1))
                mutableResidualGraph.addEdge(endPoint1, endPoint2);
        MutableUndirectedGraph<T> maximumMatching = new MutableUndirectedGraph<T>();
        Match<T> bestMatch = new Match<T>(new HashMap<T, T>(), Double.POSITIVE_INFINITY);
        do {
            final Map<T, T> matching = buildMatchingMap(maximumMatching);
            final double cost = computeCost(residualGraph, matching);
            if (Double.compare(cost, bestMatch.getCost()) == -1) bestMatch = new Match<T>(matching, cost);
            for (final Entry<T, T> entry : matching.entrySet())
                mutableResidualGraph.removeEdge(entry.getKey(), entry.getValue());
            maximumMatching = EdmondsAlgorithm.maximumMatching(mutableResidualGraph);
        }
        while (isPerfect(maximumMatching));
        return bestMatch;
    }
}