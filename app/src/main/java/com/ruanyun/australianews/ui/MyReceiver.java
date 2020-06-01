package com.ruanyun.australianews.ui;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.ruanyun.australianews.App;
import com.ruanyun.australianews.model.JPushInfo;
import com.ruanyun.australianews.model.NewsInfo;
import com.ruanyun.australianews.model.uimodel.LifeReleaseCommonUiModel;
import com.ruanyun.australianews.util.FileUtil;
import com.ruanyun.australianews.util.GsonUtil;
import com.ruanyun.australianews.util.LogX;
import com.ruanyun.australianews.util.WebViewUrlUtil;

import org.json.JSONObject;

import cn.jpush.android.api.JPushInterface;

/**
 * 自定义接收器
 * <p>
 * 如果不定义这个 Receiver，则：
 * 1) 默认用户会打开主界面
 * 2) 接收不到自定义消息
 */
public class MyReceiver extends BroadcastReceiver {
    private static final String TAG = "JPush";

    @Override
    public final void onReceive(Context context, Intent intent) {
        try {
            Bundle bundle = intent.getExtras();
            if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent.getAction())) {//点击通知时
                LogX.e(TAG, "[MyReceiver] 用户点击打开了通知");
                if (App.getInstance().containsActivity(MainActivity.class)) {
                    String jsonStr = bundle.getString(JPushInterface.EXTRA_EXTRA);
                    JSONObject jsonObject = new JSONObject(jsonStr);
                    String json = jsonObject.getString("AndroidNotificationExtrasKey");
                    LogX.e("retrofit", "onReceive() called with: json = " + json);
                    JPushInfo jPushInfo = GsonUtil.parseJson(json, JPushInfo.class);
                    if(jPushInfo!=null){
                        switch (jPushInfo.type){

                            case 1://普通新闻
                            case 2://视频新闻
                            case 3://外部新闻
                                getNewsDetails(jPushInfo);
                                break;
                            case 4://广告不跳转
                                break;
                            case 5://广告详情
                                WebViewActivity.Companion.startNewTask(context, FileUtil.getWebViewUrl(WebViewUrlUtil.ADVERTISING_DETAILS, jPushInfo.commonOid));
                                break;
                            case 6://广告外部链接
                                WebViewActivity.Companion.startNewTask(context, jPushInfo.outUrl);
                                break;
                            case 7://房屋出租
                            case 8://招聘信息
                            case 9://汽车买卖
                            case 10://宠物交易
                            case 11://交易市场
                            case 12://房屋求租
                            case 13://生意转让
                            case 14://教科书
                            case 15://餐饮美食
                                WebViewUrlUtil.Companion.showLifeDetailsWebNewTask(context, jPushInfo.type-5, jPushInfo.commonOid);
                                break;
                            case 16://黄页
                                WebViewUrlUtil.Companion.showLifeDetailsWebNewTask(context, LifeReleaseCommonUiModel.LIFE_YELLOW_PAGE_INFO, jPushInfo.commonOid);
                                break;
                        }
                    }
                }else {
                    Intent i = new Intent(context, SplashActivity.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    context.startActivity(i);
                }
            } else {
                LogX.e(TAG, "[MyReceiver] 用户收到消息");
//                解析扩展字段
//                String title = bundle.getString(JPushInterface.EXTRA_NOTIFICATION_TITLE);
//                String content = bundle.getString(JPushInterface.EXTRA_ALERT);
                String jsonStr = bundle.getString(JPushInterface.EXTRA_EXTRA);
                JSONObject jsonObject = new JSONObject(jsonStr);
                jsonObject = new JSONObject(jsonObject.getString("MessageExtrasKey"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     *
     * @param info
     */
    public void getNewsDetails(JPushInfo info) {
        NewsInfo newsInfo = new NewsInfo(info.type,0,info.outUrl,info.videoUrl, "","","",0, 0,0,null);
        newsInfo.setOid(info.commonOid);

        WebViewUrlUtil.showNewsDetailsWebNewTask(App.getInstance(), newsInfo);

//        ApiManger.getApiService().getNewsDetails(oid)
//                .compose(RxUtil.normalSchedulers())
//                .subscribe(new ApiSuccessAction<ResultBase<NewsInfo>>() {
//                    @Override
//                    public void onSuccess(ResultBase<NewsInfo> result) {
//                        LogX.e("retrofit", "onSuccess() called with: result = [" + result + "]");
//                        WebViewUrlUtil.showNewsDetailsWebNewTask(App.getInstance(), result.data);
//                    }
//
//                    @Override
//                    public void onError(int errorCode, String errorMsg) {
//                        LogX.e("retrofit", "onError() called with: errorCode = [" + errorCode + "], errorMsg = [" + errorMsg + "]");
//                    }
//                }, new ApiFailAction() {
//                    @Override
//                    public void onFail(String msg) {
//                        LogX.e("retrofit", "onFail() called with: msg = [" + msg + "]");
//                    }
//                });
    }

}
