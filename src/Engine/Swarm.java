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

    //private vars
    private Integer swarmInt = null;

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
            case X_0: {
                if (age == 5) {
                    gameMap.addShip(new Ship(gameMap.getTick(), gameMap.getSpawnLocation(3), Ship.ShipType.TYPE_X, 5));
                    gameMap.addShip(new Ship(gameMap.getTick(), gameMap.getSpawnLocation(8), Ship.ShipType.TYPE_X, 5));
                }
                if (age == 50) {
                    gameMap.addShip(new Ship(gameMap.getTick(), gameMap.getSpawnLocation(3), Ship.ShipType.TYPE_X, 5));
                    gameMap.addShip(new Ship(gameMap.getTick(), gameMap.getSpawnLocation(8), Ship.ShipType.TYPE_X, 5));

                    done = true;
                }
            }
            break;
            case U_1: {
                if (age == 5) {
                    gameMap.addShip(new Ship(gameMap.getTick(), gameMap.getSpawnLocation(2), Ship.ShipType.TYPE_U, 1));

                    gameMap.addShip(new Ship(gameMap.getTick(), gameMap.getSpawnLocation(4), Ship.ShipType.TYPE_U, 0));
                    gameMap.addShip(new Ship(gameMap.getTick(), gameMap.getSpawnLocation(5), Ship.ShipType.TYPE_U, 0));
                    gameMap.addShip(new Ship(gameMap.getTick(), gameMap.getSpawnLocation(6), Ship.ShipType.TYPE_U, 0));
                    gameMap.addShip(new Ship(gameMap.getTick(), gameMap.getSpawnLocation(7), Ship.ShipType.TYPE_U, 0));

                    gameMap.addShip(new Ship(gameMap.getTick(), gameMap.getSpawnLocation(9), Ship.ShipType.TYPE_U, -1));

                    done = true;
                }
            }
            break;
            case U_0: {
                if (age == 0 || age == 110 || age == 220) {
                    swarmInt = (int) (Math.random() * 4);
                }
                if (age == 0 || age == 30 || age == 60) {
                    gameMap.addShip(new Ship(gameMap.getTick(), gameMap.getSpawnLocation(8 + swarmInt), Ship.ShipType.TYPE_U, 0));
                }
                if (age == 110 || age == 140 || age == 170) {
                    gameMap.addShip(new Ship(gameMap.getTick(), gameMap.getSpawnLocation(0 + swarmInt), Ship.ShipType.TYPE_U, 0));
                }
                if (age == 220 || age == 250 || age == 280) {
                    gameMap.addShip(new Ship(gameMap.getTick(), gameMap.getSpawnLocation(4 + swarmInt), Ship.ShipType.TYPE_U, 0));
                }
                if (age == 345) {
                    done = true;
                }
            }
            break;

            case V_0: {
                if (age == 0) {
                    gameMap.addShip(new Ship(gameMap.getTick(), gameMap.getSpawnLocation(11), Ship.ShipType.TYPE_V, -2));
                }
                if (age == 20) {
                    gameMap.addShip(new Ship(gameMap.getTick(), gameMap.getSpawnLocation(11), Ship.ShipType.TYPE_V, -4));
                }
                if (age == 40) {
                    gameMap.addShip(new Ship(gameMap.getTick(), gameMap.getSpawnLocation(11), Ship.ShipType.TYPE_V, -6));
                }
                if (age == 100) {
                    gameMap.addShip(new Ship(gameMap.getTick(), gameMap.getSpawnLocation(0), Ship.ShipType.TYPE_V, 2));
                }
                if (age == 120) {
                    gameMap.addShip(new Ship(gameMap.getTick(), gameMap.getSpawnLocation(0), Ship.ShipType.TYPE_V, 4));
                }
                if (age == 140) {
                    gameMap.addShip(new Ship(gameMap.getTick(), gameMap.getSpawnLocation(0), Ship.ShipType.TYPE_V, 6));
                }
                if (age == 200) {
                    gameMap.addShip(new Ship(gameMap.getTick(), gameMap.getSpawnLocation(6), Ship.ShipType.TYPE_V, -1));
                }
                if (age == 220) {
                    gameMap.addShip(new Ship(gameMap.getTick(), gameMap.getSpawnLocation(6), Ship.ShipType.TYPE_V, 2));
                }
                if (age == 240) {
                    gameMap.addShip(new Ship(gameMap.getTick(), gameMap.getSpawnLocation(6), Ship.ShipType.TYPE_V, -3));
                }
            }
            break;

            case K_0: {
                if (age == 0 || age == 150) {
                    gameMap.addShip(new Ship(gameMap.getTick(), gameMap.getSpawnLocation((int) (Math.random() * 12)), Ship.ShipType.TYPE_K, 0));
                }
                if (age == 150) {
                    done = true;
                }
            }
            break;
            case H_0: {
                if (age == 0 || age == 80 || age == 160) {
                    gameMap.addShip(new Ship(gameMap.getTick(), gameMap.getSpawnLocation(0), Ship.ShipType.TYPE_H, 1));
                }
                if (age == 40 || age == 120 || age == 200) {
                    gameMap.addShip(new Ship(gameMap.getTick(), gameMap.getSpawnLocation(11), Ship.ShipType.TYPE_H, -1));
                }
                if (age == 200) {
                    done = true;
                }
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
        U_1,
        X_0,
        V_0,
        K_0,
        H_0,
    }

}
