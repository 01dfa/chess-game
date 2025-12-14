package com.hc.chess.model;

public class SignupResult {
    private SignupUser success;
    private Integer error;

    public SignupResult(Integer error) { this.error = error; }

    public SignupResult(SignupUser success) {
        this.success = success;
    }

    public boolean isSuccess() { return this.success != null && this.error == null; }

    public SignupUser getSuccess() {
        return success;
    }

    public Integer getError() {
        return error;
    }
}
