package com.ruanyun.australianews.base.refreshview.impl;


import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;
import com.ruanyun.australianews.base.refreshview.view.ILoadMoreListenerHandler;

/**
 * Description:
 * author: zhangsan on 2017/8/11 下午4:07.
 */

public class ListViewLoadMoreHandler implements ILoadMoreListenerHandler {
    @Override
    public void setScrollBottomListener(View target, ScrollBottomListener scrollBottomListener) {
        if(!(target instanceof ListView)) throw  new IllegalStateException("target view is not valid");
        if(scrollBottomListener ==null) throw new IllegalStateException("scrollBottomListener can't be null");
        ListView listView= (ListView) target;
        listView.setOnScrollListener(new ListViewOnScrollListener(scrollBottomListener));
    }



     /** listview 滚动到底部监听  **/
    public static class ListViewOnScrollListener implements AbsListView.OnScrollListener {
        private ScrollBottomListener onScrollBottomListener;

        public ListViewOnScrollListener(ScrollBottomListener onScrollBottomListener) {
            super();
            this.onScrollBottomListener = onScrollBottomListener;
        }

        @Override
        public void onScrollStateChanged(AbsListView listView, int scrollState) {
            if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE && listView.getLastVisiblePosition() + 1 == listView.getCount()) {// 如果滚动到最后一行
                if (onScrollBottomListener != null) {
                    onScrollBottomListener.onScrollBottom();
                }
            }
        }

        @Override
        public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

        }
    }





   // public static class Auto
}
