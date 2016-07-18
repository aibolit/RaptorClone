package raptorclone;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.StringTokenizer;
import javax.imageio.ImageIO;

/**
 *
 * @author Aleks
 */
public class Configurations {

    private static boolean inited = false;
    private static int port = 17429;
    private static String host = "127.0.0.1";
    private static String puzzleAnswer = "Octopus Decoy";
    private static String dbUrl = "";
    private static String user = "", password = "";
    private static boolean saveLocal = false;
    private static String aspectRatio = "1200x1600";

    private static final List<Image> shipImages = new ArrayList<>();

    private static void init() {
        try {
            BufferedImage spaceships = ImageIO.read(new File("spaceships.png"));
            for (int i = 0; i < 20; i++) {
                for (int j = 0; j < 3; j++) {
                    BufferedImage subimage = spaceships.getSubimage(48 * j, i * 24, 24, 24);
                    for (int k = 0; k < 24; k++) {
                        for (int l = 0; l < 24; l++) {
                            if (subimage.getRGB(k, l) == -1 || subimage.getRGB(k, l) == -921103) {
                                subimage.setRGB(k, l, 0x00000000);
                            }
                        }
                    }
                    shipImages.add(subimage);
                }
            }
            Collections.shuffle(shipImages, new Random(14729));
        } catch (IOException ex) {
        }
        inited = true;
        //INIT PARAMS HERE
    }

    public static Image getPlayerImage(int id) {
        return shipImages.get(id % shipImages.size());
    }

    public static int getPort() {
        return port;
    }

    public static String getHost() {
        return host;
    }

    public static String getPuzzleAnswer() {
        return puzzleAnswer;
    }

    public static String getDbUrl() {
        return dbUrl;
    }

    public static String getUser() {
        return user;
    }

    public static String getPassword() {
        return password;
    }

    public static void setUser(String user) {
        Configurations.user = user;
    }

    public static void setPassword(String password) {
        Configurations.password = password;
    }

    public static boolean isSaveLocal() {
        return saveLocal;
    }

    public static void setSaveLocal(boolean saveLocal) {
        Configurations.saveLocal = saveLocal;
    }

    public static boolean isInited() {
        return inited;
    }

    public static void setInited(boolean inited) {
        Configurations.inited = inited;
    }

    public static String getAspectRatio() {
        return aspectRatio;
    }

    public static void setAspectRatio(String aspectRatio) {
        Configurations.aspectRatio = aspectRatio;
    }

    public static void readCongfigs(String file) throws IOException {
        if (!inited) {
            init();
        }
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                StringTokenizer st = new StringTokenizer(line);
                if (!st.hasMoreTokens()) {
                    continue;
                }
                switch (st.nextToken()) {
                    case "port":
                        port = Integer.parseInt(st.nextToken());
                        break;
                    case "host":
                        host = st.nextToken();
                        break;
                    case "db-url":
                        dbUrl = st.nextToken();
                        break;
                    case "puzzle-answer":
                        puzzleAnswer = "";
                        while (st.hasMoreTokens()) {
                            if (puzzleAnswer.length() > 0) {
                                puzzleAnswer += " ";
                            }
                            puzzleAnswer += st.nextToken();
                        }
                        break;
                    case "user":
                        user = "";
                        while (st.hasMoreTokens()) {
                            if (user.length() > 0) {
                                user += " ";
                            }
                            user += st.nextToken();
                        }
                        break;
                    case "password":
                        password = "";
                        while (st.hasMoreTokens()) {
                            if (password.length() > 0) {
                                password += " ";
                            }
                            password += st.nextToken();
                        }
                        break;
                    case "save-local":
                        saveLocal = Boolean.parseBoolean(st.nextToken());
                        break;
                    case "aspect-ratio":
                        aspectRatio = st.nextToken();
                        break;
                    default:
                        if (line.charAt(0) != '#') {
                            System.out.println("Oops no such setting " + line);
                        }
                        break;
                }
            }
        }
    }

    public static void saveLocalConfigurations() throws IOException {
        try (PrintWriter pw = new PrintWriter(new File("local.cfg"))) {
            pw.print(getLocalConfigString());
        }
    }

    public static String getLocalConfigString() {
        StringBuilder out = new StringBuilder();
        out.append("user ")
                .append(user)
                .append("\npassword ")
                .append(password)
                .append("\nsave-local ")
                .append(saveLocal)
                .append("\naspect-ratio ")
                .append(aspectRatio)
                .append("\n");
        return out.toString();
    }

    private Configurations() {

    }
}
