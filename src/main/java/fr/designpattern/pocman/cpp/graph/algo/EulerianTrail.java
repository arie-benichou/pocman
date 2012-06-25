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

import fr.designpattern.pocman.cpp.graph.UndirectedGraph;
import fr.designpattern.pocman.cpp.graph.WeightedEdge;

public final class EulerianTrail {

    /**
     * Fleury algorithm
     */
    public static <T> void apply(final T startingVertex, final UndirectedGraph<T> eulerianGraph, final Map<WeightedEdge<T>, Integer> map, final List<T> trail) {
        // TODO passer un graphe et retourner une liste
        //TODO ! vérifier que le graphe est eulerien
        //Preconditions.checkArgument(eulerianGraph.isEulerian());

        final List<WeightedEdge<T>> edges = eulerianGraph.getEdges(startingVertex);
        for (final WeightedEdge<T> edge : edges) {
            final Integer integer = map.get(edge);
            if (integer < 1) continue;
            map.put(edge, integer - 1);
            T nextVertex;
            if (edge.getEndPoint1().equals(startingVertex)) nextVertex = edge.getEndPoint2();
            else if (edge.getEndPoint2().equals(startingVertex)) nextVertex = edge.getEndPoint1();
            else throw new RuntimeException(edge.toString()); // TODO
            apply(nextVertex, eulerianGraph, map, trail);
        }
        trail.add(startingVertex);
    }

    private EulerianTrail() {}

}