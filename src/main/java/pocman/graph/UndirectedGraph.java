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

package pocman.graph;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.NoSuchElementException;
import java.util.Set;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

// TODO ? vertex x = null edge of (x,x)
public final class UndirectedGraph<T> implements UndirectedGraphInterface<T> {

    public final static class Builder<T> {

        private final int order;
        private final Map<T, Integer> endPoints;
        private final Map<T, List<WeightedEdge<T>>> edgesByEndPoint;
        private final Map<T, Set<T>> connectedEndPointsByEndPoint;
        private final Set<WeightedEdge<T>> edges;

        private int ordinal;

        public Builder(final int order) {
            Preconditions.checkArgument(order >= 2, order);
            this.order = order;
            this.endPoints = Maps.newHashMap();
            this.edges = Sets.newHashSet();
            this.connectedEndPointsByEndPoint = Maps.newHashMap();
            this.edgesByEndPoint = Maps.newHashMap();
            this.ordinal = 0;
        }

        public int getOrder() {
            return this.order;
        }

        public boolean contains(final WeightedEdge<T> edge) {
            return this.edges.contains(edge);
        }

        private T checkEndPoint(final T endPoint) {
            if (this.endPoints.get(endPoint) == null) {
                Preconditions.checkState(this.ordinal != this.order, "Maximal number of vertices (" + this.order + ") reached.");
                this.endPoints.put(endPoint, ++this.ordinal);
                this.connectedEndPointsByEndPoint.put(endPoint, new HashSet<T>());
                this.edgesByEndPoint.put(endPoint, new ArrayList<WeightedEdge<T>>());
            }
            return endPoint;
        }

        public Builder<T> addEdge(final WeightedEdge<T> edge) {
            Preconditions.checkArgument(edge != null);
            Preconditions.checkState(!this.contains(edge), "Edge " + edge + " is already defined.");
            final T endPoint1 = this.checkEndPoint(edge.getEndPoint1());
            final T endPoint2 = this.checkEndPoint(edge.getEndPoint2());
            this.edges.add(edge);
            this.edgesByEndPoint.get(endPoint1).add(edge);
            this.connectedEndPointsByEndPoint.get(endPoint1).add(endPoint2);
            this.edgesByEndPoint.get(endPoint2).add(edge);
            this.connectedEndPointsByEndPoint.get(endPoint2).add(endPoint1);
            return this;
        }

        public Builder<T> addEdge(final T endPoint1, final T endPoint2, final double weight) {
            return this.addEdge(WeightedEdge.from(endPoint1, endPoint2, weight));
        }

        public UndirectedGraph<T> build() {
            final int order = this.connectedEndPointsByEndPoint.size();
            Preconditions
                    .checkState(order == this.order, "Declared number of vertices: " + this.order + " does not match number of defined vertices: " + order);
            return new UndirectedGraph<T>(this);
        }

    }

    private final int order;
    public final Map<T, Integer> endPoints;
    private final Map<T, Set<WeightedEdge<T>>> edgesByEndpoint;
    private final Map<T, Set<T>> connectedEndPointsByEndPoint;
    private final Set<WeightedEdge<T>> edgeSet;

    private UndirectedGraph(final Builder<T> graphBuilder) {
        this.order = graphBuilder.order;
        this.endPoints = ImmutableMap.copyOf(graphBuilder.endPoints);
        this.edgeSet = ImmutableSet.copyOf(graphBuilder.edges);
        final ImmutableMap.Builder<T, Set<T>> connectedEndPointsByEndPointBuilder = new ImmutableMap.Builder<T, Set<T>>();
        for (final Entry<T, Set<T>> entry : graphBuilder.connectedEndPointsByEndPoint.entrySet())
            connectedEndPointsByEndPointBuilder.put(entry.getKey(), ImmutableSet.copyOf(entry.getValue()));
        this.connectedEndPointsByEndPoint = connectedEndPointsByEndPointBuilder.build();
        final ImmutableMap.Builder<T, Set<WeightedEdge<T>>> edgesByEndpointBuilder = new ImmutableMap.Builder<T, Set<WeightedEdge<T>>>();
        for (final Entry<T, List<WeightedEdge<T>>> entry : graphBuilder.edgesByEndPoint.entrySet())
            edgesByEndpointBuilder.put(entry.getKey(), ImmutableSet.copyOf(entry.getValue()));
        this.edgesByEndpoint = edgesByEndpointBuilder.build();
    }

    @Override
    public int getOrder() {
        return this.order;
    }

    @Override
    public boolean hasEndPoint(final T endPoint) {
        Preconditions.checkArgument(endPoint != null);
        return this.endPoints.containsKey(endPoint);
    }

    private void checkEndPoint(final T endPoint) {
        if (!this.hasEndPoint(endPoint)) throw new NoSuchElementException("Node " + endPoint + "does not exist.");
    }

    @Override
    public boolean hasEdge(final T endPoint1, final T endPoint2) {
        this.checkEndPoint(endPoint1);
        this.checkEndPoint(endPoint2);
        return this.connectedEndPointsByEndPoint.get(endPoint1).contains(endPoint2);
    }

    @Override
    public Set<T> getConnectedEndPoints(final T endPoint) {
        this.checkEndPoint(endPoint);
        return this.connectedEndPointsByEndPoint.get(endPoint);
    }

    @Override
    public Set<WeightedEdge<T>> getEdgesFrom(final T endPoint) {
        this.checkEndPoint(endPoint);
        return this.edgesByEndpoint.get(endPoint);
    }

    @Override
    public Set<T> getSetOfEndPoints() {
        return this.endPoints.keySet();
    }

    @Override
    public Set<WeightedEdge<T>> getSetOfEdges() {
        return this.edgeSet;
    }

    @Override
    public WeightedEdge<T> getEdge(final T endPoint1, final T endPoint2) {
        this.checkEndPoint(endPoint1);
        this.checkEndPoint(endPoint2);
        // TODO ? générer un hash à partir de l'ordinal et stocker les edges par hash
        final Set<WeightedEdge<T>> edges = this.getEdgesFrom(endPoint1);
        WeightedEdge<T> edge = null;
        for (final WeightedEdge<T> weightedEdge : edges) {
            if (weightedEdge.getEndPoint1().equals(endPoint2)) {
                edge = weightedEdge;
                break;
            }
            if (weightedEdge.getEndPoint2().equals(endPoint2)) {
                edge = weightedEdge;
                break;
            }
        }
        return edge;
    }

    @Override
    public Iterator<T> iterator() {
        return this.endPoints.keySet().iterator();
    }

    private final Map<Feature, Object> features = Maps.newConcurrentMap();

    @SuppressWarnings("unchecked")
    public <X> X getFeature(final Feature feature) {
        Object object = this.features.get(feature);
        if (object == null) {
            object = feature.on(this);
            this.features.put(feature, object);
        }
        return (X) object;
    }

}