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
import androidx.lifecycle.ViewModelProvider;

import com.hc.chess.databinding.ActivityMainBinding;
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

        loadingProgressBar = binding.loading;

        mainViewModel = new ViewModelProvider(this, new MainViewModelFactory(this))
                .get(MainViewModel.class);

        settingsLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                this::handleActivityResult
        );

        binding.buttonSignUp.setOnClickListener(v -> {
            Intent intent = new Intent(this, SignupActivity.class);
            settingsLauncher.launch(intent);
        });
    }

    private void handleActivityResult (ActivityResult result) {
        Log.d(TAG, "handleActivityResult: " + result);
        loadingProgressBar.setVisibility(View.GONE);
    }
}