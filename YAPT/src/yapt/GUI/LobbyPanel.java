/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yapt.GUI;

import java.awt.CardLayout;
import java.awt.event.KeyEvent;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JPanel;
import yapt.GAME.Session;
import yapt.RMI.ILobby;
import static yapt.RMI.INode.RMI_PORT;
import yapt.RMI.IPongGame;
import yapt.RMI.ISession;
import yapt.RMI.IYAPTServer;

/**
 *
 * @author tonnu
 */
public class LobbyPanel extends javax.swing.JPanel {

    private String username;
    private IYAPTServer server;
    private Session sessionImpl;
    private YAPTPanel gamePanel;
    private CardLayout cl;
    private JPanel cards;
    private List<IPongGame> pongGames;
    private List<ISession> onlinePlayers;
    //private ILobby lobby;

    /**
     * Creates new form LobbyPanel
     */
    LobbyPanel(CardLayout cl) {
        initComponents();
        this.cl = cl;

        pongGames = new ArrayList<>();
        onlinePlayers = new ArrayList<>();
    }

    public void newMessage(String chatMessage) {
        //System.out.println("Someone is calling newMessage()");
        this.jTextArea1.append(chatMessage + "\n");
    }

    public void addNewGame(IPongGame game) {
        this.pongGames.add(game);
    }

    public void addPlayer(ISession player) {
        this.onlinePlayers.add(player);
    }

    public void removeGame(IPongGame game) {
        this.pongGames.remove(game);
    }

    public void removePlayer(ISession player) {
        this.onlinePlayers.remove(player);
    }

    public void tryLogin(String serverAddress, String _username, YAPTPanel gamepanel, JPanel cards) throws RemoteException, NotBoundException, MalformedURLException {
        this.username = _username;
        this.gamePanel = gamepanel;
        this.cards = cards;
        //unwise
        System.setSecurityManager(null);
        //String serverAddress = (GameFrame.ARGS.length < 1) ? "localhost" : GameFrame.ARGS[0];
        //aserverAddress = "188.226.136.184";
        //when trying to find a game, try to connect to server first

        //register clientStub at remote server
        //Registry remoteRegistry = LocateRegistry.getRegistry(serverAddress, RMI_PORT);
        server = (IYAPTServer) Naming.lookup(IYAPTServer.class.getSimpleName());
        //server = (IYAPTServer) remoteRegistry.lookup(IYAPTServer.class.getSimpleName());
        //lobby = (ILobby) remoteRegistry.lookup(ILobby.class.getSimpleName());
        //create RMI-stub for a ClientImpl
        //lobby = (ILobby) Naming.lookup(ILobby.class.getSimpleName());
        sessionImpl = new Session(username, server, gamepanel, this);
        final ISession sessionStub = (ISession) UnicastRemoteObject.exportObject(sessionImpl, 0);

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

        jTextArea1.setColumns(20);
        jTextArea1.setRows(5);
        jScrollPane1.setViewportView(jTextArea1);

        jTextField1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField1ActionPerformed(evt);
            }
        });
        jTextField1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jTextField1KeyTyped(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextField1KeyReleased(evt);
            }
        });

        lst_currentGames.setModel(new javax.swing.AbstractListModel() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        jScrollPane2.setViewportView(lst_currentGames);

        lst_onlinePlayers.setModel(new javax.swing.AbstractListModel() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        jScrollPane3.setViewportView(lst_onlinePlayers);

        btn_joinGame.setText("Join Game");
        btn_joinGame.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_joinGameActionPerformed(evt);
            }
        });

        btn_startGame.setText("Start Game");

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
                        .addGap(177, 177, 177)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 340, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(66, 66, 66)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(btn_startGame, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(btn_joinGame, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 340, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(184, 184, 184)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 373, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 373, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(37, 37, 37)
                        .addComponent(btn_joinGame)
                        .addGap(70, 70, 70)
                        .addComponent(btn_startGame)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 57, Short.MAX_VALUE)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 268, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(10, 10, 10)
                .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void jTextField1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField1ActionPerformed

    private void jTextField1KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField1KeyTyped
        // TODO add your handling code here:
        //jTextArea1.append("" + evt.getKeyCode());       
    }//GEN-LAST:event_jTextField1KeyTyped

    private void btn_joinGameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_joinGameActionPerformed
        try {
            // TODO add your handling code here:
            this.cl.show(cards, "Game");
            this.gamePanel.start(sessionImpl, this, cards);

        } catch (RemoteException ex) {
            Logger.getLogger(LobbyPanel.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NotBoundException ex) {
            Logger.getLogger(LobbyPanel.class.getName()).log(Level.SEVERE, null, ex);
        } catch (MalformedURLException ex) {
            Logger.getLogger(LobbyPanel.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_btn_joinGameActionPerformed

    private void jTextField1KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField1KeyReleased
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_ENTER && !jTextField1.getText().equals("")) {
            try {
                this.sessionImpl.onMessage("SendPublicChatMessage", username + ": " + jTextField1.getText());
                this.jTextField1.setText("");
            } catch (RemoteException ex) {
                Logger.getLogger(LobbyPanel.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }//GEN-LAST:event_jTextField1KeyReleased


    // Variables declaration - do not modify//GEN-BEGIN:variables
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
