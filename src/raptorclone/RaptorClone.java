/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package raptorclone;

import GUI.RaptorUi;
import Server.RaptorCloneServer;
import java.io.IOException;

/**
 *
 * @author Sasa
 */
public class RaptorClone {

    public static void main(String[] args) throws InterruptedException {
        System.setProperty("sun.java2d.opengl", "true");
        try {
            Configurations.readCongfigs("settings.cfg");
        } catch (IOException ex) {
            System.out.println("Warning: Could not read configuration file; reverting to defaults");
        }
        if (args.length > 0 && args[0].equals("client")) {
            RaptorUi.main(args);
        } else if (args.length > 0 && args[0].equals("local")) {
            new Thread(new RaptorCloneServer(), "Server-Master").start();
            Thread.sleep(1000);
            RaptorUi.main(args);
        } else {
            new Thread(new RaptorCloneServer(), "Server-Master").start();
        }

    }

}
