package com.hc.chess.game.process;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class GameProcess {
    private final ExecutorService executor;
    private volatile boolean running = false;
    //private final Game game;
    private final UpdateProcess updateProcess;
    private final RenderProcess renderProcess;

    public interface UpdateProcess {
        void action();
    }

    public interface RenderProcess {
        void action();
    }

    public GameProcess(UpdateProcess updateProcess, RenderProcess renderProcess) {
        this.updateProcess = updateProcess;
        this.renderProcess = renderProcess;
        this.executor = Executors.newSingleThreadExecutor();
        //this.game = game;
    }

    private void task () {
        //int i = 1;
        while (running && !Thread.currentThread().isInterrupted()) {
            /*float num = i/100f;

            if(!(num > (int)num))
                System.out.println(String.format("-%d ", i));
            i++;*/

            this.updateProcess.action();
            this.renderProcess.action();
        }
    }

    public void start() {
        if(!running) {
            running = true;
            this.executor.submit(this::task);
        }
    }

    public void pause() {
        running = false;
    }

    public void interrupt() {
        running = false;
        executor.shutdown();

        try {
            if (!executor.awaitTermination(1, TimeUnit.SECONDS)) {
                executor.shutdownNow();
            }
        } catch (InterruptedException e) {
            executor.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }
}
