
package fr.designpattern.pocman.graph;

public class Node<T> {

    private final T data;

    public T getData() {
        return this.data;
    }

    private final int hashcode;

    @Override
    public int hashCode() {
        return this.hashcode;
    }

    public Node(final T data) {
        this.data = data;
        this.hashcode = data == null ? -1 : this.data.hashCode();
    }

    @Override
    public boolean equals(final Object object) {
        if (object == null) return false;
        if (object == this) return true;
        if (!(object instanceof Node)) return false;
        final Node<?> that = (Node<?>) object;
        if (this.getData() != null) return this.getData().equals(that.getData());
        else return that.getData() == null;
    }

    @Override
    public String toString() {
        return this.data == null ? "NULL" : this.data.toString();
    }

}