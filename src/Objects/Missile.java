/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Objects;

/**
 *
 * @author Aleks
 */
public class Missile extends GameObject {

    private double direction;
    private final MissileType missileType;
    private double speed;

    public Missile(long creationTick, Point position, MissileType missileType, double direction) {
        super(creationTick, position, missileType.radius);
        this.missileType = missileType;
        this.direction = direction;
        this.speed = missileType.getSpeed();
    }

    public boolean isFriendly() {
        return missileType.isFriendly();
    }

    public double getSpeed() {
        return speed;
    }

    public double getDirection() {
        return direction;
    }

    public MissileType getMissileType() {
        return missileType;
    }

    public Point getAcceleration() {
        return missileType.getAcceleration();
    }

    public double getDamage() {
        return missileType.getDamage();
    }

    @Override
    public void nextRound(GameMap gameMap) {
        getPosition().addVector(getSpeed(), getDirection());
        if (getAcceleration() != null) {
            double ny = Math.sin(direction) * speed + getAcceleration().getY(),
                    nx = Math.cos(direction) * speed + getAcceleration().getX();
            speed = Math.sqrt(ny * ny + nx * nx);
            direction = Math.atan2(ny, nx);
        }
    }

    public Explosion getExplosion(GameMap gameMap) {
        return new Explosion(gameMap.getTick(), getPosition(), getRadius(), 15, getDirection(), getSpeed());
    }

    public enum MissileType {
        FIREBALL(false, 10, 8),
        MISSILE(false, 6, 2),
        PLASMABALL(false, 4, 6),
        LASER(false, 0, 0),
        BULLET(true, 18, 3, 1),
        MICRO_MISSILE(true, 25, 1.5, 5, new Point(0, -0.07)),
        DUMBFIRE_MISSILE(true, 5, 2, 15, new Point(0, -.2));

        private MissileType(boolean friendly, double speed, double radius) {
            this.friendly = friendly;
            this.speed = speed;
            this.radius = radius;
            this.acceleration = null;
            this.damage = 0;
        }

        private MissileType(boolean friendly, double speed, double radius, Point acceleration) {
            this.friendly = friendly;
            this.speed = speed;
            this.radius = radius;
            this.acceleration = new Point(acceleration);
            this.damage = 0;
        }

        private MissileType(boolean friendly, double speed, double radius, double damage) {
            this.friendly = friendly;
            this.speed = speed;
            this.radius = radius;
            this.acceleration = null;
            this.damage = damage;
        }

        private MissileType(boolean friendly, double speed, double radius, double damage, Point acceleration) {
            this.friendly = friendly;
            this.speed = speed;
            this.radius = radius;
            this.acceleration = acceleration;
            this.damage = damage;
        }

        private final boolean friendly;
        private final double speed;
        private final double radius;
        private final Point acceleration;
        private final double damage;

        public boolean isFriendly() {
            return friendly;
        }

        public double getSpeed() {
            return speed;
        }

        public Point getAcceleration() {
            return acceleration;
        }

        public double getRadius() {
            return radius;
        }

        public double getDamage() {
            return damage;
        }
    }
}
