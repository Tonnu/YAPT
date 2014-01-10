/*
 * Phys2D - a 2D physics engine based on the work of Erin Catto.
 * 
 * This source is provided under the terms of the BSD License.
 * 
 * Copyright (c) 2006, Phys2D
 * All rights reserved.
 */
package yapt.RMI;

import java.io.Serializable;

/**
 * A two dimensional vector
 *
 * @author Kevin Glass
 */
public strictfp class Vector2f implements Serializable {

    /**
     * The x component of this vector
     */
    public float x;
    /**
     * The y component of this vector
     */
    public float y;

    /**
     * Create an empty vector
     */
    public Vector2f() {
    }

    /**
     * @see net.phys2d.math.ROVector2f#getX()
     */
    public float getX() {
        return x;
    }

    /**
     * @see net.phys2d.math.ROVector2f#getY()
     */
    public float getY() {
        return y;
    }

    /**
     * Create a new vector based on another
     *
     * @param other The other vector to copy into this one
     */
    public Vector2f(Vector2f other) {
        this(other.getX(), other.getY());
    }

    /**
     * Create a new vector
     *
     * @param x The x component to assign
     * @param y The y component to assign
     */
    public Vector2f(float x, float y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Set the value of this vector
     *
     * @param other The values to set into the vector
     */
    public void set(Vector2f other) {
        set(other.getX(), other.getY());
    }

    /**
     * @see net.phys2d.math.ROVector2f#dot(net.phys2d.math.ROVector2f)
     */
    public float dot(Vector2f other) {
        return (x * other.getX()) + (y * other.getY());
    }

    /**
     * Set the values in this vector
     *
     * @param x The x component to set
     * @param y The y component to set
     */
    public void set(float x, float y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Negate this vector
     *
     * @return A copy of this vector negated
     */
    public Vector2f negate() {
        return new Vector2f(-x, -y);
    }

    /**
     * Add a vector to this vector
     *
     * @param v The vector to add
     */
    public void add(Vector2f v) {
        x += v.getX();
        y += v.getY();
    }

    /**
     * Subtract a vector from this vector
     *
     * @param v The vector subtract
     */
    public void sub(Vector2f v) {
        x -= v.getX();
        y -= v.getY();
    }

    /**
     * Scale this vector by a value
     *
     * @param a The value to scale this vector by
     */
    public void scale(float a) {
        x *= a;
        y *= a;
    }

    /**
     * Normalise the vector
     *
     */
    public void normalise() {
        float l = length();

        if (l == 0) {
            return;
        }

        x /= l;
        y /= l;
    }

    /**
     * The length of the vector squared
     *
     * @return The length of the vector squared
     */
    public float lengthSquared() {
        return (x * x) + (y * y);
    }

    /**
     * @see net.phys2d.math.ROVector2f#length()
     */
    public float length() {
        return (float) Math.sqrt(lengthSquared());
    }

    /**
     * Project this vector onto another
     *
     * @param b The vector to project onto
     * @param result The projected vector
     */
    public void projectOntoUnit(Vector2f b, Vector2f result) {
        float dp = b.dot(this);

        result.x = dp * b.getX();
        result.y = dp * b.getY();
    }

    /**
     * @see java.lang.Object#toString()
     */
    public String toString() {
        return "[Vec " + x + "," + y + " (" + length() + ")]";
    }

    /**
     * Get the distance from this point to another
     *
     * @param other The other point we're measuring to
     * @return The distance to the other point
     */
    public float distance(Vector2f other) {
        return (float) Math.sqrt(distanceSquared(other));
    }

    /**
     * Get the distance squared from this point to another
     *
     * @param other The other point we're measuring to
     * @return The distance to the other point
     */
    public float distanceSquared(Vector2f other) {
        float dx = other.getX() - getX();
        float dy = other.getY() - getY();

        return (dx * dx) + (dy * dy);
    }

    /**
     * Compare two vectors allowing for a (small) error as indicated by the
     * delta. Note that the delta is used for the vector's components
     * separately, i.e. any other vector that is contained in the square box
     * with sides 2*delta and this vector at the center is considered equal.
     *
     * @param other The other vector to compare this one to
     * @param delta The allowed error
     * @return True iff this vector is equal to other, with a tolerance defined
     * by delta
     */
    public boolean equalsDelta(Vector2f other, float delta) {
        return (other.getX() - delta < x
                && other.getX() + delta > x
                && other.getY() - delta < y
                && other.getY() + delta > y);

    }
}
