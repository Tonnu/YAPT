/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yapt.RMI;

import java.awt.Rectangle;

/**
 *
 * @author Toon
 */
public interface IPong {

    public double getX();

    public double getY();

    public double getWidth();

    public double getHeight();

    public Rectangle getRectangle();

    public void update(Rectangle leftPlayer, Rectangle rightPlayer);
}
