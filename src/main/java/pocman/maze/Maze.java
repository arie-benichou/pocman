
package pocman.maze;

import pocman.graph.UndirectedGraph;

import com.google.common.base.Preconditions;
import com.google.common.base.Supplier;

public class Maze implements Supplier<UndirectedGraph<MazeNode>> {

    private final MazeAsGraph mazeAsGraph;

    public Maze(final String data) {
        this.mazeAsGraph = MazeAsGraph.from(MazeAsBoard.from(data));
        Preconditions.checkState(this.mazeAsGraph.hasIsland(), "Maze must not contain island");
    }

    public char[] toCharArray() {
        return this.mazeAsGraph.getBoard().toCharArray();
    }

    public MazeNode getNode(final int nodeId) {
        return this.mazeAsGraph.getNodeById(nodeId);
    }

    public int size() {
        return MazeAsBoard.SIZE;
    }

    public int width() {
        return MazeAsBoard.WIDTH;
    }

    public int height() {
        return MazeAsBoard.HEIGHT;
    }

    @Override
    public UndirectedGraph<MazeNode> get() {
        return this.mazeAsGraph.get();
    }

}