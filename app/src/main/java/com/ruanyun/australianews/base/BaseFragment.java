package com.ruanyun.australianews.base;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.ruanyun.australianews.App;
import com.ruanyun.australianews.ui.login.LoginActivity;
import com.ruanyun.australianews.util.CommonUtil;
import com.ruanyun.australianews.util.LogX;
import com.ruanyun.australianews.widget.LoadingDialog;

import dagger.android.support.DaggerFragment;
import org.greenrobot.eventbus.EventBus;
import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

import javax.inject.Inject;

/**
 * Description:
 * author: zhangsan on 16/7/19 下午2:50.
 */
public abstract class BaseFragment extends DaggerFragment implements TipCommonMvpView {
    protected String TAG = getClass().getSimpleName();
    protected View mContentView;
    protected Context mContext;
    protected LoadingDialog loadingDialog;
    protected CompositeSubscription mCompositeSubscription;
    @Inject
    protected App app;
    protected boolean isVisiable, isPrepared, isFirstIn = true;
//    FragmentComponent fragmentComponent;

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
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        fragmentComponent = DaggerFragmentComponent.builder()
//                .applicationComponent(App.getInstance().getAppCoponent())
//                .fragmentModule(new FragmentModule(this)).build();
//        getFragmentComponent().inject(this);
        mContext = getActivity();
        app = App.getInstance();
    }

    protected void lazyLoad() {

    }

//    public FragmentComponent getFragmentComponent() {
//        return fragmentComponent;
//    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            isVisiable = true;
            onVisiable();
        } else {
            isVisiable = false;
        }
    }

    protected void onVisiable() {
        if (isVisiable && isFirstIn && isPrepared) {
            isFirstIn = false;
            lazyLoad();
        }
    }


    protected void registerBus() {
        EventBus.getDefault().register(this);
    }

    protected void unRegisterBus() {
        EventBus.getDefault().unregister(this);
    }

    protected void onInVisiable() {

    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }
    protected <T extends View> T getView(int viewId, View root){
        return (T) root.findViewById(viewId);
    }

    protected <T extends View> T getView(int viewId) {
        return (T) mContentView.findViewById(viewId);
    }

    protected <T extends View> T getViewFromLayout(@LayoutRes int layout) {
        return (T) LayoutInflater.from(mContext).inflate(layout, null, false);
    }

    protected <T extends View> T getViewFromLayout(@LayoutRes int layout, ViewGroup root, boolean attach) {
        return (T) LayoutInflater.from(mContext).inflate(layout, root, attach);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mContext = null;
        unSubscribe();
    }

    public View getContentView() {
        return mContentView;
    }

    public void showActivity(Intent it) {
        startActivity(it);
    }


    protected void showLoading() {
        showLoading("加载中...");
    }

    protected void showLoading(String msg) {
        loadingDialog = new LoadingDialog(mContext);
        loadingDialog.setTvMessage(msg);
        loadingDialog.show();
    }

    protected void disMissLoading() {
        loadingDialog.dismiss();
    }

    /**
     * show to @param(cls)
     */
    public void showActivity(Class<?> cls, Bundle extras) {
        Intent intent = new Intent(mContext, cls);
        intent.putExtras(extras);
        startActivity(intent);
    }

    /**
     * show to @param(cls)
     */
    public void showActivity(Class<?> cls) {
        Intent intent = new Intent(mContext, cls);
        startActivity(intent);
    }

    @Override
    public void showToast(String msg) {
        CommonUtil.showToast(msg);
    }

    @Override
    public void showLoadingView(String msg) {
        showLoading(msg);

    }

    @Override
    public void showLoadingView(int msg) {
        showLoading(getString(msg));
    }

    @Override
    public void disMissLoadingView() {
        disMissLoading();
    }

    public boolean isLoginToActivity(){
         if(app.getUser()!=null){
             return true;
        }else {
            LoginActivity.Companion.start(mContext);
             return false;
        }
    }

}
