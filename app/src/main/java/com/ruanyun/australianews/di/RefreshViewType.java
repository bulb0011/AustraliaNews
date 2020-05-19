package com.ruanyun.australianews.di;

import javax.inject.Qualifier;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Description:
 * author: zhangsan on 2017/12/12 下午2:29.
 */
@Qualifier
@Documented
@Retention(RUNTIME)
public @interface RefreshViewType {
    int TYPE_ACTIVITY=1;
    int TYPE_FRAGMENT=2;
    int value() ;
}
