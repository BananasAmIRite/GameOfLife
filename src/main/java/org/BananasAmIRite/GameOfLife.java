package org.BananasAmIRite;

import javax.swing.*;
import java.awt.*;

public class GameOfLife {

    public static void main(String[] args) throws InterruptedException {
        JFrame mainFrame = new JFrame();

        Dimension size = new Dimension(500, 500);

        mainFrame.setSize(size);

        GameCanvas canvas = new GameCanvas((int)size.getWidth(), (int)size.getHeight(), new Dimension(10, 10));

        JFrame controlFrame = new GameControlFrame(canvas);

        System.out.println(canvas.getMap());

        mainFrame.add(canvas);

        mainFrame.setResizable(false);

        mainFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        mainFrame.setVisible(true);
        controlFrame.setVisible(true);
    }
}
