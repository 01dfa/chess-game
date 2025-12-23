package com.hc.chess;

import android.content.Context;
import android.content.Intent;

import androidx.activity.result.ActivityResultCaller;
import androidx.activity.result.ActivityResultLauncher;
import androidx.browser.auth.AuthTabIntent;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class MainViewModel extends ViewModel {
    private static final String TAG = "MainViewModel";
    private MutableLiveData<String> signOutResult = new MutableLiveData<>();
    private IntentWrapper intentWrapper;
    private ActivityResultLauncher<Intent> launcher;

    MainViewModel(Context context) {
        this.launcher = AuthTabIntent.registerActivityResultLauncher(
                (ActivityResultCaller) context,
                this::handleSignOutResult);

        this.intentWrapper = new IntentWrapper(context, this.launcher);
    }

    public LiveData<String> getSignOutResult() {
        return signOutResult;
    }

    private void handleSignOutResult(AuthTabIntent.AuthResult result) {
    }
}
