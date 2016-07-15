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
public class Explosion extends GameObject {

    private final Double direction;
    private final long duration;
    private final double speed;

    public Explosion(long creationTick, Point position, double radius, long duration, Double direction, double speed) {
        super(creationTick, position, radius);
        this.duration = duration;
        this.direction = direction;
        this.speed = speed;
    }

    @Override
    public void nextRound(GameMap gameMap) {
    }

    public Double getDirection() {
        return direction;
    }

    public long getDuration() {
        return duration;
    }

    public double getSpeed() {
        return speed;
    }

    @Override
    public Explosion getExplosion(GameMap gameMap) {
        return this;
    }

}
