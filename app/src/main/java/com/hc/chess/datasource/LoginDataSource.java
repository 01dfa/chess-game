package com.hc.chess.datasource;

import android.net.Uri;
import android.util.Log;

import com.hc.chess.model.UserSession;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.Map;

public class LoginDataSource {
    private static final String TAG = "LoginDataSource";
    private String codeVerifier;
    private String codeChallenge;
    private final String CLIENT_ID;
    private final String CLIENT_SECRET;
    private final String AUTHORIZATION_ENDPOINT;
    private final String TOKEN_ENDPOINT;
    private final String LOGOUT_ENDPOINT;
    private final String REDIRECT_SCHEME;
    private final String REDIRECT_URI;
    private final String LOGOUT_REDIRECT_URI;
    private final String PLAYER_INFO_ENDPOINT;
    private String currentAccessToken;
    private String currentIdToken;

    private LoginDataSource(Builder builder) {
        this.CLIENT_ID = builder.clientId;
        this.CLIENT_SECRET = builder.clientSecret;
        this.AUTHORIZATION_ENDPOINT = builder.authorizationEndpoint;
        this.TOKEN_ENDPOINT = builder.tokenEndpoint;
        this.LOGOUT_ENDPOINT = builder.logoutEndpoint;
        this.PLAYER_INFO_ENDPOINT = builder.playerInfoEndpoint;
        this.REDIRECT_SCHEME = builder.redirectScheme;
        this.REDIRECT_URI = builder.redirectUri;
        this.LOGOUT_REDIRECT_URI = builder.logoutRedirectUri;
        this.currentAccessToken = "";
        this.currentIdToken = "";
    }

    public static class Builder {
        private String clientId;
        private String clientSecret;
        private String authorizationEndpoint;
        private String tokenEndpoint;
        private String logoutEndpoint;
        private String redirectScheme;
        private String redirectUri;
        private String logoutRedirectUri;
        private String playerInfoEndpoint;

        public Builder clientId(String clientId) {
            this.clientId = clientId;
            return this;
        }

        public Builder clientSecret(String clientSecret) {
            this.clientSecret = clientSecret;
            return this;
        }

        public Builder authorizationEndpoint(String authorizationEndpoint) {
            this.authorizationEndpoint = authorizationEndpoint;
            return this;
        }

        public Builder tokenEndpoint(String tokenEndpoint) {
            this.tokenEndpoint = tokenEndpoint;
            return this;
        }

        public Builder logoutEndpoint(String logoutEndpoint) {
            this.logoutEndpoint = logoutEndpoint;
            return this;
        }

        public Builder redirectScheme(String redirectScheme) {
            this.redirectScheme = redirectScheme;
            return this;
        }

        public Builder redirectUri(String redirectUri) {
            this.redirectUri = redirectUri;
            return this;
        }

        public Builder logoutRedirectUri(String logoutRedirectUri) {
            this.logoutRedirectUri = logoutRedirectUri;
            return this;
        }

        public Builder playerInfoEndpoint(String playerInfoEndpoint) {
            this.playerInfoEndpoint = playerInfoEndpoint;
            return this;
        }

        public LoginDataSource build() {
            return new LoginDataSource(this);
        }
    }

    public Result<UserSession> login(Uri uri) {
        try {
            UserSession user = this.authFlow(uri);

            return new Result.Success<>(user);
        } catch (Exception e) {
            return new Result.Error(new IOException("Failed to login", e));
        }
    }

    public String getRedirectScheme() { return this.REDIRECT_SCHEME; }

    public Uri getAuthorizeUri() {
        this.createCode();

        Uri uri = Uri.parse(this.AUTHORIZATION_ENDPOINT)
                .buildUpon()
                .appendQueryParameter("response_type", "code")
                .appendQueryParameter("client_id", this.CLIENT_ID)
                .appendQueryParameter("scope", "openid")
                //.appendQueryParameter("state", state)
                .appendQueryParameter("redirect_uri", this.REDIRECT_URI)
                .appendQueryParameter("code_challenge", this.codeChallenge)
                .appendQueryParameter("code_challenge_method", "S256")
                .build();

        return uri;
    }

    private void createCode () {
        try {
            SecureRandom secureRandom = new SecureRandom();
            byte [] code = new byte[32];
            secureRandom.nextBytes(code);
            this.codeVerifier = Base64.getUrlEncoder()
                    .withoutPadding()
                    .encodeToString(code);

            MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
            byte [] digested = messageDigest.digest(codeVerifier.getBytes());
            this.codeChallenge = Base64.getUrlEncoder()
                    .withoutPadding()
                    .encodeToString(digested);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public UserSession authFlow(Uri uri) throws JSONException, IOException {
        String code = uri.getQueryParameter("code");

        String tokenResult = HttpRequest.post(this.TOKEN_ENDPOINT,
                AuthorizationHeaderType.BASIC,
                this.CLIENT_ID +":"+this.CLIENT_SECRET,
                ContentType.URL_ENCODED_TYPE,
                Map.of(
                        "client_id", this.CLIENT_ID,
                        "redirect_uri", this.REDIRECT_URI,
                        "grant_type", "authorization_code",
                        "code", code,
                        "code_verifier", this.codeVerifier
                ));

        JSONObject jsonTokenResponse = new JSONObject(tokenResult);

        this.currentAccessToken = jsonTokenResponse.getString("access_token");
        this.currentIdToken = jsonTokenResponse.getString("id_token");

        String playerInfoResult = HttpRequest.post(PLAYER_INFO_ENDPOINT,
                AuthorizationHeaderType.BEARER,
                this.currentAccessToken,
                ContentType.JSON_TYPE,
                null);

        JSONObject jsonPlayerResult = new JSONObject(playerInfoResult);

        return UserSession.createInstance(
                jsonPlayerResult.getString("id"),
                jsonPlayerResult.getString("name"),
                this.currentAccessToken);
    }

    public Uri getLogoutUri() {
        Uri uri = Uri.parse(this.LOGOUT_ENDPOINT)
                .buildUpon()
                .appendQueryParameter("id_token_hint", this.currentIdToken)
                .appendQueryParameter("client_id", this.CLIENT_ID)
                .appendQueryParameter("post_logout_redirect_uri", this.LOGOUT_REDIRECT_URI)
                .build();

        return uri;
    }
}
