package com.hc.chess.repository;

import android.net.Uri;

import com.hc.chess.IntentWrapper;
import com.hc.chess.datasource.Result;
import com.hc.chess.datasource.LoginDataSource;
import com.hc.chess.model.UserSession;

public class SessionRepository {
    private static volatile SessionRepository instance;
    private final LoginDataSource dataSource;
    private UserSession session;
    private SessionRepository(LoginDataSource dataSource) {
        this.dataSource = dataSource;
        session = null;
    }

    public static SessionRepository getInstance(LoginDataSource dataSource) {
        if (instance == null) {
            instance = new SessionRepository(dataSource);
        }

        return instance;
    }

    public boolean isActiveSession() { return session != null; }

    public void launchLogout(IntentWrapper intentWrapper) {
        this.session.clear();
        intentWrapper.launch(dataSource.getLogoutUri(), dataSource.getRedirectScheme());
    }

    public void launchAuthorize(IntentWrapper intentWrapper) {
        intentWrapper.launch(
                dataSource.getAuthorizeUri(),
                dataSource.getRedirectScheme());
    }

    public boolean isOauthRedirectURI(Uri uri) {
        return dataSource.isOauthRedirectURI(uri);
    }

    public boolean isLogoutRedirectURI(Uri uri) {
        return dataSource.isLogoutRedirectURI(uri);
    }

    private void setSession(UserSession user) {
        this.session = user;
    }

    public Result<UserSession> login(Uri uri) {
        Result<UserSession> session = dataSource.login(uri);

        if (Result.isSuccess(session)) {
            setSession(session.getData());
        }

        return session;
    }
}
