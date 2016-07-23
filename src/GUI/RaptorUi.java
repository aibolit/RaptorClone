/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GUI;

import CommObjects.ControlMessage;
import CommObjects.GameStatusMessage;
import CommObjects.Message;
import Engine.GameMapImpl;
import Objects.ControlType;
import Objects.Explosion;
import Objects.GameObject;
import Objects.GameStatus;
import Objects.MapBounds;
import Objects.Missile;
import static Objects.Missile.MissileType.BULLET;
import static Objects.Missile.MissileType.DUMBFIRE_MISSILE;
import static Objects.Missile.MissileType.FIREBALL;
import static Objects.Missile.MissileType.MICRO_MISSILE;
import Objects.Point;
import Objects.Raptor;
import Objects.Ship;
import static Objects.Ship.ShipType.TYPE_H;
import static Objects.Ship.ShipType.TYPE_K;
import static Objects.Ship.ShipType.TYPE_U;
import static Objects.Ship.ShipType.TYPE_V;
import static Objects.Ship.ShipType.TYPE_X;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.Transparency;
import java.awt.event.KeyEvent;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import raptorclone.Configurations;

/**
 *
 * @author Aleks
 */
public class RaptorUi extends javax.swing.JFrame {

    private final int biHeight = 1600, biWidth = 1200;
    private BufferedImage biImage;

    private ObjectOutputStream clientOutput;
    private volatile GameStatusMessage gameStatus;
    private volatile boolean debugOn;
    private long lastTick = -1;
    private Set<Star> stars = new HashSet<>();
    private final Random random = new Random();
    private final Map<Long, List<ExplosionParticle>> explosions = new HashMap<>();
    private final MapBounds mapBounds = new MapBounds(0, 1200, 0, 1600);
    private final int gameWidth, gameHeight;

    private static final Color DARK_BLUE = new Color(0, 25, 50);

    /**
     * Creates new form RaptorUi
     */
    public RaptorUi(int width, int height) {
        this.gameWidth = width;
        this.gameHeight = height;
        initComponents();
        initArt();
    }

    public void setGameStatus(GameStatusMessage gameStatus) {
        this.gameStatus = gameStatus;
    }

    public void connectStreams(ObjectInputStream ois, ObjectOutputStream oos) {
        clientOutput = oos;
    }

    private void initArt() {
        for (int i = 0; i < 100; i++) {
            stars.add(new Star(random.nextDouble() * 5, new Point(
                    mapBounds.getMinX() + random.nextDouble() * mapBounds.getMaxX(),
                    mapBounds.getMinY() + random.nextDouble() * mapBounds.getMaxY())));
        }
    }

    public void drawFrame() {
        GameStatusMessage status = gameStatus;
        //System.out.println("B" + gameStatus);

        if (gameStatus.getTick() != lastTick) {
            if (random.nextInt(10) == 0) {
                stars.add(new Star(random.nextDouble() * 5, new Point(mapBounds.getMinX() + random.nextDouble() * (mapBounds.getMaxX() + 2 * GameMapImpl.MAP_BOUNDS_PADDING) - GameMapImpl.MAP_BOUNDS_PADDING, mapBounds.getMinY() - GameMapImpl.MAP_BOUNDS_PADDING)));
            }

            for (long i = lastTick; i < gameStatus.getTick(); i++) {
                List<Star> removeStars = new ArrayList<>();
                for (Star star : stars) {
                    star.getPosition().add(0, Math.max(star.getSize() / 2, .8));
                    if (star.getPosition().getY() < -50) {
                        removeStars.add(star);
                    }
                }
                for (Star star : removeStars) {
                    stars.remove(star);
                }
            }

            lastTick = gameStatus.getTick();
        }

        if (biImage == null) {
            //biImage = new BufferedImage(biWidth, biHeight, BufferedImage.TYPE_INT_ARGB);
            biImage = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration().createCompatibleImage(biWidth, biHeight, Transparency.TRANSLUCENT);
        }

        Graphics2D cg = (Graphics2D) biImage.getGraphics();

        cg.setColor(Color.BLACK);
        cg.fillRect(0, 0, biWidth, biHeight);

        for (Star star : stars) {
            cg.setColor(Color.LIGHT_GRAY);
            cg.fillOval(
                    (int) (star.getPosition().getX() - star.getSize() / 2),
                    (int) (star.getPosition().getY() - star.getSize() / 2),
                    (int) (star.getSize()),
                    (int) (star.getSize()));
        }

        Raptor raptor = null;
        Ship boss = null;

        for (GameObject gameObject : status.getGameObjects()) {
            if (gameObject instanceof Raptor) {
                Image i = Configurations.getPlayerImage(12);
                int width = 45, height = 50;
                cg.drawImage(i, (int) (gameObject.getPosition().getX() - width / 2), (int) (gameObject.getPosition().getY() - height / 2), width, height, null);
                raptor = (Raptor) gameObject;
                //cg.drawImage(i, (int) (gameObject.getPosition().getX() - i.getWidth(null) / 2), (int) (gameObject.getPosition().getY() - i.getHeight(null) / 2), null);
            } else if (gameObject instanceof Ship) {
                Ship ship = (Ship) gameObject;
                double radius = ship.getRadius();
                int width = (int) (radius * 2.2), height = (int) (radius * 2.2);
                if (ship.isBoss()) {
                    boss = ship;
                }
                switch (ship.getShipType()) {
                    case TYPE_U: {
                        Image i = Configurations.getPlayerImage(4);
                        cg.drawImage(i, (int) (gameObject.getPosition().getX() - width / 2), (int) (gameObject.getPosition().getY() - height / 2), width, height, null);
                    }
                    break;
                    case TYPE_X: {
                        Image i = Configurations.getPlayerImage(16);
                        cg.drawImage(i, (int) (gameObject.getPosition().getX() - width / 2), (int) (gameObject.getPosition().getY() - height / 2), width, height, null);
                    }
                    break;
                    case TYPE_V: {
                        Image i = Configurations.getPlayerImage(23);
                        cg.drawImage(i, (int) (gameObject.getPosition().getX() - width / 2), (int) (gameObject.getPosition().getY() - height / 2), width, height, null);
                    }
                    break;
                    case TYPE_K: {
                        Image i = Configurations.getPlayerImage(8);
                        cg.drawImage(i, (int) (gameObject.getPosition().getX() - width / 2), (int) (gameObject.getPosition().getY() - height / 2), width, height, null);
                    }
                    break;
                    case TYPE_H: {
                        Image i = Configurations.getPlayerImage(17);
                        cg.drawImage(i, (int) (gameObject.getPosition().getX() - width / 2), (int) (gameObject.getPosition().getY() - height / 2), width, height, null);
                    }
                    break;
                    case TYPE_B: {
                        Image i = Configurations.getPlayerImage(15);
                        cg.drawImage(i, (int) (gameObject.getPosition().getX() - width / 2), (int) (gameObject.getPosition().getY() - height / 2), width, height, null);
                    }
                    break;
                    default:
                        throw new AssertionError();
                }
            } else if (gameObject instanceof Explosion) {
                if (!explosions.containsKey(gameObject.getId())) {
                    Explosion explosion = (Explosion) gameObject;
                    List<ExplosionParticle> particles = new ArrayList<>();
                    for (int i = 0; i < explosion.getRadius(); i++) {
                        particles.add(new ExplosionParticle(
                                explosion.getPosition(),
                                explosion.getDirection() == null ? random.nextDouble() * Math.PI * 2 : explosion.getDirection() + random.nextDouble() * Math.PI - Math.PI / 2,
                                explosion.getSpeed(),
                                explosion.getCreationTick(),
                                explosion.getCreationTick() + explosion.getDuration()));
                    }
                    if (particles.size() > 0) {
                        explosions.put(explosion.getId(), particles);
                    }
                }
            } else if (gameObject instanceof Missile) {
                Missile missile = (Missile) gameObject;
                switch (missile.getMissileType()) {
                    case BULLET:
                        cg.setColor(Color.WHITE);
                        cg.setStroke(new BasicStroke(2));
                        switch ((int) missile.getId() % 3) {
                            case 0:
                                cg.drawLine(
                                        (int) (missile.getPosition().getX()),
                                        (int) (missile.getPosition().getY() - 2),
                                        (int) (missile.getPosition().getX()),
                                        (int) (missile.getPosition().getY() + 2));
                                break;
                            case 1:
                                cg.drawLine(
                                        (int) (missile.getPosition().getX() + 2),
                                        (int) (missile.getPosition().getY() - 2),
                                        (int) (missile.getPosition().getX() - 2),
                                        (int) (missile.getPosition().getY() + 2));
                                break;
                            default:
                                cg.drawLine(
                                        (int) (missile.getPosition().getX() - 2),
                                        (int) (missile.getPosition().getY() - 2),
                                        (int) (missile.getPosition().getX() + 2),
                                        (int) (missile.getPosition().getY() + 2));
                                break;
                        }
                        break;
                    case DUMBFIRE_MISSILE:
                        cg.setStroke(new BasicStroke(4, BasicStroke.CAP_SQUARE, BasicStroke.JOIN_MITER));
                        cg.setColor(Color.LIGHT_GRAY);
                        cg.drawLine(
                                (int) (missile.getPosition().getX()),
                                (int) (missile.getPosition().getY()),
                                (int) (missile.getPosition().getX()),
                                (int) (missile.getPosition().getY()) + 20);

                        cg.setStroke(new BasicStroke(4, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                        cg.setColor(Color.RED);
                        cg.drawLine(
                                (int) (missile.getPosition().getX()),
                                (int) (missile.getPosition().getY() + 16),
                                (int) (missile.getPosition().getX()),
                                (int) (missile.getPosition().getY()) + 20);
                        break;
                    case MICRO_MISSILE:
                        cg.setStroke(new BasicStroke(3, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                        cg.setColor(Color.CYAN);
                        cg.drawLine(
                                (int) (missile.getPosition().getX()),
                                (int) (missile.getPosition().getY()),
                                (int) (missile.getPosition().getX()),
                                (int) (missile.getPosition().getY()) + 8);
                        break;
                    case FIREBALL:
                        cg.setColor(Color.ORANGE);
                        cg.setStroke(new BasicStroke(1, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                        cg.fillOval((int) (missile.getPosition().getX() - 6), (int) (missile.getPosition().getY() - 6), 12, 12);
                        cg.setColor(Color.YELLOW);
                        cg.fillOval((int) (missile.getPosition().getX() - 5), (int) (missile.getPosition().getY() - 5), 10, 10);
                        break;
                    default:
                        break;
                }
            }
            if (debugOn) {
                cg.setColor(Color.ORANGE);
                cg.fillOval((int) (gameObject.getPosition().getX() - gameObject.getRadius()), (int) (gameObject.getPosition().getY() - gameObject.getRadius()), (int) (gameObject.getRadius() * 2), (int) (gameObject.getRadius() * 2));
            }
        }

        cg.setColor(Color.ORANGE);
        List<Long> removeExplosions = new ArrayList<Long>();
        for (Map.Entry<Long, List<ExplosionParticle>> entry : explosions.entrySet()) {
            Long key = entry.getKey();
            List<ExplosionParticle> value = entry.getValue();
            boolean removeExplosion = value.size() == 0;

            for (ExplosionParticle explosionParticle : value) {
                if (explosionParticle.getEndRound() <= gameStatus.getTick()) {
                    removeExplosion = true;
                    break;
                }

                double p = 1.0 * (gameStatus.getTick() - explosionParticle.getStartRound()) / (explosionParticle.getEndRound() - explosionParticle.getStartRound());
                double size = 10.0 * (1 - p);
                cg.fillOval(
                        (int) (explosionParticle.getPosition().getX() + Math.cos(explosionParticle.getDirection()) * (gameStatus.getTick() - explosionParticle.startRound)),
                        (int) (explosionParticle.getPosition().getY() + Math.sin(explosionParticle.getDirection()) * (gameStatus.getTick() - explosionParticle.startRound)),
                        (int) size,
                        (int) size);

            }
            if (removeExplosion) {
                removeExplosions.add(key);
            }
        }
        for (Long removeExplosion : removeExplosions) {
            explosions.remove(removeExplosion);
        }

        //HUD
        if (raptor != null && raptor.isAlive()) {
            for (int i = 0; i < raptor.getHp(); i++) {
                Image img = Configurations.getPlayerImage(12);
                int width = 30, height = 30;
                cg.drawImage(img, 40 + 35 * i, 40, width, height, null);
            }

            if (raptor.getSubsystemLevel(Raptor.RaptorSubsystem.HULL_RADAR) < Raptor.RaptorSubsystem.HULL_RADAR.getMaxLevel()) {
                float[] phase = {20.0f, 100.0f};
                cg.setStroke(new BasicStroke(1, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND, 1, phase, 0));
                cg.setColor(Color.GRAY);
                double radius = (600 + 300 * raptor.getSubsystemLevel(Raptor.RaptorSubsystem.HULL_RADAR));
                cg.drawOval((int) (raptor.getPosition().getX() - radius), (int) (raptor.getPosition().getY() - radius), (int) radius * 2, (int) radius * 2);
            }

            if (raptor.getSubsystemLevel(Raptor.RaptorSubsystem.HULL_SHEILD) > 0) {
                cg.setColor(new Color(8, 182, 180, 100));
                cg.fillRect(40, 80, 200, 30);

                cg.setColor(new Color(8, 182, 180));
                cg.fillRect(40, 80, (int) (200 * raptor.getShield()), 30);

                if (raptor.getShield() >= 1) {
                    cg.setStroke(new BasicStroke(4, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                    cg.setColor(Color.WHITE);
                    cg.drawRect(40, 80, (int) (200 * raptor.getShield()), 30);
                }
            }
            if (boss != null) {
                cg.setColor(Color.RED);
                cg.fillRect(150, 1500, 900, 50);
                cg.setColor(Color.GREEN);
                cg.fillRect(150, 1500, (int) (900.0 * boss.getHp() / boss.getMaxHp()), 50);
            }
        }

        //MESSAGE BOX
        if (gameStatus.getMessage() != null) {
            cg.setColor(DARK_BLUE);
            cg.fillRect(50, 1350, 1100, 200);
            cg.setColor(Color.ORANGE);
            cg.setStroke(new BasicStroke(5, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
            cg.drawRect(50, 1350, 1100, 200);
            cg.setFont(new Font("Monospaced", Font.BOLD, 48));
            int i = 0;
            for (String line : gameStatus.getMessage().split("\n")) {
                cg.drawString(line, 75, 1400 + (48 * i++));
            }
            if (gameStatus.getGameStatus() == GameStatus.WAITING) {
                cg.setFont(new Font("Monospaced", Font.BOLD, 32));
                cg.drawString("Space to continue...", 430, 1535);
            }
        }
        //OVERLAY TEXT
        if (gameStatus.getGameStatus() == GameStatus.PAUSED) {
            cg.setColor(Color.ORANGE);
            cg.setFont(new Font("Monospaced", Font.PLAIN, 108));
            Rectangle2D b = cg.getFontMetrics().getStringBounds("Paused", cg);
            cg.drawString("Paused", (float) (biWidth / 2 - b.getWidth() / 2), (float) (biHeight / 2));
        } else if (gameStatus.getGameStatus() == GameStatus.GAME_OVER) {
            cg.setColor(gameStatus.getTick() % 80 < 40 ? Color.RED : new Color(150, 0, 0));
            cg.setFont(new Font("Monospaced", Font.PLAIN, 108));
            Rectangle2D b = cg.getFontMetrics().getStringBounds("GAME OVER", cg);
            cg.drawString("GAME OVER", (float) (biWidth / 2 - b.getWidth() / 2), (float) (biHeight / 2));
        }

        //PAINT SCREEN BELOW
        try {
            java.awt.EventQueue.invokeAndWait(() -> {
                Graphics2D g2x = (Graphics2D) canvas.getGraphics();
                g2x.drawImage(biImage, AffineTransform.getScaleInstance(1.0 * canvas.getWidth() / biImage.getWidth(), 1.0 * canvas.getHeight() / biImage.getHeight()), null);
            });
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        } catch (InvocationTargetException ex) {
            ex.printStackTrace();
        }
    }

    private void sendMessage(Message message) {
        try {
            clientOutput.writeObject(message);
            clientOutput.reset();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        canvas = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setPreferredSize(new Dimension(gameWidth, gameHeight));
        setResizable(false);
        addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                formKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                formKeyReleased(evt);
            }
        });

        javax.swing.GroupLayout canvasLayout = new javax.swing.GroupLayout(canvas);
        canvas.setLayout(canvasLayout);
        canvasLayout.setHorizontalGroup(
            canvasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1043, Short.MAX_VALUE)
        );
        canvasLayout.setVerticalGroup(
            canvasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1195, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(canvas, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(canvas, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void formKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_formKeyPressed
        switch (evt.getKeyCode()) {
            case KeyEvent.VK_ESCAPE:
                System.exit(0);
                break;
            case KeyEvent.VK_UP:
                sendMessage(new ControlMessage(ControlType.UP, true));
                break;
            case KeyEvent.VK_DOWN:
                sendMessage(new ControlMessage(ControlType.DOWN, true));
                break;
            case KeyEvent.VK_LEFT:
                sendMessage(new ControlMessage(ControlType.LEFT, true));
                break;
            case KeyEvent.VK_RIGHT:
                sendMessage(new ControlMessage(ControlType.RIGHT, true));
                break;
            case KeyEvent.VK_META:
            case KeyEvent.VK_CONTROL:
            case KeyEvent.VK_SHIFT:
                sendMessage(new ControlMessage(ControlType.FIRE, true));
                break;
            case KeyEvent.VK_P:
                sendMessage(new ControlMessage(ControlType.PAUSE, true));
                break;
            case KeyEvent.VK_SPACE:
                sendMessage(new ControlMessage(ControlType.SKIP, true));
                break;
            case KeyEvent.VK_D:
                debugOn = !debugOn;
                break;
            default:
                break;
        }
    }//GEN-LAST:event_formKeyPressed

    private void formKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_formKeyReleased
        switch (evt.getKeyCode()) {
            case KeyEvent.VK_UP:
                sendMessage(new ControlMessage(ControlType.UP, false));
                break;
            case KeyEvent.VK_DOWN:
                sendMessage(new ControlMessage(ControlType.DOWN, false));
                break;
            case KeyEvent.VK_LEFT:
                sendMessage(new ControlMessage(ControlType.LEFT, false));
                break;
            case KeyEvent.VK_RIGHT:
                sendMessage(new ControlMessage(ControlType.RIGHT, false));
                break;
            case KeyEvent.VK_META:
            case KeyEvent.VK_CONTROL:
            case KeyEvent.VK_SHIFT:
                sendMessage(new ControlMessage(ControlType.FIRE, false));
                break;
            default:
                break;

        }
    }//GEN-LAST:event_formKeyReleased

    private class Star {

        private final double size;
        private final Point position;

        public Star(double size, Point position) {
            this.size = size;
            this.position = position;
        }

        public double getSize() {
            return size;
        }

        public Point getPosition() {
            return position;
        }
    }

    private class ExplosionParticle {

        private final Point position;
        private final double direction;
        private final double speed;
        private final long startRound, endRound;

        public ExplosionParticle(Point position, double direction, double speed, long startRound, long endRound) {
            this.position = position;
            this.direction = direction;
            this.startRound = startRound;
            this.endRound = endRound;
            this.speed = speed;
        }

        public Point getPosition() {
            return position;
        }

        public double getDirection() {
            return direction;
        }

        public long getStartRound() {
            return startRound;
        }

        public long getEndRound() {
            return endRound;
        }

        @Override
        public String toString() {
            return "ExplosionParticle{" + "position=" + position + ", direction=" + direction + ", speed=" + speed + ", startRound=" + startRound + ", endRound=" + endRound + '}';
        }
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;

                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(RaptorUi.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);

        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(RaptorUi.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);

        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(RaptorUi.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);

        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(RaptorUi.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> {
            new RaptorUi(1200, 1600).setVisible(true);
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    javax.swing.JPanel canvas;
    // End of variables declaration//GEN-END:variables
}
