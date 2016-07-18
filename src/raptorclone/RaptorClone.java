/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package raptorclone;

import GUI.LaunchUi;
import Server.RaptorCloneServer;
import java.io.IOException;

/**
 *
 * @author Sasa
 */
public class RaptorClone {

    private static void startServer() {
        new Thread(new RaptorCloneServer(), "Server-Master").start();
    }

    private static void startClient() {
        LaunchUi.main(new String[0]);
    }

    public static void main(String[] args) throws InterruptedException {
        System.setProperty("sun.java2d.opengl", "true");
        try {
            Configurations.readCongfigs("settings.cfg");
            Configurations.readCongfigs("private.cfg");
            Configurations.readCongfigs("local.cfg");
        } catch (IOException ex) {
            System.out.println("Warning: Could not read configuration file; reverting to defaults");
        }
        if (args.length > 0 && args[0].equals("client")) {
            startClient();
        } else if (args.length > 0 && args[0].equals("local")) {
            startServer();
            Thread.sleep(1000);
            startClient();
        } else if (args.length > 0 && args[0].equals("server")) {
            startServer();
        } else {
            startClient();
        }

    }

}
