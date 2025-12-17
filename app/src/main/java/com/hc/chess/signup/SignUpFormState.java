package com.hc.chess.signup;

import androidx.annotation.Nullable;

public class SignUpFormState {
    @Nullable
    private Integer emailError;
    @Nullable
    private Integer passwordError;

    SignUpFormState(@Nullable Integer emailError, @Nullable Integer passwordError) {
        this.emailError = emailError;
        this.passwordError = passwordError;
    }

    SignUpFormState() {
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
