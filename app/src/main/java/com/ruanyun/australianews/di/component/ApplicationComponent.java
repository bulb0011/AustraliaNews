package com.ruanyun.australianews.di.component;

import com.ruanyun.australianews.App;
import com.ruanyun.australianews.di.module.ActivityBindingModule;
import com.ruanyun.australianews.di.module.ApplicationModule;
import com.ruanyun.australianews.di.module.RepositoryModule;
import dagger.BindsInstance;
import dagger.Component;
import dagger.android.AndroidInjector;
import dagger.android.support.AndroidSupportInjectionModule;

import javax.inject.Singleton;

/**
 * Description:
 * author: zhangsan on 16/11/21 上午11:20.
 */
@Singleton
@Component(modules = {RepositoryModule.class, ApplicationModule.class, ActivityBindingModule.class, AndroidSupportInjectionModule.class})
public interface ApplicationComponent extends AndroidInjector<App> {

    @Component.Builder
    interface Builder {

        @BindsInstance
        ApplicationComponent.Builder application(App application);

        ApplicationComponent build();
    }

}
