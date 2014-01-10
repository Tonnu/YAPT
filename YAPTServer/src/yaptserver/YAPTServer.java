/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yaptserver;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;
import yapt.RMI.IPong;
import yapt.RMI.ISession;
import yapt.RMI.IYAPTServer;
import yapt.RMI.Node;

/**
 *
 * @author Lenny
 */
public class YAPTServer extends Node<ISession> implements IYAPTServer {

    private ISession a, b;
    private int activeGames = 0;
    private Thread pongGameThread;
    private List<PongGame> games;
    private ExecutorService executor;
    /*TODO:
     IMPLEMENT THREADPOOL
     */

    public YAPTServer() throws RemoteException {
        //pong = new Pong(this);
        //pong.register(this);
        this.games = new ArrayList<>();
        executor = Executors.newFixedThreadPool(50);//50 threads
    }

    @Override
    public void register(ISession other) throws RemoteException {
        super.register(other); //To change body of generated methods, choose Tools | Templates.
        other.register(this);
    }

    @Override
    public void onMessage(String message, Object o) {
        try {
            super.onMessage(message); //To change body of generated methods, choose Tools | Templates.
            switch (message) {
                case "pushSessionUpdate":
                    ISession _temp = (ISession) o;
                    //recieved session update from one of the clients, notify the other
                    this.notifyAll("getSessionUpdate", _temp);
                    //if there's a session update, find the correct game belonging to the session and update it
                    for (PongGame game : games) {
                        if (game.getGameNumber() == (_temp.getGamePongNumber())) {
                            game.getSessionUpdate(_temp);
                        }
                    }
                    break;
                case "pongUpdate":
                    //update clients with new pong coordinates
                    notifyAll(message, (IPong) o);
                    break;
                case "newLookingForGame":
                    //if it's the first player registering for looking for game
                    if (a == null) {
                        this.a = (ISession) o;
                        //this.a.setPlayerNumber(1);
                        //this.a.setGamePongNumber(activeGames);
                        System.out.println("Player 1 has joined the que!");
                        break;
                        //if it's the second player looking for game
                    } else if (a != null && b == null) {
                        this.activeGames++;
                        this.b = (ISession) o;

                        this.a.onMessage("getPongGameNumber", activeGames);
                        this.b.onMessage("getPongGameNumber", activeGames);

                        this.a.onMessage("getPlayerNumber", 1);
                        this.b.onMessage("getPlayerNumber", 2);

                        //we have to notify the second player first he has found a game
                        //because player 1 has already made gameclient and a player + bat object
                        //if you would first notify player a that player b has joined and pass the session b object to session a,
                        //the game would start immediately, before player b has even made a player + bat object
                        this.b.onMessage("gameFound", a);
                        this.a.onMessage("gameFound", b);
                        final PongGame game = new PongGame(this, a, b, activeGames);
                        this.a = null;
                        this.b = null;
                        Runnable newGame = new Runnable() {
                            @Override
                            public void run() {
                                game.start();
                            }
                        };

                        games.add(game);
                        executor.execute(newGame);

                        System.out.println("Player 2 has joined the que and the game has started!");
                    } else {
                        notifyAll("Looking for game error!", null);
                    }
                    break;
                case "pushPongGameNumber":
                    notifyAll("getPongGameNumber", (int) o);
                    break;
                case "disc":
                    System.out.println("Someone disconnected!");
                    _temp = (ISession) o;
                    if (_temp.equals(a) && b == null) {
                        //disconnect happend while LFG
                        this.a = null;
                        this.unRegister(_temp);
                        System.out.println("Player 1 has left the que!");
                    } else {
                        //disconnected while in game
                        //disconnect other player as well for now
                        //find the correct game first

                        for (PongGame game : games) {
                            if (game.getGameNumber() == _temp.getGamePongNumber()) {
                                if ((_temp.equals(game.getPlayerB()) || _temp.equals(game.getPlayerB())) && 
                                        (game.getPlayerB() != null && game.getPlayerA() != null)) {
                                    game.stop();
                                    //send disconnect to other player
                                    b.onMessage("serverDisconnect", null);
                                    this.unRegister(b);

                                    a.onMessage("serverDisconnect", null);
                                    this.unRegister(a);

                                    games.remove(game);
                                    activeGames--;
                                    break;
                                }
                            }
                        }
                    }
                    break;
                case "someoneWon":
                //end the game;
                default:
                    System.out.println("Got unknown message:" + message);

            }
        } catch (RemoteException ex) {
            Logger.getLogger(YAPTServer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        //unwise
        System.setSecurityManager(null);
        try {
            final YAPTServer server = new YAPTServer();
            final IYAPTServer serverStub = (IYAPTServer) UnicastRemoteObject.exportObject(server, 0);
            final Registry registry = LocateRegistry.createRegistry(RMI_PORT);
            registry.rebind(IYAPTServer.class.getSimpleName(), serverStub);
            Logger.getLogger(IYAPTServer.class.getName()).log(Level.INFO, "started {0}", IYAPTServer.class.getSimpleName());
            //server.startPushing();
        } catch (Throwable t) {
            Logger.getLogger(YAPTServer.class.getName()).log(
                    Level.SEVERE,
                    "An error ocurred. Ensure that no RMI server is running, then run this class as follows:\n"
                    + "java -Djava.rmi.server.hostname=PUBLIC_SERVER_IP -cp RMI-project-1.0-SNAPSHOT.jar nl.fontys.vangeenen.rmi.ServerImpl\n"
                    + "The value PUBLIC_SERVER_IP must equal the publicly routable IP of the server",
                    t);
            System.exit(1);
        }
    }
}
