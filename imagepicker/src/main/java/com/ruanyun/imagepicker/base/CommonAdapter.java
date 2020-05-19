package com.ruanyun.imagepicker.base;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;



import java.util.List;

/***
 * 通用的adapter适配器
 *
 * @param <T> 2015年9月23日14:06:54
 * @author jery
 */
public abstract class CommonAdapter<T> extends BaseAdapter {
    protected Context mContext;
    protected List<T> mDatas;
    protected LayoutInflater mInflater;
    protected int mlayoutId;

    public CommonAdapter(Context context, List<T> datas, int layoutId) {
        this.mContext = context;
        this.mDatas = datas;
        this.mlayoutId = layoutId;
        mInflater = LayoutInflater.from(context);
    }

    public void setData(List<T> datas) {
        this.mDatas = datas;
    }

    public void addData(List<T> datas) {
        this.mDatas.addAll(datas);
    }

    public List<T> getData() {
        return mDatas;
    }

    @Override
    public int getCount() {
        return mDatas == null ? 0 : mDatas.size();
    }

    @Override
    public T getItem(int position) {
        return mDatas.get(position);
    }

    @Override
    public long getItemId(int position) {

        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        CommonViewHolder holder = CommonViewHolder.get(mContext, convertView,
                parent, mlayoutId, position);
       /// AutoUtils.auto(holder.getmConvertView());
        convert(holder, getItem(position));
        return holder.getmConvertView();
    }

    public abstract void convert(CommonViewHolder holder, T t);
}
