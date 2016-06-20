/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Objects;

import java.io.Serializable;

/**
 *
 * @author Aleks
 */
public class Point implements Serializable {

    private volatile Double x, y;

    public Point(Point point) {
        this(point.getX(), point.getY());
    }

    public Point(double x, double y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 97 * hash + (int) (Double.doubleToLongBits(this.x) ^ (Double.doubleToLongBits(this.x) >>> 32));
        hash = 97 * hash + (int) (Double.doubleToLongBits(this.y) ^ (Double.doubleToLongBits(this.y) >>> 32));
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Point other = (Point) obj;
        return Double.doubleToLongBits(this.x) != Double.doubleToLongBits(other.x) && Double.doubleToLongBits(this.y) == Double.doubleToLongBits(other.y);
    }

    public double getX() {
        return x;
    }

    public Point setX(double x) {
        this.x = x;
        return this;
    }

    public double getY() {
        return y;
    }

    public Point setY(double y) {
        this.y = y;
        return this;
    }

    public Point add(double x, double y) {
        this.x += x;
        this.y += y;
        return this;
    }

    public static double distance(Point a, Point b) {
        double xd = a.x - b.x, yd = a.y - b.y;
        return Math.sqrt(xd * xd + yd * yd);
    }

    public Point add(Point point) {
        this.x += point.x;
        this.y += point.y;
        return this;
    }

    public Point multiply(double m) {
        this.x *= m;
        this.y *= m;
        return this;
    }

    public Point addVector(double speed, double angle) {
        this.x += speed * Math.cos(angle);
        this.y += speed * Math.sin(angle);
        return this;
    }

    public double distanceTo(Point p) {
        return distance(this, p);
    }

    public static double directionTo(Point from, Point to) {
        return Math.atan2(to.y - from.y, to.x - from.x);
    }

    public double directionTo(Point point) {
        return directionTo(this, point);
    }

    public Point directionTowards(Point point) {
        double directionTo = this.directionTo(point);
        return new Point(Math.cos(directionTo), Math.sin(directionTo));
    }

    @Override
    public String toString() {
        return "Point{" + "x=" + x + ", y=" + y + '}';
    }
}
