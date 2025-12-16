package com.hc.chess.game.element.board;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.hc.chess.R;
import com.hc.chess.game.GameActivity;

public enum BoardImage {
    BOARD(R.drawable.board);
    private Bitmap spriteSheet;
    private Bitmap[][] sprites = new Bitmap[1][1];

    BoardImage(int resID) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inScaled = false;

        spriteSheet = BitmapFactory.decodeResource(
                GameActivity.getGameContext().getResources(), resID, options);

        for (int j = 0; j < sprites.length; j++)
            for (int i = 0; i < sprites[j].length; i++)
                sprites[j][i] = Bitmap.createScaledBitmap(
                        Bitmap.createBitmap(spriteSheet, 0, 0, 735, 735),
                        1075,
                        1075,
                        false);
    }

    public Bitmap getSprite(int yPos, int xPos) {
        return sprites[yPos][xPos];
    }
}
