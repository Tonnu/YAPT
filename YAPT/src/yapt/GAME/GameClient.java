/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yapt.GAME;

import java.awt.Color;
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
import yapt.GUI.YAPTPanel;
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
    private IPlayer player1, player2;
    private ISession session;
    private final boolean spectator;

    /**
     * *
     * Manages the local gameplay, such as drawing graphics.
     *
     * @param session
     * @param spectator
     * @throws RemoteException
     */
    public GameClient(ISession session, boolean spectator) throws RemoteException {
        //you.register(this);
        //opponent = new Player("other", 2);
        this.session = session;
        player1 = new Player("Toon", session);
        this.spectator = spectator;
    }

    public GameClient(ISession spectator, ISession p1, ISession p2) {
        this.spectator = true;
        this.player1 = new Player("player 1", p1);
        this.player2 = new Player("player 2", p2);
        this.session = spectator;
        try {
            System.out.println("width: " + this.session.getClientRectangle().width + ", height: " + this.session.getClientRectangle().height);
        } catch (RemoteException ex) {
            Logger.getLogger(GameClient.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void setPlayer1(IPlayer player1) {
        this.player1 = player1;
    }

    @Override
    public IPlayer getPlayer() throws RemoteException {
        return this.player1;
    }

    @Override
    public void update(int direction) {
        this.player1.update(direction);
    }

    @Override
    public void draw(Graphics g) {
        try {
            g.drawRect(0, 0, YAPTPanel.WIDTH, YAPTPanel.HEIGHT);
            g.setColor(Color.BLACK);
            g.fillRect(0, 0, YAPTPanel.WIDTH, YAPTPanel.HEIGHT);
            g.setColor(Color.WHITE);

            for (int x = this.session.getClientRectangle().height; x > 0; x = x - 10) {
                g.drawRect(this.session.getClientRectangle().width / 2, x, 3, 5);
                g.fillRect(this.session.getClientRectangle().width / 2, x, 3, 5);
            }

            this.player1.draw(g);

            if (this.player2 != null) {
                this.player2.draw(g);
            }
            //draw the pong
            Graphics2D g2d = (Graphics2D) g;

            if (pongLocation != null) {
                Rectangle draw_pong = new Rectangle((int) pongLocation.x, (int) pongLocation.y, pongWidth, pongHeight);
                g2d.fill(draw_pong);
                g2d.draw(draw_pong);
            }
            if (player1 != null && player2 != null) {
                g2d.setFont(new Font(Font.MONOSPACED, Font.BOLD, 50));
                g2d.drawString(Integer.toString(this.player1.getScore()), (int) this.session.getClientRectangle().getWidth() / 4, 50);
                g2d.drawString(Integer.toString(this.player2.getScore()), (int) this.session.getClientRectangle().getWidth() - (int) (this.session.getClientRectangle().getWidth() / 4), 50);
            }
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
        this.player2 = (IPlayer) _opponent;
    }

    @Override
    public IPlayer getOpponent() {
        return this.player2;
    }

    @Override
    public void resetPlayer() {
        this.player1 = new Player("Name", session);
    }

}
