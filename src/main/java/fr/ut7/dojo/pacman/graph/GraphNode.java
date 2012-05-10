
package fr.ut7.dojo.pacman.graph;

import java.util.List;
import java.util.Set;

import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;

import fr.ut7.dojo.pacman.model.Move;

public final class GraphNode implements Comparable<GraphNode> {

    public enum Type {

        DEAD_END(1),
        STREET(2),
        CORNER(2),
        CROSSROADS(3),
        ROUNDABOUT(4);

        private int numberOfOptions;

        public static Type from(final Set<Move> options) {
            Preconditions.checkNotNull(options);
            final int numberOfOptions = options.size();
            Preconditions.checkArgument(numberOfOptions >= 1 && numberOfOptions <= 4);
            if (numberOfOptions == 1)
                return Type.DEAD_END;
            if (numberOfOptions == 3)
                return Type.CROSSROADS;
            if (numberOfOptions == 4)
                return Type.ROUNDABOUT;
            int sum = 0;
            for (final Move move : options)
                sum += move.getDelta();
            return sum == 0 ? Type.STREET : Type.CORNER;
        }

        private Type(final int numberOfOptions) {
            this.setNumberOfOptions(numberOfOptions);
        }

        public int getNumberOfOptions() {
            return this.numberOfOptions;
        }

        public void setNumberOfOptions(final int numberOfOptions) {
            this.numberOfOptions = numberOfOptions;
        }

    }

    private final int id;
    private final List<Move> options;
    private final Type type;

    public static GraphNode from(final int id, final Set<Move> options) {
        return new GraphNode(id, options);
    }

    private GraphNode(final int id, final Set<Move> options) {
        this.id = id;
        this.options = ImmutableList.copyOf(options);
        this.type = Type.from(options);
    }

    public int getId() {
        return this.id;
    }

    public Type getType() {
        return this.type;
    }

    public boolean is(final Type type) {
        return this.getType().equals(type);
    }

    public List<Move> getOptions() {
        return this.options;
    }

    public int getNumberOfOptions() {
        return this.getType().getNumberOfOptions();
    }

    public int compareTo(final GraphNode that) {
        return this.id - that.id;
    }

    @Override
    public int hashCode() {
        return this.id;
    }

    @Override
    public boolean equals(final Object object) {
        if (object == null) return false;
        if (object == this) return true;
        if (!(object instanceof GraphNode)) return false;
        final GraphNode that = (GraphNode) object;
        return this.getId() == that.getId();
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(this)
                .add("id", this.getId())
                .add("options", this.getOptions())
                .add("type", this.getType())
                .toString();
    }

}