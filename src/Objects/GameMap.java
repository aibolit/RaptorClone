/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Objects;

import CommObjects.ControlMessage;
import CommObjects.GameStatusMessage;
import Engine.GameMapImpl;
import java.util.Set;

/**
 *
 * @author Aleks
 */
public interface GameMap {

    public void nextRound();

    public GameStatusMessage getStatus();

    public void registerControlMessage(ControlMessage controlMessage);

    public long getTick();

    public void addExplosion(Explosion explosion);

    public GameMapImpl.GameStatus getGameStatus();

    public void addMissile(Missile missile);

    public Set<ControlType> getControls();

    public Point getSpawnLocation(int idx);

    public Point getRandomLocation(int rows, int cols);

    public void addShip(Ship ship);

    public Raptor getRaptor();

    public MapBounds getMapBounds();
}
