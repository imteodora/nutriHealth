package com.nutrihealth.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.databinding.DataBindingUtil;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;

import com.nutrihealth.R;
import com.nutrihealth.databinding.ViewToolbarBinding;

/**
 * Created by Teo on 3/30/2018.
 */

public class CustomToolbar extends Toolbar {

    public interface OnBackButtonPressedListener {
        void onBackButtonPressed();
    }

    private OnBackButtonPressedListener onBackButtonPressedListener = null;
    private ViewToolbarBinding binding;


    public CustomToolbar(Context context) {

        this(context, null);
        init(context);
    }

    public CustomToolbar(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.CustomToolbar);
        String headerStr      = typedArray.getString(R.styleable.CustomToolbar_title);
        binding.toolbarTitleTv.setText(headerStr);
        typedArray.recycle();

    }

    public void init(Context context) {
        binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.view_toolbar, this, true);
        binding.setCustomToolbar(this);

    }

    public void setOnBackButtonPressedListener(OnBackButtonPressedListener onBackButtonPressedListener) {
        CustomToolbar.this.onBackButtonPressedListener = onBackButtonPressedListener;
        binding.setShowBackIcon(true);
    }

    public void hideBackContainer() {
        binding.setShowBackIcon(false);
    }

    public void onBackPressed(View view) {
        if (onBackButtonPressedListener != null) {
            onBackButtonPressedListener.onBackButtonPressed();
        }
    }
}
