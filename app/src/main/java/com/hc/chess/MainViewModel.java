package com.hc.chess;

import android.content.Context;
import android.content.Intent;

import androidx.activity.result.ActivityResultCaller;
import androidx.activity.result.ActivityResultLauncher;
import androidx.browser.auth.AuthTabIntent;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.hc.chess.repository.SessionRepository;

public class MainViewModel extends ViewModel {
    private static final String TAG = "MainViewModel";
    private MutableLiveData<String> signOutResult = new MutableLiveData<>();
    private SessionRepository sessionRepository;
    private IntentWrapper intentWrapper;
    private ActivityResultLauncher<Intent> launcher;


    MainViewModel(Context context, SessionRepository sessionRepository) {
        this.launcher = AuthTabIntent.registerActivityResultLauncher(
                (ActivityResultCaller) context,
                this::handleSignOutResult);

        this.intentWrapper = new IntentWrapper(context, this.launcher);
        this.sessionRepository = sessionRepository;
    }

    public LiveData<String> getSignOutResult() {
        return signOutResult;
    }

    public void signOut() {
        if(this.sessionRepository.isActiveSession())
            sessionRepository.launchLogout(this.intentWrapper);
    }

    private void handleSignOutResult(AuthTabIntent.AuthResult result) {
        if(result.resultUri != null
                && result.resultUri.toString().contains("logout_callback")) {
            this.signOutResult.setValue(MainState.SIGN_OUT_FINISH.name());
        }
    }
}
