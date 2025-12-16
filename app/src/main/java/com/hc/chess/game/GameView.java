package com.hc.chess.game;

import android.content.Context;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

import androidx.annotation.NonNull;

import com.hc.chess.game.process.Game;

public class GameView extends SurfaceView implements SurfaceHolder.Callback {
    private SurfaceHolder holder;
    private final Game game;

    public GameView(Context context) {
        super(context);
        holder = getHolder();
        holder.addCallback(this);
        this.game = new Game(holder);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(event.getY() > 2000) {
            this.game.stop();
            super.setVisibility(View.GONE);
        }
        else
            this.game.handleTouchEvent(event);

        return true;
    }

    @Override
    public void surfaceChanged(@NonNull SurfaceHolder holder, int format, int width, int height) {
    }

    @Override
    public void surfaceCreated(@NonNull SurfaceHolder holder) {
        this.game.start();
    }

    @Override
    public void surfaceDestroyed(@NonNull SurfaceHolder holder) {
    }
}
