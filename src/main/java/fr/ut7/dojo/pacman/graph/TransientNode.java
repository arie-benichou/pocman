
package fr.ut7.dojo.pacman.graph;

import java.util.List;

import com.google.common.base.Objects;
import com.google.common.collect.Lists;

public class TransientNode implements Comparable<TransientNode> {

    private final GraphNode data;
    private TransientNode parent;
    private final List<TransientNode> children = Lists.newArrayList();
    private int numberOfDescendants = 0;

    public TransientNode(final TransientNode treeNode) {
        this.data = treeNode.getData();
    }

    public TransientNode(final GraphNode data) {
        this.data = data;
    }

    public void addChild(final TransientNode childNode) {
        this.children.add(childNode);
        childNode.parent = this;
    }

    /*
    public boolean hasChild() {
        return !this.getChildren().isEmpty();
    }
    */

    /*
    public void addChildren(final List<TransientNode> childrenOfChild) {
        for (final TransientNode childNode : childrenOfChild)
            this.addChild(childNode);
    }
    */

    public int getNumberOfDescendants() {
        return this.numberOfDescendants;
    }

    public void setNumberOfDescendants(final int n) {
        this.numberOfDescendants = n;
    }

    public GraphNode getData() {
        return this.data;
    }

    public int getId() {
        return this.data.getId();
    }

    public TransientNode getParent() {
        return this.parent;
    }

    public List<TransientNode> getChildren() {
        return this.children;
    }

    @Override
    public int hashCode() {
        return this.getData().hashCode();
    }

    @Override
    public boolean equals(final Object object) {
        if (object == null) return false;
        if (object == this) return true;
        if (!(object instanceof TransientNode)) return false;
        final TransientNode that = (TransientNode) object;
        return this.getData().equals(that.getData());
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(this)
                .add("data", this.getData())
                .add("descendants", this.getNumberOfDescendants())
                .toString();
    }

    public int compareTo(final TransientNode that) {
        return this.getNumberOfDescendants() - that.getNumberOfDescendants();
    }

}
