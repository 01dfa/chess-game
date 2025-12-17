package com.hc.chess.repository;

import com.hc.chess.datasource.Result;
import com.hc.chess.datasource.SignUpDataSource;
import com.hc.chess.model.SignUpUser;

public class SignUpRepository {
    private static volatile SignUpRepository instance;
    private SignUpDataSource dataSource;
    private SignUpRepository(SignUpDataSource dataSource) {
        this.dataSource = dataSource;
    }

    public static SignUpRepository getInstance(SignUpDataSource dataSource) {
        if (instance == null) {
            instance = new SignUpRepository(dataSource);
        }

        return instance;
    }

    public Result<SignUpUser> register(String email, String password) {
        Result<SignUpUser> result = dataSource.register(email, password);
        return result;
    }
}
