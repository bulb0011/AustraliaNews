package com.ruanyun.australianews.util;

import android.support.annotation.Keep;
//import com.qiniu.android.http.ResponseInfo;
//import com.qiniu.android.storage.Configuration;
//import com.qiniu.android.storage.UploadManager;
import org.json.JSONException;
import rx.Observable;
import rx.Subscriber;
import rx.functions.Func1;

import java.io.File;
import java.util.LinkedHashMap;
import java.util.List;


public class Token2UrlFunc/* implements Func1<String, Observable<LinkedHashMap<String, String>>>*/ {

//    List<InputInfo> mInputInfos;
//
//    UploadManager mUploadManager;
//    public static String userNum;
//
//    public Token2UrlFunc(String userNum, List<InputInfo> inputInfos) {
//        Configuration config = new Configuration.Builder()
//                .chunkSize(256 * 1024)  //分片上传时，每片的大小。 默认256K
//                .putThreshhold(512 * 1024)  // 启用分片上传阀值。默认512K
//                .connectTimeout(10) // 链接超时。默认10秒
//                .responseTimeout(60).build(); // 服务器响应超时。默认60秒
//        // .recorder(recorder)  // recorder分片上传时，已上传片记录器。默认null
//        // .recorder(recorder, keyGen)  // keyGen 分片上传时，生成标识符，用于片记录器区分是
//        mUploadManager = new UploadManager(config);
//        mInputInfos = inputInfos;
//        this.userNum = userNum;
//    }
//
//    @Override
//    public Observable<LinkedHashMap<String, String>> call(String token) {
//
//        return Observable.unsafeCreate(new Token2UpSubscribe(mInputInfos, token, mUploadManager));
//    }
//
//
//    static class Token2UpSubscribe implements Observable.OnSubscribe<LinkedHashMap<String, String>> {
//
//        List<InputInfo> mInputInfos;
//
//        String token;
//
//        UploadManager mUploadManager;
//
//        public Token2UpSubscribe(List<InputInfo> inputInfos, String token, UploadManager uploadManager) {
//            mInputInfos = inputInfos;
//            mUploadManager = uploadManager;
//            this.token = token;
//        }
//
//        @Override
//        public void call(Subscriber<? super LinkedHashMap<String, String>> subscriber) {
//            LinkedHashMap<String, String> map = new LinkedHashMap<>();
//
//            for (int i = 0; i < mInputInfos.size(); i++) {
//                InputInfo inputInfo = mInputInfos.get(i);
//                File idCardFront = inputInfo.upFile;
//                String upName;
//                if (idCardFront != null) {
//                    ResponseInfo idCardFrontResponseInfo = mUploadManager.syncPut(idCardFront, userNum + "_" + inputInfo.key + i + "_" + System.currentTimeMillis() + ".jpg", token, null);
//                    String name = null;
//                    if (idCardFrontResponseInfo.isOK()) {
//                        try {
//                            name = idCardFrontResponseInfo.response.getString("key");
//                        } catch (JSONException e) {
//                        }
//                    }else {
//                        LogX.e("retrofit", "上传七牛: "+idCardFrontResponseInfo.response.toString());
//                    }
//                    upName = name;
//                } else {
//                    upName = null;
//                }
//                map.put(inputInfo.key, upName);
//            }
//            subscriber.onNext(map);
//        }
//    }
//
//
//
//    @Keep
//    public static class InputInfo {
//        String key;
//        File upFile;
//
//        public InputInfo(String key, File upFile) {
//            this.key = key;
//            this.upFile = upFile;
//        }
//    }


}
    
 