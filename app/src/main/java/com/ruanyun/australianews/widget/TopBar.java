package com.ruanyun.australianews.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ruanyun.imagepicker.Util;
import com.ruanyun.imagepicker.base.NoDoubleClicksListener;
import com.ruanyun.australianews.R;

/**
 * @author lisi
 * @description xml 简单版topBar
 * @date 2018/8/13
 */
public class TopBar extends RelativeLayout {

    // 标题默认字体颜色
    private static final int DEFAULT_TEXT_COLOR = 0xff000000;// 默认字体颜色
    private static final int DEFAULT_BG_COLOR = 0xffffffff;// 默认背景颜色

    private ImageView topBarLeftImg;
    private TextView topBarTitle;
    private ImageView topBarRightImg;
    private TextView topBarRightTitle;
    private View topBarViewLine;

    private int topBarLeftImageSrc, topBarRightImageSrc, topBarBgColor;
    private boolean isAdaptationStatusBar, topBarTitleEnable, topBarLeftImageEnable, topBarRightImageEnable, topBarRightTextEnable, topBarViewLineEnable;

    public TopBar(Context context) {
        this(context, null);
    }

    public TopBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        inflate(getContext(), R.layout.layout_topbar, this);
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.TopBar, 0, 0);
        String titleStr = "";
        String rightTitleStr = "";
        int titleStrColor = DEFAULT_TEXT_COLOR;
        if (ta != null) {
            isAdaptationStatusBar = ta.getBoolean(R.styleable.TopBar_topBarAdaptationStatusBar, false);
            titleStr = ta.getString(R.styleable.TopBar_topBarTitleText);
            titleStrColor = ta.getColor(R.styleable.TopBar_topBarTitleTextColor, DEFAULT_TEXT_COLOR);
            topBarTitleEnable = ta.getBoolean(R.styleable.TopBar_topBarTitleEnable, true);
            topBarLeftImageEnable = ta.getBoolean(R.styleable.TopBar_topBarLeftImageEnable, true);
            topBarLeftImageSrc = ta.getResourceId(R.styleable.TopBar_topBarLeftImageSrc, R.drawable.nav_btn_back);
            topBarRightImageEnable = ta.getBoolean(R.styleable.TopBar_topBarRightImageEnable, true);
            topBarRightImageSrc = ta.getResourceId(R.styleable.TopBar_topBarRightImageSrc, 0);
            topBarBgColor = ta.getColor(R.styleable.TopBar_topBarBgColor, DEFAULT_BG_COLOR);
            rightTitleStr = ta.getString(R.styleable.TopBar_topBarRightText);
            topBarRightTextEnable = ta.getBoolean(R.styleable.TopBar_topBarRightTextEnable, false);
            topBarViewLineEnable = ta.getBoolean(R.styleable.TopBar_topBarViewLineEnable, true);

            ta.recycle();
        }
        if (isAdaptationStatusBar) {
            setStatusBarHeightPaddingTop(this);
        }
        topBarLeftImg = findViewById(R.id.topbar_left_img);
        topBarTitle = findViewById(R.id.topbar_title);
        topBarRightImg = findViewById(R.id.topbar_right_img);
        topBarRightTitle = findViewById(R.id.topbar_right_text);
        topBarViewLine = findViewById(R.id.view_line);

        topBarTitle.setText(titleStr);
        topBarTitle.setTextColor(titleStrColor);
        topBarTitle.setVisibility(topBarTitleEnable ? VISIBLE : GONE);
        topBarRightTitle.setText(rightTitleStr);
        topBarRightTitle.setTextColor(titleStrColor);
        topBarRightTitle.setVisibility(topBarRightTextEnable ? VISIBLE : GONE);

        topBarLeftImg.setImageResource(topBarLeftImageSrc);
        topBarLeftImg.setVisibility(topBarLeftImageEnable ? VISIBLE : GONE);
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) topBarTitle.getLayoutParams();
        if (topBarLeftImageEnable) {
            layoutParams.setMargins(0, 0, 0, 0);
        } else {
            layoutParams.setMargins(Util.dp2px(context, 14), 0, 0, 0);
        }
        topBarRightImg.setImageResource(topBarRightImageSrc);
        topBarRightImg.setVisibility(topBarRightImageEnable ? VISIBLE : GONE);
        topBarViewLine.setVisibility(topBarViewLineEnable ? VISIBLE : GONE);
        onTopBarViewClick(topBarLeftImg);
        onTopBarViewClick(topBarRightImg);
        onTopBarViewClick(topBarRightTitle);
        setBackgroundColor(topBarBgColor);
    }

    public TopBar setRightTitleText(String titleText) {
        topBarRightTitle.setText(titleText);
        return this;
    }

    public TopBar setTitleText(String title) {
        topBarTitle.setText(title);
        return this;
    }

    public TopBar setLeftImg(int resId) {
        topBarLeftImg.setImageResource(resId);
        return this;
    }

    public TopBar setRightImg(int resId) {
        topBarRightImg.setImageResource(resId);
        return this;
    }

    public TopBar setRightTitleEnable(boolean enable) {
        topBarRightTitle.setVisibility(enable ? VISIBLE : GONE);
        return this;
    }

    public TopBar setTitleEnable(boolean enable) {
        topBarTitle.setVisibility(enable ? VISIBLE : GONE);
        return this;
    }

    public TopBar setLeftImgEnable(boolean enable) {
        topBarLeftImg.setVisibility(enable ? VISIBLE : GONE);
        return this;
    }

    public TopBar setRightImgEnable(boolean enable) {
        topBarRightImg.setVisibility(enable ? VISIBLE : GONE);
        return this;
    }

    public TopBar setRightTextEnable(boolean enable) {
        topBarRightTitle.setVisibility(enable ? VISIBLE : GONE);
        return this;
    }

    public ImageView getTopBarLeftImg() {
        return topBarLeftImg;
    }

    public TextView getTopBarTitle() {
        return topBarTitle;
    }

    public ImageView getTopBarRightImg() {
        return topBarRightImg;
    }

    public TextView getTopBarRightTitle() {
        return topBarRightTitle;
    }

    public void onTopBarViewClick(View v) {
        v.setOnClickListener(new NoDoubleClicksListener() {
            @Override
            public void noDoubleClick(View v) {
                if (topBarClickListener != null) topBarClickListener.onTopBarViewClick(v);
            }
        });
    }

    public void setTopBarClickListener(onTopBarClickListener topBarClickListener) {
        this.topBarClickListener = topBarClickListener;
    }

    onTopBarClickListener topBarClickListener;

    public interface onTopBarClickListener {
        void onTopBarViewClick(View v);
    }


    /**
     * 添加一个状态栏高度的paddingTop
     */
    public void setStatusBarHeightPaddingTop(View view) {
        int height = getStatusHeight(view.getContext());
        view.setPadding(view.getPaddingLeft(), view.getPaddingTop() + height, view.getPaddingRight(), view
                .getPaddingBottom());
    }

    /**
     * 获得状态栏的高度
     */
    public int getStatusHeight(Context context) {
        int statusHeight = -1;
        try {
            @SuppressLint("PrivateApi")
            Class<?> clazz = Class.forName("com.android.internal.R$dimen");
            Object object = clazz.newInstance();
            int height = Integer.parseInt(clazz.getField("status_bar_height")
                    .get(object)
                    .toString());
            statusHeight = context.getResources().getDimensionPixelSize(height);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return statusHeight;
    }

}
