/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yapt.GAME;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;
import java.io.Serializable;
import java.rmi.RemoteException;
import java.util.Timer;
import yapt.RMI.ISession;
import yapt.RMI.Vector2f;

/**
 *
 * @author Lenny
 */
public class GameClient implements IGameClient, Serializable {

    //private Pong pong;
    private Vector2f pongLocation;
    private double pongWidth = 50, pongHeight = 50;
    private Timer t;
    private IPlayer you, opponent;
    private ISession session;

    /**
     * *
     * Manages the local gameplay, such as drawing graphics.
     *
     * @param session
     * @throws RemoteException
     */
    public GameClient(ISession session) throws RemoteException {
        //you.register(this);
        //opponent = new Player("other", 2);
        this.session = session;
        you = new Player("Toon", session);
    }


    @Override
    public IPlayer getPlayer() throws RemoteException {
        return this.you;
    }


    @Override
    public void update(int direction) {
        this.you.update(direction);
    }

    @Override
    public void draw(Graphics g) {
        this.you.draw(g);
        if (this.opponent != null) {
            this.opponent.draw(g);
        }
        //draw the pong
        Graphics2D g2d = (Graphics2D) g;
        
        if(pongLocation != null){
            Ellipse2D.Double draw_pong = new Ellipse2D.Double(pongLocation.x, pongLocation.y, pongWidth, pongHeight);
            g2d.fill(draw_pong);
            g2d.draw(draw_pong);
        }
    }


    @Override
    public void setPongCoordinates(Vector2f _coords){
        //this.pong.setPongCoordinates(coords);
        this.pongLocation = _coords;
    }
    @Override
    public void setOpponent(IPlayer _opponent) {
        this.opponent = (IPlayer) _opponent;
    }

    @Override
    public IPlayer getOpponent() {
        return this.opponent;
    }

    @Override
    public void resetPlayer() {
        this.you = new Player("Name", session);
    }
    
}
