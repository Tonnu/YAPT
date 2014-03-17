/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yapt.GAME;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import yapt.GUI.LobbyPanel;
import yapt.GUI.YAPTPanel;
import yapt.RMI.ILobby;
import static yapt.RMI.INode.RMI_PORT;
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
    private YAPTPanel gamePanel;
    private LobbyPanel lobbyPanel;
    private String username;
    private ILobby lobby;
    private boolean challengeMode;
    private boolean isSpectating = false;

    public void setIsSpectating(boolean isSpectating) {
        this.isSpectating = isSpectating;
    }

    @Override
    public boolean isSpectating() {
        return isSpectating;
    }

    /**
     * *
     * Session manages a game between two players. It registers when the game
     * lookingForGames, ends or when a player leaves. It receives updates from
     * the server through RMI. It updates the YAPTClient with player and bat
     * positions.
     *
     * @param server
     * @throws java.rmi.RemoteException
     */
    public Session(String username, IYAPTServer server, YAPTPanel gamepanel, LobbyPanel lobbyPanel) throws RemoteException {
        try {
            this.pongGameNumber++;
            this.server = server;
            lookingForGame = false;
            this.gamePanel = gamepanel;
            this.lobbyPanel = lobbyPanel;
            this.username = username;
            Registry remoteRegistry = LocateRegistry.getRegistry("188.226.136.184", RMI_PORT);
            this.lobby = lobby = (ILobby)remoteRegistry.lookup(ILobby.class.getSimpleName());
            this.isSpectating = false;
        } catch (NotBoundException ex) {
            Logger.getLogger(Session.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    @Override
    public String getUsername() {
        return username;
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

    /**
     * Calls the GameClient's draw() method.
     *
     * @param g
     */
    public void draw(Graphics g) {
        try {
            this.game.draw(g);
        } catch (RemoteException ex) {
            Logger.getLogger(Session.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Calls the GameClient's update() method.
     *
     * @param direction the direction in which the bat is moving (1=UP, -1=DOWN)
     */
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
            case "spectating":

                pongGame = (IPongGame) o;
                pongGame.register(this);

                game = new GameClient(this, pongGame.getPlayerA(), pongGame.getPlayerB());

                System.out.println("got spectating!");
                this.isSpectating = true;
                this.gameStarted = true;
                this.gameInterrupted = false;
                this.lookingForGame = false;

                break;
            case "scoreUpdate":
                if ((int) o == 1) {
                    this.getGameClient().getPlayer().score();
                } else {
                    this.getGameClient().getOpponent().score();
                }
                break;
            case "getPlayerList":
                lobbyPanel.setOnlinePlayers((Collection<ISession>) o);
                break;
            case "getGameList":
                List<String> gameStringList = new ArrayList<String>();
                List<IPongGame> gameList = (List<IPongGame>) o;
                for (IPongGame game : gameList) {
                    gameStringList.add(game.getGameDetails());
                }
                lobbyPanel.setGameList(gameStringList);
                break;
            case "GetPublicChatMessage":
                String chatMessage = (String) o;
                lobbyPanel.newMessage(chatMessage);
                break;
            case "GetGameChatMessage":
                chatMessage = (String) o;
                gamePanel.newMessage(chatMessage);
                break;
            case "SendPublicChatMessage":
                lobby.onMessage("PublicChatMessage", (String) o);
                break;
            case "SendGameChatMessage":
                pongGame.onMessage("GameChatMessage", (String) o);
                break;
            case "pongUpdate":
                Vector2f _tempcoords = (Vector2f) o;
                if (this.game != null) {
                    this.game.setPongCoordinates(_tempcoords); //needed for drawing
                }
                break;
            case "pushBatUpdate":
                if (!gameInterrupted || pongGame == null) {
                    //my bat moved, notify server
                    System.out.println("pushing batupdate to server");
                    pongGame.onMessage("pushSessionUpdate", this); //push new sessionstate to server
                }
                break;
            case "getSessionUpdate":
                //opponent's session updated
                //set opponent's bat for drawing purposes
                if (!gameInterrupted) {
                    Vector2f _opponentPosition = (Vector2f) o;
                    this.game.getOpponent().setBatCoordinates(_opponentPosition); //needed for drawing 
                }
                break;
            case "spectatorUpdate":
                if (isSpectating && !gameInterrupted) {
                    ISession[] _players = (ISession[]) o;
                    if (_players != null) {
                        this.game.getPlayer().setBatCoordinates(_players[0].getPlayerPosition());
                        this.game.getOpponent().setBatCoordinates(_players[1].getPlayerPosition());
                        this.game.getPlayer().setScore(this.pongGame.getPlayerScores()[0]);
                        this.game.getOpponent().setScore(this.pongGame.getPlayerScores()[1]);
                    }
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
                game = new GameClient(this, false);
                gameStarted = true;
                lookingForGame = false;
                gameInterrupted = false;
                ISession _temp = (ISession) o;

                this.game.setOpponent(new Player("Other", _temp)); //needed for drawing
                if (this.getPlayerNumber() == 1) {
                    //spawn at the left side
                    this.game.getPlayer().setBatCoordinates(new Vector2f(0, 150));
                } else {
                    //spawn at right side
                    this.game.getPlayer().setBatCoordinates(new Vector2f((float) ((float) this.gamePanel.getWidth() - this.game.getPlayer().getBat().getRectangle().getWidth()), 150));
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
                break;
            case "cancelChallengeRequest":
                lookingForGame = false;
                gameInterrupted = true;
                gameStarted = false;
                break;
            case "serverDisconnect":
                this.pongGame = null;
                gameStarted = false;
                isSpectating = false;
                gameInterrupted = true;
                lookingForGame = false;
                game.resetPlayer();
                lobbyPanel.showPanel();
                break;
            default:
                System.out.println("Session didn't recognize: " + message);
        }
    }

    @Override
    public boolean isGameStarted() {
        return gameStarted;
    }

    @Override
    public IPongGame getPongGame() {
        return pongGame;
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

    @Override
    public Rectangle getClientRectangle() throws RemoteException {
        return this.gamePanel.getGameField();

    }

    public ISession getplayers(String _username) throws RemoteException {
        for (Iterator it = this.lobby.getOthers().iterator(); it.hasNext();) {
            ISession s = (ISession) it.next();
            if (s.getUsername().equals(_username)) {
                return s;
            }
        }
        return null;
    }

    /**
     * Notifies the server that this player wants to challenge another player.
     *
     * @param _opponent the player to be challenged.
     * @return -1 if the player could not be challenged and 1 if the player was
     * challenged successfully.
     */
    public int challengePlayer(ISession _opponent) {
        gameInterrupted = false;
        challengeMode = true;
        try {
            if (_opponent.isGameStarted()) {
                return -1;
            } else {
                game = new GameClient(this, true);

                if (!lookingForGame) {
                    if (this.lobby.getOthers().contains(_opponent)) {
                        lookingForGame = false;
                        gameStarted = true;
                        ISession[] _players = {this, _opponent};
                        server.onMessage("newGameWithOpponent", _players);
                        return 1;
                    } else {
                        System.out.println("Opponent not found!");
                        return -1;
                    }
                }
            }
        } catch (RemoteException ex) {
            Logger.getLogger(Session.class.getName()).log(Level.SEVERE, null, ex);
        }
        return -1;
    }

    /**
     * This method is called when the player gets challenged by another player.
     * It shows a dialog where the user can either accept or decline the
     * challenge. Also notifies the server when the user accepts the challenge.
     *
     * @param _opponent the opponent that is challenging this user.
     * @return the result of the dialog (0=accepted, -1=declined)
     * @throws java.rmi.RemoteException if the server can not be notified.
     */
    @Override
    public int recieveChallengeRequest(ISession _opponent) throws RemoteException {
        if (lobbyPanel.spawnChallengeRequest() == 0) {
            game = new GameClient(this, true);
            lookingForGame = false;
            gameInterrupted = false;
            challengeMode = true;
            ISession[] _players = {this, _opponent};
            server.onMessage("acceptChallenge", _players);
            return 0;
        }
        return -1;
    }

    @Override
    public boolean getChallengeMode() {
        return challengeMode;
    }

    public void setChallengeMode(boolean challengeMode) {
        this.challengeMode = challengeMode;
    }

}
