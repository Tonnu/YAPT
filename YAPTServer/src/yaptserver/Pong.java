/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yaptserver;

import java.awt.Rectangle;
import java.io.Serializable;
import java.util.Random;
import yapt.RMI.IPong;
import yapt.RMI.Vector2f;

/**
 *
 * @author Toon
 */
public class Pong implements IPong, Serializable {

    private Vector2f location;
    private final double velocity = 5;
    private final double width, height, delta = 0;
    private final Random random;
    private final Vector2f destination;
    private boolean goingright = true;
    private boolean goingup;
    private Rectangle rectangle, left, right, leftBound, rightBound, topBound, bottomBound;
    private int angle;
    private boolean firstMovement, goingStraight;

    public Pong(Rectangle left, Rectangle right) {
        this.location = new Vector2f(250, 150);
        this.width = 50;
        this.height = 50;
        random = new Random();
        destination = new Vector2f(300, 150);
        this.rectangle = new Rectangle((int) location.x, (int) location.y, (int) width, (int) height);
        this.left = left;
        this.right = right;
        angle = 0;
        topBound = new Rectangle(0, 0, 750, 10);
        bottomBound = new Rectangle(0, 400, 750, 10);
        firstMovement = true;
    }

    @Override
    public void update(Rectangle leftPlayer, Rectangle rightPlayer) {
        this.left = leftPlayer;
        this.right = rightPlayer;
        move();
    }

    private void move() {
        this.rectangle = new Rectangle((int) location.x, (int) location.y, (int) width, (int) height);

        Rectangle lbottom = new Rectangle(left.x, (int) left.getCenterY(), (int) left.width, (int) left.height / 2);
        Rectangle rbottom = new Rectangle(right.x, (int) right.getCenterY(), (int) width, (int) right.height / 2);
        Rectangle ltop = new Rectangle(left.x, left.y, (int) left.width, (int) left.height / 2);
        Rectangle rtop = new Rectangle(right.x, right.y, (int) right.width, (int) right.height / 2);

        //set direction and movement
        if (!firstMovement) {
            if (goingright) {
                this.location.x += 5 * velocity * 0.16;
            } else {
                this.location.x -= 5 * velocity * 0.16;
            }

            if (!goingStraight) {
                if (goingup) {
                    this.location.y += angle * velocity * 0.16;
                } else {
                    this.location.y -= angle * velocity * 0.16;
                }
            }
        } else {
            this.location.x += 5 * velocity * 0.16;
            firstMovement = false;
            goingup = false;
            goingright = true;
            goingStraight = true;
        }

        
        //check intersections
        if (rectangle.intersects(left)) {
            angle = random.nextInt(5); //some kind of random angle
            //intersection with left player
            //determine if bottom or top part
            if (lbottom.intersects(left)) {
                //intersected with bottom half of pong, send pong downwards
                goingup = false;
                goingStraight = false;

            } else if (ltop.intersects(left)) {
                goingup = true;
                goingStraight = false;
            } else {
                goingStraight = true;
            }
            goingright = true;
        } else if (rectangle.intersects(right)) {
            angle = random.nextInt(5);
            //intersection with right player
            if (rbottom.intersects(right)) {
                //intersected with bottom half of pong, send pong downwards
                goingup = false;
                goingStraight = false;

            } else if (rtop.intersects(right)) {
                goingup = true;
                goingStraight = false;

            } else {
                goingStraight = true;

            }
            goingright = false;

        } else if (rectangle.intersects(bottomBound) || rectangle.intersects(topBound)) {
            goingStraight = false;
            angle = random.nextInt(5);
            goingup = location.y > 0 || location.y <= 15;
        }
    }

    @Override
    public double getX() {
        return this.location.x;
    }

    @Override
    public double getY() {
        return this.location.y;
    }

    @Override
    public double getWidth() {
        return this.width;
    }

    @Override
    public double getHeight() {
        return this.height;
    }

    @Override
    public Rectangle getRectangle() {
        return this.rectangle;
    }

    @Override
    public boolean isOutOfRightBound() {
        return this.rectangle.x >= 750;

    }

    @Override
    public boolean isOutOfLeftBound() {
        return this.rectangle.x <= 0;
    }

    @Override
    public void setPongCoordinates(Vector2f coords) {
        this.location = coords;
    }

    @Override
    public Vector2f getPongCoordinates() {
        return this.location;
    }

}
