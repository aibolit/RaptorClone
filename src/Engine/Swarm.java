/*
 * The MIT License
 *
 * Copyright 2016 Aleks.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package Engine;

import Objects.GameMap;
import Objects.Ship;

/**
 *
 * @author Aleks
 */
public class Swarm {

    private final SwarmType swarmType;
    private final long creationTick;
    private boolean done = false;

    public static Swarm random(long tick) {
        return new Swarm(SwarmType.values()[(int) (Math.random() * SwarmType.values().length)], tick);
    }

    public Swarm(SwarmType swarmType, long tick) {
        creationTick = tick;
        this.swarmType = swarmType;
    }

    public void nextRound(GameMap gameMap) {
        long age = gameMap.getTick() - creationTick;
        switch (swarmType) {
            case X:
                if (age == 5) {
                    gameMap.addShip(new Ship(gameMap.getTick(), gameMap.getSpawnLocation(3), Ship.ShipType.TYPE_X, 5));
                    gameMap.addShip(new Ship(gameMap.getTick(), gameMap.getSpawnLocation(8), Ship.ShipType.TYPE_X, 5));
                }
                if (age == 50) {
                    gameMap.addShip(new Ship(gameMap.getTick(), gameMap.getSpawnLocation(3), Ship.ShipType.TYPE_X, 5));
                    gameMap.addShip(new Ship(gameMap.getTick(), gameMap.getSpawnLocation(8), Ship.ShipType.TYPE_X, 5));

                    done = true;
                }
                break;
            case U_3:
                if (age == 5) {
                    gameMap.addShip(new Ship(gameMap.getTick(), gameMap.getSpawnLocation(2), Ship.ShipType.TYPE_U, 1));

                    gameMap.addShip(new Ship(gameMap.getTick(), gameMap.getSpawnLocation(4), Ship.ShipType.TYPE_U, 0));
                    gameMap.addShip(new Ship(gameMap.getTick(), gameMap.getSpawnLocation(5), Ship.ShipType.TYPE_U, 0));
                    gameMap.addShip(new Ship(gameMap.getTick(), gameMap.getSpawnLocation(6), Ship.ShipType.TYPE_U, 0));
                    gameMap.addShip(new Ship(gameMap.getTick(), gameMap.getSpawnLocation(7), Ship.ShipType.TYPE_U, 0));

                    gameMap.addShip(new Ship(gameMap.getTick(), gameMap.getSpawnLocation(9), Ship.ShipType.TYPE_U, -1));

                    done = true;
                }
                break;
            case U_0:
                if (age == 5) {
                    gameMap.addShip(new Ship(gameMap.getTick(), gameMap.getSpawnLocation(2), Ship.ShipType.TYPE_U, 0));

                    done = true;
                }
                break;
            default:
                throw new AssertionError();
        }

    }

    public boolean isDone() {
        return done;
    }

    public enum SwarmType {
        U_0,
        U_3,
        X
    }

}
