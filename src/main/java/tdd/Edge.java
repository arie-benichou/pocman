
package tdd;

import java.util.List;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;

public class Edge implements Comparable<Edge> {

    private final int vertex1;
    private final List<Integer> betweenVertices;
    private final int vertex2;
    private final double cost;
    private final int hashCode;

    public Edge(final int vertex1, final List<Integer> betweenNodes, final int vertex2) {
        this.vertex1 = vertex1;
        this.betweenVertices = ImmutableList.copyOf(betweenNodes);
        this.vertex2 = vertex2;
        this.cost = this.betweenVertices.size() + 1;
        this.hashCode = vertex1 * vertex2;
    }

    private Edge(final Edge edge) {
        this.vertex1 = edge.getLastVertex();
        this.betweenVertices = ImmutableList.copyOf(Lists.reverse(edge.getBetweenVertices()));
        this.vertex2 = edge.getFirstVertex();
        this.cost = edge.cost;
        this.hashCode = edge.hashCode();
    }

    public Edge(final int vertex1, final int vertex2, final double cost) {
        this.vertex1 = vertex1;
        this.betweenVertices = ImmutableList.of();
        this.vertex2 = vertex2;
        this.cost = cost;
        this.hashCode = vertex1 * vertex2;
    }

    public int getFirstVertex() {
        return this.vertex1;
    }

    public List<Integer> getBetweenVertices() {
        return this.betweenVertices;
    }

    public int getLastVertex() {
        return this.vertex2;
    }

    public double getCost() {
        return this.cost;
    }

    @Override
    public int hashCode() {
        return this.hashCode;
    }

    public Edge getSymetric() {
        return new Edge(this);
    }

    @Override
    public boolean equals(final Object object) {
        if (object == null) return false;
        if (object == this) return true;
        if (!(object instanceof Edge)) return false;
        final Edge that = (Edge) object;
        return this.hashCode() == that.hashCode();
    }

    @Override
    public int compareTo(final Edge that) {
        return Double.compare(this.getCost(), that.getCost());
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append(this.getCost() + "$");
        sb.append(" [");
        sb.append(this.getFirstVertex());
        sb.append(" - ");
        sb.append(this.getBetweenVertices().toString());
        sb.append(" - ");
        sb.append(this.getLastVertex());
        sb.append("]");
        return sb.toString();
    }

    public static void main(final String[] args) {

        final Edge edge1 = new Edge(1, Lists.newArrayList(2, 3), 4);
        final Edge edge2 = new Edge(4, Lists.newArrayList(3, 2), 1);
        final Edge edge3 = new Edge(4, Lists.newArrayList(2, 3), 1);
        final Edge edge4 = new Edge(1, Lists.newArrayList(2, 3), 8);

        System.out.println(edge1.equals(edge2));
        System.out.println(edge2.equals(edge1));
        System.out.println(edge1.equals(edge3));
        System.out.println(edge1.equals(edge4));

        System.out.println(edge1);
        System.out.println(edge1.getSymetric());
        System.out.println(edge1.getSymetric().getSymetric());

    }

}