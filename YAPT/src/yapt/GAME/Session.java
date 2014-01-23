/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yapt.GAME;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.rmi.RemoteException;
import java.util.logging.Level;
import java.util.logging.Logger;
import yapt.GUI.LobbyPanel;
import yapt.GUI.YAPTPanel;
import yapt.RMI.IPongGame;
import yapt.RMI.ISession;
import yapt.RMI.IYAPTServer;
import yapt.RMI.Node;
import yapt.RMI.Vector2f;

/**
 *
 * @author Toon
 */
public class Session extends Node<IPongGame> implements ISession {

    private IGameClient game;
    public boolean lookingForGame = false, gameStarted = false, gameInterrupted = false;
    private boolean winner;
    private IYAPTServer server;
    private IPongGame pongGame;
    private int playerNumber, pongGameNumber = 0;
    private YAPTPanel lobbyPanel;

    /**
     * *
     * Session manages a game between two players. It registers when the game
     * starts, ends or when a player leaves. It receives updates from the server
     * through RMI. It updates the YAPTClient with player and bat positions.
     *
     * @param server
     * @throws java.rmi.RemoteException
     */
    public Session(IYAPTServer server, YAPTPanel lobbyPanel) throws RemoteException {
        this.pongGameNumber++;
        this.server = server;
        lookingForGame = false;
        game = new GameClient(this);
        this.lobbyPanel = lobbyPanel;
    }

    public GameClient getGameClient() {
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
            case "PublicChatMessage":
                String chatMessage = (String)o;
                lobbyPanel.newMessage(chatMessage);
                break;
            case "pongUpdate":
                Vector2f _tempcoords = (Vector2f) o;
                this.game.setPongCoordinates(_tempcoords); //needed for drawing
                break;
            case "pushBatUpdate":
                if (!gameInterrupted) {
                    //my bat moved, notify server
                    System.out.println("pushing batupdate to server");
                    pongGame.onMessage("pushSessionUpdate", this); //push new sessionstate to server
                }
                break;
            case "getSessionUpdate":
                //opponent's session updated
                //set opponent's bat for drawing purposes
                System.out.println("recieved opponent sessionupdate from server");
                if (!gameInterrupted) {
                    Vector2f _opponentPosition = (Vector2f) o;
                    this.game.getOpponent().setBatCoordinates(_opponentPosition); //needed for drawing 
                }
                break;
            case "getPongGameNumber":
                this.setGamePongNumber((int) o);
                break;
            case "getPongGameInstance":
                this.pongGame = (IPongGame) o;
                break;
            case "pushLookingForGame":
                gameInterrupted = false;
                if (!lookingForGame) {
                    lookingForGame = true;
                    server.onMessage("newLookingForGame", this);
                }
                break;
            case "gameFound":
                gameStarted = true;
                lookingForGame = false;
                gameInterrupted = false;
                ISession _temp = (ISession) o;

                this.game.setOpponent(new Player("Other", _temp)); //needed for drawing
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
            case "leaveQue":
                server.onMessage("leftQue", this);
                lookingForGame = false;
                gameInterrupted = true;
                gameStarted = false;
                break;
            case "pushDisconnect":
                pongGame.onMessage("gameDisconnect", this);
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
    public void setPongGame(IPongGame pongGame) {
        this.pongGame = pongGame;
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
