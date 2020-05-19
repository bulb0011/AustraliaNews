package com.ruanyun.australianews.base;


import com.ruanyun.australianews.data.ApiService;
import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

/**
 * Description:自动取消请求的presenter 需要手动在页面onndestroy 调用detatch
 * author: zhangsan on 16/11/21 下午5:28.
 */

public class RxPresenter<T extends MvpView> extends BasePresenter<T> {

    protected CompositeSubscription mCompositeSubscription;

    protected ApiService apiService;

    public RxPresenter(ApiService apiService) {
        this.apiService = apiService;
    }

    public void unSubscribe() {
        if (mCompositeSubscription != null) {
            mCompositeSubscription.unsubscribe();
        }
    }

    protected void addSubscrebe(Subscription subscription) {
        if (mCompositeSubscription == null) {
            mCompositeSubscription = new CompositeSubscription();
        }
        mCompositeSubscription.add(subscription);
    }

    @Override
    public void detachView() {
        unSubscribe();
        super.detachView();
    }
}
