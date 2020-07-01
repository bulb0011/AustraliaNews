package com.ruanyun.australianews.base.refreshview.view;

import android.view.View;

/**
 * Description:
 * author: zhangsan on 2017/8/11 下午2:56.
 */

public interface ILoadMoreListenerHandler {

   void setScrollBottomListener(View target, ScrollBottomListener ScrollBottomListener);


    interface  ScrollBottomListener{
        void onScrollBottom();
    }
}
