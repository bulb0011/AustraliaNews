package com.ruanyun.australianews.di.module;

import android.content.Context;
import com.ruanyun.australianews.App;
import dagger.Binds;
import dagger.Module;

/**
 * description:
 * <p/>
 * Created by ycw on 2018/11/20.
 */
@Module
public abstract class ApplicationModule {

    //expose Application as an injectable context
    @Binds
    abstract Context bindContext(App application);

}
