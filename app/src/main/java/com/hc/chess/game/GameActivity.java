package com.hc.chess.game;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.SurfaceHolder;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class GameActivity extends AppCompatActivity implements SurfaceHolder.Callback {
    private static Context gameContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        gameContext = this;

        GameView gameView = new GameView(this);
        super.setContentView(gameView);
        gameView.getHolder().addCallback(this);
    }

    public static Context getGameContext() {
        return gameContext;
    }

    @Override
    public void surfaceChanged(@NonNull SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceCreated(@NonNull SurfaceHolder holder) {

    }

    @Override
    public void surfaceDestroyed(@NonNull SurfaceHolder holder) {
        setResult(Activity.RESULT_OK);
        finish();
    }
}