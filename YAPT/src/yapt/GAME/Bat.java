/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yapt.GAME;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.io.Serializable;
import yapt.RMI.Vector2f;

/**
 *
 * @author Toon
 */
public class Bat implements IBat, Serializable {

    private Vector2f location;
    private final double velocity = 15;
    //private IPlayer owner;
    private final int width = 10, height = 60;
    private final Rectangle batRectangle;

    public Bat(Vector2f location) {
        this.location = location;
        //this.owner = p;
        //this.velocity = new Vector2f(1, 0); // move 5x coords per update
        this.batRectangle = new Rectangle((int) this.location.x, (int) this.location.y, width, height);
    }

    public void update(int direction) {
        //this.location.add(velocity);
        this.location.y += direction * velocity * 0.16;
        this.batRectangle.x = (int) this.location.x;
        this.batRectangle.y = (int) this.location.y;
    }

    public void move(Vector2f newLocation) {
        location.add(newLocation);
        //location.normalise();
    }

    public void draw(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        Rectangle rect = new Rectangle((int) location.getX(), (int) location.getY(), this.width, this.height);
        g2d.draw(rect);
        g2d.setColor(Color.white);
        g2d.fill(rect);
    }

    @Override
    public Vector2f getPosition() {
        return this.location;
    }

    @Override
    public Rectangle getRectangle() {
        return this.batRectangle;
    }

    @Override
    public void setPosition(Vector2f _position) {
        this.location = _position;
    }

}
