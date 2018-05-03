package com.nutrihealth.activities;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.View;

import com.nutrihealth.R;
import com.nutrihealth.base.BaseActivity;
import com.nutrihealth.databinding.ActivityLoginBinding;
import com.nutrihealth.databinding.ActivityRegisterBinding;
import com.nutrihealth.utils.InputValidator;
import com.nutrihealth.utils.IntentStarter;

/**
 * Created by Teo on 5/3/2018.
 */

public class LoginActivity extends BaseActivity {

    private ActivityLoginBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(LoginActivity.this, R.layout.activity_login);

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
        String password = binding.passwordEt.getText().toString();

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
    }

    public void onNoAccountPressed(View view) {
        IntentStarter.gotoRegisterActivity(LoginActivity.this, false);
    }
}
