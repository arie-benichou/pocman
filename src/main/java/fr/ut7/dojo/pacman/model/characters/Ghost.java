
package fr.ut7.dojo.pacman.model.characters;

import fr.ut7.dojo.pacman.model.Game;
import fr.ut7.dojo.pacman.model.Move;
import fr.ut7.dojo.pacman.model.MoveEmitter;

public class Ghost {

    private final MoveEmitter moveEmitter;

    public Ghost(final MoveEmitter moveEmitter) {
        this.moveEmitter = moveEmitter;
    }

    public Move getMove(final Game game) {
        return this.moveEmitter.getMove(game);
    }

}
