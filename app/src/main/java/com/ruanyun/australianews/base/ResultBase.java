package com.ruanyun.australianews.base;

import com.google.gson.annotations.SerializedName;

/**
 * Description:
 * author: zhangsan on 16/7/20 下午5:11.
 */
public  class ResultBase<T> {
//    @SerializedName(value = "code", alternate = {"datas"})
    public int code;

    public String msg;

//    @SerializedName(value = "data", alternate = {"obj"})
    public T data;

    @Override
    public String toString() {
        return "ResultBase{" + "code=" + code + ", msg='" + msg + '\'' + ", data=" + data + '}';
    }
}
