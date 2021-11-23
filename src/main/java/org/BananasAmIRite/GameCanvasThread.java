package org.BananasAmIRite;

public class GameCanvasThread extends Thread {
    private final GameCanvas canvas;
    public GameCanvasThread(GameCanvas canvas) {
        this.canvas = canvas;
    }

    @Override
    public void run() {
        while (canvas.isGenerationStarting()) {
            canvas.runGeneration();
            canvas.repaint();
            try {
                Thread.sleep(canvas.getGenerationTime());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
