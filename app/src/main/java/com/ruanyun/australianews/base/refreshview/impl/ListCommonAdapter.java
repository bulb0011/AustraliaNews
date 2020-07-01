package com.ruanyun.australianews.base.refreshview.impl;

import android.content.Context;
import com.ruanyun.australianews.base.refreshview.view.IDataAdapter;
import com.zhy.adapter.abslistview.MultiItemTypeAdapter;
import com.zhy.adapter.abslistview.ViewHolder;
import com.zhy.adapter.abslistview.base.ItemViewDelegate;

import java.util.List;


/**
 * Description:
 * author: zhangsan on 2017/9/18 上午11:37.
 */

public abstract class ListCommonAdapter<T> extends MultiItemTypeAdapter<T> implements IDataAdapter<List<T>> {

    public ListCommonAdapter(Context context, final int layoutId, List<T> datas) {
        super(context, datas);
        addItemViewDelegate(new ItemViewDelegate<T>()
        {
            @Override
            public int getItemViewLayoutId()
            {
                return layoutId;
            }

            @Override
            public boolean isForViewType(T item, int position)
            {
                return true;
            }

            @Override
            public void convert(ViewHolder holder, T t, int position)
            {
                ListCommonAdapter.this.convert(holder, t, position);
            }
        });
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
    public void refresh() {
        notifyDataSetChanged();
    }

    @Override
    public int getAdapterType() {
        return TYPE_LISTVIEW;
    }
}
