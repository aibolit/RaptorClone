/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CommObjects;

import Objects.GameObject;
import Objects.GameStatus;
import java.util.List;

/**
 *
 * @author Aleks
 */
public class GameStatusMessage extends Message {

    private final long tick;
    private final List<GameObject> gameObjects;
    private final GameStatus gameStatus;

    public GameStatusMessage(long tick, List<GameObject> gameObjects, GameStatus gameStatus) {
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

    public GameStatus getGameStatus() {
        return gameStatus;
    }

}
