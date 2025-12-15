package com.hc.chess.signin;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.hc.chess.IntentResult;
import com.hc.chess.R;
import com.hc.chess.model.SignInResult;
import com.hc.chess.databinding.ActivitySigninBinding;

public class SignInActivity extends AppCompatActivity {
    private static final String TAG = "SignInActivity";
    private SignInViewModel signinViewModel;
    private ActivitySigninBinding binding;
    private ProgressBar loadingProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_signin);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        binding = ActivitySigninBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        loadingProgressBar = binding.loading;

        signinViewModel = new ViewModelProvider(this, new SignInViewModelFactory(this))
                .get(SignInViewModel.class);

        signinViewModel.getAuthResult().observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                if(integer == 1) {
                    loadingProgressBar.setVisibility(View.GONE);
                    finish();
                }
            }
        });

        signinViewModel.getSignInResult().observe(this, new Observer<SignInResult>() {
            @Override
            public void onChanged(SignInResult signinResult) {
                loadingProgressBar.setVisibility(View.GONE);

                if(signinResult.isSuccess()) {
                    showLoginSuccess(signinResult.getUserName());
                } else {
                    showLoginFailed(signinResult.getError());
                }

                Intent intent = new Intent();
                intent.putExtra(IntentResult.SIGN_IN.name(), signinResult);

                setResult(Activity.RESULT_OK, intent);

                finish();
            }
        });

        signinViewModel.launchAuthorize();
    }

    private void showLoginSuccess(String message) {
        String welcome = String
                .format("%s: %s", getString(R.string.welcome), message);

        Toast.makeText(getApplicationContext(), welcome, Toast.LENGTH_LONG).show();
    }

    private void showLoginFailed(@StringRes Integer errorString) {
        Toast.makeText(getApplicationContext(), errorString, Toast.LENGTH_SHORT).show();
    }
}