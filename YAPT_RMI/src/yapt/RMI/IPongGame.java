/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yapt.RMI;

import java.rmi.RemoteException;

/**
 *
 * @author Toon
 */
public interface IPongGame extends INode<ISession> {

    int getGameNumber() throws RemoteException;

    public IPong getPong() throws RemoteException;

    public ISession getPlayerA() throws RemoteException;

    public ISession getPlayerB() throws RemoteException;
    
    public void start() throws RemoteException;
    
    public int[] getPlayerScores() throws RemoteException;
    
    public void stop() throws RemoteException;

}
