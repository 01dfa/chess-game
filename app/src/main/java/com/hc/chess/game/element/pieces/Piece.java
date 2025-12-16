package com.hc.chess.game.element.pieces;

import android.graphics.Bitmap;
import android.graphics.PointF;

import com.hc.chess.game.element.board.Square;

public interface Piece {
    AbstractPiece.MoveResult move(Square square);
    PointF getPosition();
    void setPosition(PointF position);
    String getNamePosition();
    void setNamePosition(String name);
    String getOpeningNamePosition();
    boolean captured();
    PieceColor getColor();
    String getName();
    Bitmap getImage();
    void print();
    PointF getOpening();
    void opening();
    void resetOpening();
}
