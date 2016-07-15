/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Server;

import CommObjects.ControlMessage;
import CommObjects.GameStatusMessage;
import CommObjects.LoginMessage;
import CommObjects.UserStatsMessage;
import Engine.GameMapImpl;
import Objects.GameMap;
import Objects.Raptor;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.HttpURLConnection;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import raptorclone.Configurations;

/**
 *
 * @author Sasa
 */
public class RaptorCloneServer implements Runnable {

    private static final Map<String, Raptor.RaptorSubsystem> subsystemCodesMap = new HashMap<>();

    static {
        subsystemCodesMap.put("r1c1p1", Raptor.RaptorSubsystem.MOVE_HORIZONTAL);
        subsystemCodesMap.put("r1c1p2", Raptor.RaptorSubsystem.MOVE_HORIZONTAL);
        subsystemCodesMap.put("r1c1p3", Raptor.RaptorSubsystem.MOVE_HORIZONTAL);
        subsystemCodesMap.put("r1c2p1", Raptor.RaptorSubsystem.MOVE_BRAKE);
        subsystemCodesMap.put("r1c2p2", Raptor.RaptorSubsystem.MOVE_BRAKE);
        subsystemCodesMap.put("r1c2p3", Raptor.RaptorSubsystem.MOVE_BRAKE);
        subsystemCodesMap.put("r1c3p1", Raptor.RaptorSubsystem.MOVE_VERTICAL);
        subsystemCodesMap.put("r1c3p2", Raptor.RaptorSubsystem.MOVE_VERTICAL);
        subsystemCodesMap.put("r1c3p3", Raptor.RaptorSubsystem.MOVE_VERTICAL);
        subsystemCodesMap.put("r1meta", Raptor.RaptorSubsystem.MOVE_SYSTEM);
        subsystemCodesMap.put("r2c1p1", Raptor.RaptorSubsystem.HULL_RADAR);
        subsystemCodesMap.put("r2c1p2", Raptor.RaptorSubsystem.HULL_RADAR);
        subsystemCodesMap.put("r2c1p3", Raptor.RaptorSubsystem.HULL_RADAR);
        subsystemCodesMap.put("r2c2p1", Raptor.RaptorSubsystem.HULL_HEALTH);
        subsystemCodesMap.put("r2c2p2", Raptor.RaptorSubsystem.HULL_HEALTH);
        subsystemCodesMap.put("r2c2p3", Raptor.RaptorSubsystem.HULL_HEALTH);
        subsystemCodesMap.put("r2c3p1", Raptor.RaptorSubsystem.HULL_SHEILD);
        subsystemCodesMap.put("r2c3p2", Raptor.RaptorSubsystem.HULL_SHEILD);
        subsystemCodesMap.put("r2c3p3", Raptor.RaptorSubsystem.HULL_SHEILD);
        subsystemCodesMap.put("r2meta", Raptor.RaptorSubsystem.HULL_SYSTEM);
        subsystemCodesMap.put("r3c1p1", Raptor.RaptorSubsystem.WEAPON_TYPES);
        subsystemCodesMap.put("r3c1p2", Raptor.RaptorSubsystem.WEAPON_TYPES);
        subsystemCodesMap.put("r3c1p3", Raptor.RaptorSubsystem.WEAPON_TYPES);
        subsystemCodesMap.put("r3c2p1", Raptor.RaptorSubsystem.WEAPONS_SPEED);
        subsystemCodesMap.put("r3c2p2", Raptor.RaptorSubsystem.WEAPONS_SPEED);
        subsystemCodesMap.put("r3c2p3", Raptor.RaptorSubsystem.WEAPONS_SPEED);
        subsystemCodesMap.put("r3c3p1", Raptor.RaptorSubsystem.WEAPON_POWER);
        subsystemCodesMap.put("r3c3p2", Raptor.RaptorSubsystem.WEAPON_POWER);
        subsystemCodesMap.put("r3c3p3", Raptor.RaptorSubsystem.WEAPON_POWER);
        subsystemCodesMap.put("r3meta", Raptor.RaptorSubsystem.WEAPON_SYSTEM);

    }

    private int nextClientId = 0;

    private Map<Raptor.RaptorSubsystem, Integer> verifyLogin(LoginMessage loginMessage) {
        try {
            URL url = new URL(Configurations.getDbUrl().replace("%USERNAME%", loginMessage.getUsername()).replace("%PASSWORD%", loginMessage.getPassword()));
            url = new URL(Configurations.getDbUrl().replace("%USERNAME%", "dryrun2").replace("%PASSWORD%", "dryrun2_glhf"));
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            connection.setDoOutput(true);

            Set<String> keys = new HashSet<>();
            Map<Raptor.RaptorSubsystem, Integer> subsystems = new EnumMap<>(Raptor.RaptorSubsystem.class);

            try (BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
                String line;
                while ((line = in.readLine()) != null) {
                    String[] kv = line.split(":", 2);
                    if (kv.length != 2) {
                        continue;
                    }
                    if (!keys.contains(kv[0]) && subsystemCodesMap.containsKey(kv[0]) && kv[1].trim().equals("Y")) {
                        Raptor.RaptorSubsystem subsystem = subsystemCodesMap.get(kv[0]);
                        subsystems.put(subsystem, subsystems.getOrDefault(subsystem, 0) + 1);
                        keys.add(kv[0]);
                    }
                }
            }
            return subsystems;

        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    @Override
    public void run() {
        try (ServerSocket serverSocket = new ServerSocket(Configurations.getPort());) {
            while (!serverSocket.isClosed()) {
                final Socket socket = serverSocket.accept();

                final int clientId = nextClientId++;
                new Thread(() -> {
                    final Map<Raptor.RaptorSubsystem, Integer> subsystems = new EnumMap<>(Raptor.RaptorSubsystem.class);

                    try (final ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
                            final ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());) {

                        try {
                            Object o = ois.readObject();
                            if (o instanceof LoginMessage) {
                                Map<Raptor.RaptorSubsystem, Integer> s;
                                if ((s = verifyLogin((LoginMessage) o)) != null) {
                                    subsystems.putAll(s);
                                } else {
                                    System.out.println("BAD CREDENTIALS" + o);
                                    return;
                                }
                            } else {
                                System.out.println("WRONG LOGIN OBJECT" + o);
                                return;
                            }
                        } catch (ClassNotFoundException ex) {
                            ex.printStackTrace();
                        }

                        oos.writeObject(new UserStatsMessage(subsystems));
                        final GameMap game = new GameMapImpl(subsystems);
                        new Thread(() -> {
                            Object oi;
                            try {
                                while ((oi = ois.readObject()) != null) {
                                    if (oi instanceof ControlMessage) {
                                        game.registerControlMessage((ControlMessage) oi);
                                    }
                                }
                            } catch (IOException ex) {
                                ex.printStackTrace();
                            } catch (ClassNotFoundException ex) {
                                ex.printStackTrace();
                            }
                        }, "Server-Listener-" + clientId).start();

                        while (true) {
                            game.nextRound();
                            GameStatusMessage message = game.getStatus();
                            oos.writeObject(message);
                            oos.reset();
                            Thread.sleep(20 - (System.currentTimeMillis() % 20));
                        }

                    } catch (IOException ex) {
                        ex.printStackTrace();
                    } catch (InterruptedException ex) {
                        ex.printStackTrace();
                    }
                }, "Server-GameEngine-" + clientId).start();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

}
