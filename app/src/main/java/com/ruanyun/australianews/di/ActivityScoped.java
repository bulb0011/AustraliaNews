package com.ruanyun.australianews.di;

import javax.inject.Scope;
import java.lang.annotation.Retention;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Created by codeest on 16/8/7.
 */

@Scope
@Retention(RUNTIME)
public @interface ActivityScoped {
}
