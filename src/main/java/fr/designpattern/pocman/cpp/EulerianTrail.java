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

package fr.designpattern.pocman.cpp;

import java.util.List;
import java.util.Map;

import com.google.common.base.Preconditions;
import com.google.common.base.Supplier;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableList.Builder;
import com.google.common.collect.Maps;

import fr.designpattern.pocman.graph.UndirectedGraph;
import fr.designpattern.pocman.graph.WeightedEdge;

public final class EulerianTrail {

    /**
     * Fleury algorithm
     */
    private static <T> List<T> from(final T startingVertex, final Map<WeightedEdge<T>, Integer> traversalByEdge, final UndirectedGraph<T> graph) {
        final Builder<T> trailBuilder = new ImmutableList.Builder<T>();
        for (final WeightedEdge<T> edge : graph.getEdges(startingVertex)) {
            final Integer integer = traversalByEdge.get(edge);
            if (integer > 0) {
                traversalByEdge.put(edge, integer - 1);
                T nextVertex = null;
                if (startingVertex.equals(edge.getEndPoint1())) nextVertex = edge.getEndPoint2();
                else if (startingVertex.equals(edge.getEndPoint2())) nextVertex = edge.getEndPoint1();
                Preconditions.checkState(nextVertex != null);
                trailBuilder.addAll(EulerianTrail.from(nextVertex, traversalByEdge, graph));
            }
        }
        trailBuilder.add(startingVertex);
        return trailBuilder.build();
    }

    public static <T> List<T> from(final UndirectedGraph<T> graph, final Map<WeightedEdge<T>, Integer> traversalByEdge, final T startingVertex) {
        return from(startingVertex, Maps.newHashMap(traversalByEdge), graph);
    }

    public static <T> List<T> from(final Supplier<UndirectedGraph<T>> graphSupplier, final Map<WeightedEdge<T>, Integer> traversalByEdge, final T startingVertex) {
        return from(graphSupplier.get(), traversalByEdge, startingVertex);
    }

    private EulerianTrail() {}

    /*
    public static List<Vertex> from(final Maze maze, final Map<WeightedEdge<Node<Vertex>>, Integer> traversalByEdge, final Vertex node) {
        final Builder<Vertex> trailBuilder = new ImmutableList.Builder<Vertex>();
        for (final WeightedEdge<Vertex> edge : maze.get().getEdges(startingVertex)) {
            final Integer integer = traversalByEdge.get(edge);
            if (integer > 0) {
                traversalByEdge.put(edge, integer - 1);
                T nextVertex = null;
                if (startingVertex.equals(edge.getEndPoint1())) nextVertex = edge.getEndPoint2();
                else if (startingVertex.equals(edge.getEndPoint2())) nextVertex = edge.getEndPoint1();
                Preconditions.checkState(nextVertex != null);
                trailBuilder.addAll(EulerianTrail.from(nextVertex, traversalByEdge, graph));
            }
        }
        trailBuilder.add(startingVertex);
        return trailBuilder.build();
    }

    public static List<Vertex> from(UndirectedGraph<Node<Vertex>> graph, Map<WeightedEdge<Node<Vertex>>, Integer> traversalByEdge, Vertex node) {
        // TODO Auto-generated method stub
        return null;
    }
    */

}