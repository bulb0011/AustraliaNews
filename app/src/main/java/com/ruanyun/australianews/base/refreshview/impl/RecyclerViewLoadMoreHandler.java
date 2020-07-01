package com.ruanyun.australianews.base.refreshview.impl;

import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;
import android.view.View;
import com.ruanyun.australianews.base.refreshview.view.ILoadMoreListenerHandler;


/**
 * Description:
 * author: zhangsan on 2017/8/16 下午4:17.
 */

public class RecyclerViewLoadMoreHandler implements ILoadMoreListenerHandler {
    @Override
    public void setScrollBottomListener(View target, ILoadMoreListenerHandler.ScrollBottomListener scrollBottomListener) {
        if(!(target instanceof RecyclerView)) throw  new IllegalStateException("target view is not valid");
        if(scrollBottomListener ==null) throw new IllegalStateException("scrollBottomListener can't be null");
        RecyclerView recyclerView= (RecyclerView) target;
        RecyclerViewOnScrollListener listener=new RecyclerViewOnScrollListener(scrollBottomListener);
        recyclerView.addOnScrollListener(listener);
        recyclerView.addOnItemTouchListener(listener);
    }



    private static class RecyclerViewOnScrollListener extends RecyclerView.OnScrollListener implements RecyclerView.OnItemTouchListener {
        private ScrollBottomListener onScrollBottomListener;

        public RecyclerViewOnScrollListener(ScrollBottomListener onScrollBottomListener) {
            super();
            this.onScrollBottomListener = onScrollBottomListener;
        }

        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                if (onScrollBottomListener != null) {
                    //(endY < startY) 如果放开的位置比按下去的位置大，说明手势向上移动。（之所以加这个判断是因为，之前列表数据较少的时候它已经在列表底部了，向下的刷新动作也可能触发）
                    //isScollBottom是否滚动到列表底部
                    if ((endY >= 0 && endY < startY) && isScollBottom(recyclerView)) {
                        onScrollBottomListener.onScrollBottom();
                    }
                }
            }
        }

        private boolean isScollBottom(RecyclerView recyclerView) {
            return !isCanScollVertically(recyclerView);
        }

        private boolean isCanScollVertically(RecyclerView recyclerView) {
            if (android.os.Build.VERSION.SDK_INT < 14) {
                return ViewCompat.canScrollVertically(recyclerView, 1) || recyclerView.getScrollY() < recyclerView.getHeight();
            } else {
                return ViewCompat.canScrollVertically(recyclerView, 1);
            }
        }


        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {

        }

        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
            switch (e.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    endY = -1;
                    startY = e.getY();
                    break;
                case MotionEvent.ACTION_UP:
                    endY = e.getY();
                    break;
            }
            return false;
        }

        private float startY = -1f;
        private float endY = -1f;

        @Override
        public void onTouchEvent(RecyclerView rv, MotionEvent e) {

        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

        }

    }



}
