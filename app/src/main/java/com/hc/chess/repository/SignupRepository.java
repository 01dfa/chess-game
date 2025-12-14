package com.hc.chess.repository;

import com.hc.chess.datasource.Result;
import com.hc.chess.datasource.SignupDataSource;
import com.hc.chess.model.SignupUser;

public class SignUpRepository {
    private static volatile SignUpRepository instance;
    private SignupDataSource dataSource;
    private SignUpRepository(SignupDataSource dataSource) {
        this.dataSource = dataSource;
    }

    public static SignUpRepository getInstance(SignupDataSource dataSource) {
        if (instance == null) {
            instance = new SignUpRepository(dataSource);
        }
        return instance;
    }

    public Result<SignupUser> register(String email, String password) {
        Result<SignupUser> result = dataSource.register(email, password);
        return result;
    }
}
