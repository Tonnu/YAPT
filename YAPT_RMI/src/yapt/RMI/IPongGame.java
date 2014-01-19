/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yapt.RMI;

/**
 *
 * @author Toon
 */
public interface IPongGame extends INode<ISession> {

    int getGameNumber();

    public IPong getPong();

    public ISession getPlayerA();

    public ISession getPlayerB();
    
    public void start();

}
