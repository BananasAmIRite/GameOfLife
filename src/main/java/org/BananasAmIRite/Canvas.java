package org.BananasAmIRite;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class Canvas extends JPanel {
    protected final Image image;
    protected final int imageWidth;
    protected final int imageHeight;

    public Canvas(int sizeX, int sizeY) {
        super();
        imageWidth = sizeX;
        imageHeight = sizeY;
        image = new BufferedImage(sizeX, sizeY, BufferedImage.TYPE_INT_RGB);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        this.draw(image.getGraphics());
        g.drawImage(image, this.getX(), this.getY(), null);
    }

    public void draw(Graphics g) {

    }

    public Graphics getContext() {
        return image.getGraphics();
    }
}
