package com.hc.chess.game.element.pieces;

import android.graphics.Bitmap;
import android.graphics.PointF;

public class King extends AbstractPiece {
    public King(PointF position, String namePosition, PieceColor color, Bitmap sprite) {
        super(position, namePosition, color, sprite);
    }
}