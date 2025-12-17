package com.hc.chess.model;

public class SignUpResult {
    private SignUpUser success;
    private Integer error;

    public SignUpResult(Integer error) { this.error = error; }

    public SignUpResult(SignUpUser success) {
        this.success = success;
    }

    public boolean isSuccess() { return this.success != null && this.error == null; }

    public SignUpUser getSuccess() {
        return success;
    }

    public Integer getError() {
        return error;
    }
}
