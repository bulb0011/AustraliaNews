package com.ruanyun.australianews.base.refreshview.impl;

import android.support.annotation.IdRes;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.ruanyun.australianews.R;
import com.ruanyun.australianews.base.refreshview.view.IRefreshListLoadViewFactory;
import in.srain.cube.views.ptr.PtrClassicDefaultHeader;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;


/**
 * Description:
 * author: zhangsan on 2017/8/15 下午1:39.
 */

public class DefaultLoadViewFactory implements IRefreshListLoadViewFactory {


    @Override
    public IRefreshView creatRefreshView() {
        return new PtrRefreshView();
    }

    @Override
    public ILoadMoreView creatLoadMoreView() {
        return new DefaultLoadMoreView();
    }

    @Override
    public IEmptyView createEmptyView(@IdRes int id) {
        return null;
    }

    private static class PtrRefreshView implements IRefreshView, PtrHandler {
        PtrFrameLayout ptrFrameLayout;
        // View root;
        onRefreshListener listener;
        int dataViewId = R.id.list;
        private boolean refreshEnable = true;

        @Override
        public void init(RefreshViewAddWrapper wrapper, int viewId) {
            // root = wrapper.getRootView();
            // ptrFrameLayout = (PtrFrameLayout) root.findViewById(R.id.refresh_layout);
            ptrFrameLayout = (PtrFrameLayout) wrapper.getRootView();
            PtrClassicDefaultHeader header = new PtrClassicDefaultHeader(ptrFrameLayout.getContext());
//            MaterialHeader header=new MaterialHeader(ptrFrameLayout.getContext());
//            StoreHouseHeader header=new StoreHouseHeader(ptrFrameLayout.getContext());
//            header.initWithString("努力加载中...");
//            header.setTextColor(Color.BLACK);
            ptrFrameLayout.setHeaderView(header);
            ptrFrameLayout.addPtrUIHandler(header);
            ptrFrameLayout.setPtrHandler(this);
            ptrFrameLayout.disableWhenHorizontalMove(true);
            this.dataViewId = viewId;
        }

        @Override
        public void setRefreshListener(onRefreshListener listener) {
            this.listener = listener;
        }

        @Override
        public View getContentView() {
            // return ptrFrameLayout.getChildAt(0);
            return ptrFrameLayout.findViewById(dataViewId);
        }

        @Override
        public View getRoot() {
            return ptrFrameLayout;
        }

        @Override
        public void startRefresh() {
            ptrFrameLayout.autoRefresh();
        }

        @Override
        public void stopRefresh() {
            ptrFrameLayout.refreshComplete();
        }

        @Override
        public void setRefreshEnable(boolean enable) {
            refreshEnable = enable;
            ptrFrameLayout.setEnabledNextPtrAtOnce(enable);
        }

        @Override
        public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
            return refreshEnable && PtrDefaultHandler.checkContentCanBePulledDown(frame, content, header);
        }

        @Override
        public void onRefreshBegin(PtrFrameLayout frame) {
            if (listener != null)
                listener.onRefresh();
        }
    }


    private static class DefaultLoadMoreView implements ILoadMoreView {
        private ProgressBar progressLoading;
        private TextView tvStatus;
        private View root;

        @Override
        public void init(LoadMoreViewAddWrapper loadMoreViewAddWrapper) {
            root = loadMoreViewAddWrapper.getRootView();
            tvStatus = (TextView) root.findViewById(R.id.tv_status);
            progressLoading = (ProgressBar) root.findViewById(R.id.progress_loading);

        }


        @Override
        public void showLoading(String tip) {
            if (root.getVisibility() == View.INVISIBLE)
                root.setVisibility(View.VISIBLE);
            progressLoading.setVisibility(View.VISIBLE);
            tvStatus.setVisibility(View.INVISIBLE);
        }

        @Override
        public void showNoMore(String tip) {
            progressLoading.setVisibility(View.INVISIBLE);
            tvStatus.setVisibility(View.VISIBLE);
            tvStatus.setText(tip);
        }

        @Override
        public void hide() {
            root.setVisibility(View.INVISIBLE);
        }
    }


}
