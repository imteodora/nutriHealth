package com.nutrihealth.activities;

import android.app.Dialog;
import android.arch.lifecycle.ViewModelProviders;
import android.content.res.Resources;
import android.databinding.DataBindingUtil;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.RadioButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.nutrihealth.R;
import com.nutrihealth.base.BaseActivity;
import com.nutrihealth.constants.Constants;
import com.nutrihealth.databinding.ActivityProfileBinding;
import com.nutrihealth.databinding.ActivityRegisterBinding;
import com.nutrihealth.model.Alarm;
import com.nutrihealth.model.ProfileInfos;
import com.nutrihealth.prefs.PrefsManager;
import com.nutrihealth.utils.FontUtils;
import com.nutrihealth.utils.InputValidator;
import com.nutrihealth.utils.IntentStarter;
import com.nutrihealth.utils.WeightUtils;
import com.nutrihealth.viewModels.ProfileViewModel;
import com.nutrihealth.views.CustomToolbar;

/**
 * Created by Teo on 5/1/2018.
 */

public class RegisterActivity extends BaseActivity {

    private ActivityRegisterBinding binding;
    private int selectedLvl = -1;
    private FirebaseAuth auth;

    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(RegisterActivity.this, R.layout.activity_register);
        auth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        binding.setRegisterActivity(this);
        binding.setShowProgressBar(false);
        setUpViews();

    }

    private void setDividerColor(NumberPicker picker, int color) {

        java.lang.reflect.Field[] pickerFields = NumberPicker.class.getDeclaredFields();
        for (java.lang.reflect.Field pf : pickerFields) {
            if (pf.getName().equals("mSelectionDivider")) {
                pf.setAccessible(true);
                try {
                    ColorDrawable colorDrawable = new ColorDrawable(color);
                    pf.set(picker, colorDrawable);
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                } catch (Resources.NotFoundException e) {
                    e.printStackTrace();
                }
                catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
                break;
            }
        }
    }

    private void setUpViews() {

        setStatusBarColor(R.color.black);
        setupUI(binding.profileParent);
        binding.ageEt.setEnabled(true);
        binding.setShowProgressBar(false);
        binding.registerToolbar.showLogo();
        binding.registerToolbar.setOnBackButtonPressedListener(new CustomToolbar.OnBackButtonPressedListener() {
            @Override
            public void onBackButtonPressed() {
                finish();
            }
        });
        setFontToButtons();

    }

    private void setFontToButtons() {

        Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/Raleway-Bold.ttf");
        binding.saveButton.setTypeface(typeface);
    }

    public void onSelectAgePressed(View view) {

        final Dialog pickAgeDialog = new Dialog(RegisterActivity.this);
        pickAgeDialog.setContentView(R.layout.dialog_number_picker);

        Button saveBtn = (Button) pickAgeDialog.findViewById(R.id.save_button);
        final NumberPicker agePicker = (NumberPicker) pickAgeDialog.findViewById(R.id.numberPicker);
        agePicker.setMaxValue(90); // max value 100
        agePicker.setMinValue(18);   // min value 0

        setDividerColor(agePicker, ContextCompat.getColor(RegisterActivity.this,R.color.light_blue));
        String selectedAge = binding.ageEt.getText().toString();
        if(!InputValidator.isInputEmpty(selectedAge)){
            agePicker.setValue(Integer.valueOf(selectedAge));
        }
        agePicker.setWrapSelectorWheel(false);

        Window window = pickAgeDialog.getWindow();
        window.setGravity(Gravity.TOP);
        window.getAttributes().windowAnimations = R.style.DialogAnimationUpToDown;
        window.setBackgroundDrawableResource(android.R.color.white);
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.ageEt.setText(String.valueOf(agePicker.getValue()));
                pickAgeDialog.dismiss();
            }
        });
        pickAgeDialog.show();
    }

    public void onSelectSports(View view) {
        showOneChoiceDialog();
    }

    public void showOneChoiceDialog() {

        final CharSequence[] items = new CharSequence[Constants.NUMBER_OF_ACTIVITIES_TYPES];
        items[0] = getResources().getString(R.string.activity_lvl_1);
        items[1] = getResources().getString(R.string.activity_lvl_2);
        items[2] = getResources().getString(R.string.activity_lvl_3);
        items[3] = getResources().getString(R.string.activity_lvl_4);
        items[4] = getResources().getString(R.string.activity_lvl_5);


        Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/Raleway-Bold.ttf");

        final android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(RegisterActivity.this);
        builder.setTitle(FontUtils.typeface(typeface, getResources().getString(R.string.choose_activities_level)));
        builder.setSingleChoiceItems(items, selectedLvl, null);
        LayoutInflater inflater = getLayoutInflater();
        View dialogLayout = inflater.inflate(R.layout.dialog_choose_activity, null);
        builder.setView(dialogLayout);
        Button closeButton = dialogLayout.findViewById(R.id.save_button);

        final android.support.v7.app.AlertDialog alert = builder.create();
        alert.show();
        alert.getButton(android.support.v7.app.AlertDialog.BUTTON_POSITIVE).setTypeface(typeface);

        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alert.dismiss();
                int selectedPosition = alert.getListView().getCheckedItemPosition();
                selectedLvl = selectedPosition;
                switch (selectedPosition) {
                    case 0:
                        binding.everyDaySportEt.setText(getResources().getString(R.string.lvl_1));
                        break;
                    case 1:
                        binding.everyDaySportEt.setText(getResources().getString(R.string.lvl_2));
                        break;
                    case 2:
                        binding.everyDaySportEt.setText(getResources().getString(R.string.lvl_3));
                        break;
                    case 3:
                        binding.everyDaySportEt.setText(getResources().getString(R.string.lvl_4));
                        break;
                    case 4:
                        binding.everyDaySportEt.setText(getResources().getString(R.string.lvl_5));
                        break;
                }
            }
        });


        Window window = alert.getWindow();
        window.setGravity(Gravity.TOP);
        window.getAttributes().windowAnimations = R.style.DialogAnimationUpToDown;
        window.setBackgroundDrawableResource(android.R.color.white);
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

    }

    public void onRegisterButtonPressed(View view) {

        binding.emailEt.setError(null);
        binding.passwordEt.setError(null);
        binding.passwordConfimationEt.setError(null);
        binding.nameEt.setError(null);
        binding.currentWeightEt.setError(null);
        binding.heightEt.setError(null);
        binding.ageEt.setError(null);

        final String name = binding.nameEt.getText().toString();
        final String currentWeight = binding.currentWeightEt.getText().toString();
        String gender = null;
        final String height = binding.heightEt.getText().toString();
        final String age = binding.ageEt.getText().toString();
        String activityType = binding.everyDaySportEt.getText().toString();
        String email = binding.emailEt.getText().toString();
        String password = binding.passwordEt.getText().toString();
        String passwordConfirmation = binding.passwordConfimationEt.getText().toString();

        int idRadioButton = binding.genderGroup.getCheckedRadioButtonId();
        View radioButton = binding.genderGroup.findViewById(idRadioButton);
        int idx = binding.genderGroup.indexOfChild(radioButton);

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

        if(!password.equals(passwordConfirmation)){
            showCustomDialog(getResources().getString(R.string.error_title), getResources().getString(R.string.error_confirmation_pass), DialogType.ERROR, null);
            binding.passwordConfimationEt.setError(getResources().getString(R.string.error_confirmation_pass));
            return;
        }

        if (idx != -1) {
            RadioButton r = (RadioButton) binding.genderGroup.getChildAt(idx);
            gender = r.getText().toString();
        }

        if (InputValidator.isInputEmpty(name)) {
            showCustomDialog(getResources().getString(R.string.error_title), getResources().getString(R.string.error_no_name), DialogType.ERROR, null);
            binding.nameEt.setError(getResources().getString(R.string.error_no_input));
            return;
        }

        if (InputValidator.isInputEmpty(currentWeight)) {
            showCustomDialog(getResources().getString(R.string.error_title), getResources().getString(R.string.error_no_current_weight), DialogType.ERROR, null);
            binding.currentWeightEt.setError(getResources().getString(R.string.error_no_input));
            return;
        }

        if (gender == null) {
            showCustomDialog(getResources().getString(R.string.error_title), getResources().getString(R.string.error_no_gender), DialogType.ERROR, null);
            return;
        }

        if (InputValidator.isInputEmpty(height)) {
            showCustomDialog(getResources().getString(R.string.error_title), getResources().getString(R.string.error_no_height), DialogType.ERROR, null);
            binding.heightEt.setError(getResources().getString(R.string.error_no_input));
            return;
        }

        if (InputValidator.isInputEmpty(age)) {
            showCustomDialog(getResources().getString(R.string.error_title), getResources().getString(R.string.error_no_age), DialogType.ERROR, null);
            return;
        }

        if (InputValidator.isInputEmpty(activityType)) {
            showCustomDialog(getResources().getString(R.string.error_title), getResources().getString(R.string.error_no_activity_type), DialogType.ERROR, null);
            return;
        }

        binding.setShowProgressBar(true);

        final String finalGender = gender;
        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        binding.setShowProgressBar(false);
                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            showCustomDialog(getResources().getString(R.string.error_title), getResources().getString(R.string.try_again), DialogType.ERROR, null);
                        } else {

                            double activity = 1;
                            switch (selectedLvl) {
                                case 0:
                                    activity = Constants.SPORT_ACTIVITY_LVL_1;
                                    break;
                                case 1:
                                    activity = Constants.SPORT_ACTIVITY_LVL_2;
                                    break;
                                case 2:
                                    activity = Constants.SPORT_ACTIVITY_LVL_3;
                                    break;
                                case 3:
                                    activity = Constants.SPORT_ACTIVITY_LVL_4;
                                    break;
                                case 4:
                                    activity = Constants.SPORT_ACTIVITY_LVL_5;
                                    break;

                            }

                            int idealWeight = WeightUtils.calculateIdealWeight(Integer.parseInt(height), Integer.parseInt(age), finalGender);
                            int kcalPerDay = WeightUtils.calculateCalPerDay(Integer.parseInt(height),  Integer.parseInt(age), idealWeight,finalGender, activity);

                            ProfileInfos profileInfos = new ProfileInfos(name, Integer.parseInt(currentWeight), finalGender, Integer.parseInt(height), Integer.parseInt(age), selectedLvl, idealWeight,kcalPerDay );
                            writeUserInfos(profileInfos);
                        }
                    }
                });




    }

    private void writeUserInfos(ProfileInfos profileInfos) {
        //get firebase user
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        //build child
        mDatabase.child("users").child(user.getUid()).setValue(profileInfos);
        mDatabase.child("alarms").child(user.getUid()).setValue(new Alarm("0","0","0","0","0"));
        finish();
    }
}
