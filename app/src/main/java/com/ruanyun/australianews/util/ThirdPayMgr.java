package com.ruanyun.australianews.util;

import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import com.alipay.sdk.app.PayTask;
import com.ruanyun.australianews.model.PayResult;

import javax.inject.Inject;
import java.lang.ref.WeakReference;
import java.util.Map;


/**
 * Description:第三方支付工具类
 * author: zhangsan on 2018/1/11 上午10:19.
 */

public class ThirdPayMgr {
    private static final int SDK_PAY_FLAG = 1;
    private static final String TAG = "ThirdPayMgr";
    private payCallback callback;

    @Inject
    public ThirdPayMgr() {
        handler = new PayResultHandler(this);
    }

    /**
     * 调用支付宝支付   paySign 服务端返回的加签字符串
     **/
    public void alipay(final Activity activity, final String paySign, payCallback callback) {
        this.callback = callback;
        Runnable payRunnable = new Runnable() {

            @Override
            public void run() {
                PayTask alipay = new PayTask(activity);
                Map<String, String> result = alipay.payV2(paySign, true);
                LogX.i("retrofit", result.toString());

                Message msg = new Message();
                msg.what = SDK_PAY_FLAG;
                msg.obj = result;
                getHandler().sendMessage(msg);
            }
        };

        Thread payThread = new Thread(payRunnable);
        payThread.start();
    }

    private static class PayResultHandler extends Handler {
        WeakReference<ThirdPayMgr> mActivityWeakReference;

        PayResultHandler(ThirdPayMgr handler) {
            mActivityWeakReference = new WeakReference<ThirdPayMgr>(handler);
        }

        @Override
        public void handleMessage(Message msg) {
            final ThirdPayMgr handler = mActivityWeakReference.get();
            if (handler != null) {

                switch (msg.what) {
                    case SDK_PAY_FLAG: {
                        @SuppressWarnings("unchecked") PayResult payResult = new PayResult((Map<String, String>) msg.obj);
                        //9000 	订单支付成功
                        //8000 	正在处理中，支付结果未知（有可能已经支付成功），请查询商户订单列表中订单的支付状态
                        //4000 	订单支付失败
                        //5000 	重复请求
                        //6001 	用户中途取消
                        //6002 	网络连接出错
                        //6004 	支付结果未知（有可能已经支付成功），请查询商户订单列表中订单的支付状态
                        //其它 	其它支付错误
                        String resultInfo = payResult.getResult();// 同步返回需要验证的信息
                        String resultStatus = payResult.getResultStatus();

                        // 判断resultStatus 为9000则代表支付成功
                        if (TextUtils.equals(resultStatus, "9000")) {
                            // 该笔订单是否真实支付成功，需要依赖服务端的异步通知。
                            if (handler.callback != null) {
                                handler.callback.onPaySuccess();
                            }
                        } else if(TextUtils.equals(resultStatus, "6001")){
                            if (handler.callback != null) {
                                handler.callback.onPayCancel();
                            }
                        } else {
                            if (handler.callback != null) {
                                handler.callback.onPayFail(payResult.getMemo());
                            }
                        }
                        break;
                    }
                    default:
                        break;
                }

            }
        }
    }

    private PayResultHandler handler;

    private PayResultHandler getHandler() {
        if (handler == null) {
            handler = new PayResultHandler(this);
        }
        return handler;
    }


    public interface payCallback {
        void onPaySuccess();

        void onPayCancel();

        void onPayFail(String msg);
    }

}
