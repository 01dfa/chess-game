package com.hc.chess.repository;

import android.net.Uri;

import com.hc.chess.IntentWrapper;
import com.hc.chess.datasource.Result;
import com.hc.chess.datasource.LoginDataSource;
import com.hc.chess.model.LoggedInUser;

public class SessionRepository {
    private static volatile SessionRepository instance;
    private final LoginDataSource dataSource;
    private LoggedInUser user;
    private SessionRepository(LoginDataSource dataSource) {
        this.dataSource = dataSource;
        user = null;
    }

    public static SessionRepository getInstance(LoginDataSource dataSource) {
        if (instance == null) {
            instance = new SessionRepository(dataSource);
        }
        return instance;
    }

    public boolean isLoggedIn() { return user != null; }

    public void launchLogout(IntentWrapper intentWrapper) {
        this.user.clear();
        intentWrapper.launch(dataSource.getLogoutUri(), dataSource.getRedirectScheme());
    }

    public void launchAuthorize(IntentWrapper intentWrapper) {
        intentWrapper.launch(
                dataSource.getAuthorizeUri(),
                dataSource.getRedirectScheme());
    }

    private void setLoggedInUser(LoggedInUser user) {
        this.user = user;
    }

    public Result<LoggedInUser> login(Uri uri) {
        Result<LoggedInUser> result = dataSource.login(uri);

        if (Result.isSuccess(result)) {
            setLoggedInUser(result.getData());
        }

        return result;
    }
}
