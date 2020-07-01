package com.ruanyun.australianews.base.refreshview.view;



/**
 * Description:
 * author: zhangsan on 2017/8/11 下午2:34.
 */

public interface IDataAdapter<T> {
    int TYPE_LISTVIEW=123;
    int TYPE_RECYERVIEW=321;

    void refresh();

    void refresh(T datas);

    void loadMore(T datas);

    int getAdapterType();


}
