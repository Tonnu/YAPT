/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yapt.GAME;

import java.awt.Graphics;
import java.awt.Rectangle;
import yapt.RMI.Vector2f;

/**
 *
 * @author Toon
 */
public interface IBat {

    void move(Vector2f newLocation);

    void draw(Graphics g);

    void update(int direction);

    Vector2f getPosition();

    Rectangle getRectangle();

    public void setPosition(Vector2f _position);
    
    
}
