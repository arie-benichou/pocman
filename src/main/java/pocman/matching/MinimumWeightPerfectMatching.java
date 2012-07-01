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

package pocman.matching;

import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import pocman.graph.Path;
import pocman.graph.UndirectedGraph;
import pocman.graph.WeightedEdge;

import com.google.common.base.Stopwatch;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

// TODO ? réduire les noeuds de type corner
public final class MinimumWeightPerfectMatching {

    private static <T> boolean isPerfect(final MutableUndirectedGraph<T> maximumMatching) {
        for (final T MazeNode : maximumMatching)
            if (maximumMatching.getConnectedVerticeSet(MazeNode).size() != 1) return false;
        return true;
    }

    private static <T> Map<T, T> buildMatchingMap(final MutableUndirectedGraph<T> maximumMatching) {
        final Map<T, T> matching = Maps.newHashMap();
        final Set<T> set = Sets.newHashSet(); // TODO ? ou bien ajouter 0.5 deux fois
        for (final T endPoint1 : maximumMatching) {
            final Set<T> endPoints = maximumMatching.getConnectedVerticeSet(endPoint1);
            if (endPoints.size() == 1) {
                final T endPoint2 = endPoints.iterator().next();
                if (!set.contains(endPoint2)) {
                    set.add(endPoint1);
                    set.add(endPoint2);
                    matching.put(endPoint1, endPoint2);
                }
            }
        }
        return matching;
    }

    private static <T> Map<WeightedEdge<T>, Integer> eulerize(final UndirectedGraph<T> originalGraph, final Map<T, T> matching) {
        final Set<WeightedEdge<T>> edges = Sets.newHashSet();
        for (final T MazeNode : originalGraph)
            edges.addAll(originalGraph.getEdges(MazeNode));
        final Map<WeightedEdge<T>, Integer> map = Maps.newHashMap();
        for (final WeightedEdge<T> edge : edges)
            map.put(edge, 1);
        for (final Entry<T, T> entry : matching.entrySet()) {
            final T endPoint1 = entry.getKey();
            final T endPoint2 = entry.getValue();
            final Path<T> path = originalGraph.getShortestPathBetween(endPoint1, endPoint2);
            for (final WeightedEdge<T> edge : path.getEdges())
                map.put(edge, (map.get(edge) + 1) % 2 == 0 ? 2 : 1);
        }
        return map;
    }

    private static <T> double computeCost(final Map<WeightedEdge<T>, Integer> edgeInstances) {
        double cost = 0;
        for (final Entry<WeightedEdge<T>, Integer> entry : edgeInstances.entrySet()) {
            final WeightedEdge<T> edge = entry.getKey();
            final Integer k = entry.getValue();
            cost += k * edge.getWeight();
        }
        return cost;
    }

    public static <T> Map<WeightedEdge<T>, Integer> from(final UndirectedGraph<T> originalGraph, final MutableUndirectedGraph<T> residualGraph,
            Map<T, T> matching) {

        MutableUndirectedGraph<T> maximumMatching = EdmondsMatching.maximumMatching(residualGraph);

        Map<T, T> bestPerfectMatching = null;

        if (isPerfect(maximumMatching)) {
            double bestPerfectMatchingWeight = Double.POSITIVE_INFINITY;
            final Stopwatch stopwatch = new Stopwatch();
            do {
                stopwatch.start();
                matching = buildMatchingMap(maximumMatching);
                final Map<WeightedEdge<T>, Integer> eulerized = eulerize(originalGraph, matching);
                final double cost = computeCost(eulerized);
                if (cost < bestPerfectMatchingWeight) {
                    bestPerfectMatchingWeight = cost;
                    bestPerfectMatching = Maps.newHashMap(matching);
                }
                for (final Entry<T, T> entry : matching.entrySet())
                    residualGraph.removeEdge(entry.getKey(), entry.getValue());
                maximumMatching = EdmondsMatching.maximumMatching(residualGraph);
                stopwatch.reset();
            }
            while (isPerfect(maximumMatching));
        }

        return eulerize(originalGraph, bestPerfectMatching);
    }

    private MinimumWeightPerfectMatching() {}

}