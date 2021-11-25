package org.BananasAmIRite;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GameControlFrame extends JFrame {
    private final GameCanvas canvas;
    private final JTextField bitSizeXField = new JTextField();
    private final JTextField bitSizeYField = new JTextField();
    private final JTextField generationTimeField = new JTextField();
    private final JButton applyBtn = new JButton("Apply Changes");
    private final JButton startBtn = new JButton("Start");
    private final JButton stopBtn = new JButton("Stop");


    public GameControlFrame(GameCanvas canvas) {
        this.canvas = canvas;
        this.init();
    }

    private void init() {
        JLabel bitSizeXLabel = new JLabel("Bit Size X: ");
        JLabel bitSizeYLabel = new JLabel("Bit Size Y: ");
        JLabel generationTimeLabel = new JLabel("Generation Time: ");

        // layout and grouping components
        GroupLayout layout = new GroupLayout(this.getContentPane());

        this.getContentPane().setLayout(layout);

        layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(true);

        // grouping!
        layout.setVerticalGroup(
                layout
                        .createSequentialGroup()
                        .addGroup(
                                layout
                                        .createParallelGroup()
                                        .addComponent(bitSizeXLabel)
                                        .addComponent(this.bitSizeXField)

                        )
                        .addGroup(
                                layout
                                        .createParallelGroup()
                                        .addComponent(bitSizeYLabel)
                                        .addComponent(this.bitSizeYField)
                        )
                        .addGroup(
                                layout
                                        .createParallelGroup()
                                        .addComponent(generationTimeLabel)
                                        .addComponent(this.generationTimeField)
                        )
                .addComponent(this.applyBtn)
                .addGroup(
                        layout
                                .createParallelGroup()
                                .addComponent(this.startBtn)
                                .addComponent(this.stopBtn)
                )
        );
        layout.setHorizontalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.CENTER)
                        .addGroup(
                                layout
                                        .createSequentialGroup()
                                        .addComponent(bitSizeXLabel)
                                        .addComponent(this.bitSizeXField)
                        )
                        .addGroup(
                                layout
                                        .createSequentialGroup()
                                        .addComponent(bitSizeYLabel)
                                        .addComponent(this.bitSizeYField)
                        )
                        .addGroup(
                                layout
                                        .createSequentialGroup()
                                        .addComponent(generationTimeLabel)
                                        .addComponent(this.generationTimeField)
                        )
                        .addGroup(
                                layout
                                        .createSequentialGroup()
                                .addComponent(this.applyBtn)
                        )
                        .addGroup(
                                layout
                                        .createSequentialGroup()
                                        .addComponent(this.startBtn)
                                    .addComponent(this.stopBtn)
                        )

        );

        this.applyBtn.addActionListener(this::applyBtnPress);
        this.startBtn.addActionListener(this::startBtnPress);
        this.stopBtn.addActionListener(this::stopBtnPress);

        this.setResizable(false);
        this.setSize(new Dimension(200, 200));
    }

    private void applyBtnPress(ActionEvent e) {
        if (!bitSizeXField.getText().equals("") && !bitSizeYField.getText().equals("")) {
            int xSize;
            int ySize;
            try {
                xSize = Integer.parseInt(bitSizeXField.getText());
                ySize = Integer.parseInt(bitSizeYField.getText());
            } catch (NumberFormatException err) {
                err.printStackTrace();
                return;
            }

            bitSizeXField.setText("");
            bitSizeYField.setText("");

            this.canvas.setBitSize(new Dimension(xSize, ySize));
        }

        if (!this.generationTimeField.getText().equals("")) {
            int time;
            try {
                time = Integer.parseInt(generationTimeField.getText());
            } catch (NumberFormatException err) {
                err.printStackTrace();
                return;
            }

            generationTimeField.setText("");
            this.canvas.setGenerationTime(time);
        }
    }

    private void startBtnPress(ActionEvent e) {
        try {
            this.canvas.start();
        } catch(Exception err) {
            err.printStackTrace();
        }
    }

    private void stopBtnPress(ActionEvent e) {
        this.canvas.stop();
    }
}
