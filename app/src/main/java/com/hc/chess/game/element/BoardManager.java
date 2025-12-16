package com.hc.chess.game.element;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.PointF;

import com.hc.chess.game.element.board.Square;
import com.hc.chess.game.element.pieces.AbstractPiece;
import com.hc.chess.game.element.pieces.Piece;
import com.hc.chess.game.event.PieceLifecycle;

import java.util.Optional;

public class BoardManager {
    private final BoardProvider boardProvider;
    private final Bitmap boardSprite;
    private final PointF boardPosition;
    private Optional<Square> currentSquare;
    private Optional<Piece> currentPiece;
    private final PieceLifecycle pieceLifecycle;

    public BoardManager(BoardProvider boardProvider, PieceLifecycle pieceLifecycle) {
        this.boardProvider = boardProvider;
        this.pieceLifecycle = pieceLifecycle;
        this.boardSprite = boardProvider.getBoardImage();
        this.boardPosition = boardProvider.getBoardPosition();

        currentSquare = Optional.ofNullable(null);
        currentPiece = Optional.ofNullable(null);
    }

    public void draw(Canvas c) {
        drawBoard(c);
        drawPieces(c);
    }

    private void drawBoard(Canvas c) {
        c.drawBitmap(boardSprite, boardPosition.x, boardPosition.y, null);
    }

    private void drawPieces(Canvas c) {
        this.boardProvider.forEachPiece((p) -> {
            c.drawBitmap(p.getImage(),
                    p.getPosition().x,
                    p.getPosition().y,
                    null);
        });
    }

    public void take(float x, float y) {
        currentSquare = this.boardProvider.findSquare(x, y);
        currentPiece = this.boardProvider.pieceOpening(currentSquare);
    }

    public void drag(float x, float y) {
        currentSquare = this.boardProvider.findSquare(x, y);
        this.boardProvider.changePiecePosition(currentSquare, currentPiece, x, y);
    }

    public void drop() {
        Optional<Piece> attackedPiece = currentSquare.isPresent() ?
                currentSquare.get().getPiece() : Optional.ofNullable(null);

        AbstractPiece.MoveResult moveResult = this.boardProvider.pieceEnding(currentSquare, currentPiece);

        if(moveResult != null && moveResult.couldMove())
            currentPiece.ifPresent((p) -> {
                String opponentPieceId = attackedPiece.isPresent() ? attackedPiece.get().getName() : "Empty";

                this.pieceLifecycle.pieceMovement(p, opponentPieceId);
            });
    }
}
