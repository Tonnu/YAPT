/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yapt.GAME;

import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.Ellipse2D;
import java.io.Serializable;
import java.rmi.RemoteException;
import java.util.Timer;
import java.util.logging.Level;
import java.util.logging.Logger;
import yapt.RMI.ISession;
import yapt.RMI.Vector2f;

/**
 *
 * @author Lenny
 */
public class GameClient implements IGameClient, Serializable {

    //private Pong pong;
    private Vector2f pongLocation;
    private int pongWidth = 15, pongHeight = 15;
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

        if (pongLocation != null) {
            Rectangle draw_pong = new Rectangle((int) pongLocation.x, (int) pongLocation.y, pongWidth, pongHeight);
            g2d.fill(draw_pong);
            g2d.draw(draw_pong);
        }
        try {
            g2d.setFont(new Font(Font.MONOSPACED, Font.BOLD, 50));
            g2d.drawString(Integer.toString(this.you.getScore()), (int) this.session.getClientRectangle().getWidth() / 4, 50);
            g2d.drawString(Integer.toString(this.opponent.getScore()), (int) this.session.getClientRectangle().getWidth() - (int) (this.session.getClientRectangle().getWidth() / 4), 50);

        } catch (RemoteException ex) {
            Logger.getLogger(GameClient.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void setPongCoordinates(Vector2f _coords) {
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
