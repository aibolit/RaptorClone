/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Objects;

import java.io.Serializable;
import java.util.Random;

/**
 *
 * @author Aleks
 */
public abstract class GameObject implements Serializable {

    public GameObject(long creationTick, Point position, double radius) {
        this.position = new Point(position);
        this.radius = radius;
        this.creationTick = creationTick;
        synchronized (SYNC) {
            id = nextId += random.nextInt(32);
        }
    }

    private static final Random random = new Random(239057612);
    private static final Object SYNC = new Object();
    private static Long nextId = 0L;
    private final long id;
    private final Point position;
    private final double radius;
    private final long creationTick;

    public Point getPosition() {
        return position;
    }

    public long getCreationTick() {
        return creationTick;
    }

    public double getRadius() {
        return radius;
    }

    public long getId() {
        return id;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 73 * hash + (int) (this.id ^ (this.id >>> 32));
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
        final GameObject other = (GameObject) obj;
        return this.id == other.id;
    }

    public abstract void nextRound(GameMap gameMap);

    public abstract Explosion getExplosion(GameMap gameMap);

    public double distanceTo(GameObject other) {
        return position.distanceTo(other.position);
    }

    public double distanceTo(Point p) {
        return position.distanceTo(p);
    }

    public double directionTo(Point p) {
        return position.directionTo(p);
    }

    public double directionTo(GameObject other) {
        return position.directionTo(other.position);
    }

    public static boolean overlapsWith(GameObject a, GameObject b) {
        return a.distanceTo(b) <= a.getRadius() + b.getRadius();
    }

    public boolean overlapsWith(GameObject other) {
        return overlapsWith(this, other);
    }

    @Override
    public String toString() {
        return "GameObject{" + "id=" + id + ", position=" + position + ", radius=" + radius + '}';
    }

}
