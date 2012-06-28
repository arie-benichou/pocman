
package fr.designpattern.pocman.graph;

import com.google.common.base.Objects;

public class Arc<T>
{

    private final T u;
    private final T v;
    private final Double cost;

    public static <T> Arc<T> from(final T u, final T v, final Double cost) {
        return new Arc<T>(u, v, cost);
    }

    private Arc(final T u, final T v, final Double cost)
    {
        this.u = u;
        this.v = v;
        this.cost = cost;
    }

    public T getU() {
        return this.u;
    }

    public T getV() {
        return this.v;
    }

    public Double getCost() {
        return this.cost;
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(this)
                .addValue(this.getU())
                .addValue(this.getV())
                .addValue(this.getCost())
                .toString();
    }

}