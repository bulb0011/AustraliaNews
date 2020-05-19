package com.ruanyun.australianews.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ruanyun.australianews.R;
import com.ruanyun.australianews.base.refreshview.view.IRefreshListLoadViewFactory;


/****
 * 自定义加载和显示数据加载失败view 需要绑定显示的view
 *
 * @author jery 2015年10月21日15:43:53
 */
public class RYEmptyView extends RelativeLayout implements IRefreshListLoadViewFactory.IEmptyView {
    private TextView tvTips;
    private Button btnReload;
    public View bindView;// 被绑定的view
    public ProgressBar progressBar;
    public boolean isShowLoading = false;
    private ImageView imgStatus;

    public RYEmptyView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public RYEmptyView(Context context) {
        super(context);
        initView(context);
    }

    private void initView(Context context) {
        inflate(context, R.layout.layout_empty_view, this);
        imgStatus = findViewById(R.id.emptyview_img);
        progressBar = findViewById(R.id.emptyview_progress);
        btnReload = findViewById(R.id.emptyview_btn);
        tvTips = findViewById(R.id.emptyview_tv_tip);


        progressBar.setIndeterminate(true);
        progressBar.setIndeterminateDrawable(getResources().getDrawable(R.drawable.anim_loading));
        imgStatus.setVisibility(INVISIBLE);
        btnReload.setText("刷新");
        btnReload.setVisibility(INVISIBLE);
//        setBackgroundColor(ContextCompat.getColor(context, R.color.white));
    }

    public void bind(View view) {
        this.bindView = view;
    }

    public void unbind() {
        loadSuccess();
        this.bindView = null;
    }

    public void loadSuccess() {
        isShowLoading = false;
        setVisibility(View.INVISIBLE);
    }

    private String tipstr = "暂无数据";

    public void setEmptyTipstr(String tipstr) {
        this.tipstr = tipstr;
    }

    public void showEmpty() {
        showEmpty(tipstr);
    }

    public void showEmpty(String msg) {
//        if (bindView != null) {
            setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.INVISIBLE);
            btnReload.setVisibility(View.VISIBLE);
            setMessage(msg);
            setImgStatusRes(IS_EMPTY);
//        }
    }

    public void showTips(String msg) {
//        if (bindView != null) {
            setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.INVISIBLE);
            btnReload.setVisibility(View.INVISIBLE);
            setMessage(msg);
            imgStatus.setVisibility(View.INVISIBLE);
//        }
    }

    public void showEmptyNoBtn(String msg) {
//        if (bindView != null) {
            setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.INVISIBLE);
            btnReload.setVisibility(View.INVISIBLE);
            setMessage(msg);
            imgStatus.setVisibility(View.INVISIBLE);
//        }
    }

    public void showNetError() {
        setShowLoading(false);
        setVisibility(View.VISIBLE);
        btnReload.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.INVISIBLE);
        setMessage("加载失败，网络不给力");
        setImgStatusRes(IS_ERROR);
    }

    public void showLoading() {
        if (isShowLoading) {
            return;
        }
        String message = "加载中...";
        showLoading(message);
    }

    public void showLoadFail(String msg) {
        isShowLoading = false;
        setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.INVISIBLE);
        btnReload.setVisibility(View.VISIBLE);
        setMessage(msg);
        setImgStatusRes(IS_ERROR);
    }

    @Override
    public void loadFinish() {
        loadSuccess();
    }

    @Override
    public void setReloadClickListener(OnClickListener clickListener) {
        setOnReloadListener(clickListener);
    }

    public void showLoadFail() {
        isShowLoading = false;
        setVisibility(View.VISIBLE);
        progressBar.setVisibility(INVISIBLE);
        btnReload.setVisibility(View.VISIBLE);
        setMessage("加载失败");
        setImgStatusRes(IS_ERROR);
    }

    /**
     * 显示数据出错页面
     */
    public static final int IS_ERROR = 0;
    /**
     * 显示空数据页面
     */
    public static final int IS_EMPTY = 1;

    /**
     * @param showWho {@link #IS_EMPTY} or {@link #IS_ERROR}
     */
    public void setImgStatusRes(int showWho) {
        if (imgStatus.getVisibility() != VISIBLE) {
            imgStatus.setVisibility(View.VISIBLE);//TODO
        }
        if (showWho == IS_EMPTY) {
             imgStatus.setImageResource(R.drawable.img_empty);
        } else if (showWho == IS_ERROR) {
             imgStatus.setImageResource(R.drawable.img_empty);
        }
    }

    /**
     * 设置按钮的文字
     *
     * @param text
     */
    public void setBtnReloadText(String text) {
        this.btnReload.setText(text);
    }

    public void setOnReloadListener(OnClickListener clickListener) {
        btnReload.setOnClickListener(clickListener);
    }

    public void setMessage(int stringId) {
        setMessage(getResources().getString(stringId));
    }

    public void setMessage(String message) {
        this.tvTips.setText(message);
    }

    @Override
    public void setVisibility(int visibility) {
        super.setVisibility(visibility);
        if (visibility == VISIBLE) {
            if (bindView != null) {
                bindView.setVisibility(INVISIBLE);
            }
        } else {
            if (bindView != null) {
                bindView.setVisibility(VISIBLE);
            }
        }
    }

    public void showLoading(String message) {
        setShowLoading(true);
        setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.VISIBLE);
        btnReload.setVisibility(View.INVISIBLE);
        setMessage(message);
        imgStatus.setVisibility(View.INVISIBLE);
    }


    public Button getBtnReload() {
        return btnReload;
    }

    public boolean isShowLoading() {
        return isShowLoading;
    }

    public void setShowLoading(boolean showLoading) {
        isShowLoading = showLoading;
    }
}
