package com.ruanyun.australianews.base.refreshview.impl;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ruanyun.australianews.base.refreshview.view.IDataAdapter;
import com.ruanyun.australianews.base.refreshview.view.IRefreshListLoadViewFactory;
import com.zhy.adapter.recyclerview.wrapper.HeaderAndFooterWrapper;

import java.util.List;

/**
 * Description: author: zhangsan on 2017/8/31 上午11:41.
 */

public class RvHeaderFootViewAdapter<T> extends HeaderAndFooterWrapper implements IDataAdapter<List<T>>, IRefreshListLoadViewFactory.ILoadMoreView.LoadMoreViewAddWrapper {

    private View loadMoreView;
    private Context context;
    private RvMuiltItemAdapter mBindAdapter;


    public RvHeaderFootViewAdapter(RvMuiltItemAdapter adapter, Context context) {
        super(adapter);
        this.mBindAdapter = adapter;
        this.context = context;
    }

    @Override
    public void refresh() {
        notifyDataSetChanged();
    }

    @Override
    public void refresh(List<T> datas) {
        mBindAdapter.refresh(datas);
        refresh();
    }

    @Override
    public void loadMore(List<T> datas) {
        mBindAdapter.loadMore(datas);
        refresh();
    }

    @Override
    public int getAdapterType() {
        return TYPE_RECYERVIEW;
    }

    @Override
    public void addLoadMoreView(@LayoutRes int layout) {
        Activity activity = (Activity) context;
        loadMoreView = LayoutInflater.from(context)
                .inflate(layout, (ViewGroup) activity.findViewById(android.R.id.content), false);
        addFootView(loadMoreView);
    }

    @Override
    public void addLoadMoreView(@NonNull View view) {
        this.loadMoreView = view;
        addFootView(loadMoreView);
    }

    @Override
    public View getRootView() {
        return loadMoreView;
    }
}
