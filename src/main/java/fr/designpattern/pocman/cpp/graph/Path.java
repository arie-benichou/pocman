
package fr.designpattern.pocman.cpp.graph;

import java.util.List;

import com.google.common.base.Joiner;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableList.Builder;

public final class Path<T> implements Comparable<Path<T>> {

    private final List<WeightedEdge<T>> edges;
    private final int numberOfEdges;
    private final double cost;
    private final WeightedEdge<T> lastEdge;
    private final int hashCode;

    public final static class Factory<T> {

        private final WeightedEdge.Factory<T> edgeFactory = new WeightedEdge.Factory<T>();

        public Path<T> newPath(final double cost) {
            return new Path<T>(cost);
        }

        public Path<T> newPath() {
            return this.newPath(0);
        }

        public Path<T> newPath(final WeightedEdge<T> edge) {
            return new Path<T>(edge);
        }

        public Path<T> newPath(final T endPoint1, final T endPoint2, final double weight) {
            return this.newPath(this.edgeFactory.newEdge(endPoint1, endPoint2, weight));
        }

        private static int hashCode(final int hashCode1, final int hashCode2) {
            return (17 + hashCode1 * hashCode2) * (hashCode1 + hashCode2);
        }

    }

    private Path(final double cost) {
        this.edges = ImmutableList.of();
        this.numberOfEdges = 0;
        this.cost = cost;
        this.lastEdge = null;
        this.hashCode = 0;
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
        this.hashCode = Factory.hashCode(endPoint1.hashCode(), endPoint2.hashCode());
    }

    public boolean isNull() {
        return this.getNumberOfEdges() == 0;
    }

    public T getEndPoint1() {
        if (this.isNull()) return null;
        return this.getEdges().get(0).getEndPoint1();
    }

    public T getEndPoint2() {
        if (this.isNull()) return null;
        return this.getLastEdge().getEndPoint2();
    }

    public double getWeight() {// TODO retourner un Double
        return this.cost;
    }

    @Override
    public int compareTo(final Path<T> that) {
        if (this.getWeight() < that.getWeight()) return -1;
        if (that.getWeight() < this.getWeight()) return 1;
        return 0;
    }

    public int getNumberOfEdges() {
        return this.numberOfEdges;
    }

    // TODO retourner une liste immutable uniquement à ce moment ?
    public List<WeightedEdge<T>> getEdges() {
        return this.edges;
    }

    public WeightedEdge<T> getLastEdge() {
        return this.lastEdge;
    }

    public Path<T> reverse() {
        if (this.isNull()) return this;
        final Builder<WeightedEdge<T>> builder = new ImmutableList.Builder<WeightedEdge<T>>();
        for (int i = this.getNumberOfEdges() - 1; i > -1; --i) {
            final WeightedEdge<T> edge = this.getEdges().get(i);
            builder.add(edge.getSymetric());
        }
        return new Path<T>(builder.build(), this.getWeight(), this.getEdges().get(0).getSymetric());
    }

    @Override
    public int hashCode() {
        return this.hashCode;
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean equals(final Object object) {
        if (object == null) return false;
        if (object == this) return true;
        if (!(object instanceof Path)) return false;
        final Path<T> that = (Path<T>) object;
        return that.hashCode == this.hashCode;
    }

    public Path<T> add(final WeightedEdge<T> edge) {
        Preconditions.checkNotNull(edge);
        final int n = this.getNumberOfEdges();
        switch (n) {
            case 0:
                return new Path<T>(edge);
            case 1: {
                WeightedEdge<T> lastEdge = this.getLastEdge();
                WeightedEdge<T> newEdge = null;
                if (lastEdge.getEndPoint2().equals(edge.getEndPoint1())) newEdge = edge;
                else if (lastEdge.getEndPoint2().equals(edge.getEndPoint2())) newEdge = edge.getSymetric();
                else if (lastEdge.getEndPoint1().equals(edge.getEndPoint1())) {
                    lastEdge = lastEdge.getSymetric();
                    newEdge = edge;
                }
                else if (lastEdge.getEndPoint1().equals(edge.getEndPoint2())) {
                    lastEdge = lastEdge.getSymetric();
                    newEdge = edge.getSymetric();
                }
                Preconditions.checkState(newEdge != null, "invalid path" + edge);
                return new Path<T>(ImmutableList.of(lastEdge, newEdge), this.getWeight() + newEdge.getWeight(), newEdge);
            }
            default: {
                final WeightedEdge<T> lastEdge = this.getLastEdge();
                WeightedEdge<T> newEdge = null;
                if (lastEdge.getEndPoint2().equals(edge.getEndPoint1())) newEdge = edge;
                else if (lastEdge.getEndPoint2().equals(edge.getEndPoint2())) newEdge = edge.getSymetric();
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

        //System.out.println("\nAdding: " + path + "\nTo: " + this + "\n");
        Preconditions.checkNotNull(path);

        if (path.isNull()) return this;
        if (this.isNull()) return path;

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
            // TODO ? effectuer le reverse sur sur le path ayant le moins d'edge
            return this.add(path.reverse());
        }

        if (endpoint1.equals(potentialEndpoint2)) {
            // TODO ? effectuer le reverse sur sur le path ayant le moins d'edge
            //return this.add(path.reverse());
            return this.reverse().add(path);
        }

        if (endpoint1.equals(potentialEndpoint1)) {
            // TODO ? effectuer le reverse sur sur le path ayant le moins d'edge
            return this.reverse().add(path);
        }

        throw new RuntimeException("invalid path" + "\nAdding: " + path + "\nTo: " + this + "\n"); // TODO

    }

    @Override
    public String toString() {
        if (this.isNull()) return "NULL PATH";
        return "\n * " + Joiner.on("\n * ").join(this.getEdges()) + "\n Cost = " + this.getWeight() + "$";
    }

}