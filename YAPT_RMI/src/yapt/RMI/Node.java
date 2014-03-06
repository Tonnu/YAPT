/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yapt.RMI;

import java.rmi.RemoteException;
import java.util.Collection;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Lenny
 * @param <T>
 */
public abstract class Node<T extends INode> implements INode<T> {

    private final Collection<T> others = new ConcurrentLinkedDeque<>();

    @Override
    public void register(T other) throws RemoteException {
        System.out.println("Amount of others: " + others.size());
        others.add(other);
    }

    @Override
    public void unRegister(T other) throws RemoteException {
        if (others.contains(other)) {
            System.out.println("Found the object to unregister. Removing from 'others' list.");
            others.remove(other);

        }else{
            System.out.println("Didn't find the object to remove!");
        }
    }

    @Override
    public void onMessage(String message) {
        Logger.getLogger(this.getClass().getSimpleName()).log(Level.INFO, "received: {0}", message);
    }

    public void onMessage(String message, Object o) throws RemoteException {
        Logger.getLogger(this.getClass().getSimpleName()).log(Level.INFO, "received: {0}", message);

    }

    @Override
    public void notifyAll(String message, Object o) throws RemoteException {
        for (T other : others) {
            if (o != null) {
                other.onMessage(message, o);
            } else {
                other.onMessage(message);
            }
        }
    }

    @Override
    public Collection getOthers() {
        return others;
    }

}
