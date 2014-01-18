/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yaptserver;

import yapt.RMI.IPong;

/**
 *
 * @author Toon
 */
public interface IPongGame {

    int getGameNumber();

    public IPong getPong();

}
