
package fr.designpattern.pocman.game;

import com.google.common.base.Preconditions;
import com.google.common.base.Supplier;

import fr.designpattern.pocman.graph.UndirectedGraph;
import fr.designpattern.pocman.graph.Vertex;

public class Maze implements Supplier<UndirectedGraph<Vertex>> {

    private final MazeAsGraph mazeAsGraph;

    public Maze(final String data) {
        this.mazeAsGraph = MazeAsGraph.from(MazeAsBoard.from(data));
        Preconditions.checkState(this.mazeAsGraph.hasIsland(), "Maze must not contain island");
    }

    public char[] toCharArray() {
        return this.mazeAsGraph.getBoard().toCharArray();
    }

    public Vertex getNode(final int nodeId) {
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

    /*
    public UndirectedGraph<Vertex> getGraph() {
        return this.mazeAsGraph.get();
    }
    */

    @Override
    public UndirectedGraph<Vertex> get() {
        return this.mazeAsGraph.get();
    }

}