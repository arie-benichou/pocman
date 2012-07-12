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

package pocman.game;

import java.util.List;
import java.util.Set;

import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableSortedSet;

// TODO ? faire une distinction entre MazeNode et GraphNode
public final class MazeNode {

    public enum Type {

        ISLAND(0),
        DEAD_END(1),
        STREET(2),
        CORNER(2),
        CROSSROADS(3),
        ROUNDABOUT(4);

        private int numberOfOptions;

        public static Type from(final Set<Move> options) {
            Preconditions.checkNotNull(options);
            final int numberOfOptions = options.size();
            Preconditions.checkArgument(numberOfOptions >= 0 && numberOfOptions <= 4);
            if (numberOfOptions == 0)
                return Type.ISLAND;
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
    private final Set<Move> options;
    private final Type type;
    private final int hashCode;

    public static MazeNode from(final int id, final Set<Move> options) {
        return new MazeNode(id, options);
    }

    private MazeNode(final int id, final Set<Move> options) {

        this.id = id;
        this.options = ImmutableSortedSet.copyOf(options);
        this.type = Type.from(options);

        int hashcode = 17;
        hashcode += id;
        hashcode *= 31;
        hashcode += this.type.name().hashCode();
        hashcode *= 31;
        hashcode += this.options.hashCode();
        hashcode *= 31;
        //this.hashCode = hashcode;
        this.hashCode = id;

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

    public Set<Move> getOptions() {
        return this.options;
    }

    public int getNumberOfOptions() {
        return this.getType().getNumberOfOptions();
    }

    @Override
    public int hashCode() {
        //return this.id;
        return this.hashCode;
    }

    @Override
    public boolean equals(final Object object) {
        if (object == null) return false;
        if (object == this) return true;
        if (!(object instanceof MazeNode)) return false;
        final MazeNode that = (MazeNode) object;
        final boolean hasSameHashCode = this.hashCode() == that.hashCode();
        final boolean isEqal = this.id == that.id && this.type.equals(that.type) && this.options.equals(that.options);
        Preconditions.checkState(hasSameHashCode == isEqal, "Both nodes does not come from the same maze.");
        return isEqal;
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(this)
                .add("id", this.getId())
                .add("type", this.getType())
                .add("options", this.getOptions())
                .toString();
    }

    public List<MazeEdge> getEdges() {
        return null; // TODO
    }

}