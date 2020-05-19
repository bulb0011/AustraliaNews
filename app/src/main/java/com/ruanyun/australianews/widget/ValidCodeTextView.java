package com.ruanyun.australianews.widget;

import android.content.Context;
import android.os.CountDownTimer;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;


/**
 * @ClassName: ValidCodeButton.java
 * @Description: 发送验证码自动倒计时按钮
 * @author Daniel
 * @date 2016/2/22 16:23
 */
public class ValidCodeTextView extends AppCompatTextView {
    ValidCodeCountDownTimer timer;


    public ValidCodeTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        timer = new ValidCodeCountDownTimer(60000, 1000);
    }

    public void stop(){
        timer.cancel();
    }

    public void start() {
        timer.start();
    }

    public void reset() {
        timer.cancel();
        setText("获取验证码");
        setClickable(true);
        setSelected(false);
    }

    /* 定义一个倒计时的内部类 */
    private class ValidCodeCountDownTimer extends CountDownTimer {
        public ValidCodeCountDownTimer(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onFinish() {
            setText("获取验证码");
            setClickable(true);
            setSelected(false);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            setText(String.format("重新获取(%s)", millisUntilFinished / 1000));
            setClickable(false);
            setSelected(true);
        }
    }
}
