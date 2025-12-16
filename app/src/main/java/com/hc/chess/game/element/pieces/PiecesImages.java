package com.hc.chess.game.element.pieces;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.hc.chess.R;
import com.hc.chess.game.GameActivity;

public enum PiecesImages {
    PIECES(R.drawable.pieces);
    private final Bitmap spriteSheet;
    private final Bitmap[][] sprites = new Bitmap[2][6];

    PiecesImages(int resID) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inScaled = false;

        spriteSheet = BitmapFactory.decodeResource(
                GameActivity.getGameContext().getResources(), resID, options);

        int offset = 0;
        int pieceWidth = 60;

        for (int j = 0; j < sprites.length; j++)
            for (int i = 0; i < sprites[j].length; i++)
                sprites[j][i] = this.getScaledBitmap(Bitmap.createBitmap(
                        spriteSheet,
                        pieceWidth * i + offset,
                        60 * j,
                        pieceWidth + offset,
                        60));
    }
    private Bitmap getScaledBitmap(Bitmap bitmap) {
        return Bitmap.createScaledBitmap(
                bitmap,
                bitmap.getWidth() * 2,
                bitmap.getHeight() * 2 ,
                false);
    }

    public Bitmap getSprite(int yPos, int xPos) {
        return sprites[yPos][xPos];
    }
}
