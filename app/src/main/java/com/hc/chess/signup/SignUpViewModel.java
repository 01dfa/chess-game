package com.hc.chess.signup;

import android.util.Patterns;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.hc.chess.R;

public class SignUpViewModel extends ViewModel {
    private static final String TAG = "SignUpViewModel";
    private MutableLiveData<SignUpFormState> signupFormState = new MutableLiveData<>();
    private MutableLiveData<SignUpResult> signupResult = new MutableLiveData<>();

    SignUpViewModel() {
    }

    LiveData<SignUpFormState> getSignupFormState() {
        return signupFormState;
    }

    LiveData<SignUpResult> getSignupResult() {
        return signupResult;
    }

    public void register(String email, String password) {
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
