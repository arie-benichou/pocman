
package fr.ut7.dojo.pacman.view;

import fr.ut7.dojo.pacman.graph.GraphEdge;
import fr.ut7.dojo.pacman.graph.GraphNode;
import fr.ut7.dojo.pacman.graph.Path;
import fr.ut7.dojo.pacman.model.Board;
import fr.ut7.dojo.pacman.model.Constants;

public final class PathView {

    private final Board board;
    private final BoardView boardView;

    public PathView(final Board board) {
        this.board = board;
        this.boardView = new BoardView();
    }

    private Board render(final Board board, final GraphEdge edge) {
        final char[] array = board.toCharArray();
        final char c = edge.getMove().toString().charAt(0);
        array[edge.getFirstNode().getId()] = c;
        for (final GraphNode node : edge.getBetweenNodes())
            array[node.getId()] = c;
        array[edge.getLastNode().getId()] = Constants.PACMAN;
        return Board.from(array);
    }

    public String render(final Path path) {
        final StringBuilder stringBuilder = new StringBuilder("\n");
        Board board = this.board;
        for (final GraphEdge edge : path.getEdgesSequence()) {
            board = this.render(board, edge);
            stringBuilder.append(edge);
            stringBuilder.append("\n");
            stringBuilder.append(this.boardView.render(board));
            stringBuilder.append("\n");
        }
        stringBuilder.append("sum($) = " + path.getSum());
        return stringBuilder.toString();
    }

}