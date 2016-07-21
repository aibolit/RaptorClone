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
package GUI;

import CommObjects.ControlMessage;
import CommObjects.GameStatusMessage;
import CommObjects.LoginMessage;
import CommObjects.UserStatsMessage;
import Objects.ControlType;
import Objects.GameStatus;
import Objects.Raptor;
import java.awt.Color;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationTargetException;
import java.net.Socket;
import java.util.EnumMap;
import java.util.Map;
import javax.swing.JLabel;
import raptorclone.Configurations;

/**
 *
 * @author Aleks
 */
public class LaunchUi extends javax.swing.JFrame {

    /**
     * Creates new form LaunchUi
     */
    public LaunchUi() {
        initComponents();
        initGameProperties();
    }

    private void initGameProperties() {
        subsystemWidgets.put(Raptor.RaptorSubsystem.MOVE_SYSTEM, moveSystemStatus);
        subsystemWidgets.put(Raptor.RaptorSubsystem.MOVE_VERTICAL, moveVerticalStatus);
        subsystemWidgets.put(Raptor.RaptorSubsystem.MOVE_HORIZONTAL, moveHorizontalStatus);
        subsystemWidgets.put(Raptor.RaptorSubsystem.MOVE_BRAKE, moveBreakStatus);

        subsystemWidgets.put(Raptor.RaptorSubsystem.WEAPON_SYSTEM, weaponSystemStatus);
        subsystemWidgets.put(Raptor.RaptorSubsystem.WEAPON_SPEED, weaponSpeedStatus);
        subsystemWidgets.put(Raptor.RaptorSubsystem.WEAPON_TYPES, weaponTypesStatus);
        subsystemWidgets.put(Raptor.RaptorSubsystem.WEAPON_POWER, weaponPowerStatus);

        subsystemWidgets.put(Raptor.RaptorSubsystem.HULL_SYSTEM, hullSystemStatus);
        subsystemWidgets.put(Raptor.RaptorSubsystem.HULL_SHEILD, hullShieldsStatus);
        subsystemWidgets.put(Raptor.RaptorSubsystem.HULL_HEALTH, hullHealthStatus);
        subsystemWidgets.put(Raptor.RaptorSubsystem.HULL_RADAR, hullRadarStatus);

        for (Map.Entry<Raptor.RaptorSubsystem, JLabel> entry : subsystemWidgets.entrySet()) {
            entry.getValue().setText("?");
        }
    }

    private void setSubsystemsStatus(Map<Raptor.RaptorSubsystem, Integer> subsystems) {
        if (subsystems.containsKey(Raptor.RaptorSubsystem.HULL_SYSTEM) && subsystems.get(Raptor.RaptorSubsystem.HULL_SYSTEM) == Raptor.RaptorSubsystem.HULL_SYSTEM.getMaxLevel()
                && subsystems.containsKey(Raptor.RaptorSubsystem.WEAPON_SYSTEM) && subsystems.get(Raptor.RaptorSubsystem.WEAPON_SYSTEM) == Raptor.RaptorSubsystem.WEAPON_SYSTEM.getMaxLevel()
                && subsystems.containsKey(Raptor.RaptorSubsystem.MOVE_SYSTEM) && subsystems.get(Raptor.RaptorSubsystem.MOVE_SYSTEM) == Raptor.RaptorSubsystem.MOVE_SYSTEM.getMaxLevel()) {
            statusLabel.setText("Ready to Launch");
            statusLabel.setForeground(Color.GREEN);
        } else {
            statusLabel.setText("Warning: Imminent Failure... Meta puzzles Not solved");
            statusLabel.setForeground(new Color(205, 83, 73));
        }

        for (Map.Entry<Raptor.RaptorSubsystem, JLabel> entry : subsystemWidgets.entrySet()) {
            Raptor.RaptorSubsystem subsystem = entry.getKey();
            JLabel label = entry.getValue();
            Integer level = 0;
            if (subsystems.containsKey(subsystem) && (level = subsystems.get(subsystem)) != null) {
                if (level <= 0) {
                    label.setForeground(new Color(205, 83, 73));
                    label.setText("OFFLINE");
                } else if (level < subsystem.getMaxLevel()) {
                    label.setText(level + "/" + subsystem.getMaxLevel());
                    label.setForeground(new Color(205, 150, 0));
                } else {
                    label.setText("ONLINE");
                    label.setForeground(Color.GREEN);
                }
            } else {
                label.setForeground(new Color(205, 83, 73));
                label.setText("OFFLINE");
            }

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

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        loginButton = new javax.swing.JButton();
        saveCredentialsButton = new javax.swing.JCheckBox();
        userEntry = new javax.swing.JTextField();
        passwordEntry = new javax.swing.JPasswordField();
        jLabel3 = new javax.swing.JLabel();
        aspectRatioCombo = new javax.swing.JComboBox<>();
        jPanel2 = new javax.swing.JPanel();
        statusLabel = new javax.swing.JLabel();
        launchGameButton = new javax.swing.JButton();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        moveBreakStatus = new javax.swing.JLabel();
        moveVerticalStatus = new javax.swing.JLabel();
        moveHorizontalStatus = new javax.swing.JLabel();
        moveSystemStatus = new javax.swing.JLabel();
        hullRadarStatus = new javax.swing.JLabel();
        hullShieldsStatus = new javax.swing.JLabel();
        hullHealthStatus = new javax.swing.JLabel();
        hullSystemStatus = new javax.swing.JLabel();
        weaponSpeedStatus = new javax.swing.JLabel();
        weaponPowerStatus = new javax.swing.JLabel();
        weaponTypesStatus = new javax.swing.JLabel();
        weaponSystemStatus = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Puzzle Challenge");
        setBackground(new java.awt.Color(255, 255, 255));
        setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        setForeground(java.awt.Color.white);
        setIconImage(Configurations
            .getPlayerImage(15)
        );
        setResizable(false);

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setBorder(new javax.swing.border.MatteBorder(null));

        jLabel1.setText("Username");

        jLabel2.setText("Password");

        loginButton.setText("Login");
        loginButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                loginButtonActionPerformed(evt);
            }
        });

        saveCredentialsButton.setBackground(new java.awt.Color(255, 255, 255));
        saveCredentialsButton.setSelected(true);
        saveCredentialsButton.setText("Save Credentials");

        userEntry.setText(Configurations.getUser());

        passwordEntry.setText(Configurations.getPassword());

        jLabel3.setText("Size");

        aspectRatioCombo.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "1200x1600", "900x1200", "600x800" }));
        aspectRatioCombo.setSelectedItem(Configurations.getAspectRatio());

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(saveCredentialsButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel1)
                                    .addComponent(jLabel2)
                                    .addComponent(jLabel3))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(aspectRatioCombo, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(userEntry)
                                    .addComponent(passwordEntry, javax.swing.GroupLayout.DEFAULT_SIZE, 386, Short.MAX_VALUE)))))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(loginButton)))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(userEntry, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(passwordEntry, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(aspectRatioCombo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(saveCredentialsButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(loginButton)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));
        jPanel2.setBorder(new javax.swing.border.MatteBorder(null));

        statusLabel.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        statusLabel.setForeground(new java.awt.Color(153, 153, 153));
        statusLabel.setText("Awaiting Login, Commander...");

        launchGameButton.setText("Launch");
        launchGameButton.setEnabled(false);
        launchGameButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                launchGameButtonActionPerformed(evt);
            }
        });

        jLabel4.setText("Weapons Systems");

        jLabel5.setText("Subsystems");

        jLabel6.setText("Power");

        jLabel7.setText("Rate");

        jLabel8.setText("Defensive Systems");

        jLabel9.setText("Plating");

        jLabel10.setText("Shields");

        jLabel11.setText("Radar");

        jLabel12.setText("Navigation Systems");

        jLabel13.setText("Lateral");

        jLabel14.setText("Parallel");

        jLabel15.setText("Breaks");

        moveBreakStatus.setText("jLabel16");

        moveVerticalStatus.setText("jLabel17");

        moveHorizontalStatus.setText("jLabel18");

        moveSystemStatus.setText("jLabel19");

        hullRadarStatus.setText("jLabel20");

        hullShieldsStatus.setText("jLabel21");

        hullHealthStatus.setText("jLabel22");

        hullSystemStatus.setText("jLabel23");

        weaponSpeedStatus.setText("jLabel24");

        weaponPowerStatus.setText("jLabel25");

        weaponTypesStatus.setText("jLabel26");

        weaponSystemStatus.setText("jLabel27");

        jLabel17.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel17.setText("v1.0 By Aleks Tamarkin");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(statusLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 463, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(launchGameButton))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel15)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(moveBreakStatus))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel14)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(moveVerticalStatus))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel13)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(moveHorizontalStatus))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel12)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(moveSystemStatus))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel11)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(hullRadarStatus))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel10)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(hullShieldsStatus))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel9)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(hullHealthStatus))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(weaponSystemStatus))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel8)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(hullSystemStatus))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel7)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(weaponSpeedStatus))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel6)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(weaponPowerStatus))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel5)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(weaponTypesStatus)))
                .addContainerGap())
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(jLabel17)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(statusLabel)
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(weaponSystemStatus))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(weaponTypesStatus))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(weaponPowerStatus))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(weaponSpeedStatus))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8)
                    .addComponent(hullSystemStatus))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel9)
                    .addComponent(hullHealthStatus))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel10)
                    .addComponent(hullShieldsStatus))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel11)
                    .addComponent(hullRadarStatus))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel12)
                    .addComponent(moveSystemStatus))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel13)
                    .addComponent(moveHorizontalStatus))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel14)
                    .addComponent(moveVerticalStatus))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel15)
                    .addComponent(moveBreakStatus))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(launchGameButton)
                .addGap(1, 1, 1)
                .addComponent(jLabel17, javax.swing.GroupLayout.PREFERRED_SIZE, 15, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void loginButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_loginButtonActionPerformed
        loginButton.setEnabled(false);
        userEntry.setEnabled(false);
        passwordEntry.setEnabled(false);
        saveCredentialsButton.setEnabled(false);
        aspectRatioCombo.setEnabled(false);

        statusLabel.setText("Logging in...");
        statusLabel.setForeground(Color.BLACK);
        final String user = userEntry.getText();
        final String password = new String(passwordEntry.getPassword());
        final boolean isSave = saveCredentialsButton.isSelected();
        String aspectRatio = (String) aspectRatioCombo.getSelectedItem();
        String[] ar = aspectRatio.split("x");
        final int width = Integer.parseInt(ar[0]);
        final int height = Integer.parseInt(ar[1]);

        new Thread(() -> {
            try {
                final Socket socket = new Socket(Configurations.getHost(), Configurations.getPort());

                try (final ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
                        final ObjectInputStream ois = new ObjectInputStream(socket.getInputStream())) {

                    clientOutput = oos;
                    oos.writeObject(new LoginMessage(user, password));
                    UserStatsMessage status = (UserStatsMessage) ois.readObject();
                    setSubsystemsStatus(status.getSubsystems());

                    Configurations.setUser(user);
                    Configurations.setPassword(password);
                    Configurations.setSaveLocal(isSave);
                    Configurations.setAspectRatio(aspectRatio);
                    if (saveCredentialsButton.isSelected()) {
                        Configurations.saveLocalConfigurations();
                    }

                    java.awt.EventQueue.invokeAndWait(() -> {
                        launchGameButton.setEnabled(true);

                        raptorUi = new RaptorUi(width, height);
                        raptorUi.connectStreams(ois, oos);
                    });
                    socket.setSoTimeout(2000);

                    boolean isGameOver = false;
                    boolean inited = false;
                    Object o;
                    try {
                        while ((o = ois.readObject()) != null) {
                            if (o instanceof GameStatusMessage) {
                                raptorUi.setGameStatus((GameStatusMessage) o);
                                if (((GameStatusMessage) o).getGameStatus() == GameStatus.GAME_OVER) {
                                    isGameOver = true;
                                }
                                if (!inited) {
                                    new Thread(() -> {
                                        while (true) {
                                            raptorUi.drawFrame();
                                        }
                                    }, "Client-FrameBuffer").start();

                                    new Thread(() -> {
                                        while (!socket.isClosed() && socket.isConnected()) {
                                            try {
                                                java.awt.EventQueue.invokeAndWait(() -> {
                                                    try {
                                                        oos.writeObject(new ControlMessage(ControlType.HEARTBEAT, false));
                                                    } catch (IOException ex) {
                                                        ex.printStackTrace();
                                                    }
                                                });
                                                Thread.sleep(200);
                                            } catch (InterruptedException ex) {
                                                ex.printStackTrace();
                                            } catch (InvocationTargetException ex) {
                                                ex.printStackTrace();
                                            }
                                        }
                                    }, "Client-Heartbeat").start();
                                    inited = true;

                                }
                            }
                        }
                    } catch (IOException ex) {
                        if (!isGameOver) {
                            ex.printStackTrace();
                            System.exit(0);
                        }
                    } catch (ClassNotFoundException ex) {
                        ex.printStackTrace();
                        ex.getCause();
                    }
                }
            } catch (IOException | InterruptedException | ClassNotFoundException | InvocationTargetException ex) {
                java.awt.EventQueue.invokeLater(() -> {
                    loginButton.setEnabled(true);
                    userEntry.setEnabled(true);
                    passwordEntry.setEnabled(true);
                    saveCredentialsButton.setEnabled(true);
                    aspectRatioCombo.setEnabled(true);

                    statusLabel.setForeground(Color.RED);
                    statusLabel.setText("Invalid Credentials, Commander.");
                });
            }
        }, "Client-Connection").start();
    }//GEN-LAST:event_loginButtonActionPerformed

    private void launchGameButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_launchGameButtonActionPerformed
        try {
            clientOutput.writeObject(new ControlMessage(ControlType.PAUSE, true));
            clientOutput.reset();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        this.setVisible(false);
        raptorUi.setVisible(true);
    }//GEN-LAST:event_launchGameButtonActionPerformed

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
            java.util.logging.Logger.getLogger(LaunchUi.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(LaunchUi.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(LaunchUi.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(LaunchUi.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> {
            new LaunchUi().setVisible(true);
        });
    }

    RaptorUi raptorUi;
    private ObjectOutputStream clientOutput;

    private final Map<Raptor.RaptorSubsystem, JLabel> subsystemWidgets = new EnumMap<>(Raptor.RaptorSubsystem.class);

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox<String> aspectRatioCombo;
    private javax.swing.JLabel hullHealthStatus;
    private javax.swing.JLabel hullRadarStatus;
    private javax.swing.JLabel hullShieldsStatus;
    private javax.swing.JLabel hullSystemStatus;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JButton launchGameButton;
    private javax.swing.JButton loginButton;
    private javax.swing.JLabel moveBreakStatus;
    private javax.swing.JLabel moveHorizontalStatus;
    private javax.swing.JLabel moveSystemStatus;
    private javax.swing.JLabel moveVerticalStatus;
    private javax.swing.JPasswordField passwordEntry;
    private javax.swing.JCheckBox saveCredentialsButton;
    private javax.swing.JLabel statusLabel;
    private javax.swing.JTextField userEntry;
    private javax.swing.JLabel weaponPowerStatus;
    private javax.swing.JLabel weaponSpeedStatus;
    private javax.swing.JLabel weaponSystemStatus;
    private javax.swing.JLabel weaponTypesStatus;
    // End of variables declaration//GEN-END:variables

}
