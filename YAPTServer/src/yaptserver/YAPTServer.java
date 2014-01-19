/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yaptserver;

import yapt.RMI.PongGame;
import yapt.RMI.IPongGame;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Level;
import java.util.logging.Logger;
import yapt.RMI.ISession;
import yapt.RMI.IYAPTServer;
import yapt.RMI.Node;

/**
 *
 * @author Lenny
 */
public class YAPTServer extends Node<ISession> implements IYAPTServer {

    private int activeGames = 0;
    private List<PongGame> games;
    private List<ISession> playersInQue;
    private ExecutorService executor;
    private final Lock lock = new ReentrantLock();

    /*TODO:
     IMPLEMENT THREADPOOL
     */
    public YAPTServer() throws RemoteException {
        //pong = new Pong(this);
        //pong.register(this);
        this.games = Collections.synchronizedList(new ArrayList<PongGame>());
        this.playersInQue = Collections.synchronizedList(new ArrayList<ISession>());
        executor = Executors.newFixedThreadPool(50);//50 threads

    }

    @Override
    public void register(ISession other) throws RemoteException {
        super.register(other);
        //other.register(this);
    }

    @Override
    public void onMessage(String message, Object o) {
        try {
            super.onMessage(message);
            switch (message) {
                case "newLookingForGame":
                    //first check if there is someone else in que for a game
                    if (playersInQue.isEmpty()) {
                        //if there's no one in que, add this player to the que
                        playersInQue.add((ISession) o);
                    } else {
                        //there is someone else in que, match the first one in que up with this new player
                        this.activeGames++;
                        ISession _tempA = playersInQue.get(0); //first one in que
                        ISession _tempB = (ISession) o; //new player LFG

                        //remove players from que
                        playersInQue.remove(_tempA);
                        playersInQue.remove(_tempB);

                        final PongGame game = new PongGame(this, _tempA, _tempB, activeGames);
                        _tempA.onMessage("getPongGameInstance", (IPongGame) game);
                        _tempB.onMessage("getPongGameInstance", (IPongGame) game);
                        
                        _tempA.onMessage("getPlayerNumber", 1);
                        _tempB.onMessage("getPlayerNumber", 2);

                        game.register(_tempA);
                        game.register(_tempB);

                        _tempA.register(game);
                        _tempB.register(game);

                        //we have to notify the second player first he has found a game
                        //because player 1 has already made gameclient and a player + bat object
                        //if you would first notify player a that player b has joined and pass the session b object to session a,
                        //the game would start immediately, before player b has even made a player + bat object
                        _tempB.onMessage("gameFound", _tempA);
                        _tempA.onMessage("gameFound", _tempB);

                        Runnable newGame = new Runnable() {
                            @Override
                            public void run() {
                                game.start();
                            }
                        };

                        games.add(game);
                        executor.execute(newGame);

                    }
                    break;
                case "leftQue":
                    //find out who disconnected
                    ISession _temp = (ISession) o;

                    for (ISession _player : playersInQue) {
                        if (_player.equals(_temp)) {
                            //if he was in que, remove him from it
                            try {
                                playersInQue.remove(_player);
                                System.out.println("A player has left the que!");
                                break;
                            } catch (Exception ex) {
                                playersInQue.remove(_player);
                                break;
                            }
                        }
                    }
                case "gameStopped":
                    IPongGame _game = (IPongGame) o;
                    games.remove((PongGame) _game);
                    break;
                case "someoneWon":
                //end the game;
                default:
                    System.out.println("Got unknown message:" + message);

            }
        } catch (RemoteException ex) {
            ex.printStackTrace();
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
            registry
                    .rebind(IYAPTServer.class
                            .getSimpleName(), serverStub);
            Logger.getLogger(IYAPTServer.class
                    .getName()).log(Level.INFO, "started {0}", IYAPTServer.class
                            .getSimpleName());
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
