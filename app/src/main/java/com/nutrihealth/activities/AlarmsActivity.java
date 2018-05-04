package com.nutrihealth.activities;

import android.app.AlarmManager;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.nutrihealth.R;
import com.nutrihealth.base.BaseActivity;
import com.nutrihealth.broadcastReceivers.AlarmBroadcastReceiver;
import com.nutrihealth.constants.Constants;
import com.nutrihealth.databinding.ActivityAlarmsBinding;
import com.nutrihealth.model.Alarm;
import com.nutrihealth.model.ProfileInfos;
import com.nutrihealth.prefs.PrefsManager;
import com.nutrihealth.repository.Resource;
import com.nutrihealth.utils.InputValidator;
import com.nutrihealth.views.CustomToolbar;

import java.util.Calendar;
import java.util.Date;

import static android.text.Spanned.SPAN_INCLUSIVE_INCLUSIVE;

/**
 * Created by Teo on 4/1/2018.
 */

public class AlarmsActivity extends BaseActivity {

    public enum Alarms {
        BREAKFAST,
        FIRST_SNACK,
        LUNCH,
        SECOND_SNACK,
        DINER
    }

    private ActivityAlarmsBinding binding;
    private DatabaseReference mDatabase;
    private FirebaseUser user;

    private String breakfastAlarm = "0";
    private String firstSnackAlarm = "0";
    private String lunchAlarm = "0";
    private String secondSnackAlarm = "0";
    private String dinnerAlarm = "0";
    private String waterAlarm = "0";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(AlarmsActivity.this, R.layout.activity_alarms);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        user = FirebaseAuth.getInstance().getCurrentUser();

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
        getUserAlarms();
        setColorsToSwitches();


    }

    private void setColorsToSwitches() {

        ColorStateList thumbStates = new ColorStateList(
                new int[][]{
                        new int[]{-android.R.attr.state_enabled},
                        new int[]{android.R.attr.state_checked},
                        new int[]{}
                },
                new int[]{
                        Color.BLUE,
                        ContextCompat.getColor(AlarmsActivity.this, R.color.light_blue),
                        ContextCompat.getColor(AlarmsActivity.this, R.color.gray_light)
                }
        );
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            binding.breakfastSw.setThumbTintList(thumbStates);
            binding.firstSnackSw.setThumbTintList(thumbStates);
            binding.lunchSw.setThumbTintList(thumbStates);
            binding.secondSnackSw.setThumbTintList(thumbStates);
            binding.dinerSw.setThumbTintList(thumbStates);
        }
        if (Build.VERSION.SDK_INT >= 24) {
            ColorStateList trackStates = new ColorStateList(
                    new int[][]{
                            new int[]{-android.R.attr.state_enabled},
                            new int[]{}
                    },
                    new int[]{
                            Color.GRAY,
                            Color.LTGRAY
                    }
            );
            binding.breakfastSw.setTrackTintList(trackStates);
            binding.firstSnackSw.setTrackTintList(trackStates);
            binding.lunchSw.setTrackTintList(trackStates);
            binding.secondSnackSw.setTrackTintList(trackStates);
            binding.dinerSw.setTrackTintList(trackStates);

            binding.breakfastSw.setTrackTintMode(PorterDuff.Mode.OVERLAY);
            binding.firstSnackSw.setTrackTintMode(PorterDuff.Mode.OVERLAY);
            binding.lunchSw.setTrackTintMode(PorterDuff.Mode.OVERLAY);
            binding.secondSnackSw.setTrackTintMode(PorterDuff.Mode.OVERLAY);
            binding.dinerSw.setTrackTintMode(PorterDuff.Mode.OVERLAY);
        }

    }

    private void populateFields(Alarm alarm){
        String alarmBreackfast = alarm.getBreakfast();
        String alarmFirstSnack = alarm.getFirstSnack();
        String alarmLunch = alarm.getLunch();
        String alarmSecondSnack = alarm.getSecondSnack();
        String alarmDiner = alarm.getDinner();
        String alarmWater = alarm.getWater();

        if (!alarmBreackfast.equals("0")) {
            binding.breakfastTimeTv.setText(alarmBreackfast);
            breakfastAlarm = alarmBreackfast;
            binding.breakfastSw.setChecked(true);
        }

        if (!alarmFirstSnack.equals("0")) {
            binding.firstSnackTimeTv.setText(alarmFirstSnack);
            firstSnackAlarm = alarmFirstSnack;
            binding.firstSnackSw.setChecked(true);
        }

        if (!alarmLunch.equals("0")) {
            binding.lunchTimeTv.setText(alarmLunch);
            lunchAlarm = alarmLunch;
            binding.lunchSw.setChecked(true);
        }
        if (!alarmSecondSnack.equals("0")) {
            binding.secondSnackTimeTv.setText(alarmSecondSnack);
            secondSnackAlarm = alarmSecondSnack;
            binding.secondSnackSw.setChecked(true);
        }

        if (!alarmDiner.equals("0")) {
            binding.dinerTimeTv.setText(alarmDiner);
            dinnerAlarm = alarmDiner;
            binding.dinerSw.setChecked(true);
        }

        if (!alarmWater.equals("0")) {
            waterAlarm = alarmWater;
            binding.waterSw.setChecked(true);
        }

        binding.waterSw.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {

                    waterAlarm = "1";
                    setWaterAlarm();
                    updateAlarmsInDatabase();
                    return;
                }
                waterAlarm = "0";
                updateAlarmsInDatabase();
                cancelWaterAlarm();
            }
        });
        binding.breakfastSw.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {

                    String selectedTime = binding.breakfastTimeTv.getText().toString();
                    breakfastAlarm = selectedTime;
                    String[] separated = selectedTime.split(":");
                    String hours = separated[0];
                    String minutes = separated[1];

                    setAlarm(Alarms.BREAKFAST, Integer.parseInt(hours), Integer.parseInt(minutes));
                    updateAlarmsInDatabase();
                    return;
                }
                breakfastAlarm = "0";
                updateAlarmsInDatabase();
                cancelAlarm(Alarms.BREAKFAST);
            }
        });

        binding.firstSnackSw.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    String selectedTime = binding.firstSnackTimeTv.getText().toString();
                    firstSnackAlarm = selectedTime;
                    String[] separated = selectedTime.split(":");
                    String hours = separated[0];
                    String minutes = separated[1];

                    setAlarm(Alarms.FIRST_SNACK, Integer.parseInt(hours), Integer.parseInt(minutes));
                    updateAlarmsInDatabase();
                    return;
                }
                cancelAlarm(Alarms.FIRST_SNACK);
                firstSnackAlarm = "0";
                updateAlarmsInDatabase();
            }
        });

        binding.lunchSw.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    String selectedTime = binding.lunchTimeTv.getText().toString();
                    lunchAlarm = selectedTime;
                    String[] separated = selectedTime.split(":");
                    String hours = separated[0];
                    String minutes = separated[1];

                    setAlarm(Alarms.LUNCH, Integer.parseInt(hours), Integer.parseInt(minutes));
                    updateAlarmsInDatabase();
                    return;
                }
                cancelAlarm(Alarms.LUNCH);
                lunchAlarm = "0";
                updateAlarmsInDatabase();
            }
        });

        binding.secondSnackSw.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    String selectedTime = binding.secondSnackTimeTv.getText().toString();
                    secondSnackAlarm = selectedTime;
                    String[] separated = selectedTime.split(":");
                    String hours = separated[0];
                    String minutes = separated[1];

                    setAlarm(Alarms.SECOND_SNACK, Integer.parseInt(hours), Integer.parseInt(minutes));
                    updateAlarmsInDatabase();
                    return;
                }
                cancelAlarm(Alarms.SECOND_SNACK);
                secondSnackAlarm = "0";
                updateAlarmsInDatabase();
            }
        });

        binding.dinerSw.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    String selectedTime = binding.dinerTimeTv.getText().toString();
                    dinnerAlarm = selectedTime;
                    String[] separated = selectedTime.split(":");
                    String hours = separated[0];
                    String minutes = separated[1];

                    setAlarm(Alarms.DINER, Integer.parseInt(hours), Integer.parseInt(minutes));
                    updateAlarmsInDatabase();
                    return;
                }
                cancelAlarm(Alarms.DINER);
                dinnerAlarm = "0";
                updateAlarmsInDatabase();
            }
        });


    }

    private void updateAlarmsInDatabase() {

        //build child
        mDatabase.child("alarms").child(user.getUid()).setValue(new Alarm(breakfastAlarm,firstSnackAlarm,lunchAlarm,secondSnackAlarm,dinnerAlarm,waterAlarm));
    }

    private void getUserAlarms() {

        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();


        mDatabase.child("alarms").child(user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Alarm userAlarms = dataSnapshot.getValue(Alarm.class);
                populateFields(userAlarms);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }

        });


    }

    private void setWaterAlarm(){
        Intent myIntent = new Intent(getBaseContext(), AlarmBroadcastReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                getBaseContext(), Constants.ALARM_WATER_CODE, myIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        alarmManager.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP,
                SystemClock.elapsedRealtime() + 2*60*1000,
                2*60*1000,
                pendingIntent);

    }

    private void cancelWaterAlarm(){
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent myIntent = new Intent(getBaseContext(), AlarmBroadcastReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                getBaseContext(), Constants.ALARM_WATER_CODE, myIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        alarmManager.cancel(pendingIntent);
    }

    private void setAlarm(Alarms alarmType, int hourOfDay, int minute) {

        int requestCode = 1;
        switch (alarmType) {
            case BREAKFAST:
                requestCode = Constants.ALARM_BREAKFAST_CODE;
                break;
            case FIRST_SNACK:
                requestCode = Constants.ALARM_FIRST_SNACK_CODE;
                break;
            case LUNCH:
                requestCode = Constants.ALARM_LUNCH_CODE;
                break;
            case SECOND_SNACK:
                requestCode = Constants.ALARM_SECOND_SNACK_CODE;
                break;
            case DINER:
                requestCode = Constants.ALARM_DINER_CODE;
                break;
        }

        Intent myIntent = new Intent(getBaseContext(), AlarmBroadcastReceiver.class);
        myIntent.putExtra(Constants.REQUEST_CODE_FOR_ALARM, requestCode);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                getBaseContext(), requestCode, myIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);


        Calendar calNow = Calendar.getInstance();
        Calendar calSet = (Calendar) calNow.clone();

        calSet.set(Calendar.HOUR_OF_DAY, hourOfDay);
        calSet.set(Calendar.MINUTE, minute);
        calSet.set(Calendar.SECOND, 0);
        calSet.set(Calendar.MILLISECOND, 0);

        if (calSet.compareTo(calNow) <= 0) {
            //Today Set time passed, count to tomorrow
            calSet.add(Calendar.DATE, 1);
        }

        alarmManager.set(AlarmManager.RTC_WAKEUP, calSet.getTimeInMillis(), pendingIntent);
    }

    private void cancelAlarm(Alarms alarmType) {

        int requestCode = 1;
        switch (alarmType) {
            case BREAKFAST:
                requestCode = Constants.ALARM_BREAKFAST_CODE;
                break;
            case FIRST_SNACK:
                requestCode = Constants.ALARM_FIRST_SNACK_CODE;
                break;
            case LUNCH:
                requestCode = Constants.ALARM_LUNCH_CODE;
                break;
            case SECOND_SNACK:
                requestCode = Constants.ALARM_SECOND_SNACK_CODE;
                break;
            case DINER:
                requestCode = Constants.ALARM_DINER_CODE;
                break;
        }

        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent myIntent = new Intent(getBaseContext(), AlarmBroadcastReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                getBaseContext(), requestCode, myIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        alarmManager.cancel(pendingIntent);
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
                t.setTextColor(ContextCompat.getColor(AlarmsActivity.this, R.color.white));

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

    public void onSelectTimePressed(final Alarms alarmType) {
        final Dialog pickTimeDialog = new Dialog(AlarmsActivity.this);
        pickTimeDialog.setContentView(R.layout.dialog_choose_alarm_time);

        Button saveBtn = (Button) pickTimeDialog.findViewById(R.id.save_button);
        final NumberPicker hoursPicker = (NumberPicker) pickTimeDialog.findViewById(R.id.hour_picker);
        final NumberPicker minutesPicker = (NumberPicker) pickTimeDialog.findViewById(R.id.minutes_picker);
        hoursPicker.setMaxValue(23);
        hoursPicker.setMinValue(00);

        minutesPicker.setMaxValue(59);
        minutesPicker.setMinValue(00);

        setDividerColor(hoursPicker, ContextCompat.getColor(AlarmsActivity.this, R.color.light_blue));
        setDividerColor(minutesPicker, ContextCompat.getColor(AlarmsActivity.this, R.color.light_blue));

        String selectedTime = "";

        switch (alarmType) {
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
                switch (alarmType) {
                    case BREAKFAST:
                        binding.breakfastTimeTv.setText(String.valueOf(hoursPicker.getValue()) + ":" + String.valueOf(minutesPicker.getValue()));
                        if (binding.breakfastSw.isChecked()) {
                            breakfastAlarm = binding.breakfastTimeTv.getText().toString() ;
                            updateAlarmsInDatabase();
                            setAlarm(alarmType,hoursPicker.getValue(),minutesPicker.getValue());
                        }
                        break;
                    case FIRST_SNACK:
                        binding.firstSnackTimeTv.setText(String.valueOf(hoursPicker.getValue()) + ":" + String.valueOf(minutesPicker.getValue()));
                        if (binding.firstSnackSw.isChecked()) {
                            firstSnackAlarm = binding.firstSnackTimeTv.getText().toString() ;
                            updateAlarmsInDatabase();
                            setAlarm(alarmType,hoursPicker.getValue(),minutesPicker.getValue());
                        }
                        break;
                    case LUNCH:
                        binding.lunchTimeTv.setText(String.valueOf(hoursPicker.getValue()) + ":" + String.valueOf(minutesPicker.getValue()));
                        if (binding.lunchSw.isChecked()) {
                            lunchAlarm = binding.lunchTimeTv.getText().toString() ;
                            updateAlarmsInDatabase();
                            setAlarm(alarmType,hoursPicker.getValue(),minutesPicker.getValue());
                        }
                        break;
                    case SECOND_SNACK:
                        binding.secondSnackTimeTv.setText(String.valueOf(hoursPicker.getValue()) + ":" + String.valueOf(minutesPicker.getValue()));
                        if (binding.secondSnackSw.isChecked()) {
                            secondSnackAlarm = binding.secondSnackTimeTv.getText().toString() ;
                            updateAlarmsInDatabase();
                            setAlarm(alarmType,hoursPicker.getValue(),minutesPicker.getValue());
                        }
                        break;
                    case DINER:
                        binding.dinerTimeTv.setText(String.valueOf(hoursPicker.getValue()) + ":" + String.valueOf(minutesPicker.getValue()));
                        if (binding.dinerSw.isChecked()) {
                            dinnerAlarm = binding.dinerTimeTv.getText().toString() ;
                            updateAlarmsInDatabase();
                            setAlarm(alarmType,hoursPicker.getValue(),minutesPicker.getValue());
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
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
                break;
            }
        }
    }


}
