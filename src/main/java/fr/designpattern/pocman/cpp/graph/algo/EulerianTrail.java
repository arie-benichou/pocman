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

package fr.designpattern.pocman.cpp.graph.algo;

import java.util.List;
import java.util.Map;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableList.Builder;
import com.google.common.collect.Maps;

import fr.designpattern.pocman.cpp.graph.Solution;
import fr.designpattern.pocman.cpp.graph.UndirectedGraph;
import fr.designpattern.pocman.cpp.graph.WeightedEdge;

public final class EulerianTrail {

    /**
     * Fleury algorithm
     */
    private static <T> List<T> from(final T startingVertex, final UndirectedGraph<T> graph, final Map<WeightedEdge<T>, Integer> traversalByEdge) {
        final Builder<T> trailBuilder = new ImmutableList.Builder<T>();
        for (final WeightedEdge<T> edge : graph.getEdges(startingVertex)) {
            final Integer integer = traversalByEdge.get(edge);
            if (integer > 0) {
                traversalByEdge.put(edge, integer - 1);
                T nextVertex = null;
                if (startingVertex.equals(edge.getEndPoint1())) nextVertex = edge.getEndPoint2();
                else if (startingVertex.equals(edge.getEndPoint2())) nextVertex = edge.getEndPoint1();
                Preconditions.checkState(nextVertex != null);
                trailBuilder.addAll(EulerianTrail.from(nextVertex, graph, traversalByEdge));
            }
        }
        trailBuilder.add(startingVertex);
        return trailBuilder.build();
    }

    public static <T> List<T> from(final Solution<T> solution) {
        return EulerianTrail.from(solution.getStartingVertex(), solution.getGraph(), Maps.newHashMap(solution.getTraversalByEdge()));
    }

    private EulerianTrail() {}

}