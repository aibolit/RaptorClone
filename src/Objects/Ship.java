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

                getPosition().add(getPosition().getY() > 400 && getPosition().getY() < 900 ? 1 * variation : 0, 4);
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
            case TYPE_V: {
                getPosition().add(variation * age * .01, 10);
            }
            break;
            case TYPE_K: {
                getPosition().add(0, 2);
                if (age % 25 == 0 && (age / 25) % 10 < 6) {
                    gameMap.addMissile(new Missile(gameMap.getTick(), new Point(getPosition()).add(0, 0), Missile.MissileType.FIREBALL, getPosition().directionTo(gameMap.getRaptor().getPosition())));
                }
            }
            break;
            case TYPE_H: {
                getPosition().add(variation * (7 + 2 * Math.random()), 16);
                if (age % 60 == 15) {
                    gameMap.addMissile(new Missile(gameMap.getTick(), new Point(getPosition()).add(0, 5), Missile.MissileType.FIREBALL, Math.PI / 2));
                }
            }
            break;
            case TYPE_B:
                double speed = 4;
                if (age % 400 < 161 && age % 20 == 0) {
                    gameMap.addMissile(new Missile(gameMap.getTick(), new Point(getPosition()).add(-4, 50), Missile.MissileType.FIREBALL, Math.PI / 2));
                    gameMap.addMissile(new Missile(gameMap.getTick(), new Point(getPosition()).add(4, 50), Missile.MissileType.FIREBALL, Math.PI / 2));
                    gameMap.addMissile(new Missile(gameMap.getTick(), new Point(getPosition()).add(-25, 50), Missile.MissileType.FIREBALL, Math.PI / 2));
                    gameMap.addMissile(new Missile(gameMap.getTick(), new Point(getPosition()).add(25, 50), Missile.MissileType.FIREBALL, Math.PI / 2));
                    gameMap.addMissile(new Missile(gameMap.getTick(), new Point(getPosition()).add(-30, 50), Missile.MissileType.FIREBALL, Math.PI / 8 * 5));
                    gameMap.addMissile(new Missile(gameMap.getTick(), new Point(getPosition()).add(30, 50), Missile.MissileType.FIREBALL, Math.PI / 8 * 3));

                    gameMap.addMissile(new Missile(gameMap.getTick(), new Point(getPosition()).add(-35, 50), Missile.MissileType.FIREBALL, Math.PI / 4 * 3));
                    gameMap.addMissile(new Missile(gameMap.getTick(), new Point(getPosition()).add(35, 50), Missile.MissileType.FIREBALL, Math.PI / 4));

                    gameMap.addMissile(new Missile(gameMap.getTick(), new Point(getPosition()).add(0, -20), Missile.MissileType.FIREBALL, Math.PI / 16 + getPosition().directionTo(gameMap.getRaptor().getPosition())));
                    gameMap.addMissile(new Missile(gameMap.getTick(), new Point(getPosition()).add(0, -20), Missile.MissileType.FIREBALL, -1 * Math.PI / 16 + getPosition().directionTo(gameMap.getRaptor().getPosition())));

                    gameMap.addMissile(new Missile(gameMap.getTick(), new Point(getPosition()).add(-20, 0), Missile.MissileType.FIREBALL, getPosition().directionTo(gameMap.getRaptor().getPosition())));
                    gameMap.addMissile(new Missile(gameMap.getTick(), new Point(getPosition()).add(20, 0), Missile.MissileType.FIREBALL, getPosition().directionTo(gameMap.getRaptor().getPosition())));
                    gameMap.addMissile(new Missile(gameMap.getTick(), new Point(getPosition()).add(-30, 0), Missile.MissileType.FIREBALL, getPosition().directionTo(gameMap.getRaptor().getPosition())));
                    gameMap.addMissile(new Missile(gameMap.getTick(), new Point(getPosition()).add(30, 0), Missile.MissileType.FIREBALL, getPosition().directionTo(gameMap.getRaptor().getPosition())));

                }
                if (shipPoint == null) {
                    if (getPosition().getY() > 600) {
                        getPosition().add(0, speed);
                    } else {
                        shipPoint = gameMap.getRandomLocation(4, 3);
                        getPosition().add(getPosition().directionTowards(shipPoint).multiply(speed));
                    }
                } else if (getPosition().distanceTo(shipPoint) < speed * 2.5) {
                    shipPoint = gameMap.getRandomLocation(4, 3);
                    getPosition().add(getPosition().directionTowards(shipPoint).multiply(speed));
                } else {
                    getPosition().add(getPosition().directionTowards(shipPoint).multiply(speed));
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

    public double getMaxHp() {
        return shipType.getHp();
    }

    public boolean modifyHp(double hp) {
        this.hp += hp;
        return this.hp > 0;
    }

    @Override
    public Explosion getExplosion(GameMap gameMap) {
        return new Explosion(gameMap.getTick(), getPosition(), 10, 40, null, 30);
    }

    public boolean isBoss() {
        return shipType.isBoss();
    }

    public enum ShipType {
        TYPE_U(20, 35, false),
        TYPE_V(15, 45, false),
        TYPE_X(10, 25, false),
        TYPE_K(25, 30, false),
        TYPE_H(7, 40, false),
        TYPE_B(1200, 150, true),;

        private ShipType(double hp, double radius, boolean boss) {
            this.hp = hp;
            this.radius = radius;
            this.boss = boss;
        }

        private final boolean boss;
        private final double hp;
        private final double radius;

        public double getHp() {
            return hp;
        }

        public double getRadius() {
            return radius;
        }

        public boolean isBoss() {
            return boss;
        }

        @Override
        public String toString() {
            return "ShipType{" + this.name() + '}';
        }
    }

    @Override
    public String toString() {
        return "Ship{" + "shipType=" + shipType + ", variation=" + variation + ", hp=" + hp + '}';
    }

}
