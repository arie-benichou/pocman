
package fr.ut7.dojo.pacman.controller;

import java.util.Map;

import com.google.common.collect.Maps;

import fr.ut7.dojo.pacman.model.Move;

public class MyMoveListener { // TODO utiliser la frame ?

    private final static Map<Integer, Move> mappedMoves = Maps.newHashMap(); // ? TODO à injecter
    static {
        mappedMoves.put(37, Move.GO_LEFT);
        mappedMoves.put(38, Move.GO_UP);
        mappedMoves.put(39, Move.GO_RIGHT);
        mappedMoves.put(40, Move.GO_DOWN);
    }

    private final MyKeyListener keyListener;

    //private final Move lastMove = Move.GO_NOWHERE;

    public MyMoveListener(final MyKeyListener keyListener) {
        this.keyListener = keyListener;
    }

    public MyKeyListener getKeyListener() {
        return this.keyListener;
    }

    /*
    public Move getMove() {
        final Move move = mappedMoves.get(this.keyListener.getLastKeyCode());
        if (move != null) this.lastMove = move;
        return this.lastMove;
    }
    */

    public Move getMove() {
        final Move move = mappedMoves.get(this.keyListener.getLastKeyCode());
        if (move == null) return Move.GO_NOWHERE;
        return move;
    }
}