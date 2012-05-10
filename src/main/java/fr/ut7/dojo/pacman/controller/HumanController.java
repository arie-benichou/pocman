
package fr.ut7.dojo.pacman.controller;

import fr.ut7.dojo.pacman.model.Game;
import fr.ut7.dojo.pacman.model.Move;
import fr.ut7.dojo.pacman.model.MoveEmitter;

public class HumanController implements MoveEmitter {

    private final MyMoveListener myMoveListener;
    private final MyFrame myFrame;

    //private final Move lastMove = Move.GO_NOWHERE;

    public HumanController() {
        this.myMoveListener = new MyMoveListener(new MyKeyListener());
        this.myFrame = new MyFrame();
        final Runnable myRunnable = new MyRunnable(this.myFrame, this.myMoveListener);
        javax.swing.SwingUtilities.invokeLater(myRunnable);
    }

    /*
    public Move getMove(final Game game) {
        final Move move = this.myMoveListener.getMove();
        if (game.isPacmanLegalMove(move)) this.lastMove = move;
        return this.lastMove;
    }
    */

    public Move getMove(final Game game) {
        final Move move = this.myMoveListener.getMove();
        if (game.isPacmanLegalMove(move)) return move;
        return Move.GO_NOWHERE;
    }

    @Override
    protected void finalize() throws Throwable {
        this.myFrame.dispose();
    }
}