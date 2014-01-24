/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yapt.GUI;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import yapt.RMI.IYAPTServer;

/**
 *
 * @author tonnu
 */
public class ClientGUI {

    public static String USERNAME;
    private IYAPTServer server;

    public static void main(String args[]) {
        final String serverAddress = (args.length < 1) ? "localhost" : args[0];
        //final String serverAddress = "localhost";
        //final String serverAddress = "188.226.136.184";
        final JFrame window = new JFrame();
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setPreferredSize(new Dimension(1280, 760));
        final CardLayout cl = new CardLayout();
        final JPanel cards = new JPanel(cl);
        final LobbyPanel lobby = new LobbyPanel(cl);
        final YAPTPanel gamePanel = new YAPTPanel(cl);
        cards.add(new JPanel());
        cards.add(lobby, "Lobby");
        cards.add(gamePanel, "Game");
        window.add(cards);

        final JPanel control = new JPanel();
        final JTextField username = new JTextField();
        final JTextArea txtarea = new JTextArea();
        username.setColumns(20);
        txtarea.setColumns(100);
        txtarea.setRows(100);
        control.add(username);

        //control.add(txtarea);
        control.add(new JButton(new AbstractAction("Login") {

            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    USERNAME = username.getText();
                    if (USERNAME.equals("")) {
                        username.setText("Please fill in your username.");
                    } else {
                        lobby.tryLogin(serverAddress, USERNAME, gamePanel, cards);
                        control.setVisible(false);
                        cl.show(cards, "Lobby");
                    }
                } catch (RemoteException | NotBoundException ex) {
                    control.setVisible(true);
                    username.setText("Server timed out. Try again later.");
                    ex.printStackTrace();
                    txtarea.append(ex.getMessage());
                } catch (MalformedURLException ex) {
                    Logger.getLogger(ClientGUI.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }));
        window.add(control, BorderLayout.NORTH);
        window.pack();
        window.setLocationRelativeTo(null);
        window.setVisible(true);
    }

}
