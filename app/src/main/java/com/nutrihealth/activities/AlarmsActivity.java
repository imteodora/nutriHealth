package com.nutrihealth.activities;

import android.app.Dialog;
import android.content.res.Resources;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.RelativeSizeSpan;
import android.text.style.TypefaceSpan;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.NumberPicker;
import android.widget.TextSwitcher;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import com.nutrihealth.R;
import com.nutrihealth.base.BaseActivity;
import com.nutrihealth.databinding.ActivityAlarmsBinding;
import com.nutrihealth.prefs.PrefsManager;
import com.nutrihealth.utils.InputValidator;
import com.nutrihealth.views.CustomToolbar;

import static android.text.Spanned.SPAN_INCLUSIVE_INCLUSIVE;

/**
 * Created by Teo on 4/1/2018.
 */

public class AlarmsActivity extends BaseActivity {

    public enum Alarms{
        BREAKFAST,
        FIRST_SNACK,
        LUNCH,
        SECOND_SNACK,
        DINER
    }

    private ActivityAlarmsBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(AlarmsActivity.this, R.layout.activity_alarms);
        setUpViews();
    }

    private void setUpViews() {

        setStatusBarColor(R.color.black);
        binding.setAlarmsActivity(this);
        binding.alarmsToolbar.setOnBackButtonPressedListener(new CustomToolbar.OnBackButtonPressedListener() {
            @Override
            public void onBackButtonPressed() {
                finish();
            }
        });

        setCurrentTestWatcher();
        startTextAnimation();
        populateFields();


    }

    private void populateFields() {

        String alarmBreackfast  = PrefsManager.getInstance(AlarmsActivity.this).getKeyAlarmBreakfast();
        String alarmFirstSnack  = PrefsManager.getInstance(AlarmsActivity.this).getKeyAlarmFirstSnack();
        String alarmLunch       = PrefsManager.getInstance(AlarmsActivity.this).getKeyAlarmLunch();
        String alarmSecondSnack = PrefsManager.getInstance(AlarmsActivity.this).getKeyAlarmSecondSnack();
        String alarmDiner       = PrefsManager.getInstance(AlarmsActivity.this).getKeyAlarmDiner();

        if(!alarmBreackfast.equals("")){
            binding.breakfastTimeTv.setText(alarmBreackfast);
            binding.breakfastSw.setChecked(true);
        }

        if(!alarmFirstSnack.equals("")){
            binding.firstSnackTimeTv.setText(alarmFirstSnack);
            binding.firstSnackSw.setChecked(true);
        }

        if(!alarmLunch.equals("")){
            binding.lunchTimeTv.setText(alarmLunch);
            binding.lunchSw.setChecked(true);
        }
        if(!alarmSecondSnack.equals("")){
            binding.secondSnackTimeTv.setText(alarmSecondSnack);
            binding.secondSnackSw.setChecked(true);
        }

        if(!alarmDiner.equals("")){
            binding.dinerTimeTv.setText(alarmDiner);
            binding.dinerSw.setChecked(true);
        }

        binding.breakfastSw.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    PrefsManager.getInstance(AlarmsActivity.this).putKeyAlarmBreakfast(binding.breakfastTimeTv.getText().toString());
                    return;
                }
                PrefsManager.getInstance(AlarmsActivity.this).putKeyAlarmBreakfast("");
            }
        });

        binding.firstSnackSw.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    PrefsManager.getInstance(AlarmsActivity.this).putKeyAlarmFirstSnack(binding.firstSnackTimeTv.getText().toString());
                    return;
                }
                PrefsManager.getInstance(AlarmsActivity.this).putKeyAlarmFirstSnack("");
            }
        });

        binding.lunchSw.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    PrefsManager.getInstance(AlarmsActivity.this).putKeyAlarmLunch(binding.lunchTimeTv.getText().toString());
                    return;
                }
                PrefsManager.getInstance(AlarmsActivity.this).putKeyAlarmLunch("");
            }
        });

        binding.secondSnackSw.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    PrefsManager.getInstance(AlarmsActivity.this).putKeyAlarmSecondSnack(binding.secondSnackTimeTv.getText().toString());
                    return;
                }
                PrefsManager.getInstance(AlarmsActivity.this).putKeyAlarmSecondSnack("");
            }
        });

        binding.dinerSw.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    PrefsManager.getInstance(AlarmsActivity.this).putKeyAlarmDiner(binding.dinerTimeTv.getText().toString());
                    return;
                }
                PrefsManager.getInstance(AlarmsActivity.this).putKeyAlarmDiner("");
            }
        });




    }

    int i = 1;

    private void startTextAnimation() {

        Animation in = AnimationUtils.loadAnimation(this,
                R.anim.fade_in_text);

        binding.textSwitcher.setInAnimation(in);

        final Handler handler = new Handler();
        final int delay = 5000; //milliseconds

        handler.postDelayed(new Runnable() {
            public void run() {

                AnimationSet animSet = new AnimationSet(true);
                Animation anim = new AlphaAnimation(1.0f, 0.0f);
                anim.setDuration(1000);

                animSet.addAnimation(anim);

                TranslateAnimation animation = new TranslateAnimation(0, 0, 0, -50);
                animation.setDuration(1000);
                animation.setFillAfter(true);
                animation.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {

                        String message = "";
                        switch (i) {
                            case 1:
                                message = getString(R.string.alarms_text_2);
                                break;
                            case 2:
                                message = getString(R.string.alarms_text_3);
                                break;
                            case 3:
                                message = getString(R.string.alarms_text_1);
                                break;
                            default:
                                break;
                        }

                        SpannableString s1 = new SpannableString(message);
                        s1.setSpan(new RelativeSizeSpan(1.6f), 0, message.length(), SPAN_INCLUSIVE_INCLUSIVE);
                        binding.textSwitcher.setText(s1);

                        if (i == 3) i = 1;
                        else i++;


                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });

                animSet.addAnimation(animation);
                binding.textSwitcher.startAnimation(animSet);
                handler.postDelayed(this, delay);

            }
        }, delay);


    }

    private void setCurrentTestWatcher() {

        binding.textSwitcher.setFactory(new ViewSwitcher.ViewFactory() {
            @Override
            public View makeView() {
                TextView t = new TextView(AlarmsActivity.this);
                t.setGravity(Gravity.TOP);
                t.setTextColor(ContextCompat.getColor(AlarmsActivity.this, R.color.gray_dark));

                Typeface face = Typeface.createFromAsset(getAssets(),
                        "fonts/Raleway-Medium.ttf");
                t.setTypeface(face);
                return t;
            }
        });

        String message = "";

        message = getString(R.string.alarms_text_1);

        SpannableString s1 = new SpannableString(message);
        s1.setSpan(new RelativeSizeSpan(1.6f), 0, message.length(), SPAN_INCLUSIVE_INCLUSIVE);
        binding.textSwitcher.setCurrentText(s1);
    }

    public void onSelectTimePressed(final Alarms alarmType){
        final Dialog pickTimeDialog = new Dialog(AlarmsActivity.this);
        pickTimeDialog.setContentView(R.layout.dialog_choose_alarm_time);

        Button saveBtn = (Button) pickTimeDialog.findViewById(R.id.save_button);
        final NumberPicker hoursPicker = (NumberPicker) pickTimeDialog.findViewById(R.id.hour_picker);
        final NumberPicker minutesPicker = (NumberPicker) pickTimeDialog.findViewById(R.id.minutes_picker);
        hoursPicker.setMaxValue(23);
        hoursPicker.setMinValue(00);

        minutesPicker.setMaxValue(59);
        minutesPicker.setMinValue(00);

        setDividerColor(hoursPicker, ContextCompat.getColor(AlarmsActivity.this,R.color.light_blue));
        setDividerColor(minutesPicker, ContextCompat.getColor(AlarmsActivity.this,R.color.light_blue));

        String selectedTime = "";

        switch (alarmType){
            case BREAKFAST:
                selectedTime = binding.breakfastTimeTv.getText().toString();
                break;
            case FIRST_SNACK:
                selectedTime = binding.firstSnackTimeTv.getText().toString();
                break;
            case LUNCH:
                selectedTime = binding.lunchTimeTv.getText().toString();
                break;
            case SECOND_SNACK:
                selectedTime = binding.secondSnackTimeTv.getText().toString();
                break;
            case DINER:
                selectedTime = binding.dinerTimeTv.getText().toString();
                break;
        }

        String[] separated = selectedTime.split(":");
        String hours = separated[0];
        String minutes = separated[1];

        hoursPicker.setValue(Integer.valueOf(hours));
        minutesPicker.setValue(Integer.valueOf(minutes));

        Window window = pickTimeDialog.getWindow();
        window.setGravity(Gravity.TOP);
        window.getAttributes().windowAnimations = R.style.DialogAnimationUpToDown;
        window.setBackgroundDrawableResource(android.R.color.white);
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (alarmType){
                    case BREAKFAST:
                        binding.breakfastTimeTv.setText(String.valueOf(hoursPicker.getValue()) + ":" + String.valueOf(minutesPicker.getValue()) );
                        if(binding.breakfastSw.isChecked()){
                            PrefsManager.getInstance(AlarmsActivity.this).putKeyAlarmBreakfast(binding.breakfastTimeTv.getText().toString());
                        }
                        break;
                    case FIRST_SNACK:
                        binding.firstSnackTimeTv.setText(String.valueOf(hoursPicker.getValue()) + ":" + String.valueOf(minutesPicker.getValue()) );
                        if(binding.firstSnackSw.isChecked()){
                            PrefsManager.getInstance(AlarmsActivity.this).putKeyAlarmFirstSnack(binding.firstSnackTimeTv.getText().toString());
                        }
                        break;
                    case LUNCH:
                        binding.lunchTimeTv.setText(String.valueOf(hoursPicker.getValue()) + ":" + String.valueOf(minutesPicker.getValue()) );
                        if(binding.lunchSw.isChecked()){
                            PrefsManager.getInstance(AlarmsActivity.this).putKeyAlarmLunch(binding.lunchTimeTv.getText().toString());
                        }
                        break;
                    case SECOND_SNACK:
                        binding.secondSnackTimeTv.setText(String.valueOf(hoursPicker.getValue()) + ":" + String.valueOf(minutesPicker.getValue()) );
                        if(binding.secondSnackSw.isChecked()){
                            PrefsManager.getInstance(AlarmsActivity.this).putKeyAlarmSecondSnack(binding.secondSnackTimeTv.getText().toString());
                        }
                        break;
                    case DINER:
                        binding.dinerTimeTv.setText(String.valueOf(hoursPicker.getValue()) + ":" + String.valueOf(minutesPicker.getValue()) );
                        if(binding.dinerSw.isChecked()){
                            PrefsManager.getInstance(AlarmsActivity.this).putKeyAlarmDiner(binding.dinerTimeTv.getText().toString());
                        }
                        break;
                }
                pickTimeDialog.dismiss();
            }
        });
        pickTimeDialog.show();
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
    


}