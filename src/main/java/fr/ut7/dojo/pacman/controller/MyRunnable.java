
package fr.ut7.dojo.pacman.controller;

import javax.swing.JFrame;

public final class MyRunnable implements Runnable {

    private final JFrame frame;

    public JFrame getFrame() {
        return this.frame;
    }

    private final MyMoveListener moveListener;

    public MyRunnable(final JFrame myFrame, final MyMoveListener myMoveListener) {
        this.frame = myFrame;
        this.moveListener = myMoveListener;
    }

    public void run() {
        this.getFrame().addKeyListener(this.moveListener.getKeyListener());
    }

}