package com.ruanyun.australianews.base.refreshview.impl;

import android.view.View;

import com.ruanyun.australianews.base.PageInfoBase;
import com.ruanyun.australianews.base.ResultBase;
import com.ruanyun.australianews.base.refreshview.data.IDataDelegate;
import com.ruanyun.australianews.base.refreshview.data.IDataSource;
import com.ruanyun.australianews.base.refreshview.view.IDataAdapter;
import com.ruanyun.australianews.base.refreshview.view.ILoadMoreListenerHandler;
import com.ruanyun.australianews.base.refreshview.view.IRefreshListLoadViewFactory;
import com.ruanyun.australianews.base.refreshview.view.IRefreshViewHolder;
import com.ruanyun.australianews.util.NoDoubleClicksListener;

import java.util.ArrayList;
import java.util.List;

import static com.ruanyun.australianews.base.refreshview.data.IDataSource.FIRST_PAGENUM;


/**
 * Description:
 * author: zhangsan on 2017/8/11 下午3:20.
 */

public class DefaultModel2ViewControler<Data> implements IDataDelegate {

    private IDataSource dataSource;

    // private IRefreshListLoadViewFactory.IRefreshView refreshView;

    // private IRefreshListLoadViewFactory.ILoadMoreView loadMoreView;

    // private IRefreshListLoadViewFactory.IEmptyView  emptyView;
    // private IRefreshListLoadViewFactory iRefreshListLoadViewFactory;

    //private IDataAdapter dataAdapter;

    public IRefreshViewHolder refreshViewHolder;

    //private int loadAction=REFRESH;
    private boolean isRefreshWithEmptyView = false;

    private LoadActionInterceptor loadActionInterceptor;
    private LoadResultInterceptor loadResultInterceptor;

    public DefaultModel2ViewControler(IRefreshViewHolder refreshViewHolder) {
        this.refreshViewHolder = refreshViewHolder;
        if (refreshViewHolder.loadMoreEnable())
            refreshViewHolder.getILoadMoreListenerHandler().setScrollBottomListener(refreshViewHolder.getRefreshView().getContentView(), scrollBottomListener);
        refreshViewHolder.getRefreshView().setRefreshListener(onRefreshListener);
        if (refreshViewHolder.emptyViewEnable()) {
            getEmptyView().setReloadClickListener(new NoDoubleClicksListener() {
                @Override
                public void noDoubleClick(View v) {
                    refreshWithLoading();
                }
            });
        }
    }

    private IDataSource.IDataSourceResultHandler<Data> taskResultHandler = new IDataSource.IDataSourceResultHandler<Data>() {
        @Override
        public void handleResult(int loadAction, Data data) {
            if (data == null) {
                handleError(loadAction,new NullPointerException("数据异常"));
                return;
            }
            if(loadResultInterceptor!=null){
                loadResultInterceptor.onLoadResult(loadAction,data);
            }
            /**  判断是否拦截data后续处理**/
            if(loadResultInterceptor==null||(loadResultInterceptor!=null&&!loadResultInterceptor.shouldInterceptHanldle())) {
                switch (loadAction) {
                    case IDataSource.REFRESH:
                        if (!isRefreshWithEmptyView) {
                            getRefreshView().stopRefresh();
                        }

                        if (data instanceof ResultBase) {
                            ResultBase resultBase = (ResultBase) data;
                            if (resultBase.data instanceof PageInfoBase) {
                                PageInfoBase result = (PageInfoBase) resultBase.data;
                                dataSource.setTotalPage(result.getMaxPage());
                                if(result.datas !=null) {
                                    if (result.datas.isEmpty()) {
                                        if (refreshViewHolder.emptyViewEnable())
                                            getEmptyView().showEmpty();
                                    }else {
                                        if (refreshViewHolder.emptyViewEnable())
                                            getEmptyView().loadFinish();
                                    }
                                    getDataAdapter().refresh(result.datas);
                                }else {
                                    getEmptyView().showLoadFail(((ResultBase) data).msg);
                                    getDataAdapter().refresh(new ArrayList<>());
                                }
                            } else if (resultBase.data instanceof List) {
                                List listData = (List) resultBase.data;
                                if (listData.isEmpty()) {
                                    if (refreshViewHolder.emptyViewEnable())
                                        getEmptyView().showEmpty();
                                }
                                getDataAdapter().refresh(data);
                            }
                        }

                        break;
                    case IDataSource.LOADMORE:
                        if (refreshViewHolder.loadMoreEnable()) {
                            if (dataSource.hasMore())
                                getLoadMoreView().hide();
                            else
                                getLoadMoreView().showNoMore("没有更多了");
                        }
                        if (data instanceof ResultBase) {
                            ResultBase resultBase = (ResultBase) data;
                            if (resultBase.data instanceof PageInfoBase) {
                                PageInfoBase datas = (PageInfoBase) resultBase.data;
                                dataSource.setTotalPage(datas.getMaxPage());
                                if (getDataAdapter() != null)
                                    getDataAdapter().loadMore(datas.datas);
                            }
                        }
                        break;
                }
            }

        }

        @Override
        public void handleError(int loadAction,Throwable throwable) {
            getDataAdapter().refresh(new ArrayList<>());
            if (refreshViewHolder.emptyViewEnable())
                getEmptyView().showLoadFail("加载失败");
            if(loadAction==IDataSource.LOADMORE){
               getLoadMoreView().hide();
           }else if(loadAction==IDataSource.REFRESH){
               getRefreshView().stopRefresh();
           }
            //CommonUtil.showToast(throwable.getMessage());
        }
    };

    //  private boolean refreshViewEnable=true;

    // private boolean loadMoreViewEnable=false;

    //  private boolean emptyViewEnable=true;


    private IRefreshListLoadViewFactory.IEmptyView getEmptyView() {
        return refreshViewHolder.getEmptyView();
    }

    private IDataAdapter getDataAdapter() {
        return refreshViewHolder.getDataAdapter();
    }

    private IRefreshListLoadViewFactory.IRefreshView getRefreshView() {
        return refreshViewHolder.getRefreshView();
    }

    private IRefreshListLoadViewFactory.ILoadMoreView getLoadMoreView() {
        return refreshViewHolder.getLoadMoreView();
    }


    @Override
    public void destroy() {
        cancel();
        taskResultHandler = null;
    }

    @Override
    public void refresh() {
        dataSource.setCurrentPage(FIRST_PAGENUM);
        loadData(IDataSource.REFRESH);

    }

    public void cancel() {
        if (dataSource != null)
            dataSource.cancel();
    }

    @Override
    public void refreshWithLoading() {
        if (!refreshViewHolder.emptyViewEnable() || getEmptyView() == null) {
            return;
        }
        isRefreshWithEmptyView = true;
        getEmptyView().showLoading("加载中...");
        refresh();
    }

    @Override
    public void loadData(int loadAction) {
        dataSource.requestData(loadAction, taskResultHandler);
    }

    @Override
    public boolean isLoadWithEmptyView() {
        return isRefreshWithEmptyView;
    }

    @Override
    public void addLoadActionInterceptor(LoadActionInterceptor interceptor) {
        this.loadActionInterceptor = interceptor;
    }

    @Override
    public void addLoadResultInterceptor(LoadResultInterceptor interceptor) {
        this.loadResultInterceptor=interceptor;
    }


    @Override
    public DefaultModel2ViewControler setDataSource(IDataSource dataSource) {
        this.dataSource = dataSource;
        return this;
    }

    private IRefreshListLoadViewFactory.IRefreshView.onRefreshListener onRefreshListener = new IRefreshListLoadViewFactory.IRefreshView.onRefreshListener() {
        @Override
        public void onRefresh() {
            isRefreshWithEmptyView = false;
            // getRefreshView().startRefresh();
//            if(loadActionInterceptor!=null&&loadActionInterceptor.shouldOverrideRefresh()){
//                loadActionInterceptor.onStartRefresh();
//            }
//            if(loadActionInterceptor==null||(loadActionInterceptor!=null&&!loadActionInterceptor.shouldOverrideRefresh()))
//            refresh();

            if(loadActionInterceptor!=null){
                if(loadActionInterceptor.onStartRefresh()){

                }else {
                    refresh();
                }
            }else {
                refresh();
            }
        }
    };

    private ILoadMoreListenerHandler.ScrollBottomListener scrollBottomListener = new ILoadMoreListenerHandler.ScrollBottomListener() {
        @Override
        public void onScrollBottom() {
            if (dataSource.hasMore()) {
                dataSource.setCurrentPage(dataSource.getCurrentPage() + 1);
                getLoadMoreView().showLoading("");
                if (loadActionInterceptor != null)
                    loadActionInterceptor.onStartLoadMore();
                loadData(IDataSource.LOADMORE);
            } else {
                getLoadMoreView().showNoMore("没有更多了");
            }
        }
    };
}
