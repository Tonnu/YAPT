/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yapt.GUI;

import java.awt.CardLayout;
import java.awt.Graphics;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JPanel;
import yapt.GAME.KeyListener;
import yapt.GAME.Session;
import static yapt.RMI.INode.RMI_PORT;
import yapt.RMI.ISession;
import yapt.RMI.IYAPTServer;

/**
 *
 * @author Toon
 */
public class YAPTPanel extends javax.swing.JPanel {

    private Session sessionImpl;
    private IYAPTServer server;
    private KeyListener keyListener = new KeyListener();
    private CardLayout cl;
    private Thread gameloop;
    private JPanel cards;
    private LobbyPanel lobbyPanel;

    Runnable r = new Runnable() {

        @Override
        public void run() {
            while (!hasGameStarted()) {
                try {
                    //wait...
                    Thread.sleep(1);
                } catch (InterruptedException ex) {
                    //search canceld!
                }
            }

            Timer t = new Timer();

            TimerTask tt = new TimerTask() {

                @Override
                public void run() {
                    repaint();
                    update();
                }
            };

            t.schedule(tt, 1000, 33); //30 FPS TODO need to synch with server ponggame
        }
    };

    /**
     * Creates new form YAPTPanel that manages menus, buttons, etc.
     */
    public YAPTPanel() {
        initComponents();
        this.setFocusable(true);
        this.requestFocusInWindow();
        this.addKeyListener(keyListener);

    }

    YAPTPanel(CardLayout cl) {
        initComponents();
        this.setFocusable(true);
        this.requestFocusInWindow();
        this.addKeyListener(keyListener);
        this.cl = cl;
    }

    public KeyListener getKeyListener() {
        return this.keyListener;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g); //To change body of generated methods, choose Tools | Templates.
        if (hasGameStarted()) {
            sessionImpl.draw(g);
        }
    }

    public void update() {
        if (hasGameStarted()) {
            if (keyListener.isUp()) {
                sessionImpl.update(-1);
            } else if (keyListener.isDown()) {
                sessionImpl.update(1);
            } else {
                sessionImpl.update(0);
            }
        } else if (!hasGameStarted() && hasGameStopped()) {
            //game was started but interrupted (so hasGameStarted() is false)
            gameloop.interrupt();
            button1.setLabel("Find game!");
        }
    }

    public boolean hasGameStarted() {
        if (sessionImpl != null) {
            return sessionImpl.gameStarted;
        }
        return false;
    }

    public boolean hasGameStopped() {
        return sessionImpl.gameInterrupted;
    }

    public boolean isLookingForGame() {
        if (sessionImpl != null) {
            return sessionImpl.lookingForGame;
        }
        return false;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        button1 = new java.awt.Button();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTextPane1 = new javax.swing.JTextPane();

        setPreferredSize(new java.awt.Dimension(1280, 760));
        addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                formMouseClicked(evt);
            }
        });

        button1.setActionCommand("findGame");
        button1.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        button1.setLabel("Find Game!");
        button1.setName("FindGame"); // NOI18N
        button1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                button1ActionPerformed(evt);
            }
        });

        jTextArea1.setColumns(20);
        jTextArea1.setRows(5);
        jScrollPane1.setViewportView(jTextArea1);

        jScrollPane2.setViewportView(jTextPane1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jScrollPane1))
                .addContainerGap())
            .addGroup(layout.createSequentialGroup()
                .addGap(468, 468, 468)
                .addComponent(button1, javax.swing.GroupLayout.PREFERRED_SIZE, 174, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(638, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap(242, Short.MAX_VALUE)
                .addComponent(button1, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(228, 228, 228)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 164, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(7, 7, 7)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void formMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_formMouseClicked
    }//GEN-LAST:event_formMouseClicked

    private void button1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_button1ActionPerformed
        try {
            if (!isLookingForGame() && (sessionImpl == null || !hasGameStarted())) {
                //unwise
                System.setSecurityManager(null);
                //String serverAddress = (GameFrame.ARGS.length < 1) ? "localhost" : GameFrame.ARGS[0];
                String serverAddress = "localhost";
                //when trying to find a game, try to connect to server first

                //register clientStub at remote server
                Registry remoteRegistry = LocateRegistry.getRegistry(serverAddress, RMI_PORT);
                server = (IYAPTServer) remoteRegistry.lookup(IYAPTServer.class.getSimpleName());

                //create RMI-stub for a ClientImpl
                //sessionImpl = new Session(server, this);
                final ISession sessionStub = (ISession) UnicastRemoteObject.exportObject(sessionImpl, 0);

                server.register(sessionStub);

                //start pushing messages to the server
                server.onMessage("Connected");

                sessionImpl.onMessage("pushLookingForGame", null);
                button1.setLabel("Disconnect...");

                this.setFocusable(true);
                this.requestFocusInWindow();

                gameloop = new Thread(r);
                gameloop.start();

                //if we are looking for game and button is pressed, we should disconnect from the server
                //OR if we're playing a game (gameStarted == true) and we pushed the button, we should also disc
            } else if (hasGameStarted()) {
                System.out.println("Leaving game!");
                gameloop.interrupt();
                sessionImpl.onMessage("pushDisconnect", null);

                button1.setLabel("Find Game!");
                cl.show(cards, "Lobby");

            } else if (isLookingForGame()) {
                System.out.println("Leaving que!");
                gameloop.interrupt();
                sessionImpl.onMessage("leaveQue", null);
                cl.show(cards, "Lobby");
                button1.setLabel("Find Game!");
            }
        } catch (NotBoundException | RemoteException t) {
            Logger.getLogger(IYAPTServer.class.getName()).log(
                    Level.SEVERE,
                    "An error ocurred. Ensure that no RMI server is running, then run this class as follows:\n"
                    + "java -Djava.rmi.server.hostname=PUBLIC_CLIENT_IP -cp RMI-project-1.0-SNAPSHOT.jar nl.fontys.vangeenen.rmi.ClientImpl PUBLIC_SERVER_IP\n"
                    + "* The value PUBLIC_SERVER_IP must equal the publicly routable IP of the server.\n"
                    + "* The value PUBLIC_CLIENT_IP must equal YOUR routable IP.",
                    t
            );
            System.exit(1);
        }

    }//GEN-LAST:event_button1ActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private java.awt.Button button1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTextArea jTextArea1;
    private javax.swing.JTextPane jTextPane1;
    // End of variables declaration//GEN-END:variables

    public void newMessage(String chatMessage) {
        this.jTextArea1.append(chatMessage);
    }

    public void start(Session sessionImpl, LobbyPanel lobbypanel, JPanel cards) throws RemoteException {
        this.sessionImpl = sessionImpl;
        this.lobbyPanel = lobbypanel;
        this.cards = cards;
        if (!isLookingForGame() && (this.sessionImpl == null || !hasGameStarted())) {
            this.sessionImpl.onMessage("pushLookingForGame", null);
            this.setFocusable(true);
            this.requestFocusInWindow();

            gameloop = new Thread(r);
            gameloop.start();

            button1.setLabel("Disconnect...");
        } else if (hasGameStarted()) {
            System.out.println("Leaving game!");
            gameloop.interrupt();
            sessionImpl.onMessage("pushDisconnect", null);

            button1.setLabel("Find Game!");
            cl.show(lobbypanel, "Lobby");
        } else if (isLookingForGame()) {
            System.out.println("Leaving que!");
            gameloop.interrupt();
            sessionImpl.onMessage("leaveQue", null);

            button1.setLabel("Find Game!");
            cl.show(lobbypanel, "Lobby");
        }
    }

}
