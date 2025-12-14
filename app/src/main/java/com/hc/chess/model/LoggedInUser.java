package com.hc.chess.model;

public class LoggedInUser {
    private static volatile LoggedInUser instance;
    private String userId;
    private String displayName;
    private String token;

    private LoggedInUser(String userId, String displayName, String token) {
        this.userId = userId;
        this.displayName = displayName;
        this.token = token;
    }

    public static LoggedInUser createInstance(String userId, String displayName, String token) {
        if(instance == null) {
            instance = new LoggedInUser(userId, displayName, token);
        }

        return instance;
    }

    public static LoggedInUser getInstance() {
        return instance;
    }

    public String getUserId() {
        return userId;
    }

    public String getDisplayName() { return displayName; }

    public String getToken() { return token; }

    public void clear() {
        this.userId = null;
        this.displayName = null;
        this.token = null;
        instance = null;
    }
}
