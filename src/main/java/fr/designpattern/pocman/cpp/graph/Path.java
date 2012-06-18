
package fr.designpattern.pocman.cpp.graph;

import java.util.List;

import com.google.common.base.Joiner;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableList.Builder;
import com.google.common.collect.Lists;

public final class Path<T> implements Comparable<Path<T>> {

    private final List<WeightedEdge<T>> edges;
    private final int numberOfEdges;
    private final double cost;
    private final WeightedEdge<T> lastEdge;
    private final int hashCode;

    public final static class Factory<T> {

        public Path<T> newPath(final double cost) {
            return new Path<T>(cost);
        }

        public Path<T> newPath() {
            return this.newPath(0);
        }

        public Path<T> newPath(final WeightedEdge<T> edge) {
            return new Path<T>(edge);
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

    public T getEndPoint1() {
        return this.getEdges().get(0).getEndPoint1(); // TODO check for null
    }

    public T getEndPoint2() {
        return this.getLastEdge().getEndPoint2(); // TODO check for null
    }

    // TODO retourner une liste immutable uniquement à ce moment ?
    public List<WeightedEdge<T>> getEdges() {
        return this.edges;
    }

    public boolean isNull() {
        return this.getNumberOfEdges() == 0;
    }

    public int getNumberOfEdges() {
        return this.numberOfEdges;
    }

    public double getWeight() {
        return this.cost;
    }

    public WeightedEdge<T> getLastEdge() {
        return this.lastEdge;
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
                //System.out.println(this);
                //System.out.println(edge);
                final WeightedEdge<T> lastEdge = this.getLastEdge();
                //System.out.println("lastEdge:" + lastEdge);
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

    public Path<T> reverse() {
        final Builder<WeightedEdge<T>> builder = new ImmutableList.Builder<WeightedEdge<T>>();

        for (int i = this.getNumberOfEdges() - 1; i > -1; --i) {
            final WeightedEdge<T> edge = this.getEdges().get(i);
            builder.add(edge.getSymetric());
        }

        return new Path<T>(builder.build(), this.getWeight(), this.getEdges().get(0).getSymetric());
    }

    public List<T> toSequence() { // TODO ? immutable
        final List<T> sequence = Lists.newArrayList();
        for (final WeightedEdge<T> edge : this.getEdges()) {
            sequence.add(edge.getEndPoint1());
            //sequence.addAll(edge.getBetweenVertices());
        }
        sequence.add(this.getLastEdge().getEndPoint2());
        return sequence;
    }

    public List<T> toSequence(final boolean trim) { // TODO ? immutable
        final List<T> sequence = Lists.newArrayList();
        for (final WeightedEdge<T> edge : this.getEdges()) {
            sequence.add(edge.getEndPoint1());
            //sequence.addAll(edge.getBetweenVertices());
        }
        if (!trim) sequence.add(this.getLastEdge().getEndPoint2());
        else sequence.remove(0);
        return sequence;
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

    @Override
    public int compareTo(final Path<T> that) {
        if (this.getWeight() < that.getWeight()) return -1;
        if (that.getWeight() < this.getWeight()) return 1;
        return 0;
    }

    public static void main(final String[] args) {

        final WeightedEdge.Factory<Integer> edgeFactory = new WeightedEdge.Factory<Integer>();

        final WeightedEdge<Integer> edge1 = edgeFactory.newEdge(1, 4, 1);
        final WeightedEdge<Integer> edge2 = edgeFactory.newEdge(4, 8, 1);
        final WeightedEdge<Integer> edge3 = edgeFactory.newEdge(8, 13, 1);

        final WeightedEdge<Integer> edge4 = edgeFactory.newEdge(13, 15, 1);
        final WeightedEdge<Integer> edge5 = edgeFactory.newEdge(15, 19, 1);
        final WeightedEdge<Integer> edge6 = edgeFactory.newEdge(19, 22, 1);

        final Path.Factory<Integer> pathFactory = new Path.Factory<Integer>();

        Path<Integer> path1;
        Path<Integer> path2;
        Path<Integer> path3;

        path1 = pathFactory.newPath();
        System.out.println(path1);

        path1 = path1.add(edge1.getSymetric());
        System.out.println(path1);

        path1 = path1.add(edge2.getSymetric());
        System.out.println(path1);

        path1 = path1.add(edge3.getSymetric());
        System.out.println(path1);

        path2 = pathFactory.newPath();
        path2 = path2.add(edge4.getSymetric());
        path2 = path2.add(edge5.getSymetric());
        path2 = path2.add(edge6.getSymetric());
        System.out.println(path2);

        path3 = path1.add(path2);
        System.out.println(path3);

        System.out.println(path3.reverse());

    }

}