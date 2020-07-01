package com.ruanyun.australianews.widget;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.PopupWindow;

import com.ruanyun.australianews.R;

import org.greenrobot.eventbus.EventBus;

import java.lang.reflect.Field;


/**
 *
 * Created by hdl on 2018/5/19
 */
public class BasePopWindow extends PopupWindow implements PopupWindow.OnDismissListener {
    protected Context mContext;
    protected boolean isBackgroundAlphta = true;

    @Override
    public void onDismiss() {
        if (isBackgroundAlphta) {
            backgroundAlpha(1f);
        }
    }

    protected void initView() {
        setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(mContext, R.color.transparent)));
        setFocusable(true);
        setInputMethodMode(PopupWindow.INPUT_METHOD_NEEDED);
        setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        setOnDismissListener(this);
    }

    protected void showTop(View parent) {
        showTop(parent, 0.6f);
    }

    protected void showDown(View parent) {
        showDown(parent, 0.6f);
    }

    protected void showTop(View parent, float alphta) {
        showAtLocation(parent, Gravity.TOP, 0, 0);
        isBgAlphta(alphta);
    }

    protected void showDown(View parent, float alphta) {
        showAsDropDown(parent, 0, 0);
        isBgAlphta(alphta);
    }

    private void isBgAlphta(float alphta) {
        if (isBackgroundAlphta) {
            backgroundAlpha(alphta);
            //   5.1  有效
            ((Activity) mContext).getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        }
    }

    public void closeKeyboard() {
        InputMethodManager inputMethodManager =
                (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
    }

    public void setApplicationSubPanel() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            try {

                Field mLayoutInScreen = PopupWindow.class.getDeclaredField("mWindowLayoutType");
                mLayoutInScreen.setAccessible(true);
                mLayoutInScreen.set(this, WindowManager.LayoutParams.TYPE_APPLICATION_SUB_PANEL);

            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            }
        }

    }

    /**
     * 设置添加屏幕的背景透明度
     *
     * @param bgAlpha
     */
    public void backgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = ((Activity) mContext).getWindow().getAttributes();
        lp.alpha = bgAlpha; //0.0-1.0
        ((Activity) mContext).getWindow().setAttributes(lp);
    }

    protected void registerBus() {
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
    }

    protected void unRegisterBus() {
        EventBus.getDefault().unregister(this);
    }

    public void setBackgroundAlphta(boolean backgroundAlphta) {
        isBackgroundAlphta = backgroundAlphta;
    }
}
