package com.hc.chess.game.element.pieces;

import android.graphics.Bitmap;

public enum PieceImage {
    W_KING(PiecesImages.PIECES.getSprite(0, 1)),
    W_QUEEN(PiecesImages.PIECES.getSprite(0, 0)),
    W_ROOKS(PiecesImages.PIECES.getSprite(0, 2)),
    W_BISHOPS(PiecesImages.PIECES.getSprite(0, 4)),
    W_KNIGHTS(PiecesImages.PIECES.getSprite(0, 3)),
    W_PAWNS(PiecesImages.PIECES.getSprite(0, 5)),
    B_KING(PiecesImages.PIECES.getSprite(1, 1)),
    B_QUEEN(PiecesImages.PIECES.getSprite(1, 0)),
    B_ROOKS(PiecesImages.PIECES.getSprite(1, 2)),
    B_BISHOPS(PiecesImages.PIECES.getSprite(1, 4)),
    B_KNIGHTS(PiecesImages.PIECES.getSprite(1, 3)),
    B_PAWNS(PiecesImages.PIECES.getSprite(1, 5));
    private Bitmap sprite;

    PieceImage(Bitmap sprite) {
        this.sprite = sprite;
    }

    public Bitmap getSprite() { return this.sprite; }
}
