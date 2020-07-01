package com.ruanyun.australianews.widget;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.PopupWindow;
import android.widget.TextView;
import com.ruanyun.australianews.R;


/**
 * description 个人中心选择相机 pop
 * Created by ycw
 * date 2016/5/26
 */
public class RYSelectPopWindow extends PopupWindow implements PopupWindow.OnDismissListener {
    public boolean isSelectedPic = false;

    private Context mContext;
    private OnSelectListener mOnSelectListener;

    public void setOnSelectListener(OnSelectListener onSelectListener) {
        mOnSelectListener = onSelectListener;
    }

    public RYSelectPopWindow(Context context, OnSelectListener onSelectListener) {
        this.mContext = context;
        this.mOnSelectListener = onSelectListener;
        initView();
    }

    private void initView() {
        setBackgroundDrawable(new ColorDrawable(mContext.getResources().getColor(R.color
                .transparent)));
        setOutsideTouchable(false);
        setFocusable(true);
        setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
        setWidth(WindowManager.LayoutParams.MATCH_PARENT);
        setAnimationStyle(R.style.popupwindow_anim_style);

        View view = LayoutInflater.from(mContext).inflate(R.layout.layout_select_view, null);
        TextView tvAlbumSelect = (TextView) view.findViewById( R.id.tv_album_select);
        TextView tvCancel = (TextView) view.findViewById(R.id.tv_cancel);
        TextView tvTake = (TextView) view.findViewById(R.id.tv_take_pic);
        //从相册选择
        tvAlbumSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isSelectedPic = true;
                mOnSelectListener.onAlbumSelectClick();
            }
        });
        //拍照
        tvTake.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isSelectedPic = true;
                mOnSelectListener.takePicClick();
            }
        });
        //        取消按钮事件
        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        setContentView(view);
        //在PopupWindow里面就加上下面代码，让键盘弹出时，不会挡住pop窗口。
        setInputMethodMode(PopupWindow.INPUT_METHOD_NEEDED);
        setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        setOnDismissListener(this);
    }

    /**
     * 居中弹出popu窗口
     * @param parent
     */
    public void showPopupWindow(View parent) {
        isSelectedPic = false;
        showAtLocation(parent, Gravity.BOTTOM, 0, 0);
        backgroundAlpha(0.6f);
        //   5.1  有效
        ((Activity) mContext).getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
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

    @Override
    public void onDismiss() {
        backgroundAlpha(1f);
    }

    /**
     * 选择相机  照片
     */
    public interface OnSelectListener {
        void onAlbumSelectClick();

        void takePicClick();
    }
}
