package com.hc.chess.signup;

public class SignUpResult {
    private SignUpUser success;
    private Integer error;

    SignUpResult(Integer error) { this.error = error; }

    SignUpResult(SignUpUser success) {
        this.success = success;
    }

    public boolean isSuccess() { return this.success != null && this.error == null; }

    SignUpUser getSuccess() {
        return success;
    }

    Integer getError() {
        return error;
    }
}
