
package fr.designpattern.pocman.cpp.graph;

import com.google.common.base.Preconditions;

public final class WeightedEdge<T> implements Comparable<WeightedEdge<T>> {

    private final T endPoint1;
    private final T endPoint2;
    private final double weight;
    private final int hashCode;

    public final static class Factory<T> {

        public WeightedEdge<T> newEdge(final T endPoint1, final T endPoint2, final double weight) {
            return new WeightedEdge<T>(endPoint1, endPoint2, weight);
        }

        public static int hashCode(final int hashCode1, final int hashCode2) {
            return (17 + hashCode1 * hashCode2) * (hashCode1 + hashCode2);
        }

    }

    private WeightedEdge(final T endPoint1, final T endPoint2, final double weight) {
        Preconditions.checkNotNull(endPoint1);
        Preconditions.checkNotNull(endPoint2);
        Preconditions.checkNotNull(weight);
        Preconditions.checkArgument(!endPoint1.equals(endPoint2));
        this.endPoint1 = endPoint1;
        this.endPoint2 = endPoint2;
        this.weight = weight;
        this.hashCode = Factory.hashCode(endPoint1.hashCode(), endPoint2.hashCode());
    }

    private WeightedEdge(final WeightedEdge<T> edge) {
        this(edge.getEndPoint2(), edge.getEndPoint1(), edge.getWeight());
    }

    public T getEndPoint1() {
        return this.endPoint1;
    }

    public T getEndPoint2() {
        return this.endPoint2;
    }

    public double getWeight() {
        return this.weight;
    }

    @Override
    public int hashCode() {
        return this.hashCode;
    }

    public WeightedEdge<T> getSymetric() {
        return new WeightedEdge<T>(this);
    }

    @Override
    public boolean equals(final Object object) {
        if (object == null) return false;
        if (object == this) return true;
        if (!(object instanceof WeightedEdge)) return false;
        @SuppressWarnings("unchecked")
        final WeightedEdge<T> that = (WeightedEdge<T>) object;
        return this.hashCode() == that.hashCode() && this.getWeight() == that.getWeight();
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append(this.getWeight() + "$");
        sb.append(" (");
        sb.append(this.getEndPoint1());
        sb.append(", ");
        sb.append(this.getEndPoint2());
        sb.append(")");
        return sb.toString();
    }

    public static void main(final String[] args) {

        final Factory<Integer> factory = new WeightedEdge.Factory<Integer>();

        final WeightedEdge<Integer> edge1 = factory.newEdge(1, 2, 1.0);
        final WeightedEdge<Integer> edge2 = factory.newEdge(1, 3, 1.0);
        final WeightedEdge<Integer> edge3 = factory.newEdge(1, 4, 1.0);
        final WeightedEdge<Integer> edge4 = factory.newEdge(4, 1, 1.0);
        final WeightedEdge<Integer> edge5 = factory.newEdge(4, 1, 2.0);
        final WeightedEdge<Integer> edge6 = factory.newEdge(1, 0, 1.0);
        final WeightedEdge<Integer> edge7 = factory.newEdge(0, 1, 1.0);

        System.out.println(edge1.equals(edge2));
        System.out.println(edge2.equals(edge1));
        System.out.println(edge1.equals(edge3));
        System.out.println(edge3.equals(edge4));
        System.out.println(edge4.equals(edge5));
        System.out.println(edge6.equals(edge7));

        System.out.println(edge1);
        System.out.println(edge1.getSymetric());
        System.out.println(edge1.getSymetric().getSymetric());

    }

    @Override
    public int compareTo(final WeightedEdge<T> that) {
        return Double.compare(this.weight, that.getWeight());
    }

}