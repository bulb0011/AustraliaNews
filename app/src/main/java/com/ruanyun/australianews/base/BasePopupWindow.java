package com.ruanyun.australianews.base;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.ColorRes;
import android.view.View;
import android.view.WindowManager;
import android.widget.PopupWindow;

/**
 * Description:
 * author: zhangsan on 17/1/16 下午2:20.
 */

public class BasePopupWindow extends PopupWindow implements PopupWindow.OnDismissListener {

    protected String TAG = getClass().getSimpleName();
    protected Context mContext;
    protected boolean isBackgroudAlphta = true;
    protected final float ALPHA = 0.3f;


    @Override
    public void onDismiss() {
        if (isBackgroudAlphta) {
            backgroundAlpha(1f);
        }
    }

    protected void setBackGroud(@ColorRes int color) {
        setBackgroundDrawable(new ColorDrawable(mContext.getResources().getColor(color)));
    }

    protected void setBackGroud(@ColorRes int color, int altha) {
        ColorDrawable colorDrawable = new ColorDrawable(mContext.getResources().getColor(color));
        colorDrawable.setAlpha(altha);
        setBackgroundDrawable(colorDrawable);
    }

    public void show(View parent) {
        show(parent, 0.6f);
    }

    public void show(View parent, float alphta) {
        if (isBackgroudAlphta) {
            backgroundAlpha(alphta);
            //   5.1  有效
            ((Activity) mContext).getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
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
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.MATCH_PARENT;
        ((Activity) mContext).getWindow().setAttributes(lp);
    }
}
