package com.hc.chess.signup;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class SignUpViewModelFactory implements ViewModelProvider.Factory {
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(SignUpViewModel.class)) {
            return (T) new SignUpViewModel();
        }
        else
            throw new IllegalArgumentException("Unknown ViewModel class");
    }
}
