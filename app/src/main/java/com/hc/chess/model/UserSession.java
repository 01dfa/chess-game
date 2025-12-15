package com.hc.chess.model;

public class UserSession {
    private static volatile UserSession instance;
    private String userId;
    private String displayName;
    private String token;

    private UserSession(String userId, String displayName, String token) {
        this.userId = userId;
        this.displayName = displayName;
        this.token = token;
    }

    public static UserSession createInstance(String userId, String displayName, String token) {
        if(instance == null) {
            instance = new UserSession(userId, displayName, token);
        }

        return instance;
    }

    public static UserSession getInstance() {
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
