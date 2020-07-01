package com.ruanyun.australianews.data;

import com.ruanyun.australianews.base.ResultBase;
import rx.functions.Action1;

/**
 * Description:api处理回调
 * author: zhangsan on 16/11/21 下午2:50.
 */

public abstract class ApiSuccessAction<T extends ResultBase> implements Action1<T> {
    @Override
    public void call(T result) {
        if (result.code == 0) {
            if(result.data == null){
                result.data = new Object();
            }
            onSuccess(result);
        } else  {
            onError(result.code, result.msg);
        }
    }

    public abstract void onSuccess(T result);

    public void onError(int erroCode, String erroMsg) {

    }

    public void onResponse() {

    }
}
