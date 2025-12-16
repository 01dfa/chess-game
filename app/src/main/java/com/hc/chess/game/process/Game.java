package com.hc.chess.game.process;

import android.graphics.Canvas;
import android.graphics.Color;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;

import androidx.lifecycle.Observer;

import com.hc.chess.game.event.PieceEvent;
import com.hc.chess.game.event.PieceLifecycle;
import com.hc.chess.game.event.PieceRepository;
import com.hc.chess.game.event.PieceDataSource;

public class Game {
    private static final String TAG = "Game";
    private SurfaceHolder surfaceHolder;
    private Playing playing;
    private GameProcess gameProcess;
    private GameState currentState;
    private final PieceLifecycle pieceLifecycle;
    public Game(SurfaceHolder surfaceHolder) {
        this.surfaceHolder = surfaceHolder;
        this.currentState = GameState.PLAYING;
        this.pieceLifecycle = new PieceLifecycle(new PieceRepository(new PieceDataSource()));
        this.playing = new Playing(this.pieceLifecycle);

        this.gameProcess = new GameProcess(this::update, this::render);

        this.pieceLifecycle.getStatus().observe(this.pieceLifecycle, new Observer<PieceEvent>() {
            @Override
            public void onChanged(PieceEvent event) {
                switch(event.getAction()) {
                    case ON_MOVE:
                        Log.i(TAG, String
                                .format("PieceEvent -> %s %s", event.getAction(), event.getContent()));
                        break;
                    case ON_ERROR:
                        Log.e(TAG, String
                                .format("PieceEvent -> %s %s", event.getAction(), event.getContent()));
                        break;
                }

                gameProcess.pause();
            }
        });
    }

    public void update() {
        switch (currentState) {
            case PLAYING : playing.update();
        }
    }

    public void render() {
        Canvas c = surfaceHolder.lockCanvas();
        c.drawColor(Color.BLACK);

        switch (currentState) {
            case PLAYING : playing.render(c);
        }

        surfaceHolder.unlockCanvasAndPost(c);
    }

    public void handleTouchEvent(MotionEvent event) {
        int actionMasked = event.getActionMasked();

        if (actionMasked == MotionEvent.ACTION_DOWN) {
            gameProcess.start();
        }

        switch (currentState) {
            case PLAYING : playing.handleTouchEvent(event);
        }
    }

    public void start() {
        gameProcess.start();
    }

    public void stop() {
        gameProcess.interrupt();
    }
}
