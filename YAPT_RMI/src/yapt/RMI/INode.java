/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yapt.RMI;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Collection;

/**
 *
 * @author Lenny
 * @param <T>the interface to which instances register itself for callbacks
 */
public interface INode<T extends INode> extends Remote {

    /**
     * the (default) RMI registry port
     */
    public static final int RMI_PORT = 1099;
    /**
     * Register for callbacks from this {@link Node}
     *
     * @param other the remote node
     * @throws RemoteException
     */
    public void register(T other) throws RemoteException;

    /**
     * Receive a callback from a remote node
     *
     * @param message
     * @throws java.rmi.RemoteException
     */
    public void onMessage(String message) throws RemoteException;

    public void onMessage(String message, Object o) throws RemoteException;

    public void notifyAll(String message, Object o) throws RemoteException;
    
    public void unRegister(T other) throws RemoteException;
    
    public Collection getOthers() throws RemoteException;
}
