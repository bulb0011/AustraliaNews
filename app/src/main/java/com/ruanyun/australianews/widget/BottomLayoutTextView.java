package com.ruanyun.australianews.widget;

import android.content.Context;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;
import android.view.View;

public class BottomLayoutTextView extends AppCompatTextView {

    private onCheckChangedListener mListener;

    public BottomLayoutTextView(Context context) {
        this(context, null);
    }

    public BottomLayoutTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BottomLayoutTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    public void setChecked(boolean flag) {
        setSelected(flag);
        if (flag) mListener.onCheckChanged(true, this);
        else mListener.onCheckChanged(false, this);

    }

    public void removeCallback() {
        mListener = null;
    }

    public void setOnCheckChangedListener(onCheckChangedListener changed) {
        this.mListener = changed;
    }

    public interface onCheckChangedListener {
        void onCheckChanged(boolean checked, View view);
    }

}
