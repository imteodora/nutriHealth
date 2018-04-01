package com.nutrihealth.activities;

import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.graphics.Typeface;
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
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.TextSwitcher;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import com.nutrihealth.R;
import com.nutrihealth.base.BaseActivity;
import com.nutrihealth.databinding.ActivityAlarmsBinding;
import com.nutrihealth.views.CustomToolbar;

import static android.text.Spanned.SPAN_INCLUSIVE_INCLUSIVE;

/**
 * Created by Teo on 4/1/2018.
 */

public class AlarmsActivity extends BaseActivity {

    private ActivityAlarmsBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(AlarmsActivity.this, R.layout.activity_alarms);

        setUpViews();
    }

    private void setUpViews() {

        binding.setAlarmsActivity(this);
        binding.alarmsToolbar.setOnBackButtonPressedListener(new CustomToolbar.OnBackButtonPressedListener() {
            @Override
            public void onBackButtonPressed() {
                finish();
            }
        });

        setCurrentTestWatcher();
        startTextAnimation();

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
    
    


}
