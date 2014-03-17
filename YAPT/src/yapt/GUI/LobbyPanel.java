/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yapt.GUI;

import java.awt.CardLayout;
import java.awt.Component;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.RMISocketFactory;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultListModel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import yapt.GAME.Session;
import yapt.RMI.ILobby;
import static yapt.RMI.INode.RMI_PORT;
import yapt.RMI.IPongGame;
import yapt.RMI.ISession;
import yapt.RMI.IYAPTServer;
import yapt.RMI.StaticPortRMISocketFactory;

/**
 *
 * @author tonnu
 */
public class LobbyPanel extends javax.swing.JPanel {

    private String username;
    private IYAPTServer server;
    private Session sessionImpl;

    private YAPTPanel gamePanel;
    private final CardLayout cl;
    private JPanel cards;
    private List<ISession> onlinePlayers;
    private DefaultListModel players, pongGames;
    private ILobby lobby;

    /**
     * Creates new form LobbyPanel
     */
    LobbyPanel(CardLayout cl) {
        initComponents();
        this.cl = cl;

        onlinePlayers = new ArrayList<>();
        players = new DefaultListModel<>();
        pongGames = new DefaultListModel<>();

    }

    public Session getSessionImpl() {
        return sessionImpl;
    }

    /**
     * Updates the GUI with a new list of currently online players.
     *
     * @param onlinePlayers The new list of online players.
     * @throws RemoteException If the new list can not be retrieved.
     */
    public void setOnlinePlayers(Collection<ISession> onlinePlayers) throws RemoteException {
        players.clear();
        for (ISession is : onlinePlayers) {
            players.addElement(is.getUsername());
        }
        this.lst_onlinePlayers.setModel(players);
    }

    /**
     * Appends a new message to the lobby chat area.
     *
     * @param chatMessage The message to be appended.
     */
    public void newMessage(String chatMessage) {
        //System.out.println("Someone is calling newMessage()");
        this.jTextArea1.append(chatMessage + "\n");
    }

    /**
     * Updates the GUI with a new list of games that are currently being played.
     *
     * @param newGameList The new list of games.
     * @throws RemoteException If the new games list can not be retrieved.
     */
    public void setGameList(List<String> newGameList) throws RemoteException {
        pongGames.clear();
        for (String _game : newGameList) {
            pongGames.addElement(_game);
        }
        this.lst_currentGames.setModel(pongGames);
    }

    /**
     * Tries to establish a connection to the server lobby through RMI. Checks
     * if the username is already taken, and if not subscribes to the Lobby &
     * Server. If the username IS taken, the user gets notified and will be
     * returned to the login screen.
     *
     * @param serverAddress The address of the remote (RMI) server.
     * @param username The username to login with
     * @param gamepanel The Gamepanel on which games will be played.
     * @param cards The cards containing all possible Swing Panels (Lobby &
     * Game)
     * @throws RemoteException If the client can not be connected to the remote
     * server.
     * @throws NotBoundException If the client can not find the Gameserver in
     * the RMI registry
     * @throws MalformedURLException If the client can not fiend the remote
     * server.
     */
    public void tryLogin(String serverAddress, String username, YAPTPanel gamepanel, JPanel cards) throws RemoteException, NotBoundException, MalformedURLException, IOException {
        this.username = username;
        this.gamePanel = gamepanel;
        this.cards = cards;
        //RMISocketFactory.setSocketFactory(new StaticPortRMISocketFactory());

        //unwise
        System.setSecurityManager(null);
        //String serverAddress = (GameFrame.ARGS.length < 1) ? "localhost" : GameFrame.ARGS[0];
        //aserverAddress = "188.226.136.184";
        //when trying to find a game, try to connect to server first

        //register clientStub at remote server
        Registry remoteRegistry = LocateRegistry.getRegistry(serverAddress, RMI_PORT);
        //server = (IYAPTServer) Naming.lookup("188.226.136.184/rmi/YAPT_RMI/build/classes/yapt/RMI/" + IYAPTServer.class.getSimpleName());
        server = (IYAPTServer) remoteRegistry.lookup(IYAPTServer.class.getSimpleName());
        lobby = (ILobby) remoteRegistry.lookup(ILobby.class.getSimpleName());
        //create RMI-stub for a ClientImpl
        //lobby = (ILobby) Naming.lookup(ILobby.class.getSimpleName());

        for (Iterator it = lobby.getOthers().iterator(); it.hasNext();) {
            Object object = it.next();
            ISession _s = (ISession) object;

            if (_s.getUsername().equals(username)) {
                JOptionPane.showMessageDialog((Component) null, "Someone is already currently logged in with this username. Please pick another one.",
                        "alert", JOptionPane.OK_OPTION);
                throw new RemoteException("Username taken");
            }
        }
        sessionImpl = new Session(username, server, gamepanel, this);
        final ISession sessionStub = (ISession) UnicastRemoteObject.exportObject(sessionImpl, 1099);

        server.register(sessionStub);

        //start pushing messages to the server
        server.onMessage("Connected", sessionImpl);
    }

    public void showPanel() {
        this.cl.show(cards, "Lobby");
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();
        jTextField1 = new javax.swing.JTextField();
        jScrollPane2 = new javax.swing.JScrollPane();
        lst_currentGames = new javax.swing.JList();
        jScrollPane3 = new javax.swing.JScrollPane();
        lst_onlinePlayers = new javax.swing.JList();
        btn_joinGame = new javax.swing.JButton();
        btn_startGame = new javax.swing.JButton();
        btn_Challenge = new javax.swing.JButton();

        jTextArea1.setColumns(20);
        jTextArea1.setRows(5);
        jScrollPane1.setViewportView(jTextArea1);

        jTextField1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextField1KeyReleased(evt);
            }
        });

        lst_currentGames.setModel(lst_currentGames.getModel());
        jScrollPane2.setViewportView(lst_currentGames);

        lst_onlinePlayers.setModel(lst_onlinePlayers.getModel());
        jScrollPane3.setViewportView(lst_onlinePlayers);

        btn_joinGame.setText("Spectate");
        btn_joinGame.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_joinGameActionPerformed(evt);
            }
        });

        btn_startGame.setLabel("Start New Game");
        btn_startGame.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_startGameActionPerformed(evt);
            }
        });

        btn_Challenge.setText("Challenge Player");
        btn_Challenge.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_ChallengeActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 1256, Short.MAX_VALUE)
                    .addComponent(jTextField1)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(178, 178, 178)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 340, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btn_startGame, javax.swing.GroupLayout.PREFERRED_SIZE, 137, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(37, 37, 37)
                        .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 340, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(184, 184, 184)))
                .addContainerGap())
            .addGroup(layout.createSequentialGroup()
                .addGap(306, 306, 306)
                .addComponent(btn_joinGame, javax.swing.GroupLayout.PREFERRED_SIZE, 131, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btn_Challenge)
                .addGap(286, 286, 286))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 373, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 373, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(btn_startGame))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 14, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btn_joinGame)
                    .addComponent(btn_Challenge))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 268, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(10, 10, 10)
                .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    /**
     * Attempts to join the selected game as a spectator.
     *
     * @param evt The button event
     */
    private void btn_joinGameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_joinGameActionPerformed
        if (this.lst_currentGames.getSelectedIndex() != -1) {
            try {
                this.cl.show(cards, "Game");
                IPongGame spectatingGame = null;
                for (IPongGame game : this.server.getCurrentGames()) {
                    if (game.getGameDetails().equals(this.lst_currentGames.getSelectedValue())) {
                        spectatingGame = game;
                        System.out.println("found game");
                        break;
                    }
                }
                if (spectatingGame != null) {
                    this.gamePanel.joinGameAsSpectator(sessionImpl, spectatingGame, cards);
                }
            } catch (RemoteException ex) {
                Logger.getLogger(LobbyPanel.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }//GEN-LAST:event_btn_joinGameActionPerformed

    /**
     * Handles the sending of chat messages in the lobby. When a user presses
     * the "ENTER" key and the message in the messagefield is NOT empty, the
     * message will be sent to all other clients in the lobby.
     *
     * @param evt The Key event
     */
    private void jTextField1KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField1KeyReleased
        if (evt.getKeyCode() == KeyEvent.VK_ENTER && !jTextField1.getText().equals("")) {
            try {
                this.sessionImpl.onMessage("SendPublicChatMessage", username + ": " + jTextField1.getText());
                this.jTextField1.setText("");
            } catch (RemoteException ex) {
                Logger.getLogger(LobbyPanel.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }//GEN-LAST:event_jTextField1KeyReleased

    /**
     * Sends a Looking For Game request to the server. Once another player has
     * been found, a game will commence.
     *
     * @param evt The button event
     */
    private void btn_startGameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_startGameActionPerformed
        try {
            this.cl.show(cards, "Game");
            this.gamePanel.lookingForGame(sessionImpl, cards);
        } catch (RemoteException | NotBoundException | MalformedURLException ex) {
            Logger.getLogger(LobbyPanel.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_btn_startGameActionPerformed

    /**
     * Challenges the selected player to a game. The opponent will receive a
     * popup containing a request for a new pong game.
     *
     * @param evt The button event
     */
    private void btn_ChallengeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_ChallengeActionPerformed
        if (this.lst_onlinePlayers.getSelectedIndex() != -1) {
            try {
                ISession opponent = this.sessionImpl.getplayers((String) this.lst_onlinePlayers.getSelectedValue());
                if (opponent.getUsername().equals(this.sessionImpl.getUsername())) {
                    JOptionPane.showMessageDialog((Component) null, "You cannot challenge yourself to a game... :-(",
                            "alert", JOptionPane.OK_OPTION);
                } else {
                    int result = this.sessionImpl.challengePlayer(opponent);
                    if (result == -1) {
                        JOptionPane.showMessageDialog((Component) null, "The selected player is currently not available for playing a game.",
                                "alert", JOptionPane.OK_OPTION);
                    } else if (result == 1) {
                        this.cl.show(cards, "Game");
                        this.gamePanel.challenge(sessionImpl, cards);
                    }
                }
            } catch (RemoteException | NotBoundException | MalformedURLException ex) {
                Logger.getLogger(LobbyPanel.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    /**
     * Notfies the user that another player is challenging him to a game of
     * Pong. If the user accepts, the game will be started. If, not nothing will
     * happen.
     *
     * @return The result of the dialog (0 for accepted)
     */
    public int spawnChallengeRequest() {
        try {
            int result = JOptionPane.showConfirmDialog((Component) null, "Someone has requested to play a game with you! Accept?",
                    "alert", JOptionPane.OK_CANCEL_OPTION);
            if (result == 0) {
                this.cl.show(cards, "Game");
                this.gamePanel.lookingForGame(sessionImpl, cards);
                return result;
            }
        } catch (RemoteException | NotBoundException | MalformedURLException ex) {
            Logger.getLogger(LobbyPanel.class.getName()).log(Level.SEVERE, null, ex);
        }

        return 0;

    }//GEN-LAST:event_btn_ChallengeActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btn_Challenge;
    private javax.swing.JButton btn_joinGame;
    private javax.swing.JButton btn_startGame;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTextArea jTextArea1;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JList lst_currentGames;
    private javax.swing.JList lst_onlinePlayers;
    // End of variables declaration//GEN-END:variables

}
