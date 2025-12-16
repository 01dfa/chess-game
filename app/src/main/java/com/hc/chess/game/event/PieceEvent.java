package com.hc.chess.game.event;

public class PieceEvent {
    private final String content;
    private final Action action;

    public PieceEvent(Action action, String content) {
        this.action = action;
        this.content = content;
    }

    public String getContent() { return this.content; }

    public Action getAction() { return this.action; }
    public enum Action {
        ON_MOVE,
        ON_DROP_RESET_OPENING,
        ON_ERROR
    }
}
