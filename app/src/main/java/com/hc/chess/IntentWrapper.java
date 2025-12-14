package com.hc.chess;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.browser.auth.AuthTabIntent;
import androidx.browser.customtabs.CustomTabsClient;
import androidx.browser.customtabs.CustomTabsIntent;

import org.jspecify.annotations.NonNull;

public class IntentWrapper {
    //private AuthTabIntent authTabIntent;
    private Context context;
    private ActivityResultLauncher<Intent> launcher;

    public IntentWrapper(Context context, ActivityResultLauncher<Intent> launcher) {
        this.context = context;
        this.launcher = launcher;
    }

    public void launch(
            @NonNull Uri uri,
            @NonNull String redirectScheme) {
        String packageName = CustomTabsClient.getPackageName(context, null);

        if (packageName == null) {
            Toast.makeText(context, "CustomTabsClient not found.", Toast.LENGTH_SHORT).show();
            return;
        }

        if (CustomTabsClient.isAuthTabSupported(context, packageName)) {
            AuthTabIntent authTabIntent = new AuthTabIntent.Builder().build();
            authTabIntent.launch(launcher, uri, redirectScheme);
        } else {
            CustomTabsIntent customTabsIntent = new CustomTabsIntent.Builder().build();
            customTabsIntent.intent.setFlags(FLAG_ACTIVITY_NEW_TASK);
            customTabsIntent.launchUrl(context, uri);
        }
    }
}
