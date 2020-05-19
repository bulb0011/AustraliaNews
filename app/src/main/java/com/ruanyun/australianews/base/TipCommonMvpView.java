package com.ruanyun.australianews.base;


/**
 * Description:
 * author: zhangsan on 16/11/21 下午5:06.
 */

public interface TipCommonMvpView extends MvpView {
    void showToast(String msg);
    void showLoadingView(String msg);
    void showLoadingView(int msg);
    void disMissLoadingView();
}
