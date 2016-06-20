package Engine;

import CommObjects.ControlMessage;
import CommObjects.GameStatusMessage;
import Objects.ControlType;
import Objects.Explosion;
import Objects.GameMap;
import Objects.GameObject;
import Objects.Missile;
import Objects.Point;
import Objects.Raptor;
import Objects.Ship;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Set;

/**
 *
 * @author Sasa
 */
public class GameMapImpl implements Serializable, GameMap {

    public final static double MAX_X = 1200, MIN_X = 0, MAX_Y = 1600, MIN_Y = 0;
    public final static double MAP_BOUNDS_PADDING = 50;

    private volatile GameStatus gameStatus = GameStatus.RUNNING;

    private final Random random = new Random();
    private volatile int tick;
    private final Raptor raptor;
    private final Set<ControlType> controls = EnumSet.noneOf(ControlType.class);
    private final Set<Missile> missiles = new HashSet<>();
    private final Set<Missile> friendlyMissiles = new HashSet<>();
    private final Set<Missile> enemyMissiles = new HashSet<>();
    private final Set<Ship> ships = new HashSet<>();
    private final Set<Swarm> swarms = new HashSet<>();
    private final Set<Explosion> explosions = new HashSet<>();

    public GameMapImpl() {
        tick = 0;
        raptor = new Raptor(new Point(600, 1400), new HashMap<>());
    }

    @Override
    public synchronized void registerControlMessage(ControlMessage controlMessage) {
        switch (controlMessage.getControl()) {
            case PAUSE:
                if (gameStatus != GameStatus.PAUSED) {
                    gameStatus = GameStatus.PAUSED;
                } else {
                    gameStatus = raptor.isAlive() ? GameStatus.RUNNING : GameStatus.GAME_OVER;
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

    @Override
    public synchronized void nextRound() {
        if (gameStatus == GameStatus.PAUSED) {
            return;
        }

        final List<Missile> removeMissiles = new ArrayList<>();
        final List<Ship> removeShips = new ArrayList<>();
        final List<Explosion> removeExplosions = new ArrayList<Explosion>();

        if (tick > 100 && tick % 300 == 0) {
            swarms.add(Swarm.random(tick));
        }

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
                    if (!ship.modifyHp(-missile.getDamage())) {
                        removeShips.add(ship);
                        break;
                    }
                }
            }

            if (ship.overlapsWith(raptor)) {
                removeShips.add(ship);
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

        if (!raptor.isAlive()) {
            gameStatus = GameStatus.GAME_OVER;
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
        return new Point(MIN_X + (MAX_X - MIN_X) / 24.0 * (2 * idx + 1), MIN_Y - MAP_BOUNDS_PADDING);
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

        return point.getX() < MIN_X - buffer
                || point.getX() > MAX_X + buffer
                || point.getY() < MIN_Y - buffer
                || point.getY() > MAX_Y + buffer;
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
        GameStatusMessage stat = new GameStatusMessage(tick, gameObects, gameStatus);
        return stat;
    }

    public enum GameStatus {
        RUNNING, PAUSED, GAME_OVER
    }

    @Override
    public Point getRandomLocation(int rows, int cols) {
        return new Point(MIN_X + (random.nextInt(cols) + .5) * (MAX_X - MIN_X) / cols, MIN_Y + (random.nextInt(rows) + .5) * (MAX_Y - MIN_Y) / rows);
    }

}
