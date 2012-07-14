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

package graph.features.eulerianTrail;

import graph.UndirectedGraph;
import graph.WeightedEdge;
import graph.features.degree.DegreeFeature;
import graph.features.degree.DegreeInterface;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableList.Builder;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

final class EulerianTrail<T> implements EulerianTrailInterface<T> {

    private final UndirectedGraph<T> graph;

    public static <T> EulerianTrail<T> from(final UndirectedGraph<T> graph) {
        return new EulerianTrail<T>(graph);
    }

    private EulerianTrail(final UndirectedGraph<T> graph) {
        this.graph = graph;
    }

    public UndirectedGraph<T> getGraph() {
        return this.graph;
    }

    private static class Data<N> implements Comparable<Data<N>> {

        private final WeightedEdge<N> edge;
        private final int degree;

        public Data(final WeightedEdge<N> edge, final int degree) {
            this.edge = edge;
            this.degree = degree;
        }

        public int _compareTo(final Data<N> that) {
            final int compare = this.degree - that.degree;
            if (compare < 0) return -1;
            if (compare > 0) return 1;
            return Double.compare(this.edge.getWeight(), that.edge.getWeight());
        }

        @Override
        public int compareTo(final Data<N> that) {
            return -this._compareTo(that);
        }
    }

    /*
    private List<T> computeEulerianTrail(final T startingNode, final Map<WeightedEdge<T>, Integer> traversalByEdge) {
        final Builder<T> trailBuilder = new ImmutableList.Builder<T>();
        final Set<WeightedEdge<T>> edges = this.getGraph().getEdgesFrom(startingNode);
        for (final WeightedEdge<T> edge : edges) {
            final Integer integer = traversalByEdge.get(edge);
            if (integer > 0) {
                traversalByEdge.put(edge, integer - 1);
                trailBuilder.addAll(this.computeEulerianTrail(edge.getEndPoint2(), traversalByEdge));
            }
        }
        trailBuilder.add(startingNode);
        return trailBuilder.build();
    }
    */

    // TODO tailrec
    private List<T> computeEulerianTrail(final T startingNode, final Map<WeightedEdge<T>, Integer> traversalByEdge, final Map<T, Integer> degreeByNode) {

        final Builder<T> trailBuilder = new ImmutableList.Builder<T>();

        final List<Data<T>> nextNodes = Lists.newArrayList();
        for (final WeightedEdge<T> edge : this.getGraph().getEdgesFrom(startingNode)) {
            final Integer integer = traversalByEdge.get(edge);
            if (integer > 0) nextNodes.add(new Data<T>(edge, degreeByNode.get(edge.getEndPoint2())));
        }
        Collections.sort(nextNodes);
        for (final Data<T> data : nextNodes) {
            final Integer integer = traversalByEdge.get(data.edge);
            if (integer > 0) {
                traversalByEdge.put(data.edge, integer - 1);
                trailBuilder.addAll(this.computeEulerianTrail(data.edge.getEndPoint2(), traversalByEdge, degreeByNode));
            }
        }
        trailBuilder.add(startingNode);

        return trailBuilder.build();
    }

    @Override
    // TODO !? directed graph
    public List<T> getEulerianTrail(final T startingNode, final Map<WeightedEdge<T>, Integer> traversalByEdge) {
        final DegreeInterface<T> degreeInterface = this.getGraph().fetch(DegreeFeature.class).up();
        return this.computeEulerianTrail(startingNode, Maps.newHashMap(traversalByEdge), degreeInterface.getDegreeByNode());
    }

}