
package fr.ut7.dojo.pacman.model;


public class NullMoveEmitter implements MoveEmitter {

    public Move getMove(final Game game) {
        return Move.GO_NOWHERE;
    }

}
