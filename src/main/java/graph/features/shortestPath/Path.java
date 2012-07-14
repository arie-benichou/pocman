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

package graph.features.shortestPath;

import graph.WeightedEdge;

import java.util.List;

import com.google.common.base.Joiner;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableList.Builder;

// TODO WeightedEdgeInterface
// TODO implémenter WeightedEdgeInterface
final class Path<T> implements PathInterface<T>, Comparable<Path<T>> {

    private final List<WeightedEdge<T>> edges;
    private final int numberOfEdges;
    private final double cost;
    private final WeightedEdge<T> lastEdge;
    private final int hashCode;

    public static <T> Path<T> from(final WeightedEdge<T> edge) {
        return new Path<T>(edge);
    }

    public static <T> Path<T> from(final T endPoint1, final T endPoint2, final double weight) {
        return from(WeightedEdge.from(endPoint1, endPoint2, weight));
    }

    private static int hashCode(final int hashCode1, final int hashCode2) {
        return (17 + hashCode1 * hashCode2) * (hashCode1 + hashCode2);
    }

    private Path(final WeightedEdge<T> edge) {
        Preconditions.checkNotNull(edge);
        this.edges = ImmutableList.of(edge);
        this.numberOfEdges = 1;
        this.cost = edge.getWeight();
        this.lastEdge = edge;
        this.hashCode = edge.hashCode();
    }

    private Path(final List<WeightedEdge<T>> edges, final double cost, final WeightedEdge<T> lastEdge) {
        this.edges = edges;
        this.numberOfEdges = edges.size();
        this.cost = cost;
        this.lastEdge = lastEdge;
        final T endPoint1 = edges.get(0).getEndPoint1();
        final T endPoint2 = lastEdge.getEndPoint2();
        this.hashCode = hashCode(endPoint1.hashCode(), endPoint2.hashCode());
    }

    // TODO à virer
    public boolean isNull() {
        return this.getNumberOfEdges() == 0;
    }

    @Override
    public T getEndPoint1() {
        if (this.isNull()) return null;
        return this.getEdges().get(0).getEndPoint1();
    }

    @Override
    public T getEndPoint2() {
        if (this.isNull()) return null;
        return this.getLastEdge().getEndPoint2();
    }

    @Override
    public double getWeight() {// TODO retourner un Double
        return this.cost;
    }

    @Override
    public int compareTo(final Path<T> that) {
        if (this.getWeight() < that.getWeight()) return -1;
        if (that.getWeight() < this.getWeight()) return 1;
        return 0;
    }

    @Override
    public int getNumberOfEdges() {
        return this.numberOfEdges;
    }

    // TODO retourner une liste immutable uniquement à ce moment ?
    @Override
    public List<WeightedEdge<T>> getEdges() {
        return this.edges;
    }

    @Override
    public WeightedEdge<T> getLastEdge() {
        return this.lastEdge;
    }

    @Override
    public Path<T> reverse() {
        if (this.isNull()) return this;
        final Builder<WeightedEdge<T>> builder = new ImmutableList.Builder<WeightedEdge<T>>();
        for (int i = this.getNumberOfEdges() - 1; i > -1; --i) {
            final WeightedEdge<T> edge = this.getEdges().get(i);
            builder.add(edge.reverse());
        }
        return new Path<T>(builder.build(), this.getWeight(), this.getEdges().get(0).reverse());
    }

    /* (non-Javadoc)
     * @see graph.features.shortestPath.PI#add(graph.WeightedEdge)
     */
    @Override
    public Path<T> add(final WeightedEdge<T> edge) {
        Preconditions.checkArgument(edge != null);
        final int n = this.getNumberOfEdges();
        switch (n) {
            case 0:
                return new Path<T>(ImmutableList.of(edge), this.getWeight() + edge.getWeight(), edge);
            case 1: {
                WeightedEdge<T> lastEdge = this.getLastEdge();
                WeightedEdge<T> newEdge = null;
                if (lastEdge.getEndPoint2().equals(edge.getEndPoint1())) newEdge = edge;
                else if (lastEdge.getEndPoint2().equals(edge.getEndPoint2())) newEdge = edge.reverse();
                else if (lastEdge.getEndPoint1().equals(edge.getEndPoint1())) {
                    lastEdge = lastEdge.reverse();
                    newEdge = edge;
                }
                else if (lastEdge.getEndPoint1().equals(edge.getEndPoint2())) {
                    lastEdge = lastEdge.reverse();
                    newEdge = edge.reverse();
                }
                Preconditions.checkState(newEdge != null, "invalid path" + edge);
                return new Path<T>(ImmutableList.of(lastEdge, newEdge), this.getWeight() + newEdge.getWeight(), newEdge);
            }
            default: {
                final WeightedEdge<T> lastEdge = this.getLastEdge();
                WeightedEdge<T> newEdge = null;
                if (lastEdge.getEndPoint2().equals(edge.getEndPoint1())) newEdge = edge;
                else if (lastEdge.getEndPoint2().equals(edge.getEndPoint2())) newEdge = edge.reverse();
                else if (this.getEndPoint1().equals(edge.getEndPoint1())) return this.reverse().add(edge); // TODO simplifier le cas général
                else if (this.getEndPoint1().equals(edge.getEndPoint2())) return this.reverse().add(edge); // TODO simplifier le cas général
                Preconditions.checkState(newEdge != null, "invalid path");
                final Builder<WeightedEdge<T>> builder = new ImmutableList.Builder<WeightedEdge<T>>();
                for (int i = 0; i < n - 1; ++i)
                    builder.add(this.getEdges().get(i));
                builder.add(lastEdge);
                builder.add(newEdge);
                return new Path<T>(builder.build(), this.getWeight() + newEdge.getWeight(), newEdge);
            }
        }
    }

    public Path<T> add(final Path<T> path) {
        return (Path<T>) this.add((PathInterface<T>) path);
    }

    @Override
    public PathInterface<T> add(final PathInterface<T> path) {

        Preconditions.checkArgument(path != null);

        if (this.isNull())
            return new Path<T>(path.getEdges(), path.getWeight() + this.getWeight(), path.getLastEdge());

        if (path.getNumberOfEdges() == 1) return this.add(path.getEdges().get(0));
        if (this.getNumberOfEdges() == 1) return path.add(this.getEdges().get(0));

        final T endpoint1 = this.getEdges().get(0).getEndPoint1(); // TODO getFirstEdge()
        final T endpoint2 = this.getLastEdge().getEndPoint2();

        final T potentialEndpoint1 = path.getEdges().get(0).getEndPoint1();
        if (endpoint2.equals(potentialEndpoint1)) {
            final Builder<WeightedEdge<T>> builder = new ImmutableList.Builder<WeightedEdge<T>>();
            builder.addAll(this.getEdges()).addAll(path.getEdges());
            return new Path<T>(builder.build(), this.getWeight() + path.getWeight(), path.getLastEdge());
        }

        final T potentialEndpoint2 = path.getLastEdge().getEndPoint2();

        if (endpoint2.equals(potentialEndpoint2)) {
            // TODO ? effectuer le reverse sur sur le path ayant le moins d'edges
            return this.add(path.reverse());
        }

        if (endpoint1.equals(potentialEndpoint2)) {
            // TODO ? effectuer le reverse sur sur le path ayant le moins d'edges
            //return this.add(path.reverse());
            return this.reverse().add(path);
        }

        if (endpoint1.equals(potentialEndpoint1)) {
            // TODO ? effectuer le reverse sur sur le path ayant le moins d'edges
            return this.reverse().add(path);
        }

        throw new IllegalStateException("invalid path" + "\nAdding: " + path + "\nTo: " + this + "\n"); // TODO

    }

    @SuppressWarnings("unchecked")
    // TODO à revoir
    @Override
    public boolean equals(final Object object) {
        if (object == null) return false;
        if (object == this) return true;
        if (!(object instanceof Path)) return false;
        final Path<T> that = (Path<T>) object;
        return that.hashCode == this.hashCode;
    }

    @Override
    public String toString() {
        if (this.isNull()) return "NULL PATH";
        return "\n * " + Joiner.on("\n * ").join(this.getEdges()) + "\n Cost = " + this.getWeight() + "$";
    }

}