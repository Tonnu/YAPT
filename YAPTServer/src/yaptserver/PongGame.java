/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yaptserver;

import java.awt.List;
import java.io.Serializable;
import java.rmi.RemoteException;
import java.util.Iterator;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;
import yapt.RMI.IPong;
import yapt.RMI.IPongGame;
import yapt.RMI.ISession;
import yapt.RMI.IYAPTServer;
import yapt.RMI.Node;

/**
 *
 * @author Toon
 */
public class PongGame extends Node<ISession> implements IPongGame, Serializable {

    private final IYAPTServer server;
    private IPong pong;
    private ISession playerA, playerB;
    private boolean game_started = false, game_stopped;
    private final int game_id;
    private int leftScore, rightScore = 0;
    private boolean game_paused = true;

    /**
     * *
     * Manages a game between two people. Specifically, it holds the pong
     * object, both player positions and calculates collisions.
     *
     * @param server the server to communicate with.
     * @param A The first player's session.
     * @param B the second player's session.
     * @param game_id the unique id for this game.
     * @throws java.rmi.RemoteException
     */
    public PongGame(IYAPTServer server, ISession A, ISession B, int game_id) throws RemoteException {
        this.game_id = game_id;
        //server.onMessage("pushPongGameNumber", this.game_id);
        this.server = server;
        this.playerA = A;
        this.playerB = B;

        this.pong = new Pong(playerA.getPlayerRectangle(), playerB.getPlayerRectangle(), playerA.getClientRectangle());
        //this.start(); //start the game immediately, for now
    }

    @Override
    public void register(ISession other) throws RemoteException {
        super.register(other); //To change body of generated methods, choose Tools | Templates.
//        other.register(this);
        //other.setPongGame(this);
    }

    @Override
    public void start() {
        this.game_stopped = false;
        this.game_paused = false;
        this.game_started = true;
        final Timer t = new Timer();
        t.schedule(new TimerTask() {

            @Override
            public void run() {
                try {
                    update();
                } catch (RemoteException ex) {
                    t.cancel();
                    System.out.println("Canceled Run method for game!");
                    ex.printStackTrace();
                }
            }

        }, 1000, 33); // 30 FPS
    }

    @Override
    public void onMessage(String message, Object o) throws RemoteException {
        System.out.println("Game_Started = " + this.game_started);
        if (this.game_started) {
            if (!game_paused) {
                super.onMessage(message);

                switch (message) {
                    case "pushSessionUpdate":
                        ISession _temp = (ISession) o;

                        if (_temp.getPlayerNumber() == 1) {
                            //update player A
                            this.playerA = _temp;
                            //got update from player A, notify player B
                            this.playerB.onMessage("getSessionUpdate", _temp.getPlayerPosition());
                        } else {
                            //update player B
                            this.playerB = _temp;
                            //got update from player B, notify player A
                            this.playerA.onMessage("getSessionUpdate", _temp.getPlayerPosition());
                        }
                        break;
                    case "gameDisconnect":
                        //this.stop();
                        if (this.getPlayerB() != null && this.getPlayerA() != null) {
                            //send disconnect to other player
                            this.getPlayerB().onMessage("serverDisconnect", null);
                            //this.unRegister(this.getPlayerB());

                            this.getPlayerA().onMessage("serverDisconnect", null);
                            //this.unRegister(this.getPlayerA());

                            this.server.getLobby().unRegister(this.playerA);
                            this.server.getLobby().unRegister(this.playerB);

                            server.onMessage("gameStopped", this);
                        }
                        break;
                    case "GameChatMessage":
                        this.notifyAll("GetGameChatMessage", o);
                        break;
                }
            } else {
                start();
            }
        }

    }

    @Override
    public void stop() {
        this.game_stopped = true;
        this.game_started = false;
        //this.server.onMessage("gameStopped", this);

    }

    public void pause() {
        this.game_paused = true;
    }

    private void update() throws RemoteException {
        if (game_started) {

            //pass player rectangles to pong
            this.pong.update(this.playerA.getPlayerRectangle(), this.playerB.getPlayerRectangle());
            this.notifyAll("pongUpdate", this.pong.getPongCoordinates());

            //now get player updates
            //this.playerA.getPlayerPosition()
            if (pong.isOutOfLeftBound()) {
                //server.onMessage("playerScore", 2);
                rightScore++;
                resetPong();
                notifyAll("scoreUpdate", 2);
            } else if (pong.isOutOfRightBound()) {
                //server.onMessage("playerScore", 1);
                leftScore++;
                resetPong();
                notifyAll("scoreUpdate", 1);
            } else {
                notifyAll("pongUpdate", this.pong.getPongCoordinates());
            }

            if (leftScore == 5) {
                server.onMessage("someoneWon", 1);
                stop();
            } else if (rightScore == 5) {
                server.onMessage("someoneWon", 2);
                stop();
            }

        } else {
            this.server.getLobby().unRegister(this.playerA);
            this.server.getLobby().unRegister(this.playerB);

            throw new RemoteException();
        }
    }

    @Override
    public int[] getPlayerScores() {
        return new int[]{leftScore, rightScore};
    }

    @Override
    public ISession getPlayerA() {
        return playerA;
    }

    @Override
    public ISession getPlayerB() {
        return playerB;
    }

    @Override
    public int getGameNumber() {
        return this.game_id;
    }

    @Override
    public IPong getPong() {
        return this.pong;
    }

    @Override
    public String getGameDetails() throws RemoteException {
        return "Game number: " + this.getGameNumber() + " - " + this.getPlayerA().getUsername() + " vs " + this.getPlayerB().getUsername();
    }

    private void resetPong() throws RemoteException {
        this.pause();
        this.pong = new Pong(this.playerA.getPlayerRectangle(), this.playerB.getPlayerRectangle(), this.playerA.getClientRectangle());
    }

}
