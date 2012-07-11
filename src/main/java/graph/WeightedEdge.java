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

import com.google.common.base.Preconditions;

// TODO EdgeInterface
// TODO WeightedEdgeInterface
// TODO implémenter WeightedEdgeInterface
public final class WeightedEdge<T> implements Comparable<WeightedEdge<T>> {

    private final T endPoint1;
    private final T endPoint2;
    private final double weight;

    private final int hashCode;

    public static <T> WeightedEdge<T> from(final T endPoint1, final T endPoint2, final double weight) {
        return new WeightedEdge<T>(endPoint1, endPoint2, weight);
    }

    public static int hashCode(final int hashCode1, final int hashCode2) {
        return (17 + hashCode1 * hashCode2) * (hashCode1 + hashCode2);
    }

    private WeightedEdge(final T endPoint1, final T endPoint2, final double weight) {

        Preconditions.checkArgument(endPoint1 != null);
        Preconditions.checkArgument(endPoint2 != null);
        Preconditions.checkArgument(!endPoint1.equals(endPoint2));

        Preconditions.checkArgument(weight >= 0);
        this.endPoint1 = endPoint1;
        this.endPoint2 = endPoint2;
        this.weight = weight;

        this.hashCode = hashCode(endPoint1.hashCode(), endPoint2.hashCode());
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

    public Double getWeight() {
        return this.weight;
    }

    public WeightedEdge<T> reverse() {
        return new WeightedEdge<T>(this);
    }

    @Override
    public int hashCode() {
        return this.hashCode;
        /*
        int hashCode = 17;
        hashCode += this.getEndPoint1().hashCode();
        hashCode *= 31;
        hashCode += this.getEndPoint2().hashCode();
        hashCode *= 31;
        hashCode += this.getWeight().hashCode();
        hashCode *= 31;
        return hashCode;
        */
    }

    @Override
    public boolean equals(final Object object) {
        if (object == null) return false;
        if (object == this) return true;
        if (!(object instanceof WeightedEdge)) return false;
        final Object endPoint = ((WeightedEdge<?>) object).getEndPoint1();
        if (!endPoint.getClass().equals(this.getEndPoint1().getClass())) return false;
        final WeightedEdge<?> that = (WeightedEdge<?>) object;
        //return this.hashCode() == that.hashCode() && this.getWeight() == that.getWeight();
        if (!this.getWeight().equals(that.getWeight())) return false;
        final boolean hasSameHashCode = this.hashCode() == that.hashCode();
        boolean isEqal = false;
        if (this.getEndPoint1().equals(that.getEndPoint1()) && this.getEndPoint2().equals(that.getEndPoint2())) isEqal = true;
        if (this.getEndPoint1().equals(that.getEndPoint2()) && this.getEndPoint2().equals(that.getEndPoint1())) isEqal = true;
        Preconditions.checkState(hasSameHashCode == isEqal);
        return isEqal;
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

    @Override
    public int compareTo(final WeightedEdge<T> that) {
        return Double.compare(this.getWeight(), that.getWeight());
    }

}