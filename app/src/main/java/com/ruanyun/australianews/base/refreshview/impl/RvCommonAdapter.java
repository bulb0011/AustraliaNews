package com.ruanyun.australianews.base.refreshview.impl;

import android.content.Context;
import android.view.LayoutInflater;

import com.ruanyun.australianews.base.refreshview.view.IDataAdapter;
import com.zhy.adapter.recyclerview.base.ItemViewDelegate;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.List;

/**
 * Description: author: zhangsan on 2017/9/11 下午1:53.
 */

public abstract class RvCommonAdapter<T> extends RvMuiltItemAdapter<T> implements IDataAdapter<List<T>> {

    protected Context mContext;
    protected int mLayoutId;
    protected LayoutInflater mInflater;

    public RvCommonAdapter(Context context, final int layoutId, List<T> datas) {
        super(context, datas);
        mContext = context;
        mInflater = LayoutInflater.from(context);
        mLayoutId = layoutId;
        mDatas = datas;

        addItemViewDelegate(new ItemViewDelegate<T>() {
            @Override
            public int getItemViewLayoutId() {
                return layoutId;
            }

            @Override
            public boolean isForViewType(T item, int position) {
                return true;
            }

            @Override
            public void convert(ViewHolder holder, T t, int position) {
                RvCommonAdapter.this.convert(holder, t, position);
            }
        });
    }

    protected abstract void convert(ViewHolder holder, T t, int position);

    @Override
    public void refresh(List<T> datas) {
        mDatas.clear();
        mDatas.addAll(datas);
        notifyDataSetChanged();
    }

    @Override
    public int getAdapterType() {
        return TYPE_RECYERVIEW;
    }


    @Override
    public void loadMore(List<T> datas) {
        mDatas.addAll(datas);
        // notifyDataSetChanged();
        notifyItemRangeInserted(mDatas.size(), datas.size());
    }

    @Override
    public void refresh() {
        notifyDataSetChanged();
    }

    public void addData(List<T> data) {
        mDatas.addAll(data);
    }

    public void setData(List<T> data) {
        this.mDatas = data;
    }
}
