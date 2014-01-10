/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yapt.GAME;

import yapt.GAME.IPlayer;
import java.awt.Graphics;
import java.rmi.RemoteException;
import java.util.Collections;
import yapt.RMI.IPong;

/**
 *
 * @author Toon
 */
public interface IGameClient {

    public void updateGameObjects(Collections c) throws RemoteException;

    public IPlayer getPlayer() throws RemoteException;

    public void update(int direction) throws RemoteException;

    public void draw(Graphics g) throws RemoteException;
    
    public void setPong(IPong _pong);
    
    public void setOpponent(IPlayer _opponent);
    
    public IPlayer getOpponent();
    
    public void resetPlayer();
}
