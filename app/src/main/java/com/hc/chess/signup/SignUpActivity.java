package com.hc.chess.signup;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.hc.chess.R;

import com.hc.chess.databinding.ActivitySignUpBinding;
import com.hc.chess.model.SignUpResult;
import com.hc.chess.model.SignUpUser;

public class SignUpActivity extends AppCompatActivity {
    private static final String TAG = "SignUpActivity";
    private SignUpViewModel signupViewModel;
    private ActivitySignUpBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivitySignUpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        signupViewModel = new ViewModelProvider(this, new SignUpViewModelFactory())
                .get(SignUpViewModel.class);

        final EditText emailEditText = binding.email;
        final EditText passwordEditText = binding.password;
        final Button registerButton = binding.register;
        final ProgressBar loadingProgressBar = binding.loading;

        signupViewModel.getSignupFormState().observe(this, new Observer<SignUpFormState>() {
            @Override
            public void onChanged(@Nullable SignUpFormState formState) {
                if (formState == null) {
                    return;
                }

                registerButton.setEnabled(formState.isSuccess());

                if (formState.getEmailError() != null) {
                    emailEditText.setError(getString(formState.getEmailError()));
                }

                if (formState.getPasswordError() != null) {
                    passwordEditText.setError(getString(formState.getPasswordError()));
                }
            }
        });

        signupViewModel.getSignupResult().observe(this, new Observer<SignUpResult>() {
            @Override
            public void onChanged(@Nullable SignUpResult signupResult) {
                loadingProgressBar.setVisibility(View.GONE);

                if (signupResult == null) {
                    return;
                }

                if (signupResult.isSuccess()) {
                    showRegisterSuccess(signupResult.getSuccess());
                    setResult(Activity.RESULT_OK);
                } else {
                    showRegisterFailed(signupResult.getError());
                }

                finish();
            }
        });

        TextWatcher afterTextChangedListener = new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                signupViewModel.loginDataChanged(emailEditText.getText().toString(),
                        passwordEditText.getText().toString());
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }
        };

        emailEditText.addTextChangedListener(afterTextChangedListener);
        passwordEditText.addTextChangedListener(afterTextChangedListener);

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadingProgressBar.setVisibility(View.VISIBLE);
                signupViewModel.register(
                        emailEditText.getText().toString(),
                        passwordEditText.getText().toString());

                getOnBackPressedDispatcher().onBackPressed();
            }
        });
    }

    private void showRegisterSuccess(SignUpUser model) {
        String welcome = String.format("%s: %s",
                getString(R.string.welcome),
                model.getEmail());

        Toast.makeText(getApplicationContext(), welcome, Toast.LENGTH_LONG).show();
    }

    private void showRegisterFailed(Integer error) {
        Toast.makeText(getApplicationContext(), error, Toast.LENGTH_LONG).show();
    }
}