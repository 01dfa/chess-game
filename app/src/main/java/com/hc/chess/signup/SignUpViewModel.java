package com.hc.chess.signup;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.util.Patterns;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.hc.chess.R;
import com.hc.chess.datasource.Result;
import com.hc.chess.model.SignUpResult;
import com.hc.chess.model.SignUpUser;
import com.hc.chess.repository.SignUpRepository;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SignUpViewModel extends ViewModel {
    private static final String TAG = "SignUpViewModel";
    private MutableLiveData<SignUpFormState> signupFormState = new MutableLiveData<>();
    private MutableLiveData<SignUpResult> signupResult = new MutableLiveData<>();
    private SignUpRepository signupRepository;
    private final Handler mainThreadHandler;
    private final ExecutorService executor = Executors.newSingleThreadExecutor();

    SignUpViewModel(SignUpRepository signupRepository) {
        this.signupRepository = signupRepository;
        mainThreadHandler = new Handler(Looper.getMainLooper());
    }

    LiveData<SignUpFormState> getSignupFormState() {
        return signupFormState;
    }

    LiveData<SignUpResult> getSignupResult() {
        return signupResult;
    }

    public void register(String email, String password) {
        executor.execute(() -> {
            Result<SignUpUser> result = signupRepository.register(email, password);
            if (Result.isSuccess(result)) {
                SignUpUser data = result.getData();
                mainThreadHandler.post(() ->
                        signupResult.postValue(new SignUpResult(data)));
                executor.shutdown();
            } else {
                Log.e(TAG, ((Result.Error)result).toString());
                mainThreadHandler.post(() ->
                        signupResult.postValue(new SignUpResult(R.string.sign_up_failed)));
                executor.shutdownNow();
            }
        });
    }

    public void loginDataChanged(String email, String password) {
        if (!isEmailValid(email)) {
            signupFormState.setValue(new SignUpFormState(R.string.invalid_email, null));
        } else if (!isPasswordValid(password)) {
            signupFormState.setValue(new SignUpFormState(null, R.string.invalid_password));
        } else {
            signupFormState.setValue(new SignUpFormState());
        }
    }

    private boolean isEmailValid(String email) {
        if (email != null && email.contains("@")) {
            return Patterns.EMAIL_ADDRESS.matcher(email).matches();
        } else
            return false;
    }

    private boolean isPasswordValid(String password) {
        return password != null && password.trim().length() > 8;
    }
}
