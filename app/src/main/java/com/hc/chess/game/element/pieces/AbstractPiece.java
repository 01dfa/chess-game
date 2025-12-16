package com.hc.chess.game.element.pieces;

import android.graphics.Bitmap;
import android.graphics.PointF;
import android.util.Log;

import com.hc.chess.game.element.board.Square;

import java.util.Optional;

public abstract class AbstractPiece implements Piece {
    private static final String TAG = "AbstractPiece";
    protected static final int ONE_SQUARE = 121;
    private final PointF position;
    private final PointF openingPosition;
    private final String name;
    private String namePosition;
    private String openingNamePosition;
    private final Bitmap sprite;
    private final PieceColor color;
    protected int movementCounter;
    private boolean capture;

    interface AttackAction {
        float command(Square s);
    }

    interface MoveAction {
        float command(Square s);
    }

    public final class MoveResult {
        private Optional<Piece> piece;
        private boolean move;

        public MoveResult(Optional<Piece> piece, boolean move) {
            this.piece = piece;
            this.move = move;
        }

        public Optional<Piece> getPiece() { return piece; }

        public boolean couldMove() { return move; }
    }

    public AbstractPiece(PointF position, String namePosition, PieceColor color, Bitmap sprite) {
        this.position = position;
        this.name = this.getClass().getName().split("\\.")[6];
        this.namePosition = namePosition;
        this.sprite = sprite;
        this.color = color;
        this.movementCounter = 0;
        this.openingPosition = new PointF(0, 0);
        this.capture = false;
    }

    public boolean captured() { return this.capture; }

    public String getName() { return this.name; }
    public PointF getPosition() { return this.position; }

    public PieceColor getColor() { return this.color; }
    public String getNamePosition() { return this.namePosition; }
    public void setNamePosition(String name) { this.namePosition = name; }
    public void setPosition(PointF position) {
        this.position.x = position.x;
        this.position.y = position.y;
    }
    public Bitmap getImage() { return this.sprite; }

    protected boolean equalColor(Piece piece) {
        return this.getColor() == piece.getColor();
    }

    private boolean canAttack(Square square) {
        return square.getPiece().isPresent()
                && square.getPiece().isPresent() && !this.equalColor(square.getPiece().get())
                && (square.getPosition().x != this.openingPosition.x
                || square.getPosition().y != this.openingPosition.y);
    }

    public MoveResult move(Square square) {
        MoveResult gmoveResult = this.moveAction(square,
                (s) -> this.canAttack(square) ? 1 : 0,
                (s) -> square.getPiece().isEmpty() ? 1 : 0);

        return gmoveResult;
    }
    protected MoveResult moveAction(Square s, AttackAction attackAction, MoveAction moveAction) {
        boolean moved = false;
        Optional<Piece> previousPiece = Optional.empty();
        this.capture = false;

        float distance = attackAction.command(s);

        if(distance > 0) {
            previousPiece = s.getPiece();
            moved = true;
            setMove(s, (int)distance, true);
            Log.i(TAG, "[capture]");
        } else  {
            distance = moveAction.command(s);
            if (distance > 0) {
                previousPiece = s.getPiece();
                moved = true;
                setMove(s, (int) distance, false);
                Log.i(TAG, String.format(
                        "delta %f position %f %f", distance, s.getPosition().x, s.getPosition().y));
            }
        }

        return new MoveResult(previousPiece, moved);
    }

    private void setMove(Square square, int inc, boolean capture) {
        this.movementCounter += Math.abs(inc);
        this.setPosition(square.getPosition());
        square.setPiece(this);
        this.namePosition = square.getName();
        this.capture = capture;
    }

    public PointF getOpening() { return this.openingPosition; }

    public String getOpeningNamePosition() { return this.openingNamePosition; }

    public void opening() {
        this.openingPosition.x = this.position.x;
        this.openingPosition.y = this.position.y;
        this.openingNamePosition = this.namePosition;
    }

    public void resetOpening() {
        this.setPosition(this.openingPosition);
        this.namePosition = this.openingNamePosition;
    }

    public void print() {
        System.out.println(
                String.format("[%s] %s x %.2f y %.2f opening x %.2f y %.2f m_counter %d",
                this.getName(),
                this.color,
                this.position.x,
                this.position.y,
                this.openingPosition.x,
                this.openingPosition.y,
                this.movementCounter));
    }
}
