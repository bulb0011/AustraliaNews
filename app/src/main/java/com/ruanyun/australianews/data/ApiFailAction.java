package com.ruanyun.australianews.data;

import android.database.sqlite.SQLiteConstraintException;
import android.util.AndroidRuntimeException;

import com.ruanyun.australianews.util.LogX;

import java.io.IOException;

import retrofit2.HttpException;
import rx.functions.Action1;

/**
 * Description:
 * author: zhangsan on 16/11/21 下午2:54.
 */

public class ApiFailAction implements Action1<Throwable> {

    @Override
    public void call(Throwable throwable) {
        LogX.e("retrofit", "ApiFailAction  throwable :" + throwable.toString(), throwable);
        String errorMsg = "";
        if (throwable instanceof IOException) {
            errorMsg = "服务器异常，请稍后再试";
        } else if (throwable instanceof HttpException) {
            errorMsg = "服务器异常，请稍后再试";
        } else if (throwable instanceof SQLiteConstraintException) {
            errorMsg = "数据库错误";
        } else if (throwable instanceof AndroidRuntimeException) {
            errorMsg = throwable.getMessage();
        } else {
            errorMsg = "数据异常";
        }
        onFail(errorMsg);

    }

    public void onFail(String msg) {

    }
}
