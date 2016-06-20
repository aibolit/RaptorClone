/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Server;

import CommObjects.ControlMessage;
import CommObjects.GameStatusMessage;
import Objects.GameMap;
import Engine.GameMapImpl;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import raptorclone.Configurations;

/**
 *
 * @author Sasa
 */
public class RaptorCloneServer implements Runnable {

    private int nextClientId = 0;

    @Override
    public void run() {
        try (ServerSocket serverSocket = new ServerSocket(Configurations.getPort());) {
            while (!serverSocket.isClosed()) {
                final Socket socket = serverSocket.accept();

                final int clientId = nextClientId++;
                new Thread(() -> {
                    final GameMap game = new GameMapImpl();
                    try (final ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
                            final ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());) {

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
