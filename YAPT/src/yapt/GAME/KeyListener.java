/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yapt.GAME;

import java.awt.event.KeyEvent;

/**
 *
 * @author Toon
 */
public class KeyListener implements java.awt.event.KeyListener {

    boolean up = false;
    boolean down = false;
    boolean right = false;
    boolean left = false;

    boolean space = false;

    @Override
    public void keyPressed(KeyEvent e) {
        // TODO Auto-generated method stub
        if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
            right = true;
        }

        if (e.getKeyCode() == KeyEvent.VK_LEFT) {
            left = true;
        }

        if (e.getKeyCode() == KeyEvent.VK_UP) {
            up = true;
        }

        if (e.getKeyCode() == KeyEvent.VK_DOWN) {
            down = true;
        }

        if (e.getKeyCode() == KeyEvent.VK_SPACE) {
            space = true;
        }

    }

    @Override
    public void keyReleased(KeyEvent e) {
        // TODO Auto-generated method stub
        System.out.println("Key is released!!! " + e.toString());

        if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
            right = false;
        }

        if (e.getKeyCode() == KeyEvent.VK_LEFT) {
            left = false;
        }

        if (e.getKeyCode() == KeyEvent.VK_UP) {
            up = false;
        }

        if (e.getKeyCode() == KeyEvent.VK_DOWN) {
            down = false;
        }

        if (e.getKeyCode() == KeyEvent.VK_SPACE) {
            space = false;
        }

    }

    @Override
    public void keyTyped(KeyEvent arg0) {
        // TODO Auto-generated method stub

    }

    public boolean isUp() {
        return up;
    }

    public void setUp(boolean up) {
        this.up = up;
    }

    public boolean isDown() {
        return down;
    }

    public void setDown(boolean down) {
        this.down = down;
    }

    public boolean isLeft() {
        return left;
    }

    public void setLeft(boolean left) {
        this.left = left;
    }

    public boolean isRight() {
        return right;
    }

    public void setRight(boolean right) {
        this.right = right;
    }

    public boolean isSpace() {
        return this.space;
    }

}
