
package fr.ut7.dojo.pacman.view;

import fr.ut7.dojo.pacman.model.Board;
import fr.ut7.dojo.pacman.model.Constants;
import fr.ut7.dojo.pacman.model.Game;

public final class GameView {

    private final BoardView boardView = new BoardView();

    public String render(final Game game) {
        final char[] data = game.getBoard().toCharArray();
        data[game.getPacmanPosition()] = Constants.PACMAN;
        if (game.getGhostPosition() > 0)
            data[game.getGhostPosition()] = Constants.GHOST;
        return this.boardView.render(Board.from(data));
    }

}