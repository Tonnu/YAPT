/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yaptserver;

import java.rmi.AccessException;
import java.rmi.NotBoundException;
import yapt.RMI.IPongGame;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;
import yapt.RMI.ILobby;
import yapt.RMI.ISession;
import yapt.RMI.IYAPTServer;
import yapt.RMI.Node;

/**
 *
 * @author Lenny
 */
public class YAPTServer extends Node<ISession> implements IYAPTServer {

    private int activeGames = 0;
    private List<IPongGame> games;
    private List<ISession> playersInQue;
    private ExecutorService executor;
    private ILobby lobby;

    @Override
    public Collection<IPongGame> getCurrentGames() throws RemoteException {
        return this.games;
    }

    public YAPTServer(ILobby lobby) throws RemoteException, AccessException, NotBoundException {
        this.games = Collections.synchronizedList(new ArrayList<IPongGame>());
        this.playersInQue = Collections.synchronizedList(new ArrayList<ISession>());
        this.lobby = lobby;
        executor = Executors.newFixedThreadPool(50);//50 threads

    }

    @Override
    public ILobby getLobby() {
        return this.lobby;
    }

    @Override
    public void register(ISession other) throws RemoteException {
        super.register(other);
        //other.register(this);
    }

    @Override
    public void registerByParameters(String username) {
    }

    @Override
    public void unRegister(ISession other) throws RemoteException {
        System.out.format("Unregistering %s @ server", other.getUsername());
        this.lobby.unRegister(other);
        super.unRegister(other);
    }

    @Override
    public void onMessage(String message, Object o) {
        try {
            super.onMessage(message);
            switch (message) {
                case "Connected":
                    this.lobby.register((ISession) o);
                    this.notifyAll("getGameList", this.games);
                    break;
                case "newGameWithOpponent":
                    ISession[] _players = (ISession[]) o;
                    int req = _players[1].recieveChallengeRequest(_players[0]);
                    break;
                case "acceptChallenge":
                    _players = (ISession[]) o;
                    createNewGame(_players[0], _players[1]);
                    break;
                case "newLookingForGame":
                    //first check if there is someone else in que for a game
                    if (playersInQue.isEmpty()) {
                        //if there's no one in que, add this player to the que
                        playersInQue.add((ISession) o);
                    } else {
                        //there is someone else in que, match the first one in que up with this new player
                        final ISession _tempA = playersInQue.get(0);
                        final ISession _tempB = (ISession) o; //new player LFG
                        System.out.format("Creating game with %s vs %s\n", _tempA.getUsername(), _tempB.getUsername());
                        createNewGame(_tempA, _tempB);
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
                                ex.printStackTrace();
                                break;
                            }
                        }
                    }
                    break;
                case "gameStopped":
                    IPongGame _game = (IPongGame) o;
                    games.remove((PongGame) _game);
                    _game = null;
                    this.notifyAll("getGameList", games);
                    this.notifyAll("getPlayerList", this.lobby.getOthers());
                    break;
                default:
                    System.out.println("Got unknown message: \n" + message);

            }
        } catch (RemoteException ex) {
            ex.printStackTrace();
        }

    }

    private void createNewGame(ISession _tempA, ISession _tempB) throws RemoteException {
        this.activeGames++;

        //remove players from que
        if (playersInQue.contains(_tempA)) {
            playersInQue.remove(_tempA);
        }

        if (playersInQue.contains(_tempB)) {
            playersInQue.remove(_tempB);
        }

        _tempA.onMessage("getPlayerNumber", 1);
        _tempB.onMessage("getPlayerNumber", 2);

        //we have to notify the second player first he has found a game
        //because player 1 has already made gameclient and a player + bat object
        //if you would first notify player a that player b has joined and pass the session b object to session a,
        //the game would start immediately, before player b has even made a player + bat object
        _tempB.onMessage("gameFound", _tempA);
        _tempA.onMessage("gameFound", _tempB);

        final PongGame game = new PongGame(this, _tempA, _tempB, activeGames);
        IPongGame gamestub = (IPongGame) UnicastRemoteObject.exportObject(game, 0);

        _tempA.onMessage("getPongGameInstance", (IPongGame) gamestub);
        _tempB.onMessage("getPongGameInstance", (IPongGame) gamestub);

        gamestub.register(_tempA);
        gamestub.register(_tempB);

        _tempA.register(gamestub);
        _tempB.register(gamestub);

        Runnable newGame = new Runnable() {
            @Override
            public void run() {
                try {
                    game.start();
                } catch (Exception ex) {
                    Logger.getLogger(YAPTServer.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        };

        games.add(game);
        this.notifyAll("getGameList", games);
        executor.execute(newGame);
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        //unwise
        System.setSecurityManager(null);
        try {
            final Lobby lobby = new Lobby();
            final ILobby lobbyStub = (ILobby) UnicastRemoteObject.exportObject(lobby, 1388);
            final YAPTServer server = new YAPTServer(lobby);
            final IYAPTServer serverStub = (IYAPTServer) UnicastRemoteObject.exportObject(server, 1388);
            final Registry registry = LocateRegistry.createRegistry(RMI_PORT);
            registry.rebind(IYAPTServer.class.getSimpleName(), serverStub);
            registry.rebind(ILobby.class.getSimpleName(), lobbyStub);

            Logger.getLogger(IYAPTServer.class
                    .getName()).log(Level.INFO, "started {0}", IYAPTServer.class
                            .getSimpleName());
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
