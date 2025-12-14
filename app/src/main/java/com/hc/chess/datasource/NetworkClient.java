package com.hc.chess.datasource;

import java.net.CookieManager;
import java.net.CookiePolicy;

import okhttp3.OkHttpClient;

public class NetworkClient {
    private static OkHttpClient instance;
    private static CookieManager cookieManager;

    public static OkHttpClient getInstance() {
        if (instance == null) {
            cookieManager = new CookieManager();
            cookieManager.setCookiePolicy(CookiePolicy.ACCEPT_ALL);

            instance = new OkHttpClient.Builder()
                    //.cookieJar(new JavaNetCookieJar(cookieManager))
                    .build();
        }
        return instance;
    }

    public static void clearCookies() {
        if (cookieManager != null) {
            cookieManager.getCookieStore().removeAll();
        }
    }
}
