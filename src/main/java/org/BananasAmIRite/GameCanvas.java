package org.BananasAmIRite;

import java.awt.*;
import java.util.*;
import java.util.List;

public class GameCanvas extends Canvas {
    private Dimension bitSize;
    private boolean generationStarting = false;
    private int generationTime = 250;
    private final GameCanvasMouseAdapter mouseAdapter;

    private int offsetX = 10;
    private int offsetY = 10;

    private List<Coordinate> litUpPoints;

    public GameCanvas(int sizeX, int sizeY) {
        super(sizeX, sizeY);
        this.bitSize = new Dimension(50, 50);
        this.mouseAdapter = new GameCanvasMouseAdapter(this);
        this.init();
    }

    public GameCanvas(int sizeX, int sizeY, Dimension bitSize) {
        super(sizeX, sizeY);
        this.bitSize = bitSize;
        this.mouseAdapter = new GameCanvasMouseAdapter(this);
        this.init();
    }

    private void init() {
        this.addMouseListener(mouseAdapter);
        this.addMouseMotionListener(mouseAdapter);
        this.addMouseWheelListener(mouseAdapter);

        litUpPoints = new ArrayList<>();

        this.repaint();
    }

    public final LitResponse isLitUp(int x, int y) {
        return isLitUp(new Coordinate(x, y));
    }

    public final LitResponse isLitUp(Coordinate c) {
        return new LitResponse(this.litUpPoints.contains(c), new Coordinate(c.getX(), c.getY()));
    }

    public final void flipBit(int x, int y) {
        flipBit(new Coordinate(x, y));
    }

    public final void flipBit(Coordinate c) {
        LitResponse isLit = this.isLitUp(c);

        if (isLit.getResponse()) {
            this.litUpPoints.remove(c);
        } else {
            this.litUpPoints.add(c);
        }
    }

    public boolean isGenerationStarting() {
        return generationStarting;
    }

    public Dimension getBitSize() {
        return bitSize;
    }

    public void setBitSize(Dimension bitSize) {
        setBitSize(bitSize, true);
    }

    public void setBitSize(Dimension bitSize, boolean repaint) {
        if (bitSize.getWidth() < 5 || bitSize.getHeight() < 5) return;
        this.bitSize = bitSize;

        if (repaint) this.repaint();
    }

    public final void setGenerationTime(int time) {
        this.generationTime = time;
    }

    public final int getGenerationTime() {
        return generationTime;
    }

    @Override
    public void draw(Graphics g) {

        int LINE_WIDTH = 1;

        // white bg
        g.setColor(Color.GRAY);
        g.fillRect(0, 0, this.imageWidth, this.imageHeight);

        int amountOfBitRows = (int) (Math.ceil(imageWidth / bitSize.getWidth()) + Math.ceil(Math.abs(this.offsetX) / bitSize.getWidth()));
        int amountOfBitCols = (int) (Math.ceil(imageHeight / bitSize.getHeight()) + Math.ceil(Math.abs(this.offsetY) / bitSize.getHeight()));

        int startingBitX = (int) Math.floor(this.offsetX / bitSize.getWidth());
        int startingBitY = (int) Math.floor(this.offsetY / bitSize.getHeight());

        int bitMinusX = (int) (this.offsetX == 0 ? 0 : this.offsetX > 0 ?
                (this.offsetX % bitSize.getWidth())
                : Math.abs(this.offsetX) % bitSize.getWidth() == 0 ? 0
                : bitSize.getWidth() - (Math.abs(this.offsetX) % bitSize.getWidth()));
        int bitMinusY = (int) (this.offsetY == 0 ? 0 : this.offsetY > 0 ?
                (this.offsetY % bitSize.getHeight())
                : Math.abs(this.offsetY) % bitSize.getHeight() == 0 ? 0
                : bitSize.getHeight() - (Math.abs(this.offsetY) % bitSize.getHeight())
                );

        for (int y = startingBitY; y < amountOfBitCols + startingBitY; y++) {
            for (int x = startingBitX; x < amountOfBitRows + startingBitX; x++) {
                LitResponse isBitLit = this.isLitUp(x, y);

                // bit draw locations:
                // ------
                Coordinate bitDrawLocationStart = new Coordinate(
                        (int)((x-startingBitX)*this.bitSize.getWidth()-bitMinusX),
                        (int)((y-startingBitY)*this.bitSize.getHeight()-bitMinusY));
                // ------

                g.setColor(isBitLit.getResponse() ? Color.WHITE : Color.BLACK);

                g.fillRect(bitDrawLocationStart.getX(), bitDrawLocationStart.getY(), (int) this.bitSize.getWidth()- LINE_WIDTH, (int) this.bitSize.getHeight()- LINE_WIDTH);
            }
        }
    }

    public final void start() {
        // start the generation thread!!!
        if (this.generationStarting) return;

        this.generationStarting = true;

        Thread t = new GameCanvasThread(this);
        t.start();
    }

    public final void stop() {
        this.generationStarting = false;
    }


    public final void runGeneration() {
        Set<Coordinate> changes = new HashSet<>();

        for (Coordinate litBit : this.litUpPoints) {
            Set<LitResponse> neighbors = getNeighbors(litBit);
            int sumOfAliveNeighbors = 0;

            for (LitResponse neighbor : neighbors) {
                if (!neighbor.getResponse()) continue;
                sumOfAliveNeighbors++;
            }

            if (sumOfAliveNeighbors != 2 && sumOfAliveNeighbors != 3) {
                // kill the bit
                changes.add(litBit);
            }

            // now we test each neighbor
            for (LitResponse neighborBit : neighbors) {
                if (neighborBit.getResponse()) continue; // we ONLY want dead cells

                Set<LitResponse> neighborNeighbors = getNeighbors(neighborBit.getCoordinates());
                int sumOfAliveNeighborNeighbors = 0;

                for (LitResponse neighborNeighbor : neighborNeighbors) {
                    if (!neighborNeighbor.getResponse()) continue;
                    sumOfAliveNeighborNeighbors++;
                }

                if (sumOfAliveNeighborNeighbors == 3) changes.add(neighborBit.getCoordinates());

            }
        }

        for (Coordinate change : changes) {
            flipBit(change);
        }
    }

    private Set<LitResponse> getNeighbors(Coordinate c) {
        int x = c.getX();
        int y = c.getY();
        LitResponse topLeft = isLitUp(x-1, y-1);
        LitResponse top = isLitUp(x, y-1);
        LitResponse topRight = isLitUp(x+1, y-1);
        LitResponse middleLeft = isLitUp(x-1, y);
        LitResponse middleRight = isLitUp(x+1, y);
        LitResponse bottomLeft = isLitUp(x-1, y+1);
        LitResponse bottom = isLitUp(x, y+1);
        LitResponse bottomRight = isLitUp(x+1, y+1);
        return new HashSet<>(Arrays.asList(topLeft, top, topRight, middleLeft, middleRight, bottomLeft, bottom, bottomRight));
    }

    public int getOffsetX() {
        return offsetX;
    }

    public int getOffsetY() {
        return offsetY;
    }

    public void setOffsetX(int offsetX) {
        this.offsetX = offsetX;
    }

    public void setOffsetY(int offsetY) {
        this.offsetY = offsetY;
    }
}
