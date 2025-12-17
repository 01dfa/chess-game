package com.hc.chess.datasource;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Map;

import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class HttpRequest {
    private static final String TAG = "HttpRequest";
    public static final MediaType JSON = MediaType.get("application/json");

    interface BuilderBody<I, O> {
        O build(I input) throws JSONException;
    }

    private static RequestBody buildBodyJSON(Map<String, String> body) throws JSONException {
        JSONObject obj = new JSONObject();

        for(Map.Entry<String, String> entry: body.entrySet()) {
            obj.putOpt(entry.getKey(), entry.getValue());
        }

        return RequestBody.create(obj.toString(), JSON);
    }

    private static RequestBody buildBodyUrlEncoded(Map<String, String> body) {
        FormBody.Builder formBodyBuilder = new FormBody.Builder();
        for(Map.Entry<String, String> entry: body.entrySet()) {
            formBodyBuilder.add(entry.getKey(), entry.getValue());
        }

        return formBodyBuilder.build();
    }

    private static String buildAuthorizationBearer(String token) {
        return String.format("Bearer %s", token);
    }

    private static String buildAuthorizationBasic(String token) {
        String encodedCredentials = Base64.getEncoder().encodeToString(
                token.getBytes(StandardCharsets.UTF_8)
        );

        return "Basic " + encodedCredentials;
    }

    private static BuilderBody<Map<String, String>, RequestBody> selectBodyBuilder(ContentType contentType) {
        BuilderBody<Map<String, String>, RequestBody> builderBody = null;

        switch (contentType) {
            case JSON_TYPE:
                builderBody = HttpRequest::buildBodyJSON;
                break;
            case URL_ENCODED_TYPE:
                builderBody = HttpRequest::buildBodyUrlEncoded;
                break;
        }

        return builderBody;
    }

    private static BuilderBody<String, String> selectAuthHeaderBuilder(AuthorizationHeaderType authType) {
        BuilderBody<String, String> builder = null;

        switch (authType) {
            case BEARER:
                builder = HttpRequest::buildAuthorizationBearer;
                break;
            case BASIC:
                builder = HttpRequest::buildAuthorizationBasic;
                break;
        }

        return builder;
    }

    public static String post(String url,
                              AuthorizationHeaderType authHeaderType,
                              String authorization,
                              ContentType contentType,
                              Map<String, String> body) throws IOException, JSONException {

        OkHttpClient client = NetworkClient.getInstance();

        Request.Builder requestBuilder = new Request.Builder();
        requestBuilder.url(url);

        if(body == null || body.isEmpty()) body = Map.of("","");

        RequestBody requestBody = selectBodyBuilder(contentType).build(body);
        requestBuilder.post(requestBody);

        String authHeaderValue = selectAuthHeaderBuilder(authHeaderType).build(authorization);
        requestBuilder.header("Authorization", authHeaderValue);

        Request request = requestBuilder.build();

        Log.d(TAG, "Post " + url);
        Log.d(TAG, "Body " + body);

        try (Response response = client.newCall(request).execute()) {
            Log.d(TAG, "isSuccessful: " + response.isSuccessful());

            String bodyResponse = response.body().string();

            Log.d(TAG, String.format("Response %s", bodyResponse));

            if(!response.isSuccessful()) {
                throw new IOException(
                        String.format("Status %d", response.code()),
                        new Exception(bodyResponse));
            }

            return bodyResponse;
        }
    }
}
