/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Objects;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author Aleks
 */
public class Raptor extends GameObject {

    private final Map<RaptorSubsystem, Integer> subsystems = new EnumMap<>(RaptorSubsystem.class);

    private int hp;
    private double shield;

    private final double SPEED = 12, SHEILD_REGEN = 0.007;
    private double horizontalSkid = 0, verticalSkid = 0;

    public Raptor(Point position, Map<RaptorSubsystem, Integer> subsystems) {
        super(0, position, 15);
        this.subsystems.putAll(subsystems);

        this.hp = 1 + getSubsystemLevel(RaptorSubsystem.HULL_HEALTH);
        this.shield = getSubsystemLevel(RaptorSubsystem.HULL_SHEILD) > 0 ? 1 : 0;
        //CUSTOM
    }

    public final int getSubsystemLevel(RaptorSubsystem subsystem) {
        return subsystems.getOrDefault(subsystem, 0);
    }

    public double getSubsystemRatio(RaptorSubsystem subsystem) {
        return subsystems.getOrDefault(subsystem, 0) * 1.0 / subsystem.getMaxLevel();
    }

    public Integer putSubsystemLevel(RaptorSubsystem key, Integer value) {
        return subsystems.put(key, value);
    }

    //private int getSubsystemLevel()
    public boolean takeDamage() {
        if (shield < 1) {
            hp--;
        }
        shield = 0;
        return hp > 0;
    }

    public boolean isAlive() {
        return hp > 0;
    }

    public int getHp() {
        return hp;
    }

    public double getShield() {
        return shield;
    }

    @Override
    public void nextRound(GameMap gameMap) {
        Set<ControlType> controls = gameMap.getControls();
        long tick = gameMap.getTick();
        double vertMovement = 0, horzMovement = 0;

        for (ControlType control : controls) {
            switch (control) {
                case FIRE:
                    if (gameMap.getGameStatus() == GameStatus.GAME_OVER) {
                        break;
                    }

                    double spread = controls.contains(ControlType.LEFT) ^ controls.contains(ControlType.RIGHT) ? 0 : 5;

                    if (getSubsystemLevel(RaptorSubsystem.WEAPON_TYPES) >= 1 && tick % (5 * (RaptorSubsystem.WEAPON_SPEED.getMaxLevel() + 1 - getSubsystemLevel(RaptorSubsystem.WEAPON_SPEED))) == 0) {
                        gameMap.addMissile(new Missile(tick, new Point(getPosition()).add(10 + spread, -2), Missile.MissileType.BULLET, -Math.PI / 2));
                        gameMap.addMissile(new Missile(tick, new Point(getPosition()).add(-10 - spread, -2), Missile.MissileType.BULLET, -Math.PI / 2));
                    }
                    if (getSubsystemLevel(RaptorSubsystem.WEAPON_TYPES) >= 3 && tick % (37 * (RaptorSubsystem.WEAPON_SPEED.getMaxLevel() + 1 - getSubsystemLevel(RaptorSubsystem.WEAPON_SPEED))) == 0) {
                        gameMap.addMissile(new Missile(tick, new Point(getPosition()).add(5 + spread, -4), Missile.MissileType.DUMBFIRE_MISSILE, Math.PI * 3 / 8));
                        gameMap.addMissile(new Missile(tick, new Point(getPosition()).add(-5 - spread, -4), Missile.MissileType.DUMBFIRE_MISSILE, Math.PI * 5 / 8));
                    }
                    if (getSubsystemLevel(RaptorSubsystem.WEAPON_TYPES) >= 2 && tick % (17 * (RaptorSubsystem.WEAPON_SPEED.getMaxLevel() + 1 - getSubsystemLevel(RaptorSubsystem.WEAPON_SPEED))) == 0) {
                        gameMap.addMissile(new Missile(tick, new Point(getPosition()).add(18 + spread, -2.5), Missile.MissileType.MICRO_MISSILE, -Math.PI / 2));
                        gameMap.addMissile(new Missile(tick, new Point(getPosition()).add(-18 - spread, -2.5), Missile.MissileType.MICRO_MISSILE, -Math.PI / 2));
                    }
                    break;
                case UP:
                    vertMovement -= 1;
                    break;
                case DOWN:
                    vertMovement += 1;
                    break;
                case LEFT:
                    horzMovement -= 1;
                    break;
                case RIGHT:
                    horzMovement += 1;
                    break;
                default:
                    break;
            }
        }

        if (gameMap.getGameStatus() == GameStatus.GAME_OVER) {
            getPosition().add(0, SPEED / 2);
            if (tick % 15 == 0) {
                double dir = Math.random() * Math.PI * 2;
                double rad = Math.random() * getRadius();
                gameMap.addExplosion(new Explosion(tick, new Point(getPosition()).add(rad * Math.cos(dir), rad * Math.sin(dir)), 25, 20, null, 35));
            }
        } else {
            double f = getSubsystemLevel(RaptorSubsystem.MOVE_BRAKE) >= RaptorSubsystem.MOVE_BRAKE.getMaxLevel() ? 1 : Math.pow(.992, Math.pow(.5, -getSubsystemLevel(RaptorSubsystem.MOVE_BRAKE)));
            double a = (1.0 - f) / f;

            double hmove = getSubsystemLevel(RaptorSubsystem.MOVE_BRAKE) >= RaptorSubsystem.MOVE_BRAKE.getMaxLevel()
                    ? horzMovement * SPEED * getSubsystemRatio(RaptorSubsystem.MOVE_HORIZONTAL)
                    : f * (horzMovement * SPEED * getSubsystemRatio(RaptorSubsystem.MOVE_HORIZONTAL) * a + horizontalSkid);
            double vmove = getSubsystemLevel(RaptorSubsystem.MOVE_BRAKE) >= RaptorSubsystem.MOVE_BRAKE.getMaxLevel()
                    ? vertMovement * SPEED * getSubsystemRatio(RaptorSubsystem.MOVE_VERTICAL)
                    : f * (vertMovement * SPEED * getSubsystemRatio(RaptorSubsystem.MOVE_VERTICAL) * a + verticalSkid);

            horizontalSkid = hmove;
            verticalSkid = vmove;

            getPosition().add(hmove, vmove);

            if (getPosition().getX() < gameMap.getMapBounds().getMinX()) {
                getPosition().setX(
                        getSubsystemLevel(RaptorSubsystem.MOVE_BRAKE) >= RaptorSubsystem.MOVE_BRAKE.getMaxLevel()
                        ? gameMap.getMapBounds().getMinX()
                        : 2 * gameMap.getMapBounds().getMinX() - getPosition().getX());
                horizontalSkid = -horizontalSkid;
            }

            if (getPosition().getX() > gameMap.getMapBounds().getMaxX()) {
                getPosition().setX(
                        getSubsystemLevel(RaptorSubsystem.MOVE_BRAKE) >= RaptorSubsystem.MOVE_BRAKE.getMaxLevel()
                        ? gameMap.getMapBounds().getMaxX()
                        : 2 * gameMap.getMapBounds().getMaxX() - getPosition().getX());
                horizontalSkid = -horizontalSkid;
            }

            if (getPosition().getY() < gameMap.getMapBounds().getMinY()) {
                getPosition().setY(
                        getSubsystemLevel(RaptorSubsystem.MOVE_BRAKE) >= RaptorSubsystem.MOVE_BRAKE.getMaxLevel()
                        ? gameMap.getMapBounds().getMinY()
                        : 2 * gameMap.getMapBounds().getMinY() - getPosition().getY());
                verticalSkid = -verticalSkid;
            }

            if (getPosition().getY() > gameMap.getMapBounds().getMaxY()) {
                getPosition().setY(
                        getSubsystemLevel(RaptorSubsystem.MOVE_BRAKE) >= RaptorSubsystem.MOVE_BRAKE.getMaxLevel()
                        ? gameMap.getMapBounds().getMaxY()
                        : 2 * gameMap.getMapBounds().getMaxY() - getPosition().getY());
                verticalSkid = -verticalSkid;
            }
        }

        shield = Math.min(shield + SHEILD_REGEN * Math.pow(getSubsystemRatio(RaptorSubsystem.HULL_SHEILD) * (controls.contains(ControlType.FIRE) ? 1 : 1.5), 2), 1);
    }

    @Override
    public Explosion getExplosion(GameMap gameMap) {
        return new Explosion(gameMap.getTick(), getPosition(), 1, 15, null, 15);
    }

    public Explosion dismantle(GameMap gameMap) {
        List<RaptorSubsystem> contestedSubsystems = new ArrayList<>();
        if (getSubsystemLevel(RaptorSubsystem.HULL_SYSTEM) < RaptorSubsystem.HULL_SYSTEM.getMaxLevel()) {
            if (getSubsystemLevel(RaptorSubsystem.HULL_SHEILD) > 0) {
                contestedSubsystems.add(RaptorSubsystem.HULL_SHEILD);
            }
            if (getSubsystemLevel(RaptorSubsystem.HULL_RADAR) > 0) {
                contestedSubsystems.add(RaptorSubsystem.HULL_RADAR);
            }
            if (getSubsystemLevel(RaptorSubsystem.HULL_HEALTH) > 0) {
                contestedSubsystems.add(RaptorSubsystem.HULL_HEALTH);
            }
        }
        if (getSubsystemLevel(RaptorSubsystem.WEAPON_SYSTEM) < RaptorSubsystem.WEAPON_SYSTEM.getMaxLevel()) {
            if (getSubsystemLevel(RaptorSubsystem.WEAPON_POWER) > 0) {
                contestedSubsystems.add(RaptorSubsystem.WEAPON_POWER);
            }
            if (getSubsystemLevel(RaptorSubsystem.WEAPON_SPEED) > 0) {
                contestedSubsystems.add(RaptorSubsystem.WEAPON_SPEED);
            }
            if (getSubsystemLevel(RaptorSubsystem.WEAPON_TYPES) > 0) {
                contestedSubsystems.add(RaptorSubsystem.WEAPON_TYPES);
            }
        }
        if (getSubsystemLevel(RaptorSubsystem.MOVE_SYSTEM) < RaptorSubsystem.MOVE_SYSTEM.getMaxLevel()) {
            if (getSubsystemLevel(RaptorSubsystem.MOVE_HORIZONTAL) > 0) {
                contestedSubsystems.add(RaptorSubsystem.MOVE_HORIZONTAL);
            }
            if (getSubsystemLevel(RaptorSubsystem.MOVE_VERTICAL) > 0) {
                contestedSubsystems.add(RaptorSubsystem.MOVE_VERTICAL);
            }
            if (getSubsystemLevel(RaptorSubsystem.MOVE_BRAKE) > 0) {
                contestedSubsystems.add(RaptorSubsystem.MOVE_BRAKE);
            }
        }
        if (contestedSubsystems.size() > 0) {
            RaptorSubsystem subsystem = contestedSubsystems.get((int) (Math.random() * contestedSubsystems.size()));
            subsystems.put(subsystem, subsystems.get(subsystem) - 1);
            switch (subsystem) {
                case HULL_HEALTH:
                    if (hp > 0) {
                        hp--;
                    }
                    break;
                case HULL_SHEILD:
                    shield = 0;
                    break;
            }

        } else if (getSubsystemLevel(RaptorSubsystem.HULL_SYSTEM) < RaptorSubsystem.HULL_SYSTEM.getMaxLevel()) {
            hp--;
        }
        return new Explosion(gameMap.getTick(), getPosition(), 10, 30, null, 6);
    }

    public enum RaptorSubsystem {
        MOVE_HORIZONTAL(3, false),
        MOVE_VERTICAL(3, false),
        MOVE_BRAKE(3, false),
        MOVE_SYSTEM(1, true),
        WEAPON_TYPES(3, false),
        WEAPON_SPEED(3, false),
        WEAPON_POWER(3, false),
        WEAPON_SYSTEM(1, true),
        HULL_HEALTH(3, false),
        HULL_SHEILD(3, false),
        HULL_RADAR(3, false),
        HULL_SYSTEM(1, true);

        private RaptorSubsystem(int maxLevel, boolean isSystem) {
            this.maxLevel = maxLevel;
            this.system = isSystem;
        }

        public int getMaxLevel() {
            return maxLevel;
        }

        public boolean isSystem() {
            return system;
        }

        private final int maxLevel;
        private final boolean system;
    }
}
