package com.hc.chess.datasource;

import com.hc.chess.AuthorizationHeaderType;
import com.hc.chess.ContentType;
import com.hc.chess.model.SignUpUser;

import org.json.JSONObject;

import java.io.IOException;
import java.util.Map;

public class SignUpDataSource {
    private final String clientId;
    private final String clientSecret;
    private final String credentials;
    private final String tokenEndpoint;
    private final String signUpEndpoint;
    private final String oauthTypeKey;
    private final String oauthTypeValue;
    private SignUpDataSource(Builder builder) {
        this.clientId = builder.clientId;
        this.clientSecret = builder.clientSecret;
        this.credentials = this.clientId+":"+this.clientSecret;
        this.tokenEndpoint = builder.tokenEndpoint;
        this.signUpEndpoint = builder.signUpEndpoint;
        this.oauthTypeKey = builder.oauthTypeKey;
        this.oauthTypeValue = builder.oauthTypeValue;
    }

    public static class Builder {
        private String clientId;
        private String clientSecret;
        private String tokenEndpoint;
        private String signUpEndpoint;
        private String oauthTypeKey;
        private String oauthTypeValue;

        public Builder clientId(String clientId) {
            this.clientId = clientId;
            return this;
        }

        public Builder clientSecret(String clientSecret) {
            this.clientSecret = clientSecret;
            return this;
        }

        public Builder tokenEndpoint(String tokenEndpoint) {
            this.tokenEndpoint = tokenEndpoint;
            return this;
        }

        public Builder signUpEndpoint(String signUpEndpoint) {
            this.signUpEndpoint = signUpEndpoint;
            return this;
        }

        public Builder oauthTypeKey(String oauthTypeKey) {
            this.oauthTypeKey = oauthTypeKey;
            return this;
        }

        public Builder oauthTypeValue(String oauthTypeValue) {
            this.oauthTypeValue = oauthTypeValue;
            return this;
        }

        public SignUpDataSource build() {
            return new SignUpDataSource(this);
        }
    }

    public Result<SignUpUser> register(String email, String password) {
        try {
            String response = HttpRequest.post(this.tokenEndpoint,
                    AuthorizationHeaderType.BASIC,
                    this.credentials,
                    ContentType.URL_ENCODED_TYPE,
                    Map.of(this.oauthTypeKey, this.oauthTypeValue));

            String accessToken = new JSONObject(response).getString("access_token");

            String playerResult = HttpRequest.post(this.signUpEndpoint,
                    AuthorizationHeaderType.BEARER,
                    accessToken,
                    ContentType.JSON_TYPE,
                    Map.of("username", email,
                            "email", email,
                            "password", password
                    )
            );

            JSONObject jsonPlayerResult = new JSONObject(playerResult);

            SignUpUser result = new SignUpUser(jsonPlayerResult.getString("email"));

            return new Result.Success<>(result);
        } catch (Exception e) {
            return new Result.Error(new IOException("Failed to register", e));
        }
    }
}
