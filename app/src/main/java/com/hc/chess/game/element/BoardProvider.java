package com.hc.chess.game.element;

import android.graphics.Bitmap;
import android.graphics.PointF;

import com.hc.chess.game.element.board.BoardInitializer;
import com.hc.chess.game.element.board.Square;
import com.hc.chess.game.element.pieces.AbstractPiece;
import com.hc.chess.game.element.pieces.Piece;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

public class BoardProvider {
    private Optional<List<Square>> row;
    private Optional<Square> openingSquare;
    private String previousSquare;
    private final BoardInitializer boardInitializer;
    private final List<List<Square>> boardMap;

    public BoardProvider(PointF boardPosition) {
        this.boardInitializer = new BoardInitializer(boardPosition);
        this.boardMap = this.boardInitializer.getMap();
        previousSquare = "";
        openingSquare = Optional.ofNullable(null);
    }

    public Bitmap getBoardImage() { return this.boardInitializer.getBoardImage(); }
    public PointF getBoardPosition() { return this.boardInitializer.getBoardPosition(); }

    public void forEachPiece(Consumer<Piece> action) {
        this.boardMap.parallelStream().forEach((row) ->
                row.parallelStream().forEach((square) ->
                        square.getPiece().ifPresent(action::accept
                        )));
    }

    public Optional<Square> findSquare(float x, float y) {
        row = this.boardMap.parallelStream()
                .filter((r) -> r.get(0).isOnHeight(y))
                .findFirst();

        if(row.isPresent()) {
            return row.get().parallelStream()
                    .filter(s -> s.isOnWidth(x))
                    .findFirst();
        } else
            return Optional.ofNullable(null);
    }

    public Optional<Piece> pieceOpening(Optional<Square> square) {
        Optional<Piece> pieceResult = Optional.ofNullable(null);

        if(square.isPresent() && square.get().getPiece().isPresent()) {
            Square s = square.get();
            previousSquare = s.getName();
            s.print();

            Piece p = s.getPiece().get();
            p.opening();
            p.print();
            openingSquare = Optional.of(s);
            pieceResult = Optional.of(p);
        }

        return pieceResult;
    }

    public void changePiecePosition(Optional<Square> square, Optional<Piece> piece, float x, float y) {
        square.ifPresent(s -> {
            piece.ifPresent(p -> {
                //if(!previousSquare.equals(square.get().getName())) {
                //s.info();
                previousSquare = s.getName();
                p.setPosition(new PointF(x-50, y-50));
                //p.info();
                //}
            });
        });
    }

    public AbstractPiece.MoveResult pieceEnding(Optional<Square> opSquare, Optional<Piece> opPiece) {
        AbstractPiece.MoveResult moveResult = null;

        if(opSquare.isPresent() && opPiece.isPresent()) {
            Square square = opSquare.get();
            Piece piece = opPiece.get();
            square.print();

            moveResult = piece.move(square);

            if(moveResult.couldMove()) {
                openingSquare.ifPresent((os) -> os.setPiece(null));
            }
            else
                piece.resetOpening();

            piece.print();
        }

        return moveResult;
    }
}
