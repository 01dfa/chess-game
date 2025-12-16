package com.hc.chess.game.element.pieces;

import android.graphics.Bitmap;
import android.graphics.PointF;

import com.hc.chess.game.element.board.Square;

public class Pawn extends AbstractPiece {
    private static final int TWO_SQUARE = ONE_SQUARE * 2;
    public Pawn(PointF position, String namePosition, PieceColor color, Bitmap sprite) {
        super(position, namePosition, color, sprite);
    }

    private boolean canAttack(Square square) {
        return square.getPiece().isPresent()
                && !this.equalColor(square.getPiece().get())
                &&
                    (square.getPosition().x == this.getOpening().x - ONE_SQUARE
                    || square.getPosition().x == this.getOpening().x + ONE_SQUARE)
                &&
                    square.getPosition().y == this.getOpening().y - ONE_SQUARE;
    }

    private float verifyAttack(Square square) {
        return this.canAttack(square) ? 1 : 0;
    }

    private float verifyMove(Square square) {
        if (square.getPiece().isEmpty()) {
            float dy = Math.abs(this.getOpening().y - square.getPosition().y);
            boolean allowedSquares = this.movementCounter == 0 ?  dy <= TWO_SQUARE : dy <= ONE_SQUARE;

            if(allowedSquares
                    && square.getPosition().x == this.getOpening().x
                    && square.getPosition().y < this.getOpening().y
            )
                return (dy / ONE_SQUARE);

        }

        return 0;
    }

    @Override
    public MoveResult move(Square square) {
        MoveResult moveResult = this.moveAction(square, this::verifyAttack, this::verifyMove);

        return moveResult;
    }
}
