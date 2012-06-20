
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
        Preconditions.checkArgument(endPoint1 != null);
        Preconditions.checkArgument(endPoint2 != null);
        Preconditions.checkArgument(!endPoint1.equals(endPoint2));
        Preconditions.checkArgument(weight >= 0.0);
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
    public int compareTo(final WeightedEdge<T> that) {
        return Double.compare(this.weight, that.getWeight());
    }

    public WeightedEdge<T> getSymetric() {
        return new WeightedEdge<T>(this);
    }

    @Override
    public int hashCode() {
        return this.hashCode;
    }

    @Override
    public boolean equals(final Object object) {
        if (object == null) return false;
        if (object == this) return true;
        if (!(object instanceof WeightedEdge)) return false;
        final Object endPoint = ((WeightedEdge<?>) object).getEndPoint1();
        if (!endPoint.getClass().equals(this.getEndPoint1().getClass())) return false;
        final WeightedEdge<?> that = (WeightedEdge<?>) object;
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

}