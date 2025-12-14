package com.hc.chess.signup;

import androidx.annotation.Nullable;

public class SignupFormState {
    @Nullable
    private Integer emailError;
    @Nullable
    private Integer passwordError;

    SignupFormState(@Nullable Integer emailError, @Nullable Integer passwordError) {
        this.emailError = emailError;
        this.passwordError = passwordError;
    }

    SignupFormState() {
        this.emailError = null;
        this.passwordError = null;
    }

    @Nullable
    Integer getEmailError() {
        return emailError;
    }

    @Nullable
    Integer getPasswordError() {
        return passwordError;
    }

    boolean isSuccess() {
        return  this.emailError == null && this.passwordError == null;
    }
}
