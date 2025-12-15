package com.hc.chess.signin;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.hc.chess.repository.SessionRepository;

public class SignInViewModelFactory implements ViewModelProvider.Factory {
    private Context context;

    public SignInViewModelFactory(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    @SuppressWarnings("unchecked")
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(SignInViewModel.class)) {
            return (T) new SignInViewModel(this.context,
                    SessionRepository.getInstance(null));
        } else {
            throw new IllegalArgumentException("Unknown ViewModel class");
        }
    }
}
