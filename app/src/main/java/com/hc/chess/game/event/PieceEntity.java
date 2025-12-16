package com.hc.chess.game.event;

public class PieceEntity {
    private final String pieceId;
    private final String originPosition;
    private final String destinationPosition;
    private final String opponentUserId;
    private final String opponentPieceId;
    private final boolean capture;
    public PieceEntity(String pieceId, String originPosition, String destinationPosition, String opponentUserId, String opponentPieceId, boolean capture) {
        this.pieceId = pieceId;
        this.originPosition = originPosition;
        this.destinationPosition = destinationPosition;
        this.opponentUserId = opponentUserId;
        this.opponentPieceId = opponentPieceId;
        this.capture = capture;
    }

    public String getPieceId() { return this.pieceId; }
    public String getOriginPosition() { return this.originPosition; }
    public String getDestinationPosition() { return this.destinationPosition; }
    public String getOpponentUserId() { return this.opponentUserId; }
    public boolean getCapture() { return this.capture; }
    public String getOpponentPieceId() {
        return opponentPieceId;
    }
}
