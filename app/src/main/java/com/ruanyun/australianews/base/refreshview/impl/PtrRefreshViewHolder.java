package com.ruanyun.australianews.base.refreshview.impl;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;

import com.ruanyun.australianews.R;
import com.ruanyun.australianews.base.refreshview.data.IDataDelegate;
import com.ruanyun.australianews.base.refreshview.view.IDataAdapter;
import com.ruanyun.australianews.base.refreshview.view.ILoadMoreListenerHandler;
import com.ruanyun.australianews.base.refreshview.view.IRefreshListLoadViewFactory;
import com.ruanyun.australianews.base.refreshview.view.IRefreshViewHolder;

import in.srain.cube.views.ptr.PtrFrameLayout;

import javax.inject.Inject;

/**
 * Description:
 * author: zhangsan on 2017/8/25 下午5:21.
 */

public class PtrRefreshViewHolder implements IRefreshViewHolder<PtrFrameLayout> {
    
    private boolean refreshViewEnable = true;
    
    private boolean loadMoreViewEnable = false;
    
    private boolean emptyViewEnable = true;
    
    private Context mContext;
    /**
     * 默认的view id
     **/
    private int listViewId = R.id.list;
    
    
    private int emptyViewId = R.id.emptyview;
    
    private int refreshViewId = R.id.refresh_layout;
    
    private IRefreshListLoadViewFactory loadViewFactory = new DefaultLoadViewFactory();
    
    private IRefreshListLoadViewFactory.IRefreshView refreshView;
    
    private IRefreshListLoadViewFactory.ILoadMoreView loadMoreView;
    
    private IRefreshListLoadViewFactory.IEmptyView emptyView;
    
    private IDataAdapter dataAdapter;
    
    private ILoadMoreListenerHandler loadMoreListenerHandler;
    
    /**
     * 创建data ->view代理类
     **/
    @Override
    public IDataDelegate createDataDelegate() {
        return new DefaultModel2ViewControler(this);
    }
    
    /**
     * 页面activity 初始化 view
     *
     * @param activity
     **/
    @Override
    public IRefreshViewHolder init(final Activity activity) {
        mContext = activity;
        refreshView = loadViewFactory.creatRefreshView();
        refreshView.init(new IRefreshListLoadViewFactory.IRefreshView.RefreshViewAddWrapper() {
            @Override
            public void addRefreshView(@LayoutRes int layout) {
            
            }
            
            @Override
            public void addRefreshView(@NonNull View view) {
            
            }
            
            @Override
            public View getRootView() {
                return activity.findViewById(refreshViewId);
            }
        }, listViewId);
        //emptyView = loadViewFactory.createEmptyView();
        loadMoreView = loadViewFactory.creatLoadMoreView();
        View dataView = activity.findViewById(listViewId);
        if (dataView instanceof AbsListView) {
            //  loadMoreView.init(new );
            loadMoreListenerHandler = new ListViewLoadMoreHandler();
        } else if (dataView instanceof RecyclerView) {
            loadMoreListenerHandler = new RecyclerViewLoadMoreHandler();
        }
        //  refreshView.init(new );
        return this;
    }
    
    @Override
    public IRefreshViewHolder init(final View root) {
        mContext = root.getContext();
        refreshView = loadViewFactory.creatRefreshView();
        refreshView.init(new IRefreshListLoadViewFactory.IRefreshView.RefreshViewAddWrapper() {
            @Override
            public void addRefreshView(@LayoutRes int layout) {
            
            }
            
            @Override
            public void addRefreshView(@NonNull View view) {
            
            }
            
            @Override
            public View getRootView() {
                return root.findViewById(refreshViewId);
            }
        }, listViewId);
        //emptyView = loadViewFactory.createEmptyView();
        loadMoreView = loadViewFactory.creatLoadMoreView();
        View dataView = root.findViewById(listViewId);
        if (dataView instanceof AbsListView) {
            //  loadMoreView.init(new );
            loadMoreListenerHandler = new ListViewLoadMoreHandler();
        } else if (dataView instanceof RecyclerView) {
            loadMoreListenerHandler = new RecyclerViewLoadMoreHandler();
        }
        return this;
    }

    @Inject
    public PtrRefreshViewHolder() {
    }
    
    public PtrRefreshViewHolder(View contentView) {
        init(contentView);
        //mContext = contentView.getContext();
    }
    
    
    public PtrRefreshViewHolder(Activity activity) {
        init(activity);
        //mContext = activity;
    }
    
    /**
     * 自定义empty view 的id
     *
     * @param id
     */
    @Override
    public IRefreshViewHolder setEmptyViewId(@IdRes int id) {
        emptyViewId = id;
        return this;
    }
    
    /**
     * 自定义list 或 rev view 的id
     *
     * @param id
     */
    @Override
    public IRefreshViewHolder setDataListViewId(@IdRes int id) {
        this.listViewId = id;
        return this;
    }
    
    /**
     * 自定义下拉刷新 view 的id
     *
     * @param id
     */
    @Override
    public IRefreshViewHolder setRefreshViewId(@IdRes int id) {
        this.refreshViewId = id;
        return this;
    }
    
    @Override
    public IRefreshViewHolder setLoadMoreView(IRefreshListLoadViewFactory.ILoadMoreView iLoadMoreView) {
        this.loadMoreView = iLoadMoreView;
        return this;
    }
    
    @Override
    public IRefreshViewHolder setRefreshView(IRefreshListLoadViewFactory.IRefreshView iRefreshView) {
        this.refreshView = iRefreshView;
        return this;
    }
    
    /**
     * 列表设置
     *
     * @param dataAdapter
     **/
    @Override
    public IRefreshViewHolder setDataAdapter(IDataAdapter dataAdapter) {
        switch (dataAdapter.getAdapterType()) {
            case IDataAdapter.TYPE_LISTVIEW:
                if (loadMoreEnable()) {
                    final ListView listView = (ListView) getRefreshView().getContentView();
                    IRefreshListLoadViewFactory.ILoadMoreView.LoadMoreViewAddWrapper wrapper = new IRefreshListLoadViewFactory.ILoadMoreView.LoadMoreViewAddWrapper() {
                        @Override
                        public void addLoadMoreView(@LayoutRes int layout) {
                        
                        }
                        
                        @Override
                        public void addLoadMoreView(@NonNull View view) {
                            listView.addFooterView(view);
                        }
                        
                        @Override
                        public View getRootView() {
                            return null;
                        }
                    };
                    wrapper.addLoadMoreView(LayoutInflater.from(mContext).inflate(R.layout.layout_load_more, null));
                    getLoadMoreView().init(wrapper);
                } else {
                    this.dataAdapter = dataAdapter;
                }
                break;
            case IDataAdapter.TYPE_RECYERVIEW:
                if (loadMoreEnable()) {
                    if (dataAdapter instanceof RvHeaderFootViewAdapter) {
                        this.dataAdapter = dataAdapter;
                        RvHeaderFootViewAdapter wrapper = (RvHeaderFootViewAdapter) dataAdapter;
                        wrapper.addLoadMoreView(R.layout.layout_load_more);
                        getLoadMoreView().init(wrapper);
                    } else {
                        RvHeaderFootViewAdapter adapter = new RvHeaderFootViewAdapter<>((RvMuiltItemAdapter) dataAdapter, mContext);
                        adapter.addLoadMoreView(R.layout.layout_load_more);
                        getLoadMoreView().init(adapter);
                        this.dataAdapter = adapter;
                    }
                } else {
                    this.dataAdapter = dataAdapter;
                }
                getLoadMoreView().hide();
                break;
        }
        
        // this.dataAdapter = dataAdapter;
        return this;
    }
    
    @Override
    public IRefreshViewHolder setRefreshViewEnable(boolean enable) {
        refreshViewEnable = enable;
        getRefreshView().setRefreshEnable(enable);
        return this;
    }
    
    @Override
    public IRefreshViewHolder setEmptyView(IRefreshListLoadViewFactory.IEmptyView emptyView) {
        this.emptyView = emptyView;
        emptyView.bind(refreshView.getRoot());
        return this;
    }
    
    @Override
    public IRefreshViewHolder setEmptyViewEnable(boolean enable) {
        emptyViewEnable = enable;
        return this;
    }
    
    /**
     * 设置加载更多是否可用
     *
     * @param enable
     **/
    @Override
    public IRefreshViewHolder setLoadMoreEnable(boolean enable) {
        this.loadMoreViewEnable = enable;
        return this;
    }
    
    /**
     * 下拉刷新是否可用
     **/
    @Override
    public boolean refreshViewEnable() {
        return refreshViewEnable;
    }
    
    @Override
    public boolean loadMoreEnable() {
        return loadMoreViewEnable;
    }
    
    /**
     * 获取 emptyview 是否可用
     **/
    @Override
    public boolean emptyViewEnable() {
        return emptyViewEnable;
    }
    
    /**
     * 获取绑定的 下拉刷新view
     **/
    @Override
    public PtrFrameLayout getRawRefreshView() {
        return (PtrFrameLayout) refreshView.getContentView().findViewById(refreshViewId);
    }
    
    @Override
    public IRefreshListLoadViewFactory.IRefreshView getRefreshView() {
        return refreshView;
    }
    
    @Override
    public IRefreshListLoadViewFactory.ILoadMoreView getLoadMoreView() {
        return loadMoreView;
    }
    
    @Override
    public IRefreshListLoadViewFactory.IEmptyView getEmptyView() {
        return emptyView;
    }
    
    @Override
    public IDataAdapter getDataAdapter() {
        return dataAdapter;
    }
    
    @Override
    public ILoadMoreListenerHandler getILoadMoreListenerHandler() {
        return loadMoreListenerHandler;
    }
}
