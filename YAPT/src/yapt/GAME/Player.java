/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yapt.GAME;

import java.awt.Graphics;
import java.io.Serializable;
import java.rmi.RemoteException;
import java.util.logging.Level;
import java.util.logging.Logger;
import yapt.RMI.ISession;
import yapt.RMI.Vector2f;

/**
 *
 * @author Toon
 */
public class Player implements IPlayer, Serializable {

    //private final int unique_id, game_id;
    private final String name;
    private ISession owningSession;
    private Bat bat;
    private int score;

    @Override
    public int getScore() {
        return score;
    }

    @Override
    public void score() {
        this.score++;
    }

    @Override
    public void setScore(int score) {
        this.score = score;
    }

    public Player(String name, ISession session) {
        this.name = name;
        this.score = 0;
        this.owningSession = session;
        this.bat = new Bat(new Vector2f(10, 10));
    }

    @Override
    public void update(int direction) {
        try {
            this.bat.update(direction);
            this.owningSession.onMessage("pushBatUpdate", this.bat);
        } catch (RemoteException ex) {
            Logger.getLogger(Player.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void draw(Graphics g) {
        this.bat.draw(g);
    }

    @Override
    public IBat getBat() {
        return this.bat;
    }

    @Override
    public void setBatCoordinates(Vector2f _position) {
        this.bat.setPosition(_position);
    }

}
