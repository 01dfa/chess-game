package com.hc.chess.signup;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.hc.chess.BuildConfig;
import com.hc.chess.datasource.SignupDataSource;
import com.hc.chess.repository.SignupRepository;

@SuppressWarnings("unchecked")
public class SignupViewModelFactory implements ViewModelProvider.Factory {
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(SignupViewModel.class)) {
            SignupDataSource signUpDataSource = new SignupDataSource.Builder()
                    .clientId(BuildConfig.SIGN_UP_CLIENT_ID)
                    .clientSecret(BuildConfig.SIGN_UP_CLIENT_SECRET)
                    .tokenEndpoint(BuildConfig.SIGN_UP_TOKEN_ENDPOINT)
                    .signUpEndpoint(BuildConfig.SIGN_UP_ENDPOINT)
                    .oauthTypeKey(BuildConfig.SIGN_UP_O_AUTH_TYPE_KEY)
                    .oauthTypeValue(BuildConfig.SIGN_UP_O_AUTH_TYPE_VALUE)
                    .build();

            return (T) new SignupViewModel(SignupRepository.getInstance(signUpDataSource));
        }
        else
            throw new IllegalArgumentException("Unknown ViewModel class");
    }
}
