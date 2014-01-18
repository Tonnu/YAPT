/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yapt.GAME;

import YAPT.GAME.GameClient;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.rmi.RemoteException;
import java.util.logging.Level;
import java.util.logging.Logger;
import yapt.RMI.IPong;
import yapt.RMI.ISession;
import yapt.RMI.IYAPTServer;
import yapt.RMI.Node;
import yapt.RMI.Vector2f;

/**
 *
 * @author Toon
 */
public class Session extends Node<IYAPTServer> implements ISession {

    private IGameClient game;
    public boolean lookingForGame = false, gameStarted = false, gameInterrupted = false;
    private boolean winner;
    private IYAPTServer server;
    private int playerNumber, pongGameNumber = 0;

    /**
     * *
     * Session manages a game between two players. It registers when the game
     * starts, ends or when a player leaves. It receives updates from the server
     * through RMI. It updates the YAPTClient with player and bat positions.
     *
     * @param server
     * @throws java.rmi.RemoteException
     */
    public Session(IYAPTServer server) throws RemoteException {
        this.pongGameNumber++;
        this.server = server;
        lookingForGame = false;
        game = new YAPT.GAME.GameClient(this);
    }

    public YAPT.GAME.GameClient getGameClient() {
        return (GameClient) this.game;
    }

    @Override
    public Vector2f getPlayerPosition() {
        try {
            return this.game.getPlayer().getBat().getPosition();
        } catch (RemoteException ex) {
            Logger.getLogger(Session.class.getName()).log(Level.SEVERE, null, ex);
        }
        return new Vector2f(99, 99); //if we can't get the position, just return 0,0
    }

    @Override
    public Rectangle getPlayerRectangle() {
        try {
            return this.game.getPlayer().getBat().getRectangle();
        } catch (RemoteException ex) {
            Logger.getLogger(Session.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @Override
    public void setWinner(boolean won) {
        this.winner = won;
    }

    @Override
    public void setPosition(Vector2f _position) {
        try {
            this.game.getPlayer().getBat().setPosition(_position);
        } catch (RemoteException ex) {
            Logger.getLogger(Session.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void draw(Graphics g) {
        try {
            this.game.draw(g);
        } catch (RemoteException ex) {
            Logger.getLogger(Session.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void update(int direction) {
        try {
            this.game.update(direction);
        } catch (RemoteException ex) {
            Logger.getLogger(Session.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void onMessage(String message, Object o) throws RemoteException {
        super.onMessage(message);

        switch (message) {
            case "pongUpdate":
                this.game.setPong((IPong) o); //needed for drawing
                //System.out.println("pongupdate: X=" + this.game.pong.getX() + "y=" + this.pong.getY());
                break;
            case "pushBatUpdate":
                if (!gameInterrupted) {
                    //my bat moved, notify server
                    System.out.println("pushing batupdate to server");
                    //server.onMessage(message, this.game.getPlayer().getBat());
                    server.onMessage("pushSessionUpdate", this); //push new sessionstate to server
                }
                break;
            case "getSessionUpdate":
                //opponent's session updated
                //set opponent's bat for drawing purposes
                System.out.println("recieved opponent sessionupdate from server");
                if (!gameInterrupted) {
                    ISession _opponent = (ISession) o;
                    //verify the opponent is in the same game as we are, and he also has the other player number
                    if (_opponent.getPlayerNumber() != this.getPlayerNumber() && this.pongGameNumber == _opponent.getGamePongNumber()) {
                    //TODO 
                        //not needed to send the whole bat object, let alone player. Only needed for drawing so send coordinates only.
                        if (this.game.getOpponent() == null) {
                            System.out.println("this.game.getOpponent() equals null");
//                            this.game.getOpponent().setBatCoordinates(new Vector2f(0, 0)); //needed for drawing 
                        } else {
                            System.out.println("Opponent does not equal null");
                            this.game.getOpponent().setBatCoordinates(_opponent.getPlayerPosition()); //needed for drawing 
                        }
                    }
                }
                break;
            case "getPongGameNumber":
                this.setGamePongNumber((int) o);
                break;
            case "pushLookingForGame":
                System.out.println("in session pushLookingForGame!!!!");
                gameInterrupted = false;
                if (!lookingForGame) {
                    lookingForGame = true;
                    System.out.println("Sending LFG request!!!!");
                    server.onMessage("newLookingForGame", this);
                }
                break;
            case "gameFound":
                gameStarted = true;
                lookingForGame = false;
                gameInterrupted = false;
                ISession _temp = (ISession) o;

                this.game.setOpponent(new Player("Other", _temp));
                //this.game.setOpponent((IPlayer) _temp.getGameClient().getPlayer());
                if (this.getPlayerNumber() == 1) {
                    //spawn at the left side
                    this.game.getPlayer().setBatCoordinates(new Vector2f(10, 150));
                } else {
                    //spawn at right side
                    this.game.getPlayer().setBatCoordinates(new Vector2f(750, 150));
                }

                break;
            case "getPlayerNumber":
                this.playerNumber = (int) o;
                break;
            case "pushDisconnect":
                server.onMessage("disc", this);
                lookingForGame = false;
                gameInterrupted = true;
                gameStarted = false;
                break;
            case "serverDisconnect":
                gameStarted = false;
                gameInterrupted = true;
                lookingForGame = false;
                game.resetPlayer();
                break;
            default:
                System.out.println("Session didn't recognize: " + message);
        }
    }

    @Override
    public void setPlayerNumber(int _number) {
        this.playerNumber = _number;
    }

    @Override
    public int getPlayerNumber() {
        return this.playerNumber;
    }

    @Override
    public void setGamePongNumber(int _number) {
        this.pongGameNumber = _number;
    }

    @Override
    public int getGamePongNumber() {
        return this.pongGameNumber;
    }
}
