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
public interface IYAPTServer extends INode<ISession> {

    public ILobby getLobby() throws RemoteException;
}
