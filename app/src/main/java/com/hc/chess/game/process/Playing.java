package com.hc.chess.game.process;

import android.graphics.Canvas;
import android.graphics.PointF;
import android.util.Log;
import android.view.MotionEvent;

import com.hc.chess.game.element.BoardManager;
import com.hc.chess.game.element.BoardProvider;
import com.hc.chess.game.event.PieceLifecycle;

public class Playing {
    private static final String TAG = "Playing";
    private float x, y;
    private int action;
    private final BoardManager boardManager;
    public Playing(PieceLifecycle pieceLifecycle) {
        this.boardManager = new BoardManager(
                new BoardProvider(new PointF(0, 800)),
                pieceLifecycle);
    }

    public void update() {}

    public void render(Canvas c) {
        this.boardManager.draw(c);
    }

    public void handleTouchEvent(MotionEvent event) {
        x = event.getX();
        y = event.getY();
        action = event.getActionMasked();

        switch (action) {
            case MotionEvent.ACTION_DOWN :
                Log.d(TAG, String.format("[ACTION_DOWN %d] x %.2f y %.2f", action, x, y));
                this.boardManager.take(x, y);

                break;
            case MotionEvent.ACTION_MOVE :
                //Log.d(TAG, String.format("[ACTION_MOVE %d] x %.2f y %.2f", action, x, y));
                this.boardManager.drag(x, y);

                break;
            case MotionEvent.ACTION_UP :
                Log.d(TAG, String.format("[ACTION_UP %d] x %.2f y %.2f", action, x, y));
                this.boardManager.drop();

                break;
        }
    }
}
