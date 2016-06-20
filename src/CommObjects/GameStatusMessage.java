/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CommObjects;

import Engine.GameMapImpl;
import Objects.GameObject;
import java.util.List;

/**
 *
 * @author Aleks
 */
public class GameStatusMessage extends Message {

    private final long tick;
    private final List<GameObject> gameObjects;
    private final GameMapImpl.GameStatus gameStatus;

    public GameStatusMessage(long tick, List<GameObject> gameObjects, GameMapImpl.GameStatus gameStatus) {
        this.tick = tick;
        this.gameObjects = gameObjects;
        this.gameStatus = gameStatus;
    }

    public long getTick() {
        return tick;
    }

    public List<GameObject> getGameObjects() {
        return gameObjects;
    }

    @Override
    public String toString() {
        return "GameStatusMessage{" + "tick=" + tick + ", gameObjects=" + gameObjects + '}';
    }

    public GameMapImpl.GameStatus getGameStatus() {
        return gameStatus;
    }

}
