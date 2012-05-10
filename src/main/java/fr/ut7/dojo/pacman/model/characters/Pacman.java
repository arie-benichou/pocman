
package fr.ut7.dojo.pacman.model.characters;

import fr.ut7.dojo.pacman.model.Game;
import fr.ut7.dojo.pacman.model.Move;
import fr.ut7.dojo.pacman.model.MoveEmitter;

public class Pacman {

    private final MoveEmitter moveEmitter;

    public Pacman(final MoveEmitter moveEmitter) {
        this.moveEmitter = moveEmitter;
    }

    public Move getMove(final Game game) {
        return this.moveEmitter.getMove(game);
    }

}
