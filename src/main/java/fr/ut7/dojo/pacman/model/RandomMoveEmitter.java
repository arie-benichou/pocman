
package fr.ut7.dojo.pacman.model;

import java.util.Random;

public class RandomMoveEmitter implements MoveEmitter {

    private final Random random = new Random();

    public Move getMove(final Game game) {
        /*
        final List<Move> legalMoves = game.getGhostLegalMoves();
        if (legalMoves.size() == 1) return legalMoves.get(0);
        System.out.println(legalMoves);
        return legalMoves.get();
        */
        //return Move.GO_RIGHT;
        return Move.values()[this.random.nextInt(1024) % 4 + 1];
    }

}
