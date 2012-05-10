
package fr.ut7.dojo.pacman.view;

import fr.ut7.dojo.pacman.model.Board;

public class BoardView {

    public String render(final Board board) {
        final StringBuilder sb = new StringBuilder();
        final char[] data = board.toCharArray();
        for (int i = 0; i < Board.HEIGHT; ++i) {
            for (int j = 0; j < Board.WIDTH; ++j)
                sb.append(data[Board.WIDTH * i + j]);
            sb.append("\n");
        }
        return sb.toString();
    }

}