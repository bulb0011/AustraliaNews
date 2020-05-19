package com.ruanyun.australianews.data;

import com.ruanyun.australianews.BuildConfig;
import com.ruanyun.australianews.data.converter.GsonConverterFactory;

import java.util.concurrent.TimeUnit;

import jiguang.chat.utils.FileUtils;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;


/**
 * Description:
 * author: zhangsan on 16/11/21 下午1:30.
 */

public class ApiManger {
    private static final long CONNECT_TIME_OUT = 60 ;
    private static final long READ_TIME_OUT = 60 ;
    private static OkHttpClient okHttpClient;
    private static Retrofit retrofit;
    private static ApiService apiService;

    public static final String API_URL = FileUtils.API_URL;//链接地址
    public static final String IMG_URL = FileUtils.IMG_URL;//新图片地址

    public ApiManger() {
        init();
    }

    public static void init() {
        if (okHttpClient == null) {
            OkHttpClient.Builder builder = new OkHttpClient().newBuilder();
            builder.connectTimeout(CONNECT_TIME_OUT, TimeUnit.SECONDS)
//                    .addInterceptor(new AddCookiesInterceptor())
                    .readTimeout(READ_TIME_OUT, TimeUnit.SECONDS)
                    .writeTimeout(READ_TIME_OUT, TimeUnit.SECONDS);
            if (BuildConfig.DEBUG) {
                builder.addInterceptor(new LoggingInterceptor());
            }
            okHttpClient = builder.build();

        }
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(API_URL)
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(okHttpClient)
                    .build();
        }
    }

    public static ApiService getApiService() {
        if (null == apiService) {
            apiService = retrofit.create(ApiService.class);
        }
        return apiService;
    }

    public static ApiService createApiService() {
        return retrofit.create(ApiService.class);
    }

    public static OkHttpClient getOkHttpClient() {
        return okHttpClient;
    }

}
