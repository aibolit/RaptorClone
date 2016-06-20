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
public class Ship extends GameObject {

    private final ShipType shipType;
    private final int variation;
    private double hp;

    //Ship stuff
    private Point shipPoint = null;
    private Integer shipInt = null;

    public Ship(long creationTick, Point position, ShipType shipType, int variation) {
        super(creationTick, position, shipType.getRadius());
        this.shipType = shipType;
        this.hp = shipType.getHp();
        this.variation = variation;
    }

    public Ship(long creationTick, Point position, ShipType shipType) {
        super(creationTick, position, shipType.getRadius());
        this.shipType = shipType;
        this.hp = shipType.getHp();
        this.variation = 0;
    }

    @Override
    public void nextRound(GameMap gameMap) {
        long age = gameMap.getTick() - getCreationTick();
        switch (shipType) {
            case TYPE_U: {

                getPosition().add(getPosition().getY() > 400 && getPosition().getY() < 1000 ? 1 * variation : 0, 4);
                if (age % 40 == 15) {
                    gameMap.addMissile(new Missile(gameMap.getTick(), new Point(getPosition()).add(0, 5), Missile.MissileType.FIREBALL, Math.PI / 2));
                }
            }
            break;
            case TYPE_X: {
                double speed = 7;
                if (age % 60 == 15) {
                    gameMap.addMissile(new Missile(gameMap.getTick(), new Point(getPosition()).add(-4, 5), Missile.MissileType.FIREBALL, Math.PI / 2));
                    gameMap.addMissile(new Missile(gameMap.getTick(), new Point(getPosition()).add(4, 5), Missile.MissileType.FIREBALL, Math.PI / 2));
                }
                if (shipPoint == null) {
                    if (getPosition().getY() > 600) {
                        getPosition().add(0, speed);
                    } else {
                        shipInt = variation;
                        shipPoint = gameMap.getRandomLocation(4, 3);
                        getPosition().add(getPosition().directionTowards(shipPoint).multiply(speed));
                    }
                } else if (shipInt == 0) {
                    getPosition().add(0, speed);
                } else if (getPosition().distanceTo(shipPoint) < speed * 2.5) {
                    shipInt--;
                    shipPoint = gameMap.getRandomLocation(4, 3);
                    getPosition().add(getPosition().directionTowards(shipPoint).multiply(speed));
                } else {
                    getPosition().add(getPosition().directionTowards(shipPoint).multiply(speed));
                }
            }
            break;
            default:
                throw new AssertionError();
        }
    }

    public ShipType getShipType() {
        return shipType;
    }

    public double getHp() {
        return hp;
    }

    public boolean modifyHp(double hp) {
        this.hp += hp;
        return this.hp > 0;
    }

    @Override
    public Explosion getExplosion(GameMap gameMap) {
        return new Explosion(gameMap.getTick(), getPosition(), 10, 40, null, 30);
    }

    public enum ShipType {

        TYPE_U(40, 25),
        TYPE_X(20, 15);

        private ShipType(double hp, double radius) {
            this.hp = hp;
            this.radius = radius;
        }

        private final double hp;
        private final double radius;

        public double getHp() {
            return hp;
        }

        public double getRadius() {
            return radius;
        }

        @Override
        public String toString() {
            return "ShipType{" + '}';
        }
    }

    @Override
    public String toString() {
        return "Ship{" + "shipType=" + shipType + ", variation=" + variation + ", hp=" + hp + '}';
    }

}
