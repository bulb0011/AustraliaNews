package com.ruanyun.australianews.base.refreshview.impl;

import android.content.Context;
import com.ruanyun.australianews.base.refreshview.view.IDataAdapter;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;

import java.util.List;

/**
 * Description:
 * author: zhangsan on 2017/9/11 下午1:50.
 */

public class RvMuiltItemAdapter<T> extends MultiItemTypeAdapter<T> implements IDataAdapter<List<T>> {

    protected String TAG = getClass().getSimpleName();

    public RvMuiltItemAdapter(Context context, List<T> datas) {
        super(context, datas);
    }


    @Override
    public void refresh() {
        notifyDataSetChanged();
    }

    @Override
    public void refresh(List<T> datas) {
        mDatas.clear();
        mDatas.addAll(datas);
        notifyDataSetChanged();
    }

    @Override
    public void loadMore(List<T> datas) {
        mDatas.addAll(datas);
        notifyDataSetChanged();
    }

    @Override
    public int getAdapterType() {
        return TYPE_RECYERVIEW;
    }
}
