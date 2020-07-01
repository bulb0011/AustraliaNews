package com.ruanyun.australianews.util;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.text.TextUtils;

import cn.jpush.android.api.JPushInterface;
import com.ruanyun.australianews.App;

/**
 * @author hdl
 * @description 极光推送
 * @date 2018/1/16
 */
public class JPushHelper {

    public static void setAlias(String alias) {
        if(TextUtils.isEmpty(alias)){
            JPushInterface.stopPush(App.getInstance());
            LogX.e("JPush", "delete alias");
        }else {
            JPushInterface.resumePush(App.getInstance());
            // 调用 Handler 来异步设置别名
            mHandler.sendMessage(mHandler.obtainMessage(MSG_SET_ALIAS, alias));
        }
    }

    private static final int MSG_SET_ALIAS = 1001;
    @SuppressLint("HandlerLeak")
    private static final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(android.os.Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case MSG_SET_ALIAS:
                    String alias = (String) msg.obj;
                    LogX.e("JPush", "Set alias in handler alias = " + alias);
                    // 调用 JPush 接口来设置别名。
                    JPushInterface.setAlias(App.getInstance().getApplicationContext(), 0, alias);
                    break;
                default:
                    LogX.e("JPush", "Unhandled msg - " + msg.what);
            }
        }
    };
}
