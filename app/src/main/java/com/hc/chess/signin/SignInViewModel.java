package com.hc.chess.signin;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import androidx.activity.result.ActivityResultCaller;
import androidx.activity.result.ActivityResultLauncher;
import androidx.browser.auth.AuthTabIntent;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.hc.chess.IntentWrapper;
import com.hc.chess.R;
import com.hc.chess.datasource.Result;
import com.hc.chess.model.UserSession;
import com.hc.chess.model.SignInResult;
import com.hc.chess.repository.SessionRepository;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SignInViewModel extends ViewModel {
    private static final String TAG = "SignInViewModel";
    private final MutableLiveData<SignInResult> signinResult;
    private final MutableLiveData<Integer> authResult;
    private final SessionRepository sessionRepository;
    private final IntentWrapper intentWrapper;
    private final ActivityResultLauncher<Intent> launcher;
    private final Handler mainThreadHandler;
    private final ExecutorService executor;

    SignInViewModel(Context context, SessionRepository sessionRepository) {
        this.signinResult = new MutableLiveData<>();
        this.authResult = new MutableLiveData<>();
        this.launcher = AuthTabIntent.registerActivityResultLauncher(
                (ActivityResultCaller) context,
                this::handleAuthResult);
        this.intentWrapper = new IntentWrapper(context, this.launcher);
        this.sessionRepository = sessionRepository;
        this.executor = Executors.newSingleThreadExecutor();
        mainThreadHandler = new Handler(Looper.getMainLooper());
    }
    public LiveData<SignInResult> getSignInResult() {
        return signinResult;
    }

    public LiveData<Integer> getAuthResult() {
        return authResult;
    }

    public void launchAuthorize() {
        try {
            sessionRepository.launchAuthorize(this.intentWrapper);
        } catch (Exception ex) {
            Log.e(TAG, String.format("%s", ex.getMessage()));
            signinResult.postValue(new SignInResult(R.string.sign_in_failed));
        }
    }

    private void handleAuthResult(AuthTabIntent.AuthResult result) {
        if(result.resultCode == AuthTabIntent.RESULT_CANCELED) {
            this.authResult.setValue(1);
        }

        if (result.resultCode == AuthTabIntent.RESULT_OK
                && result.resultUri.toString().contains("callback")) {
            this.login(result.resultUri);
        }
    }

    private void login(Uri uri) {
        executor.execute(() -> {
            Result<UserSession> result = sessionRepository.login(uri);

            if (Result.isSuccess(result)) {
                UserSession data = result.getData();
                mainThreadHandler.post(() ->
                        signinResult.postValue(new SignInResult(data.getDisplayName())));
            } else {
                mainThreadHandler.post(() ->
                        signinResult.postValue(new SignInResult(R.string.sign_in_failed)));
            }

            executor.shutdown();
        });
    }
}
