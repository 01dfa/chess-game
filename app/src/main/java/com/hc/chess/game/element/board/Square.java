package com.hc.chess.game.element.board;

import android.graphics.PointF;

import com.hc.chess.game.element.pieces.Piece;

import java.util.Optional;

public class Square {
    private final static int SIDE_LENGTH = 121;
    private final PointF position;
    private final String name;
    private final float width;
    private final float height;
    private Optional<Piece> piece;

    public Square(PointF position, String name, Piece piece) {
        this.position = position;
        this.name = name;
        this.width = this.position.x + SIDE_LENGTH;
        this.height = this.position.y + SIDE_LENGTH;
        this.piece = Optional.ofNullable(piece);
    }

    public PointF getPosition() { return this.position; }
    public String getName() { return this.name; }

    public Optional<Piece> getPiece() { return this.piece; }
    public void setPiece(Piece piece) { this.piece = Optional.ofNullable(piece); }

    public boolean isOnWidth(float pos) {
        return pos > this.position.x && pos < this.width;
    }

    public boolean isOnHeight(float pos) {
        return pos > this.position.y && pos < this.height;
    }

    public void print() {
        System.out.println(
                String.format("[%s] x %.2f y %.2f [piece] %s",
                this.name,
                this.position.x,
                this.position.y,
                this.getPiece().isPresent() ? this.getPiece().get().getName() : "empty"));
    }
}
