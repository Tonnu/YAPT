/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yaptserver;

import java.rmi.RemoteException;
import java.util.Iterator;
import yapt.RMI.*;

/**
 *
 * @author tonnu
 */
public class Lobby extends Node<ISession> implements ILobby {
    public Lobby() {
        
    }

    @Override
    public void register(ISession other) throws RemoteException {
        super.register(other); //To change body of generated methods, choose Tools | Templates.
        other.onMessage("PublicChatMessage", "Welcome to the server!");
    }
    

    @Override
    public void onMessage(String message, Object o) throws RemoteException {
        switch (message) {
            case "PublicChatMessage":
                //got a message in the lobby, send to all clients
                this.notifyAll("PublicChatMessage", (String) o);
                break;
            case "GameChatMessage":
                ISession _sender = (ISession) o;
                for (Iterator it = _sender.getPongGame().getOthers().iterator(); it.hasNext();) {
                    ISession other = (ISession) it.next();
                    other.onMessage("GameChatMessage", (String) o);
                }
        }
    }

    public void newMessage() throws RemoteException {
        this.notifyAll("PublicChatMessage", "Hello!");

    }
}