
package fr.designpattern.pocman.view;

import fr.designpattern.pocman.model.MazeAsBoard;

public class MazeAsBoardView {

    public String render(final MazeAsBoard board) {
        final StringBuilder sb = new StringBuilder();
        final char[] data = board.toCharArray();
        for (int i = 0; i < MazeAsBoard.HEIGHT; ++i) {
            for (int j = 0; j < MazeAsBoard.WIDTH; ++j)
                sb.append(data[MazeAsBoard.WIDTH * i + j]);
            sb.append("\n");
        }
        return sb.toString();
    }

}