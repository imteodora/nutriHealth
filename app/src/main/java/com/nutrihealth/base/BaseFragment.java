package com.nutrihealth.base;


import android.app.Dialog;
import android.os.CountDownTimer;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nutrihealth.R;

/**
 * Created by Teodora on 12.04.2018.
 */

public abstract class BaseFragment extends Fragment {

    public abstract String getFragmentTag();

    private Dialog customDialog;

    protected void showCustomDialog(String title, String info, BaseActivity.DialogType type, View.OnClickListener listener) {

        customDialog = new Dialog(getContext(), R.style.DialogTheme);

        LayoutInflater inflater = getLayoutInflater();
        View dialoglayout = inflater.inflate(R.layout.dialog_custom, null);

        TextView titleTv = dialoglayout.findViewById(R.id.title_tv);
        TextView infoTv = dialoglayout.findViewById(R.id.info_tv);
        ImageView imageView = dialoglayout.findViewById(R.id.image);
        final RelativeLayout parentView = dialoglayout.findViewById(R.id.parent);

        if (listener != null) {
            parentView.setOnClickListener(listener);
        }


        if (title != null) {
            titleTv.setText(title);
        } else {
            titleTv.setVisibility(View.GONE);
        }

        if (info != null) {
            infoTv.setText(info);
        }

        switch (type) {
            case ERROR:
                imageView.setImageResource(R.drawable.ic_error_white_24dp);
                break;
            case INFO:
                imageView.setImageResource(R.drawable.ic_info_white_24dp);
                parentView.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.dialog_blue_background));
                break;
            case SUCCESS:
                imageView.setImageResource(R.drawable.ic_check_circle_white_24dp);
                parentView.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.dialog_green_background));
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
                if (customDialog.isShowing()) {
                    customDialog.dismiss();
                    parentView.performClick();
                }
            }

        };

        timer.start();
    }

    protected void dismissCustomDialog() {
        customDialog.dismiss();
    }


}
