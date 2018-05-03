package com.nutrihealth.activities;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.nutrihealth.R;
import com.nutrihealth.base.BaseActivity;
import com.nutrihealth.databinding.ActivityLoginBinding;
import com.nutrihealth.databinding.ActivityRegisterBinding;
import com.nutrihealth.prefs.PrefsManager;
import com.nutrihealth.utils.InputValidator;
import com.nutrihealth.utils.IntentStarter;

/**
 * Created by Teo on 5/3/2018.
 */

public class LoginActivity extends BaseActivity {

    private ActivityLoginBinding binding;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(LoginActivity.this, R.layout.activity_login);

        auth = FirebaseAuth.getInstance();
        binding.setLoginActivity(this);
        binding.setShowProgressBar(false);
        setUpViews();

    }

    private void setUpViews() {

        setStatusBarColor(R.color.black);
        setupUI(binding.loginParent);
        binding.loginToolbar.showLogo();
    }

    public void onLoginButtonPressed(View view) {
        binding.emailEt.setError(null);
        binding.passwordEt.setError(null);

        String email = binding.emailEt.getText().toString();
        final String password = binding.passwordEt.getText().toString();

        if(!InputValidator.isValidEmail(email)){
            showCustomDialog(getResources().getString(R.string.error_title), getResources().getString(R.string.error_invalid_email), DialogType.ERROR, null);
            binding.emailEt.setError(getResources().getString(R.string.error_invalid_email));
            return;
        }

        if(InputValidator.isInputEmpty(password)){
            showCustomDialog(getResources().getString(R.string.error_title), getResources().getString(R.string.error_no_password), DialogType.ERROR, null);
            binding.passwordEt.setError(getResources().getString(R.string.error_no_input));
            return;
        }

        if(password.length() < 6){
            showCustomDialog(getResources().getString(R.string.error_title), getResources().getString(R.string.error_password_length), DialogType.ERROR, null);
            binding.passwordEt.setError(getResources().getString(R.string.error_password_length));
            return;
        }

        binding.setShowProgressBar(true);

        //authenticate user
        auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        binding.setShowProgressBar(false);
                        if (!task.isSuccessful()) {
                            // there was an error
                            showCustomDialog(getResources().getString(R.string.error_title), getResources().getString(R.string.try_again), DialogType.ERROR, null);
                        } else {
                            PrefsManager.getInstance(LoginActivity.this).putKeyIsUserLoggedIn(true);
                            IntentStarter.gotoHomeActivity(LoginActivity.this, true);
                        }
                    }
                });


    }

    public void onNoAccountPressed(View view) {
        IntentStarter.gotoRegisterActivity(LoginActivity.this, false);
    }
}
