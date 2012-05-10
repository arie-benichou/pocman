
package fr.ut7.dojo.pacman.controller;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class MyKeyListener implements KeyListener {

    private int lastKeyCode = 0;

    public MyKeyListener() {
        this(0);
    }

    public MyKeyListener(final int lastKeyCode) {
        this.lastKeyCode = lastKeyCode;
    }

    public void keyPressed(final KeyEvent e) {
        this.lastKeyCode = e.getKeyCode();
        //System.out.println(this.lastKeyCode);
    }

    public void keyTyped(final KeyEvent e) {}

    public void keyReleased(final KeyEvent e) {
        this.lastKeyCode = 0;
    }

    public int getLastKeyCode() {
        final int lastKeyCode = this.lastKeyCode;
        this.lastKeyCode = 0;
        return lastKeyCode;
    }
}
