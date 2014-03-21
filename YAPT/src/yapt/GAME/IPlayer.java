/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yapt.GAME;

import java.awt.Graphics;
import yapt.RMI.Vector2f;

/**
 *
 * @author Toon
 */
public interface IPlayer {

    public void update(int direction);

    public void draw(Graphics g);

    public IBat getBat();

    public void setBatCoordinates(Vector2f _position);

    public int getScore();

    public void setScore(int score);

    public void score();
}
