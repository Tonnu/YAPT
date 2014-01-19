/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yapt.RMI;

import java.awt.Rectangle;
import java.rmi.RemoteException;

/**
 *
 * @author Toon
 */
public interface ISession extends INode<IPongGame> {

    public Vector2f getPlayerPosition() throws RemoteException;

    public Rectangle getPlayerRectangle() throws RemoteException;

    public void setWinner(boolean won) throws RemoteException;

    public void setPosition(Vector2f _position) throws RemoteException;

    public void setPlayerNumber(int _number) throws RemoteException;

    public int getPlayerNumber() throws RemoteException;

    public int getGamePongNumber() throws RemoteException;

    public void setGamePongNumber(int _number) throws RemoteException;
    
    public void setPongGame(IPongGame _game) throws RemoteException;
    
}
