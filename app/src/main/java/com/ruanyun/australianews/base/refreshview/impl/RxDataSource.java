package com.ruanyun.australianews.base.refreshview.impl;

import com.ruanyun.australianews.base.refreshview.data.IDataSource;
import com.ruanyun.australianews.data.ApiService;
import com.ruanyun.australianews.util.LogX;
import com.ruanyun.australianews.util.RxUtil;
import rx.Observable;
import rx.Subscription;
import rx.functions.Action1;
import rx.subscriptions.CompositeSubscription;

/**
 * Description:
 * author: zhangsan on 2017/8/16 下午3:28.
 */

public abstract class RxDataSource<Data> implements IDataSource<Data> {


    protected ApiService apiService;

    protected CompositeSubscription mCompositeSubscription;


    public RxDataSource(ApiService apiService) {
        this.apiService = apiService;
    }

    @Override
    public void cancel() {
        unSubscribe();
    }

    @Override
    public void requestData(int loadAction, IDataSourceResultHandler<Data> handler) {
        startRequest(loadAction, fetchData(loadAction, handler), handler);
    }

    protected abstract Observable<Data> fetchData(int loadType, IDataSourceResultHandler<Data> handler);


    protected void startRequest(final int loadAction, Observable<Data> dataObservable, final IDataSourceResultHandler<Data> handler) {
        if (handler == null) return;
        addSubscrebe(dataObservable.compose(RxUtil.<Data>normalSchedulers())
                .subscribe(new Action1<Data>() {
                    @Override
                    public void call(Data data) {
                        handler.handleResult(loadAction, data);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        LogX.e("retrofit","error:"+throwable.getMessage());
                        handler.handleError(loadAction,throwable);
                    }
                }));
    }


    protected void unSubscribe() {
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
}
