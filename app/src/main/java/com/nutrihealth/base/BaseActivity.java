package com.nutrihealth.base;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nutrihealth.R;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

/**
 * Created by Teo on 3/23/2018.
 */

public class BaseActivity extends AppCompatActivity {

    private Dialog customDialog;

    public enum DialogType {
        ERROR,
        INFO,
        SUCCESS
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        super.setContentView(layoutResID);


    }


    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    protected void showCustomDialog(String title, String info, DialogType type, View.OnClickListener listener){


        if(this.isFinishing()){
            return;
        }

        customDialog = new Dialog(this, R.style.DialogTheme);

        LayoutInflater inflater = getLayoutInflater();
        View dialoglayout = inflater.inflate(R.layout.dialog_custom, null);

        TextView titleTv = dialoglayout.findViewById(R.id.title_tv);
        TextView infoTv = dialoglayout.findViewById(R.id.info_tv);
        ImageView imageView = dialoglayout.findViewById(R.id.image);
        final RelativeLayout parentView = dialoglayout.findViewById(R.id.parent);

        if(listener != null){
            parentView.setOnClickListener(listener);
        }


        if(title != null){
            titleTv.setText(title);
        }else{
            titleTv.setVisibility(View.GONE);
        }

        if(info!=null){
            infoTv.setText(info);
        }

        switch (type){
            case ERROR:
                imageView.setImageResource( R.drawable.ic_error_white_24dp);
                break;
            case INFO:
                imageView.setImageResource( R.drawable.ic_info_white_24dp);
                parentView.setBackground(ContextCompat.getDrawable(BaseActivity.this,R.drawable.dialog_blue_background));
                break;
            case SUCCESS:
                imageView.setImageResource( R.drawable.ic_check_circle_white_24dp);
                parentView.setBackground(ContextCompat.getDrawable(BaseActivity.this,R.drawable.dialog_green_background));
                break;
            default:
                break;
        }

        customDialog.setContentView(dialoglayout);
        customDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimationUpAndUp;
        customDialog.show();

        CountDownTimer timer = new CountDownTimer(4000, 1000) {

            public void onTick(long millisUntilFinished) {

            }

            public void onFinish() {
                if(customDialog.isShowing()){
                    customDialog.dismiss();
                    parentView.performClick();
                }
            }

        };

        timer.start();
    }

    protected void dismissCustomDialog(){
        customDialog.dismiss();
    }

    public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager =
                (InputMethodManager) activity.getSystemService(
                        Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(
                activity.getCurrentFocus().getWindowToken(), 0);
    }

    public void setupUI(View view) {
        // Set up touch listener for non-text box views to hide keyboard.
        if (!(view instanceof EditText)) {
            view.setOnTouchListener(new View.OnTouchListener() {
                public boolean onTouch(View v, MotionEvent event) {
                    hideSoftKeyboard(BaseActivity.this);
                    return false;
                }
            });
        }
        //If a layout container, iterate over children and seed recursion.
        if (view instanceof ViewGroup) {
            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
                View innerView = ((ViewGroup) view).getChildAt(i);
                setupUI(innerView);
            }
        }
    }
}
