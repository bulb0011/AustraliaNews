package com.ruanyun.australianews.di.module;

import android.content.Context;

import com.ruanyun.australianews.data.ApiManger;
import com.ruanyun.australianews.data.ApiService;
import com.ruanyun.australianews.model.DaoMaster;
import com.ruanyun.australianews.model.DaoSession;
import com.ruanyun.australianews.util.CacheHelper;
import com.ruanyun.australianews.util.DbHelper;
import com.ruanyun.australianews.util.DefaultCompressResultFactory;
import com.ruanyun.australianews.util.PrefUtility;
import com.ruanyun.imagepicker.compressimage.CompressImageProxy;
import com.ruanyun.imagepicker.compressimage.CompressImageProxyService;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Description:
 * author: zhangsan on 16/11/21 上午11:25.
 */
@Module
public class RepositoryModule {

    @Provides
    @Singleton
    public ApiService provideApiservice(Context context) {
        PrefUtility.init(context);
        ApiManger.init();
        return ApiManger.getApiService();
    }

    @Provides
    @Singleton
    public DaoMaster provideDaoMaster(Context context) {
        DaoMaster.OpenHelper helper = new DaoMaster.DevOpenHelper(context, "info_db");
        return new DaoMaster(helper.getWritableDatabase());
    }

    @Provides
    @Singleton
    public DaoSession provideDaoSession(DaoMaster daoMaster) {
        return daoMaster.newSession();
    }

    @Provides
    @Singleton
    public DbHelper provideDbHelper(DaoMaster daoMaster, DaoSession daoSession) {
        DbHelper instance = DbHelper.getInstance();
        instance.initHelper(daoMaster, daoSession);
        return instance;
    }

    @Provides
    @Singleton
    public CacheHelper provideCacheHelper(ApiService apiService, DbHelper dbHelper) {
        CacheHelper instance = CacheHelper.Companion.getInstance();
        instance.initHelper(apiService, dbHelper);
        return instance;
    }

    @Provides
    @Singleton
    public CompressImageProxyService provideCompressService(){
        CompressImageProxy proxy=new CompressImageProxy();
        proxy.setResultConverter(new DefaultCompressResultFactory().creat());
        CompressImageProxyService proxyService=proxy.getProxyService(CompressImageProxyService.class);
        return proxyService;
    }



}
