package com.ruanyun.australianews.base;

import android.os.Parcelable;

/**
 * Description:
 * author: zhangsan on 16/12/13 上午9:13.
 */

public interface BaseViewModel<T> extends Parcelable {
    /**返回实现此接口的model 为了与服务端实体类解耦 原则上尽量不使用此方法
     * 如遇到要转换json 实体类可以选择使用此方法
     */
    T relatedModel();

    boolean isSelected();

    void setSelected(boolean f);
}
