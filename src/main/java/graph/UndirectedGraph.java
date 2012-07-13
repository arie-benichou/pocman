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

package graph;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.NoSuchElementException;
import java.util.Set;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.ImmutableSortedMap;
import com.google.common.collect.Maps;
import com.google.common.collect.Ordering;
import com.google.common.collect.Sets;

// TODO ? vertex x = null edge of (x,x)
public final class UndirectedGraph<T> implements UndirectedGraphInterface<T> {

    public final static boolean USER_MODE = true;
    public final static boolean SUPERVISER_MODE = false;

    public final static class Builder<T> {

        private final int order;
        private final Map<T, Integer> idByEndpoint;
        private final Map<T, Set<T>> connectedEndPointsByEndPoint;
        private final Set<WeightedEdge<T>> edges;
        private final ImmutableSortedMap.Builder<Integer, WeightedEdge<T>> edgeByHashCodeBuilder;
        private final boolean isInUserMode;

        private int id;

        public Builder(final int order, final boolean isInUserMode) {
            Preconditions.checkArgument(order >= 2, order);
            this.order = order;
            this.idByEndpoint = Maps.newHashMap();
            this.edges = Sets.newHashSet();
            this.connectedEndPointsByEndPoint = Maps.newHashMap();
            this.edgeByHashCodeBuilder = new ImmutableSortedMap.Builder<Integer, WeightedEdge<T>>(Ordering.natural());
            this.isInUserMode = isInUserMode;
            this.id = 0;
        }

        public Builder(final int order) {
            this(order, true);
        }

        public int getOrder() {
            return this.order;
        }

        public boolean contains(final WeightedEdge<T> edge) {
            return this.edges.contains(edge);
        }

        private Integer getId(final T endPoint) {
            return this.idByEndpoint.get(endPoint);
        }

        private T checkEndPoint(final T endPoint) {
            if (this.getId(endPoint) == null) {
                Preconditions.checkState(this.id != this.order, "Maximal number of vertices (" + this.order + ") reached.");
                this.idByEndpoint.put(endPoint, ++this.id);
                this.connectedEndPointsByEndPoint.put(endPoint, new HashSet<T>());
            }
            return endPoint;
        }

        private int hashCode(final WeightedEdge<T> edge) {
            return this.getOrder() * this.getId(edge.getEndPoint1()) + this.getId(edge.getEndPoint2());
        }

        public Builder<T> addEdge(final WeightedEdge<T> edge) { // TODO utiliser un objet Arc
            Preconditions.checkArgument(edge != null);
            Preconditions.checkState(!this.contains(edge), "Edge " + edge + " is already defined.");
            final T endPoint1 = this.checkEndPoint(edge.getEndPoint1());
            final T endPoint2 = this.checkEndPoint(edge.getEndPoint2());
            this.edges.add(edge);
            this.connectedEndPointsByEndPoint.get(endPoint1).add(endPoint2);
            this.connectedEndPointsByEndPoint.get(endPoint2).add(endPoint1);
            this.edgeByHashCodeBuilder.put(this.hashCode(edge), edge);
            final WeightedEdge<T> symetric = edge.reverse();
            this.edgeByHashCodeBuilder.put(this.hashCode(symetric), symetric);
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

    private final Map<T, Integer> idByEndpoint;
    private final Map<Integer, T> endPointById;

    private final Map<T, Set<WeightedEdge<T>>> edgesByEndpoint;
    private final Map<T, Set<T>> connectedEndPointsByEndPoint;

    private final Set<WeightedEdge<T>> edgeSet;
    private final Map<Integer, WeightedEdge<T>> edgeByHashCode;

    private final boolean isInUserMode;

    private UndirectedGraph(final Builder<T> builder) {

        this.order = builder.order;

        this.idByEndpoint = ImmutableMap.copyOf(builder.idByEndpoint);

        final ImmutableSortedMap.Builder<Integer, T> endPointByIdBuilder = new ImmutableSortedMap.Builder<Integer, T>(Ordering.natural());
        for (final Entry<T, Integer> entry : this.idByEndpoint.entrySet())
            endPointByIdBuilder.put(entry.getValue(), entry.getKey());
        this.endPointById = endPointByIdBuilder.build();

        this.edgeSet = ImmutableSet.copyOf(builder.edges);

        this.edgeByHashCode = builder.edgeByHashCodeBuilder.build();

        final ImmutableMap.Builder<T, Set<T>> connectedEndPointsByEndPointBuilder = new ImmutableMap.Builder<T, Set<T>>();
        for (final Entry<T, Set<T>> entry : builder.connectedEndPointsByEndPoint.entrySet())
            connectedEndPointsByEndPointBuilder.put(entry.getKey(), ImmutableSet.copyOf(entry.getValue()));
        this.connectedEndPointsByEndPoint = connectedEndPointsByEndPointBuilder.build();

        this.edgesByEndpoint = Maps.newHashMap();

        this.isInUserMode = builder.isInUserMode;
    }

    @Override
    public int getOrder() {
        return this.order;
    }

    @Override
    public boolean hasEndPoint(final T endPoint) {
        Preconditions.checkArgument(endPoint != null);
        return this.idByEndpoint.containsKey(endPoint);
    }

    private T checkEndPoint(final T endPoint) {
        if (this.isInUserMode)
            if (!this.hasEndPoint(endPoint)) throw new NoSuchElementException("Node " + endPoint + "does not exist.");
        return endPoint;
    }

    private Integer getId(final T endPoint) {
        return this.idByEndpoint.get(endPoint);
    }

    @Override
    public Integer getOrdinal(final T endPoint) {
        return this.getId(this.checkEndPoint(endPoint)) - 1;
    }

    @Override
    public T get(final int ordinal) {
        Preconditions.checkArgument(ordinal > -1 && ordinal < this.getOrder());
        return this.endPointById.get(ordinal + 1);
    }

    private Integer hashCode(final T endPoint1, final T endPoint2) {
        return this.getOrder() * this.getId(endPoint1) + this.getId(endPoint2);
    }

    @Override
    public Set<T> getConnectedEndPoints(final T endPoint) {
        return this.connectedEndPointsByEndPoint.get(this.checkEndPoint(endPoint));
    }

    @Override
    public boolean hasEdge(final T endPoint1, final T endPoint2) {
        return this.edgeByHashCode.containsKey(this.hashCode(this.checkEndPoint(endPoint1), this.checkEndPoint(endPoint2)));
    }

    @Override
    public WeightedEdge<T> getEdge(final T endPoint1, final T endPoint2) {
        final WeightedEdge<T> edge = this.edgeByHashCode.get(this.hashCode(this.checkEndPoint(endPoint1), this.checkEndPoint(endPoint2)));
        if (this.isInUserMode) Preconditions.checkState(edge != null, "Edge (" + endPoint1 + " - " + endPoint2 + ") does not exist.");
        return edge;
    }

    @Override
    public Set<WeightedEdge<T>> getEdgesFrom(final T endPoint1) {
        Set<WeightedEdge<T>> edges = this.edgesByEndpoint.get(this.checkEndPoint(endPoint1));
        if (edges == null) {
            final ImmutableSet.Builder<WeightedEdge<T>> builder = new ImmutableSet.Builder<WeightedEdge<T>>();
            for (final T endPoint2 : this.getConnectedEndPoints(endPoint1))
                builder.add(this.getEdge(endPoint1, endPoint2));
            edges = builder.build();
            this.edgesByEndpoint.put(endPoint1, edges);
        }
        return edges;
    }

    @Override
    public Set<WeightedEdge<T>> getSetOfEdges() {
        return this.edgeSet;
    }

    @Override
    public Iterator<T> iterator() {
        return this.endPointById.values().iterator();
    }

    private final Map<Feature, Object> features = Maps.newConcurrentMap();

    @SuppressWarnings("unchecked")
    public <F> F getFeature(final Feature feature) { // TODO !! à revoir (retourner une Function<T> et aplly() retourne la feature ?)
        Object object = this.features.get(feature);
        if (object == null) {
            object = feature.on(this);
            this.features.put(feature, object);
        }
        return (F) object;
    }

}