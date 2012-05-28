
package cpp;

public class Path {

    private final int fromNode;
    private final int toNode;

    public Path(final int fromNode, final int toNode) { // TODO name = between nodes ?
        this.fromNode = fromNode;
        this.toNode = toNode;
    }

    public int getFromNode() {
        return this.fromNode;
    }

    public int getToNode() {
        return this.toNode;
    }

    @Override
    public String toString() {
        return "(" + this.getFromNode() + ", " + this.getToNode() + ")";
    }
}