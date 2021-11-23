package org.BananasAmIRite;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.*;
import java.util.List;

public class GameCanvas extends Canvas implements MouseListener {
    private List<List<Bit>> map;
    private Dimension bitSize;
    private boolean generationStarting = false;
    private int generationTime = 250;
    private GameCanvasThread currentThread;

    public GameCanvas(int sizeX, int sizeY) {
        super(sizeX, sizeY);
        this.bitSize = new Dimension(50, 50);
        this.addMouseListener(this);
        this.init();
    }

    public GameCanvas(int sizeX, int sizeY, Dimension bitSize) {
        super(sizeX, sizeY);
        this.bitSize = bitSize;
        this.addMouseListener(this);
        this.init();
    }

    private void init() {
        // TODO: revamp the whole thing so that the canvas only stores lit up coordinates
        //  (eg. [(0, 0), (1, 0)] for 2 points lit up next to each other in the top left corner).
        //  this will be used for an infinite grid.
        //  ~~ The renderer will need to translate the points into an arraylist of arraylists each time though. ~~
        //  ^^ outdated imma use diff idea
        //  The renderer will calculate the amountOfBitRows, amountOfBitCols, shown under, loop through that
        //  if a tuple containing the coordinates are not in the array, then don't light up, if yes, then light up.
        //  -
        //  For generation logic, go through SHIT THIS WONT WORK CUZ HOW DO I DETECT DEAD CELLS
        //  ACTUALLY, I could loop through each lighted up point, which I have, then find each surrounding cell.
        //   For each surrounding cell, if its dead, then test the Any dead cell with 3 neighbors will come back
        //   to life, else, don't do anything.
        //  Test everything else for that cell.
        //  --
        //  I AM SO SMART!
        map = new ArrayList<>();

        int amountOfBitRows = (int) Math.ceil(imageWidth / bitSize.getWidth());
        int amountOfBitCols = (int) Math.ceil(imageHeight / bitSize.getHeight());

        for (int y = 0; y < amountOfBitCols; y++) {
            List<Bit> bitRow = new ArrayList<>();
            for (int x = 0; x < amountOfBitRows; x++) {
                Bit bit = new Bit();
                bitRow.add(bit);
            }
            map.add(bitRow);
        }

        this.repaint();
    }

    public final Bit getBit(int x, int y) {
        try {
            return map.get(y).get(x);
        } catch(IndexOutOfBoundsException err) {
            return null;
        }
    }

    public boolean isGenerationStarting() {
        return generationStarting;
    }

    public void setBitSize(Dimension bitSize) {
        this.bitSize = bitSize;
        // refresh EVERYTHING cuz we need to
        this.init();
    }

    public final void setGenerationTime(int time) {
        this.generationTime = time;
    }

    public final int getGenerationTime() {
        return generationTime;
    }

    public final List<List<Bit>> getMap() {
        return map;
    }

    @Override
    public void draw(Graphics g) {
        // white bg
        g.setColor(Color.GRAY);
        g.fillRect(0, 0, this.imageWidth, this.imageHeight);
        for (int y = 0; y < map.size(); y++) {
            for (int x = 0; x < map.get(0).size(); x++) {
                Bit bit = getBit(x, y);
                if (bit == null) continue;

                // bit draw locations:
                // ------
                Coordinate bitDrawLocationStart = new Coordinate((int)(x*this.bitSize.getWidth()), (int)(y*this.bitSize.getHeight()));
                // Coordinate bitDrawLocationEnd = new Coordinate((int)((x+1)*this.bitSize.getWidth()), (int)((y+1)*this.bitSize.getHeight()));
                // ------

                g.setColor(bit.isLit() ? Color.WHITE : Color.BLACK);

                g.fillRect(bitDrawLocationStart.getX(), bitDrawLocationStart.getY(), (int) this.bitSize.getWidth()-1, (int) this.bitSize.getHeight()-1);
            }
        }
    }

    public final void start() {
        // start the generation thread!!!
        if (this.generationStarting) return;

        this.generationStarting = true;
        // deprecated: moved to GameCanvasThread
//        while (this.generationStarting) {
//            runGeneration();
//            this.repaint();
//            Thread.sleep(this.generationTime);
//        }

        // if (this.currentThread != null) this.currentThread.interrupt();

        this.currentThread = new GameCanvasThread(this);
        this.currentThread.start();
    }

    public final void stop() {
        this.generationStarting = false;
    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {
        // get the exact bit that we clicked :D
        int clickedRow = (int) Math.floor(e.getY()/bitSize.getHeight());
        int clickedCol = (int) Math.floor(e.getX()/bitSize.getWidth());

        Bit bit = getBit(clickedCol, clickedRow);

        if (bit == null) return;

        // System.out.println("Bit we clicked is " + (bit.isLit() ? "FLIPPED" : "NOT FLIPPED") + "!");

        bit.flip();

        this.repaint();
    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    public final void runGeneration() {
        List<Bit> changes = new ArrayList<>();

        for (int y = 0; y < map.size(); y++) {
            for (int x = 0; x < map.get(0).size(); x++) {
                Bit bit = getBit(x, y);

                Bit topLeft = getBit(x-1, y-1);
                Bit top = getBit(x, y-1);
                Bit topRight = getBit(x+1, y-1);
                Bit middleLeft = getBit(x-1, y);
                Bit middleRight = getBit(x+1, y);
                Bit bottomLeft = getBit(x-1, y+1);
                Bit bottom = getBit(x, y+1);
                Bit bottomRight = getBit(x+1, y+1);
                Set<Bit> neighbors = new HashSet<>(Arrays.asList(topLeft, top, topRight, middleLeft, middleRight, bottomLeft, bottom, bottomRight));
                int sumOfAliveNeighbors = 0;

                for (Bit neighbor : neighbors) {
                    if (neighbor == null || !neighbor.isLit()) continue;
                    sumOfAliveNeighbors++;
                }

                if (bit != null && bit.isLit()) {
                    // bit is lit, lets use the rules :D
                    if (sumOfAliveNeighbors == 2 || sumOfAliveNeighbors == 3) continue;
                    changes.add(bit);
                } else {
                    // bit is dead, lets still use the rules :D
                    if (sumOfAliveNeighbors == 3) changes.add(bit);
                }
            }
        }

        for (Bit change : changes) {
            change.flip();
        }
    }
}
