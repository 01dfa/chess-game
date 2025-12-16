package com.hc.chess.game.element.board;

import android.graphics.Bitmap;
import android.graphics.PointF;

import com.hc.chess.game.element.pieces.Bishop;
import com.hc.chess.game.element.pieces.King;
import com.hc.chess.game.element.pieces.Knight;
import com.hc.chess.game.element.pieces.Pawn;
import com.hc.chess.game.element.pieces.Piece;
import com.hc.chess.game.element.pieces.PieceColor;
import com.hc.chess.game.element.pieces.PieceImage;
import com.hc.chess.game.element.pieces.Queen;
import com.hc.chess.game.element.pieces.Rook;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class BoardInitializer {
    private final List<String> columnsNames;
    private final Map<PieceImage, Class<? extends Piece>> mapPieceInstance;
    private final List<PieceImage> whitePiecesImages;
    private final List<PieceImage> blackPiecesImages;

    private final List<List<Square>> map;
    private final PointF boardPosition;
    private static final int borderBoardLength = 53;
    private static final int squareSideLength = 121;

    public BoardInitializer(PointF boardPosition) {
        this.boardPosition = boardPosition;

        mapPieceInstance = new LinkedHashMap<>();
        columnsNames = List.of("A","B","C","D","E","F","G","H");

        whitePiecesImages = List.of(
                PieceImage.W_ROOKS, PieceImage.W_KNIGHTS, PieceImage.W_BISHOPS,
                PieceImage.W_KING,
                PieceImage.W_QUEEN,
                PieceImage.W_BISHOPS, PieceImage.W_KNIGHTS, PieceImage.W_ROOKS
        );

        blackPiecesImages = List.of(
                PieceImage.B_ROOKS, PieceImage.B_KNIGHTS, PieceImage.B_BISHOPS,
                PieceImage.B_KING,
                PieceImage.B_QUEEN,
                PieceImage.B_BISHOPS, PieceImage.B_KNIGHTS, PieceImage.B_ROOKS
        );

        mapPieceInstance.put(PieceImage.W_ROOKS, Rook.class);
        mapPieceInstance.put(PieceImage.W_KNIGHTS, Knight.class);
        mapPieceInstance.put(PieceImage.W_BISHOPS, Bishop.class);
        mapPieceInstance.put(PieceImage.W_KING, King.class);
        mapPieceInstance.put(PieceImage.W_QUEEN, Queen.class);
        mapPieceInstance.put(PieceImage.W_PAWNS, Pawn.class);
        mapPieceInstance.put(PieceImage.B_ROOKS, Rook.class);
        mapPieceInstance.put(PieceImage.B_KNIGHTS, Knight.class);
        mapPieceInstance.put(PieceImage.B_BISHOPS, Bishop.class);
        mapPieceInstance.put(PieceImage.B_KING, King.class);
        mapPieceInstance.put(PieceImage.B_QUEEN, Queen.class);
        mapPieceInstance.put(PieceImage.B_PAWNS, Pawn.class);

        this.map = createMap();
    }

    public List<List<Square>> getMap() { return this.map; }

    public Bitmap getBoardImage() {
        return BoardImage.BOARD.getSprite(0, 0);
    }

    public PointF getBoardPosition() {
        return this.boardPosition;
    }

    private List<List<Square>> createMap() {
        List<List<Square>> board = new ArrayList<>();

        int rowNumber = columnsNames.toArray().length;

        for (int j = 0; j < columnsNames.toArray().length; j++) {
            List<Square> squareRow = new ArrayList<>(8);

            for (int i = 0; i < columnsNames.toArray().length; i++)
            {
                PointF point = new PointF(
                        squareSideLength * i + (boardPosition.x + borderBoardLength),
                        squareSideLength * j + (boardPosition.y + borderBoardLength));

                String columnName = columnsNames.get(i).toString();
                String name = String.format("%s-%d", columnName, rowNumber);
                Piece piece = null;

                switch (rowNumber) {
                    case 8 :
                        piece = createPiece(point, name, PieceColor.BLACK, whitePiecesImages.get(i));
                        break;
                    case 7 :
                        piece = createPiece(point, name, PieceColor.BLACK, PieceImage.W_PAWNS);
                        break;
                    case 2 :
                        piece = createPiece(point, name, PieceColor.WHITE, PieceImage.B_PAWNS);
                        break;
                    case 1 :
                        piece = createPiece(point, name, PieceColor.WHITE, blackPiecesImages.get(i));
                        break;
                }

                squareRow.add(new Square(point, name, piece));
            }

            board.add(squareRow);
            rowNumber--;
        }

        return board;
    }

    private Piece createPiece(PointF point, String namePosition, PieceColor color, PieceImage pieceImage) {
        try {
            return mapPieceInstance.get(pieceImage)
                    .getDeclaredConstructor(PointF.class, String.class, PieceColor.class, Bitmap.class)
                    .newInstance(new PointF(point.x, point.y), namePosition, color, pieceImage.getSprite());
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        return null;
    }
}
