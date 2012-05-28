
package tdd;

import java.util.List;

import com.google.common.base.Joiner;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableList.Builder;
import com.google.common.collect.Lists;

public final class Path implements Comparable<Path> {

    private final List<Edge> edges;
    private final int numberOfEdges;
    private final double cost;
    private final Edge lastEdge;
    private final int hashCode;

    public static Path from(final double cost) {
        return new Path(cost);
    }

    public static Path from() {
        return from(0);
    }

    public static Path from(final Edge edge) {
        Preconditions.checkNotNull(edge);
        return new Path(edge);
    }

    private Path(final double cost) {
        this.edges = ImmutableList.of();
        this.numberOfEdges = 0;
        this.cost = cost;
        this.lastEdge = null;
        this.hashCode = 0;
    }

    private Path(final Edge edge) {
        this.edges = ImmutableList.of(edge);
        this.numberOfEdges = 1;
        this.cost = edge.getCost();
        this.lastEdge = edge;
        this.hashCode = edge.hashCode();
    }

    private Path(final List<Edge> edges, final double cost, final Edge lastEdge) {
        this.edges = edges;
        this.numberOfEdges = edges.size();
        this.cost = cost;
        this.lastEdge = lastEdge;
        this.hashCode = edges.get(0).getFirstVertex() * lastEdge.getLastVertex();
    }

    @Override
    public int hashCode() {
        return this.hashCode;
    }

    @Override
    public boolean equals(final Object object) {
        if (object == null) return false;
        if (object == this) return true;
        if (!(object instanceof Path)) return false;
        final Path that = (Path) object;
        return that.hashCode == this.hashCode;
    }

    public int getFirstVertex() {
        return this.getEdges().get(0).getFirstVertex(); // TODO check for null
    }

    public int getLastVertex() {
        return this.getLastEdge().getLastVertex(); // TODO check for null
    }

    // TODO retourner une liste immutable uniquement à ce moment ?
    public List<Edge> getEdges() {
        return this.edges;
    }

    public boolean isNull() {
        return this.getNumberOfEdges() == 0;
    }

    public int getNumberOfEdges() {
        return this.numberOfEdges;
    }

    public double getCost() {
        return this.cost;
    }

    public Edge getLastEdge() {
        return this.lastEdge;
    }

    public Path add(final Edge edge) {
        Preconditions.checkNotNull(edge);
        final int n = this.getNumberOfEdges();
        switch (n) {
            case 0:
                return new Path(edge);
            case 1: {
                Edge lastEdge = this.getLastEdge();
                Edge newEdge = null;
                if (lastEdge.getLastVertex() == edge.getFirstVertex()) newEdge = edge;
                else if (lastEdge.getLastVertex() == edge.getLastVertex()) newEdge = edge.getSymetric();
                else if (lastEdge.getFirstVertex() == edge.getFirstVertex()) {
                    lastEdge = lastEdge.getSymetric();
                    newEdge = edge;
                }
                else if (lastEdge.getFirstVertex() == edge.getLastVertex()) {
                    lastEdge = lastEdge.getSymetric();
                    newEdge = edge.getSymetric();
                }
                Preconditions.checkState(newEdge != null, "invalid path");
                return new Path(ImmutableList.of(lastEdge, newEdge), this.getCost() + newEdge.getCost(), newEdge);
            }
            default: {
                final Builder<Edge> builder = new ImmutableList.Builder<Edge>();
                for (int i = 0; i < n - 1; ++i)
                    builder.add(this.getEdges().get(i));
                final Edge lastEdge = this.getLastEdge();
                //System.out.println("lastEdge:" + lastEdge);
                Edge newEdge = null;
                if (lastEdge.getLastVertex() == edge.getFirstVertex()) newEdge = edge;
                else if (lastEdge.getLastVertex() == edge.getLastVertex()) newEdge = edge.getSymetric();
                Preconditions.checkState(newEdge != null, "invalid path");
                builder.add(lastEdge, newEdge);
                return new Path(builder.build(), this.getCost() + newEdge.getCost(), newEdge);
            }
        }
    }

    public Path reverse() {
        final Builder<Edge> builder = new ImmutableList.Builder<Edge>();

        for (int i = this.getNumberOfEdges() - 1; i > -1; --i) {
            final Edge edge = this.getEdges().get(i);
            builder.add(edge.getSymetric());
        }

        return new Path(builder.build(), this.getCost(), this.getEdges().get(0));
    }

    public List<Integer> toSequence() { // TODO ? immutable
        final List<Integer> sequence = Lists.newArrayList();
        for (final Edge edge : this.getEdges()) {
            sequence.add(edge.getFirstVertex());
            sequence.addAll(edge.getBetweenNodes());
        }
        sequence.add(this.getLastEdge().getLastVertex());
        return sequence;
    }

    public List<Integer> toSequence(final boolean trim) { // TODO ? immutable
        final List<Integer> sequence = Lists.newArrayList();
        for (final Edge edge : this.getEdges()) {
            sequence.add(edge.getFirstVertex());
            sequence.addAll(edge.getBetweenNodes());
        }
        if (!trim) sequence.add(this.getLastEdge().getLastVertex());
        else sequence.remove(0);
        return sequence;
    }

    public Path add(final Path path) {

        ///System.out.println("\nAdding: " + path + "\nTo: " + this + "\n");
        Preconditions.checkNotNull(path);

        if (path.isNull()) return this;
        if (this.isNull()) return path;
        if (path.getNumberOfEdges() == 1) return this.add(path.getEdges().get(0));

        {
            final int lastNode = this.getLastEdge().getLastVertex();
            final int firstNode = path.getEdges().get(0).getFirstVertex();

            //System.out.println(lastNode);
            //System.out.println(firstNode);

            Preconditions.checkState(firstNode == lastNode, "invalid path");

            //if (lastNode == firstNode) {
            final Builder<Edge> builder = new ImmutableList.Builder<Edge>();
            builder.addAll(this.getEdges()).addAll(path.getEdges());
            return new Path(builder.build(), this.getCost() + path.getCost(), path.getLastEdge());
            //}
        }

        /*
        {
            final int firstNode = this.getLastEdge().getFirstNode();
            final int lastNode = path.getEdges().get(0).getLastNode();
            if (firstNode == lastNode) return this.add(path.reverse());
        }

        throw new RuntimeException("invalid path");
        */

    }

    @Override
    public String toString() {
        if (this.isNull()) return "NULL PATH";
        return "\n * " + Joiner.on("\n * ").join(this.getEdges()) + "\n Cost = " + this.getCost() + "$";
    }

    @Override
    public int compareTo(final Path that) {
        if (this.getCost() < that.getCost()) return -1;
        if (that.getCost() < this.getCost()) return 1;
        return 0;
    }

    public static void main(final String[] args) {

        final Edge edge1 = new Edge(1, Lists.newArrayList(2, 3), 4);
        final Edge edge2 = new Edge(4, Lists.newArrayList(5, 6, 7), 8);
        final Edge edge3 = new Edge(8, Lists.newArrayList(9, 10, 11, 12), 13);

        final Edge edge4 = new Edge(13, Lists.newArrayList(14), 15);
        final Edge edge5 = new Edge(15, Lists.newArrayList(16, 17, 18), 19);
        final Edge edge6 = new Edge(19, Lists.newArrayList(20, 21), 22);

        Path path1;
        Path path2;

        path1 = Path.from();
        System.out.println(path1);

        path1 = path1.add(edge1.getSymetric());
        System.out.println(path1);

        path1 = path1.add(edge2.getSymetric());
        System.out.println(path1);

        path1 = path1.add(edge3.getSymetric());
        System.out.println(path1);

        path2 = Path.from();
        path2 = path2.add(edge4.getSymetric());
        path2 = path2.add(edge5.getSymetric());
        path2 = path2.add(edge6.getSymetric());
        System.out.println(path2);

        final Path path3 = path1.add(path2);
        System.out.println(path3);

        System.out.println(path3.reverse());

    }

}