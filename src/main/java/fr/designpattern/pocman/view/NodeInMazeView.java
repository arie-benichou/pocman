
package fr.designpattern.pocman.view;

import fr.designpattern.pocman.cpp.graph.Vertex;
import fr.designpattern.pocman.model.Constants;
import fr.designpattern.pocman.model.MazeAsBoard;

public class NodeInMazeView {

    private final MazeAsBoard board;

    public NodeInMazeView(final MazeAsBoard board) {
        this.board = board;
    }

    public String render(final int nodeId) {
        final char[] array = this.board.toCharArray();
        array[nodeId] = Constants.POCMAN;
        return nodeId + "\n" + MazeAsBoard.from(array);
    }

    public String render(final Vertex vertex) {
        return this.render(vertex.getId());
    }

}