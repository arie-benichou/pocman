
package tdd;

import java.util.List;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;

public class Edge implements Comparable<Edge> {

    private final int firstNode;
    private final List<Integer> betweenNodes;
    private final int lastNode;
    private final int value;
    private final int hashCode;

    public Edge(final int firstNode, final List<Integer> betweenNodes, final int lastNode) {
        this.firstNode = firstNode;
        this.betweenNodes = ImmutableList.copyOf(betweenNodes);
        this.lastNode = lastNode;
        this.value = this.betweenNodes.size();
        this.hashCode = firstNode * lastNode;
    }

    private Edge(final Edge edge) {
        this.firstNode = edge.getLastNode();
        this.betweenNodes = ImmutableList.copyOf(Lists.reverse(edge.getBetweenNodes()));
        this.lastNode = edge.getFirstNode();
        this.value = edge.value;
        this.hashCode = edge.hashCode();
    }

    public int getFirstNode() {
        return this.firstNode;
    }

    public List<Integer> getBetweenNodes() {
        return this.betweenNodes;
    }

    public int getLastNode() {
        return this.lastNode;
    }

    public int getCost() {
        return this.value;
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

    public int compareTo(final Edge that) {
        return this.getCost() - that.getCost();
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append(this.getCost() + "$");
        sb.append(" [");
        sb.append(this.getFirstNode());
        sb.append(" - ");
        sb.append(this.getBetweenNodes().toString());
        sb.append(" - ");
        sb.append(this.getLastNode());
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