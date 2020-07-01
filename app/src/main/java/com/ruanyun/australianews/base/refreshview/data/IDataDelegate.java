package com.ruanyun.australianews.base.refreshview.data;


/**
 * Description:
 * author: zhangsan on 2017/8/18 下午1:55.
 */

public interface IDataDelegate {


    void refresh();

    void refreshWithLoading();

    void destroy();

    void loadData(int loadAction);

    boolean  isLoadWithEmptyView();
    /** 添加获取开始请求数据的监听 **/
    void addLoadActionInterceptor(LoadActionInterceptor interceptor);
    /** 添加请求结果的监听 **/
    void addLoadResultInterceptor(LoadResultInterceptor interceptor);

    IDataDelegate setDataSource(IDataSource dataSource);

    interface LoadResultInterceptor<T>{
        void onLoadResult(int loadAction, T t);
        /** 是否拦截后续默认处理 如果返回true 表示 拦截后续放入adapter 操作  **/
        boolean shouldInterceptHanldle();
    }
    /** 在刷新 加载更多时拦截回调 加入其它操作 **/
    interface LoadActionInterceptor {
       // boolean shouldOverrideRefresh();
       // boolean shouldOverrideLoadMore();
        /** return true  表示只执行onStartRefresh里的方法 后续需要手动调用datedelegate.refresh 方法   return false **/
        boolean onStartRefresh();
        boolean onStartLoadMore();
    }
}
