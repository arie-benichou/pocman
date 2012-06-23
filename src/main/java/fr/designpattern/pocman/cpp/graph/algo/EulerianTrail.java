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
import fr.designpattern.pocman.cpp.graph.Vertex;
import fr.designpattern.pocman.cpp.graph.WeightedEdge;

public final class EulerianTrail {

    /**
     * Fleury algorithm
     */
    public static void apply(final Vertex startingVertex, final UndirectedGraph<Vertex> eulerianGraph, final Map<WeightedEdge<Vertex>, Integer> map,
            final List<Vertex> trail) {
        //TODO ! vérifier que le graphe est eulerien
        final List<WeightedEdge<Vertex>> edges = eulerianGraph.getEdges(startingVertex);
        for (final WeightedEdge<Vertex> edge : edges) {
            final Integer integer = map.get(edge);
            if (integer < 1) continue;
            map.put(edge, integer - 1);
            Vertex nextVertex;
            if (edge.getEndPoint1().equals(startingVertex)) nextVertex = edge.getEndPoint2();
            else if (edge.getEndPoint2().equals(startingVertex)) nextVertex = edge.getEndPoint1();
            else throw new RuntimeException(edge.toString());

            apply(nextVertex, eulerianGraph, map, trail);
        }
        trail.add(startingVertex);
    }

    private EulerianTrail() {}

}