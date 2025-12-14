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
import com.hc.chess.model.SignupResult;
import com.hc.chess.model.SignupUser;
import com.hc.chess.repository.SignupRepository;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SignupViewModel extends ViewModel {
    private static final String TAG = "SignUpViewModel";
    private MutableLiveData<SignupFormState> signupFormState = new MutableLiveData<>();
    private MutableLiveData<SignupResult> signupResult = new MutableLiveData<>();
    private SignupRepository signupRepository;
    private final Handler mainThreadHandler;
    private final ExecutorService executor = Executors.newSingleThreadExecutor();

    SignupViewModel(SignupRepository signupRepository) {
        this.signupRepository = signupRepository;
        mainThreadHandler = new Handler(Looper.getMainLooper());
    }

    LiveData<SignupFormState> getSignupFormState() {
        return signupFormState;
    }

    LiveData<SignupResult> getSignupResult() {
        return signupResult;
    }

    public void register(String email, String password) {
        executor.execute(() -> {
            Result<SignupUser> result = signupRepository.register(email, password);
            if (Result.isSuccess(result)) {
                SignupUser data = result.getData();
                mainThreadHandler.post(() ->
                        signupResult.postValue(new SignupResult(data)));
                executor.shutdown();
            } else {
                Log.e(TAG, ((Result.Error)result).toString());
                mainThreadHandler.post(() ->
                        signupResult.postValue(new SignupResult(R.string.sign_up_failed)));
                executor.shutdownNow();
            }
        });
    }

    public void loginDataChanged(String email, String password) {
        if (!isEmailValid(email)) {
            signupFormState.setValue(new SignupFormState(R.string.invalid_email, null));
        } else if (!isPasswordValid(password)) {
            signupFormState.setValue(new SignupFormState(null, R.string.invalid_password));
        } else {
            signupFormState.setValue(new SignupFormState());
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
