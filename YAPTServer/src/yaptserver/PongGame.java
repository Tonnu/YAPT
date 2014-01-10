/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yaptserver;

import java.rmi.RemoteException;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;
import yapt.RMI.*;

/**
 *
 * @author Toon
 */
public class PongGame implements IPongGame {

    private final YAPTServer server;
    private Pong pong;
    private ISession left, right;
    private boolean game_started;
    private final int game_id;
    private Timer t;
    private int leftScore, rightScore = 0;
    
    TimerTask tt = new TimerTask() {

        @Override
        public void run() {
            update();
        }
    };

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
        server.onMessage("pushPongGameNumber", this.game_id);
        this.game_started = false;
        this.server = (YAPTServer) server;
        this.left = A;
        this.right = B;
        this.pong = new Pong(left.getPlayerRectangle(), right.getPlayerRectangle());
        t = new Timer();
        //this.start(); //start the game immediately, for now
    }

    public void start() {
        try {
            this.pong = new Pong(left.getPlayerRectangle(), right.getPlayerRectangle());
            t = new Timer();
            t.schedule(tt, 1000, 16);
        } catch (RemoteException ex) {
            Logger.getLogger(PongGame.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void stop() {
        t.cancel();
    }

    private void update() {
        try {
            //pass player rectangles to pong
            this.pong.update(this.left.getPlayerRectangle(), this.right.getPlayerRectangle());
        } catch (RemoteException ex) {
            Logger.getLogger(PongGame.class.getName()).log(Level.SEVERE, null, ex);
        }
        if (pong.isOutOfLeftBound()) {
            //server.onMessage("playerScore", 2);
            rightScore++;
            stop();
        } else if (pong.isOutOfRightBound()) {
            //server.onMessage("playerScore", 1);
            leftScore++;
            stop();
        } else {
            server.onMessage("pongUpdate", (IPong) this.pong);
        }
        
        if(leftScore == 5){
            server.onMessage("someoneWon", 1);
            stop();
        }else if(rightScore == 5){
            server.onMessage("someoneWon", 2);
            stop();
        }
    }

    public int[] getPlayerScores(){
        return new int[]{leftScore,rightScore};
    }
    
    
    public ISession getPlayerA(){
        return left;
    }
    
    public ISession getPlayerB(){
        return right;
    }
    
    public void getSessionUpdate(ISession _temp) {
        try {
            if (_temp.getPlayerNumber() == this.left.getPlayerNumber()) {
                //recieved left player update;
                this.left = _temp;
            } else if (_temp.getPlayerNumber() == this.right.getPlayerNumber()) {
                //recieved right player update;
                this.right = _temp;
            }
        } catch (RemoteException ex) {
            Logger.getLogger(PongGame.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public int getGameNumber() {
        return this.game_id;
    }

    public IPong getPong() {
        return this.pong;
    }
}
