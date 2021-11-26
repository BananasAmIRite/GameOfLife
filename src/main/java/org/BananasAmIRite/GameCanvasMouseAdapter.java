package org.BananasAmIRite;

import javax.swing.event.MouseInputAdapter;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;

public class GameCanvasMouseAdapter extends MouseInputAdapter {

    private final GameCanvas canvas;
    private Coordinate lastDrag;

    public GameCanvasMouseAdapter(GameCanvas canvas) {
        this.canvas = canvas;
    }

    @Override
    public void mouseClicked(MouseEvent e) {

        // get the exact bit that we clicked :D
        int clickedRow = (int) Math.floor((e.getY()+this.canvas.getOffsetY())/this.canvas.getBitSize().getHeight());
        int clickedCol = (int) Math.floor((e.getX()+this.canvas.getOffsetX())/this.canvas.getBitSize().getWidth());

        this.canvas.flipBit(clickedCol, clickedRow);

        this.canvas.repaint();
    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {
        lastDrag = null;
    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    @Override
    public void mouseDragged(MouseEvent e) {
        if (lastDrag == null) {
            lastDrag = new Coordinate(e.getX(), e.getY());
            return;
        }

        int diffDragX = e.getX() - lastDrag.getX();
        int diffDragY = e.getY() - lastDrag.getY();

        lastDrag = new Coordinate(e.getX(), e.getY());

        this.canvas.setOffsetX(this.canvas.getOffsetX() - diffDragX);
        this.canvas.setOffsetY(this.canvas.getOffsetY() - diffDragY);
        this.canvas.repaint();
    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        int rotation = e.getWheelRotation();

        int finalRotationAmount = rotation * -1;


        this.canvas.setBitSize(new Dimension((int) (this.canvas.getBitSize().getWidth() + finalRotationAmount),
                (int) (this.canvas.getBitSize().getHeight() + finalRotationAmount)), false);

        // google maps zoom with mouse position effect now
        // NVM THIS IS TOO HARD

        this.canvas.repaint();
    }
}
