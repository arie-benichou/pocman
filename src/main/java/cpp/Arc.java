
package cpp;

import com.google.common.base.Objects;

public class Arc
{

    private final String label;
    private final int u;
    private final int v;
    private final double cost;

    public static Arc from(final String lab, final int u, final int v, final double cost) {
        return new Arc(lab, u, v, cost);
    }

    private Arc(final String label, final int u, final int v, final double cost)
    {
        this.label = label;
        this.u = u;
        this.v = v;
        this.cost = cost;
    }

    public String getLabel() {
        return this.label;
    }

    public int getU() {
        return this.u;
    }

    public int getV() {
        return this.v;
    }

    public double getCost() {
        return this.cost;
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(this)
                /*
                .add("label", this.getLabel())
                .add("first node", this.getU())
                .add("last node", this.getV())
                .add("cost", this.getCost())
                */
                .addValue(this.getLabel())
                .addValue(this.getU())
                .addValue(this.getV())
                .addValue(this.getCost())
                .toString();
    }

}