
package fr.designpattern.pocman.view;

import fr.designpattern.pocman.cpp.graph.Vertex;
import fr.designpattern.pocman.model.MazeAsBoard;
import fr.designpattern.pocman.model.MazeAsGraph;

public class MazeAsGraphView {

    public String render(final MazeAsGraph mazeAsGraph) {
        final char[] array = new char[MazeAsBoard.SIZE];
        for (int nodeId = 0; nodeId < MazeAsBoard.SIZE; ++nodeId) {
            final Vertex node = mazeAsGraph.getNodeById(nodeId);
            final int n = node == null ? 0 : node.getNumberOfOptions();
            char c;
            switch (n) {
                case 0:
                    c = ' ';
                    break;
                case 2:
                    c = node.is(Vertex.Type.STREET) ? '⬤' : '2';
                    break;
                default:
                    c = String.valueOf(n).charAt(0);
            }
            array[nodeId] = c;
        }
        final StringBuilder sb = new StringBuilder("\n");
        sb.append(new MazeAsBoardView().render(MazeAsBoard.from(array)).replaceAll(" {27}\\n", ""));//.append("\n");
        return sb.toString();
    }

}