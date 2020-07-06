package com.ruanyun.australianews.widget;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.SeekBar;
import android.widget.TextView;

import com.ruanyun.australianews.R;

public class SlideDialog implements SeekBar.OnSeekBarChangeListener {

    Context context;
    private Dialog dialog;
    private View inflate;
    private TextView textView;

    public SlideDialog(Context context) {
        this.context = context;
        initView();
    }

    void initView(){

        dialog = new Dialog(context, R.style.ActionSheetDialogStyle);
        //填充对话框的布局
        inflate = LayoutInflater.from(context).inflate(R.layout.layout_slidedialog, null);

        SeekBar seekbarTextsize= inflate.findViewById(R.id.seekbar_textsize);

        TextView tv_wancheng=inflate.findViewById(R.id.tv_wancheng);

        seekbarTextsize.setOnSeekBarChangeListener(this);
        dialog.setCanceledOnTouchOutside(true);
        //将布局设置给Dialog
        dialog.setContentView(inflate);
        //获取当前Activity所在的窗体
        Window dialogWindow = dialog.getWindow();
        //设置Dialog从窗体底部弹出
        dialogWindow.setGravity( Gravity.BOTTOM);
        //获得窗体的属性
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        //设置宽
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        //设置高
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;

//       将属性设置给窗体
        dialogWindow.setAttributes(lp);
        dialog.show();//显示对话框

        tv_wancheng.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

    }


    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

        mOnSeekBarChangeListener.onProgressChanged(seekBar,progress,fromUser);
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        mOnSeekBarChangeListener.onStartTrackingTouch(seekBar);
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        mOnSeekBarChangeListener.onStopTrackingTouch(seekBar);
    }

   public interface  OnSlideSeekBarChangeListener{

      void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser);

       void onStartTrackingTouch(SeekBar seekBar);

       void onStopTrackingTouch(SeekBar seekBar);
    }

    OnSlideSeekBarChangeListener mOnSeekBarChangeListener;

    public void setOnSeekBarChangeListener(OnSlideSeekBarChangeListener l) {
        mOnSeekBarChangeListener = l;
    }
}
