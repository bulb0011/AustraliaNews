package com.ruanyun.australianews.base.refreshview.impl;

import com.ruanyun.australianews.base.PageParamsBase;
import com.ruanyun.australianews.base.ResultBase;
import com.ruanyun.australianews.data.ApiService;

/**
 * Description:
 * author: zhangsan on 2017/8/16 下午3:38.
 */

public abstract class PageDataSource<T extends ResultBase, P extends PageParamsBase> extends RxDataSource<T> {
    protected int currentPage = FIRST_PAGENUM;
    protected int totalPage;
    protected P params;


    public PageDataSource(ApiService apiService) {
        super(apiService);
    }

    public void setParams(P params) {
        this.params = params;
    }

    @Override
    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }

    @Override
    public boolean hasMore() {
        return currentPage < totalPage;
    }


    @Override
    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
        params.setPageNumber(currentPage);
    }

    @Override
    public int getCurrentPage() {
        return currentPage;
    }
}
