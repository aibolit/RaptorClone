package Engine;

import CommObjects.ControlMessage;
import CommObjects.GameStatusMessage;
import Objects.ControlType;
import Objects.Explosion;
import Objects.GameMap;
import Objects.GameObject;
import Objects.GameStatus;
import Objects.MapBounds;
import Objects.Missile;
import Objects.Point;
import Objects.Raptor;
import Objects.Ship;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;
import raptorclone.Configurations;

/**
 *
 * @author Sasa
 */
public class GameMapImpl implements Serializable, GameMap {

    //public final static double MAX_X = 1200, MIN_X = 0, MAX_Y = 1600, MIN_Y = 0;
    private final MapBounds mapBounds = new MapBounds(0, 1200, 0, 1600);
    public final static double MAP_BOUNDS_PADDING = 50;

    private volatile GameStatus gameStatus;
    private final Random random = new Random();
    private volatile long tick;
    private volatile long gameOverTick;
    private final Raptor raptor;
    private final Set<ControlType> controls = EnumSet.noneOf(ControlType.class);
    private final Set<Missile> missiles = new HashSet<>();
    private final Set<Missile> friendlyMissiles = new HashSet<>();
    private final Set<Missile> enemyMissiles = new HashSet<>();
    private final Set<Ship> ships = new HashSet<>();
    private final Set<Swarm> swarms = new HashSet<>();
    private final Set<Explosion> explosions = new HashSet<>();
    private volatile String message;

    public GameMapImpl(Map<Raptor.RaptorSubsystem, Integer> subsystems) {
        gameStatus = GameStatus.PAUSED;
        tick = 0;
        raptor = new Raptor(new Point(600, 1400), subsystems);
    }

    @Override
    public synchronized void registerControlMessage(ControlMessage controlMessage) {
        switch (controlMessage.getControl()) {
            case HEARTBEAT:
                break;
            case PAUSE:
                if (gameStatus == GameStatus.GAME_OVER || gameStatus == GameStatus.WAITING) {
                    break;
                }
                if (gameStatus != GameStatus.PAUSED) {
                    gameStatus = GameStatus.PAUSED;
                } else {
                    gameStatus = raptor.isAlive() ? GameStatus.RUNNING : GameStatus.GAME_OVER;
                }
                break;
            case SKIP:
                if (gameStatus == GameStatus.WAITING) {
                    gameStatus = GameStatus.RUNNING;
                }
                break;
            default:
                if (controlMessage.isOn()) {
                    controls.add(controlMessage.getControl());
                } else {
                    controls.remove(controlMessage.getControl());
                }
                break;
        }
    }

    private synchronized void nextStoryRound() {
        if (tick == 35) {
            message = "Welcome Commander,\nGet ready to take the controls... ";
            gameStatus = GameStatus.WAITING;
        } else if (tick == 36) {
            if (System.getProperty("os.name").contains("Mac")) {
                message = "Use the arrow keys to move\nand Cmd to shoot.";
            } else {
                message = "Use the arrow keys to move\nand Ctrl to shoot.";
            }
            gameStatus = GameStatus.WAITING;
        }

        if (raptor.getSubsystemLevel(Raptor.RaptorSubsystem.HULL_SYSTEM) < Raptor.RaptorSubsystem.HULL_SYSTEM.getMaxLevel()
                || raptor.getSubsystemLevel(Raptor.RaptorSubsystem.MOVE_SYSTEM) < Raptor.RaptorSubsystem.MOVE_SYSTEM.getMaxLevel()
                || raptor.getSubsystemLevel(Raptor.RaptorSubsystem.WEAPON_SYSTEM) < Raptor.RaptorSubsystem.WEAPON_SYSTEM.getMaxLevel()) {

            if (tick < 300 && tick > 50) {
                message = "WARNING!!!\nMeta Puzzles Not Solved\nShip is breaking apart";
            }

            if (gameStatus != GameStatus.GAME_OVER) {
                if (tick > 1500 && tick % 50 == 0) {
                    explosions.add(raptor.dismantle(this));
                }

                if (tick == 1450) {
                    message = "Commander, the ship...\nShe is breaking apart";
                    gameStatus = GameStatus.WAITING;
                }
                if (tick == 1600) {
                    message = "We need to go back\nand solve the metas...";
                    gameStatus = GameStatus.WAITING;
                }
                if (tick == 1800) {
                    message = "Abort Captian, ABORT";
                    gameStatus = GameStatus.WAITING;
                }
            }
        }
        if (tick > 100 && tick % 300 == 0 && tick < 3200) {
            swarms.add(Swarm.random(tick));
        }

        if (tick == 3500) {
            ships.add(new Ship(tick, getSpawnLocation(9), Ship.ShipType.TYPE_B));
        }

    }

    @Override
    public synchronized void nextRound() {
        if (gameStatus == GameStatus.PAUSED || gameStatus == GameStatus.WAITING) {
            return;
        }
        message = null;

        nextStoryRound();

        final List<Missile> removeMissiles = new ArrayList<>();
        final List<Ship> removeShips = new ArrayList<>();
        final List<Explosion> removeExplosions = new ArrayList<>();

        for (Iterator<Swarm> it = swarms.iterator(); it.hasNext();) {
            Swarm swarm = it.next();
            swarm.nextRound(this);
            if (swarm.isDone()) {
                it.remove();
            }
        }

        raptor.nextRound(this);

        for (Missile missile : missiles) {
            missile.nextRound(this);

            if (isOutOfBounds(missile.getPosition(), true)) {
                removeMissiles.add(missile);
            }
        }

        for (Missile missile : enemyMissiles) {
            if (raptor.overlapsWith(missile)) {
                raptor.takeDamage();
                removeMissiles.add(missile);
            }
        }

        for (Ship ship : ships) {
            ship.nextRound(this);

            for (Missile missile : friendlyMissiles) {
                if (ship.overlapsWith(missile)) {
                    explosions.add(missile.getExplosion(this));
                    removeMissiles.add(missile);
                    if (!ship.modifyHp(-missile.getDamage() * (raptor.getSubsystemLevel(Raptor.RaptorSubsystem.WEAPON_POWER) + 1) / (Raptor.RaptorSubsystem.WEAPON_POWER.getMaxLevel() + 1))) {
                        if (ship.isBoss()) {
                            message = "Commander... We have recieved\na message\n" + Configurations.getPuzzleAnswer();
                            gameStatus = GameStatus.WAITING;
                        }
                        removeShips.add(ship);
                        break;
                    }
                }
            }

            if (ship.overlapsWith(raptor)) {
                if (!ship.isBoss()) {
                    removeShips.add(ship);
                }
                raptor.takeDamage();
                raptor.takeDamage();
            }

            if (isOutOfBounds(ship.getPosition(), true) || ship.getHp() <= 0) {
                removeShips.add(ship);
            }
        }

        for (Explosion explosion : explosions) {
            if (explosion.getCreationTick() + explosion.getDuration() <= tick) {
                removeExplosions.add(explosion);
            }
        }

        for (Ship removeShip : removeShips) {
            explosions.add(removeShip.getExplosion(this));
            ships.remove(removeShip);

        }
        for (Missile removeMissile : removeMissiles) {
            removeMissile(removeMissile);
        }
        removeExplosions.stream().forEach((removeExplosion) -> {
            explosions.remove(removeExplosion);
        });

        if (!raptor.isAlive() && gameStatus != GameStatus.GAME_OVER) {
            gameStatus = GameStatus.GAME_OVER;
            gameOverTick = tick;
        }

        tick++;
    }

    @Override
    public Set<ControlType> getControls() {
        return Collections.unmodifiableSet(controls);
    }

    @Override
    public void addMissile(Missile missile) {
        missiles.add(missile);
        if (missile.isFriendly()) {
            friendlyMissiles.add(missile);
        } else {
            enemyMissiles.add(missile);
        }
    }

    public synchronized void removeMissile(Missile missile) {
        explosions.add(missile.getExplosion(this));
        missiles.remove(missile);
        if (missile.isFriendly()) {
            friendlyMissiles.remove(missile);
        } else {
            enemyMissiles.remove(missile);
        }
    }

    public Point getSpawnLocation(int idx) {
        return new Point(mapBounds.getMinX() + (mapBounds.getMaxX() - mapBounds.getMinX()) / 24.0 * (2 * idx + 1), mapBounds.getMinY() - MAP_BOUNDS_PADDING);
    }

    public synchronized void addShip(Ship ship) {
        ships.add(ship);
    }

    @Override
    public synchronized void addExplosion(Explosion explosion) {
        explosions.add(explosion);
    }

    private boolean isOutOfBounds(Point point, boolean addPadding) {
        double buffer = addPadding ? MAP_BOUNDS_PADDING : 0;

        return point.getX() < mapBounds.getMinX() - buffer
                || point.getX() > mapBounds.getMaxX() + buffer
                || point.getY() < mapBounds.getMinY() - buffer
                || point.getY() > mapBounds.getMaxY() + buffer;
    }

    @Override
    public long getTick() {
        return tick;
    }

    @Override
    public synchronized GameStatus getGameStatus() {
        return gameStatus;
    }

    @Override
    public synchronized GameStatusMessage getStatus() {

        List<GameObject> gameObects = new ArrayList<>();
        gameObects.add(raptor);

        gameObects.addAll(ships);
        gameObects.addAll(missiles);
        gameObects.addAll(explosions);

        if (raptor.getSubsystemLevel(Raptor.RaptorSubsystem.HULL_RADAR) != Raptor.RaptorSubsystem.HULL_RADAR.getMaxLevel()) {
            gameObects = gameObects.stream().filter((o) -> {
                return raptor.distanceTo(o) < 400 + 200 * raptor.getSubsystemLevel(Raptor.RaptorSubsystem.HULL_RADAR);
            }).collect(Collectors.toList());
        }

        GameStatusMessage stat = new GameStatusMessage(tick, gameObects, gameStatus, message);
        return stat;
    }

    @Override
    public Raptor getRaptor() {
        return raptor;
    }

    @Override
    public MapBounds getMapBounds() {
        return mapBounds;
    }

    @Override
    public Long getGameOverTick() {
        if (gameStatus == GameStatus.GAME_OVER) {
            return gameOverTick;
        }
        return null;
    }

    @Override
    public Point getRandomLocation(int rows, int cols) {
        return new Point(mapBounds.getMinX() + (random.nextInt(cols) + .5) * (mapBounds.getMaxX() - mapBounds.getMinX()) / cols,
                mapBounds.getMinY() + (random.nextInt(rows) + .5) * (mapBounds.getMaxY() - mapBounds.getMinY()) / rows);
    }

}
