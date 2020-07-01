package com.ruanyun.australianews.data;


import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.provider.Settings;
import com.ruanyun.australianews.App;
import com.ruanyun.australianews.util.C;
import com.ruanyun.australianews.util.CommonUtil;
import com.ruanyun.australianews.util.MD5;
import com.ruanyun.australianews.util.PrefUtility;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import rx.Observable;
import rx.functions.Action1;

import java.io.IOException;

public class AddCookiesInterceptor implements Interceptor {

public static final String TAG="retrofit";

    @Override
    public Response intercept(Chain chain) throws IOException {
        final Request.Builder builder = chain.request().newBuilder();

        Observable.just(getPHPSESSID()).subscribe(new Action1<String>() {
                    @Override
                    public void call(String cookie) {
                        builder.addHeader("cookie", cookie);
                    }
                });
        return chain.proceed(builder.build());
    }

    public static String getPHPSESSID(){
        String PHPSESSID = PrefUtility.get(C.PrefName.COOKIE_PHPSESSID,"");
        if(CommonUtil.isNotEmpty(PHPSESSID)){
            return PHPSESSID;
        }
        String androidID = Settings.Secure.getString(App.getInstance().getContentResolver(), Settings.Secure.ANDROID_ID);
        String mac = getMacAddress();
        String id = androidID + mac + Build.SERIAL;
        PHPSESSID = "PHPSESSID=" + MD5.md5(id);
        PrefUtility.put(C.PrefName.COOKIE_PHPSESSID, PHPSESSID);
        return PHPSESSID;
    }

    /**
     * 获取 mac地址
     * @return
     */
    @SuppressLint("HardwareIds")
    public static String getMacAddress() {
        String macAddress = null;
        android.net.wifi.WifiManager wifi = (android.net.wifi.WifiManager) App.getInstance().getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        if(wifi !=null && wifi.getConnectionInfo() !=null) {
            macAddress = wifi.getConnectionInfo().getMacAddress();
        }
        return macAddress == null?"":macAddress;
    }

}