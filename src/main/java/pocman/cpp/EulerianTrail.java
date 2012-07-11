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

package pocman.cpp;

import java.util.List;
import java.util.Map;

import pocman.graph.UndirectedGraph;
import pocman.graph.WeightedEdge;

import com.google.common.base.Preconditions;
import com.google.common.base.Supplier;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableList.Builder;
import com.google.common.collect.Maps;


public final class EulerianTrail {

    /**
     * Fleury algorithm
     */
    private static <T> List<T> from(final T startingMazeNode, final Map<WeightedEdge<T>, Integer> traversalByEdge, final UndirectedGraph<T> graph) {
        final Builder<T> trailBuilder = new ImmutableList.Builder<T>();
        for (final WeightedEdge<T> edge : graph.getEdgesFrom(startingMazeNode)) {
            final Integer integer = traversalByEdge.get(edge);
            if (integer > 0) {
                traversalByEdge.put(edge, integer - 1);
                T nextMazeNode = null;
                if (startingMazeNode.equals(edge.getEndPoint1())) nextMazeNode = edge.getEndPoint2();
                else if (startingMazeNode.equals(edge.getEndPoint2())) nextMazeNode = edge.getEndPoint1();
                Preconditions.checkState(nextMazeNode != null);
                trailBuilder.addAll(EulerianTrail.from(nextMazeNode, traversalByEdge, graph));
            }
        }
        trailBuilder.add(startingMazeNode);
        return trailBuilder.build();
    }

    public static <T> List<T> from(final UndirectedGraph<T> graph, final Map<WeightedEdge<T>, Integer> traversalByEdge, final T startingNode) {
        return from(startingNode, Maps.newHashMap(traversalByEdge), graph);
    }

    public static <T> List<T> from(final Supplier<UndirectedGraph<T>> graphSupplier, final Map<WeightedEdge<T>, Integer> traversalByEdge, final T startingNode) {
        return from(graphSupplier.get(), traversalByEdge, startingNode);
    }

    private EulerianTrail() {}

}