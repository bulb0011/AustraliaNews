package com.ruanyun.australianews.base.refreshview.view;

import android.app.Activity;
import android.support.annotation.IdRes;
import android.view.View;
import com.ruanyun.australianews.base.refreshview.data.IDataDelegate;

/**
 * Description:
 * author: zhangsan on 2017/8/18 下午1:59.
 */

public interface IRefreshViewHolder<T extends View> {
    /**
     * 创建data ->view代理类
     **/
    IDataDelegate createDataDelegate();

    /**
     * 页面activity 初始化 view
     **/
    IRefreshViewHolder init(Activity activity);

    IRefreshViewHolder init(View root);

    /**
     * 自定义empty view 的id
     **/
    IRefreshViewHolder setEmptyViewId(@IdRes int id);

    /**
     * 自定义list 或 recyerview 的id
     **/
    IRefreshViewHolder setDataListViewId(@IdRes int id);

    /**
     * 自定义下拉刷新 view 的id
     **/
    IRefreshViewHolder setRefreshViewId(@IdRes int id);
    /** 设置自定义 loadmore view **/
    IRefreshViewHolder setLoadMoreView(IRefreshListLoadViewFactory.ILoadMoreView iLoadMoreView);
    /** 设置自定义 下拉刷新view **/
    IRefreshViewHolder setRefreshView(IRefreshListLoadViewFactory.IRefreshView iRefreshView);
    IRefreshViewHolder setEmptyView(IRefreshListLoadViewFactory.IEmptyView emptyView);
    /**
     * 列表设置
     **/
    IRefreshViewHolder setDataAdapter(IDataAdapter dataAdapter);

    IRefreshViewHolder setRefreshViewEnable(boolean enable);



    IRefreshViewHolder setEmptyViewEnable(boolean enable);

    /**
     * 设置加载更多是否可用
     **/
    IRefreshViewHolder setLoadMoreEnable(boolean enable);

    /**
     * 下拉刷新是否可用
     **/
    boolean refreshViewEnable();

    boolean loadMoreEnable();

    /**
     * 获取 emptyview 是否可用
     **/
    boolean emptyViewEnable();

    /**
     * 获取绑定的 下拉刷新view
     **/
    T getRawRefreshView();

    IRefreshListLoadViewFactory.IRefreshView getRefreshView();

    IRefreshListLoadViewFactory.ILoadMoreView getLoadMoreView();

    IRefreshListLoadViewFactory.IEmptyView getEmptyView();

    IDataAdapter getDataAdapter();

    ILoadMoreListenerHandler getILoadMoreListenerHandler();
}
