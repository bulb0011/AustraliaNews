package com.ruanyun.australianews.base.refreshview.view;

import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.view.View;

/**
 * Description:
 * author: zhangsan on 2017/8/15 上午11:24.
 */

public interface IRefreshListLoadViewFactory {
    
    IRefreshView creatRefreshView();
    
    ILoadMoreView creatLoadMoreView();
    
    IEmptyView createEmptyView(@IdRes int id);
    
    
    interface IRefreshView {
        
        void init(RefreshViewAddWrapper wrapper, @IdRes int dataViewId);
        
        void setRefreshListener(onRefreshListener listener);
        
        View getContentView();
        
        View getRoot();
        
        void startRefresh();
        
        void stopRefresh();
        
        void setRefreshEnable(boolean enable);
        
        interface onRefreshListener {
            void onRefresh();
        }
        
        interface RefreshViewAddWrapper {
            void addRefreshView(@LayoutRes int layout);
            
            void addRefreshView(@NonNull View view);
            
            View getRootView();
        }
    }
    
    
    interface ILoadMoreView {
        
        void init(LoadMoreViewAddWrapper loadMoreViewAddWrapper);
        // void setOnLoadMoreListener(ILoadMoreView.onLoadMoreListener onLoadMoreListener);
        
        void showLoading(String tip);
        
        void showNoMore(String tip);
        
        void hide();
        
        interface onLoadMoreListener {
            void onLoadMore();
        }
        
        interface LoadMoreViewAddWrapper {
            void addLoadMoreView(@LayoutRes int layout);
            
            void addLoadMoreView(@NonNull View view);
            
            View getRootView();
        }
    }
    
    
    interface IEmptyView {
        
        void bind(View view);
        
        void unbind();
        
        void showLoading(String loadingMsg);
        
        void showEmpty();
        
        void showLoadFail(String erroMsg);
        
        void loadFinish();
        
        void setReloadClickListener(View.OnClickListener clickListener);
    }
}
