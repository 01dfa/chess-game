package com.hc.chess;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.hc.chess.databinding.ActivityMainBinding;
import com.hc.chess.model.SignInResult;
import com.hc.chess.signin.SignInActivity;
import com.hc.chess.signup.SignupActivity;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private MainViewModel mainViewModel;
    private ActivityResultLauncher<Intent> settingsLauncher;
    private ActivityMainBinding binding;
    private ProgressBar loadingProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSignOutSuccessButtonVisibility();

        loadingProgressBar = binding.loading;

        mainViewModel = new ViewModelProvider(this, new MainViewModelFactory(this))
                .get(MainViewModel.class);

        settingsLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                this::handleActivityResult
        );

        binding.buttonSignOut.setOnClickListener(v -> {
            loadingProgressBar.setVisibility(View.VISIBLE);
            mainViewModel.signOut();
        });

        binding.buttonSignIn.setOnClickListener(v -> {
            loadingProgressBar.setVisibility(View.VISIBLE);
            Intent intent = new Intent(this, SignInActivity.class);
            settingsLauncher.launch(intent);
        });

        binding.buttonSignUp.setOnClickListener(v -> {
            Intent intent = new Intent(this, SignupActivity.class);
            settingsLauncher.launch(intent);
        });

        this.mainViewModel.getSignOutResult().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                if(s.equals(MainState.SIGN_OUT_FINISH.name())) {
                    setSignOutSuccessButtonVisibility();
                    binding.textviewPrincipal.setText(R.string.welcome);
                }

                loadingProgressBar.setVisibility(View.GONE);
            }
        });
    }

    private void setSignInSuccessButtonVisibility () {
        binding.buttonSignOut.setVisibility(View.VISIBLE);
        binding.buttonSignIn.setVisibility(View.INVISIBLE);
        binding.buttonSignUp.setVisibility(View.INVISIBLE);
        binding.buttonPlay.setVisibility(View.VISIBLE);
    }

    private void setSignOutSuccessButtonVisibility () {
        binding.buttonSignOut.setVisibility(View.INVISIBLE);
        binding.buttonSignIn.setVisibility(View.VISIBLE);
        binding.buttonSignUp.setVisibility(View.VISIBLE);
        binding.buttonPlay.setVisibility(View.INVISIBLE);
    }

    private void handleActivityResult (ActivityResult result) {
        Log.d(TAG, "handleActivityResult: " + result);
        loadingProgressBar.setVisibility(View.GONE);

        if (result.getResultCode() == Activity.RESULT_OK
                && result.getData() != null) {
            Intent data = result.getData();

            SignInResult signinResult = data
                    .getParcelableExtra(IntentResult.SIGN_IN.name());

            if (signinResult != null && signinResult.isSuccess()) {
                String name = signinResult.getUserName();
                binding.textviewPrincipal.setText(name);

                setSignInSuccessButtonVisibility();
            }
        }
    }
}