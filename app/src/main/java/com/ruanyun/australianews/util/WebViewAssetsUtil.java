package com.ruanyun.australianews.util;

import android.text.TextUtils;
import com.ruanyun.australianews.App;
import com.ruanyun.australianews.data.ApiManger;

import java.io.IOException;

/**
 * @author hdl
 * @description
 * @date 2019/12/28
 */
public class WebViewAssetsUtil {
    public static String getAssetsName(String url){
        if(TextUtils.isEmpty(url)){
            return "";
        }
//        String[] nameList = null;
//        try {
//            nameList = App.getInstance().getAssets().list("webview");
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        if(nameList!=null) {
//            for (String s : nameList) {
//                if (url.contains("/" + s)) {
//                    return "webview/" + s;
//                }
//            }
//        }
//        return "";


        if (
                url.startsWith(ApiManger.API_URL + "/css")
//                || url.startsWith(ApiManger.API_URL + "/font")
//                || url.startsWith(ApiManger.API_URL + "/fonts")
//                || url.startsWith(ApiManger.API_URL + "/include")
                || url.startsWith(ApiManger.API_URL + "/js")
//                || url.startsWith(ApiManger.API_URL + "/WEB-INF")
        ) {
            String name = url.substring(ApiManger.API_URL.length()+1);
            if(!name.startsWith("/")){
                name = "/" + name;
            }
            return "webapp"+ name;
        }
        return null;
    }
}
