/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yapt.RMI;

import java.rmi.RemoteException;
import java.util.Collection;

/**
 *
 * @author Toon
 */
public interface IYAPTServer extends INode<ISession> {

    public ILobby getLobby() throws RemoteException;
    
    public Collection<IPongGame> getCurrentGames() throws RemoteException;
    
    public void registerByParameters(String username) throws RemoteException;
}
