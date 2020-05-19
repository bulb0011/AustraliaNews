package com.ruanyun.australianews.widget;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.view.*;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * @author hdl
 * @description 样式由外部传递
 * @date 2019/2/22
 */
public class CommonDialog extends AlertDialog {
    private Context mContext;

    public CommonDialog(@NonNull Context context) {
        super(context);
        mContext = context;
    }

    public CommonDialog showDialog(@LayoutRes int layoutId) {
        super.show();
        super.setContentView(layoutId);
        Window window = getWindow();
        if (window != null) {
            window.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#00000000")));
            WindowManager.LayoutParams params = window.getAttributes();
            params.width = ViewGroup.LayoutParams.WRAP_CONTENT;
            params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
            params.gravity = Gravity.CENTER;
            window.setAttributes(params);
        }
        return this;
    }

    /**
     * desc: 默认会将View转换成TextView
     *
     * @param componentId 控件ID
     * @param text        文字内容
     **/
    public CommonDialog setText(@IdRes int componentId, CharSequence text) {
        ((TextView) findViewById(componentId)).setText(text);
        return this;
    }

    /**
     * desc: 默认会将View转换成ImageView
     *
     * @param componentId 控件ID
     * @param resId        资源id
     **/
    public CommonDialog setImageResource(@IdRes int componentId, int resId) {
        ((ImageView) findViewById(componentId)).setImageResource(resId);
        return this;
    }

    /**
     * desc: 设置View显示隐藏
     *
     * @param componentId 控件ID
     * @param visibility  显示隐藏
     **/
    public CommonDialog setVisibility(@IdRes int componentId, int visibility) {
        findViewById(componentId).setVisibility(visibility);
        return this;
    }

    /**
     * desc: 设置点击事件
     *
     * @param viewId   控件ID
     * @param listener 监听事件
     **/
    public CommonDialog setOnClickListener(@IdRes int viewId, View.OnClickListener listener) {
        findViewById(viewId).setOnClickListener(listener);
        return this;
    }

    /**
     * desc: 注册点击事件 点击隐藏Dialog
     *
     * @param viewId 控件ID
     **/
    public CommonDialog setOnDismissListener(@IdRes int viewId) {
        findViewById(viewId).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        return this;
    }
}

