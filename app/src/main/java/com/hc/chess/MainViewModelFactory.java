package com.hc.chess;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.hc.chess.datasource.LoginDataSource;
import com.hc.chess.repository.SessionRepository;

public class MainViewModelFactory implements ViewModelProvider.Factory {
    private Context context;

    public MainViewModelFactory (Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    @SuppressWarnings("unchecked")
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(MainViewModel.class)) {
            LoginDataSource loginDataSource = new LoginDataSource.Builder()
                    .clientId(BuildConfig.OAUTH_CLIENT_ID)
                    .clientSecret(BuildConfig.OAUTH_CLIENT_SECRET)
                    .grantType(BuildConfig.OAUTH_GRANT_TYPE)
                    .grantTypeParameterName(BuildConfig.OAUTH_GRANT_TYPE_PARAMETER_NAME)
                    .authorizationEndpoint(BuildConfig.OAUTH_AUTHORIZATION_ENDPOINT)
                    .tokenEndpoint(BuildConfig.OAUTH_TOKEN_ENDPOINT)
                    .logoutEndpoint(BuildConfig.OAUTH_LOGOUT_ENDPOINT)
                    .playerInfoEndpoint(BuildConfig.OAUTH_PLAYER_INFO_ENDPOINT)
                    .redirectScheme(BuildConfig.OAUTH_REDIRECT_SCHEME)
                    .redirectUri(BuildConfig.OAUTH_REDIRECT_URI)
                    .logoutRedirectUri(BuildConfig.OAUTH_LOGOUT_REDIRECT_URI)
                    .build();

            return (T) new MainViewModel(this.context,
                    SessionRepository.getInstance(loginDataSource));
        } else {
            throw new IllegalArgumentException("Unknown ViewModel class");
        }
    }
}
