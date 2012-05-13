
package fr.ut7.dojo.pacman.graph;

import java.util.List;

import com.google.common.base.Objects;
import com.google.common.collect.ImmutableList;

public final class PathNode implements Comparable<PathNode> {

    private final int id;
    private final int parent;
    private final List<Integer> children;
    private final int numberOfDescendants;

    public PathNode(final int parent, final int id, final List<Integer> children, final int numberOfDescendants) {
        this.parent = parent;
        this.id = id;
        this.children = ImmutableList.copyOf(children);
        this.numberOfDescendants = numberOfDescendants;
    }

    public int getId() {
        return this.id;
    }

    public int getParent() {
        return this.parent;
    }

    public boolean hasChild() {
        return !this.getChildren().isEmpty();
    }

    public List<Integer> getChildren() {
        return this.children;
    }

    public int getNumberOfDescendants() {
        return this.numberOfDescendants;
    }

    public int compareTo(final PathNode that) {
        return this.getNumberOfDescendants() - that.getNumberOfDescendants();
        //return that.getNumberOfDescendants() - this.getNumberOfDescendants();
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(this)
                .add("parent", this.getParent())
                .add("id", this.getId())
                .add("children", this.getChildren())
                .add("numberOfDescendants", this.getNumberOfDescendants())
                .toString();
    }

}